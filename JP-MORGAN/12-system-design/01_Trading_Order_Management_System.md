# System Design: Trading Order Management System (OMS)

## Problem Statement
Design a Trading Order Management System that can receive, validate, route, execute, and track equity trade orders. The system must handle high throughput with low latency and guarantee no duplicate executions.

---

## Step 1 — Clarify Requirements

### Functional Requirements
- Accept buy/sell orders from traders and algorithms
- Validate orders (sufficient funds, valid instrument, market hours)
- Route orders to appropriate exchange/venue
- Receive execution confirmations
- Maintain real-time order status
- Support order cancellation and amendment
- Provide order history and audit trail

### Non-Functional Requirements
- **Latency**: Order acknowledgment < 10ms, execution < 100ms
- **Throughput**: 100,000 orders/second peak
- **Availability**: 99.99% during market hours (9:30 AM – 4:00 PM ET)
- **Consistency**: Exactly-once order execution (no duplicates, no missed orders)
- **Durability**: All orders persisted, audit log immutable

### Out of Scope
- Risk management calculation (separate system)
- Settlement and clearing (post-trade system)

---

## Step 2 — Capacity Estimation

```
Orders:      100,000 orders/second peak
             ~1 billion orders/day
Order size:  ~1 KB per order
Storage:     1 billion × 1 KB = 1 TB/day
Executions:  ~80% fill rate → 800,000 executions/second peak

Read/Write ratio: 3:1 (status checks vs new orders)
```

---

## Step 3 — API Design

```
POST /orders
  Body: { clientId, instrument, side (BUY/SELL), quantity, orderType (MARKET/LIMIT), limitPrice?, timeInForce }
  Returns: { orderId, status: PENDING, timestamp }

GET /orders/{orderId}
  Returns: { orderId, status, filledQty, remainingQty, avgPrice, executions[] }

DELETE /orders/{orderId}
  Returns: { orderId, status: CANCEL_PENDING }

PUT /orders/{orderId}
  Body: { newQuantity, newLimitPrice }
  Returns: { orderId, status: AMEND_PENDING }

GET /orders?clientId=X&status=OPEN&from=2024-01-01&to=2024-01-31
```

---

## Step 4 — High-Level Architecture

```
 Traders / Algo Systems
         │
         ▼
  ┌─────────────┐
  │ API Gateway │  (Rate limiting, auth, TLS termination)
  └──────┬──────┘
         │
         ▼
  ┌─────────────┐     ┌────────────────┐
  │  Order      │────▶│ Validation     │
  │  Receiver   │     │ Service        │──── Risk Engine
  └──────┬──────┘     └────────────────┘
         │
         ▼
  ┌─────────────┐
  │  Order      │  (Kafka topic: orders.new)
  │  Queue      │
  └──────┬──────┘
         │
         ▼
  ┌─────────────┐     ┌──────────────────┐
  │  Order      │────▶│ Exchange Router  │────▶ NYSE/NASDAQ/Dark Pools
  │  Processor  │     └──────────────────┘
  └──────┬──────┘
         │
         ▼
  ┌─────────────┐     ┌──────────────────┐
  │  Execution  │◀────│ FIX Engine       │◀─── Exchange Confirmations
  │  Handler    │     └──────────────────┘
  └──────┬──────┘
         │
  ┌──────▼──────┐   ┌──────────────┐   ┌──────────────┐
  │  Order DB   │   │  Cache       │   │  Audit Log   │
  │ (PostgreSQL)│   │  (Redis)     │   │  (Kafka)     │
  └─────────────┘   └──────────────┘   └──────────────┘
```

---

## Step 5 — Component Deep Dive

### Order Receiver
- Exposes REST + FIX Protocol endpoints
- Generates unique `orderId` using UUID v4
- Publishes to Kafka topic `orders.new`
- Returns ACK immediately (async processing)

### Validation Service
- Checks: valid instrument, market hours, client exists, sufficient buying power
- Calls Risk Engine synchronously (timeout: 5ms)
- If invalid → publishes to `orders.rejected`, notifies client via WebSocket

### Order Processor
- Consumes from `orders.new` Kafka topic
- Assigns to appropriate exchange/venue based on routing rules (NBBO, dark pool logic)
- Writes `OPEN` status to Order DB
- Publishes to `orders.routed`

### Exchange Router
- Uses FIX Protocol (Financial Information eXchange) to send to venues
- Maintains persistent TCP connections (session layer)
- Handles retries with exponential backoff

### Execution Handler
- Receives fill confirmations from exchanges via FIX
- Updates order status: PARTIALLY_FILLED / FILLED / REJECTED
- Publishes execution events to `executions` Kafka topic
- Downstream: P&L, positions, settlement systems consume this topic

### Order Database
- PostgreSQL with read replicas (3 replicas)
- Sharded by `clientId` for horizontal scale
- Schema:

```sql
CREATE TABLE orders (
    order_id       UUID PRIMARY KEY,
    client_id      VARCHAR(50) NOT NULL,
    instrument     VARCHAR(20) NOT NULL,
    side           CHAR(4) NOT NULL,        -- BUY/SELL
    quantity       BIGINT NOT NULL,
    filled_qty     BIGINT DEFAULT 0,
    order_type     VARCHAR(10) NOT NULL,    -- MARKET/LIMIT
    limit_price    DECIMAL(18,6),
    status         VARCHAR(20) NOT NULL,    -- PENDING/OPEN/FILLED/CANCELLED
    created_at     TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at     TIMESTAMPTZ NOT NULL DEFAULT NOW()
);
CREATE INDEX idx_orders_client_status ON orders(client_id, status);
```

### Cache (Redis)
- Stores active order state (OPEN orders) with TTL = end of trading day
- `GET order:{orderId}` → instant status for polling clients
- Write-through: updated on every status change

### Audit Log (Kafka + S3)
- Every order event (create/amend/cancel/fill) published to `orders.audit`
- Kafka consumer writes to S3 in Parquet format for compliance/replay
- Immutable — never deleted, 7-year retention per regulations

---

## Step 6 — Exactly-Once Processing

**Problem**: Network failures can cause duplicate order submissions.

**Solution — Idempotency Key**:
- Client includes `idempotencyKey` (UUID) with each request
- Server stores `idempotencyKey → orderId` in Redis with TTL=24h
- If same key received again, return original orderId without reprocessing

**Kafka Exactly-Once**:
- Use Kafka transactions (producer + consumer in same transaction)
- Enable `enable.idempotence=true` on Kafka producer

---

## Step 7 — Bottlenecks & Solutions

| Bottleneck | Solution |
|-----------|---------|
| Order DB write throughput | Shard by clientId; use write-optimized DB (TiDB) |
| Exchange connection limits | Connection pool; multiple FIX sessions per venue |
| Status polling overhead | Push via WebSocket instead of polling |
| Single Kafka partition bottleneck | Partition by instrumentId for parallelism |
| Redis single point of failure | Redis Cluster with 3 masters + 3 replicas |

---

## Step 8 — Trade-offs

| Decision | Chosen | Reason | Trade-off |
|----------|--------|--------|-----------|
| Consistency | Strong (CP) | No duplicate trades | Lower availability |
| Order ack | Async (Kafka) | Ultra-low latency ack | Temporary inconsistency |
| Protocol | FIX | Industry standard | Complex parsing |
| Storage | PostgreSQL | ACID, complex queries | Less scalable than NoSQL |
| Cache | Redis | Sub-ms reads | Stale data risk |
