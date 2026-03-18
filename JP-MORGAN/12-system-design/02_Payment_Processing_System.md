# System Design: Payment Processing System

## Problem Statement
Design a payment processing system for JP Morgan that handles domestic and international money transfers, ensuring exactly-once processing, high availability, and regulatory compliance.

---

## Step 1 — Clarify Requirements

### Functional Requirements
- Process domestic (ACH, wire) and international (SWIFT) payments
- Validate payments (account exists, sufficient balance, fraud check)
- Support payment status tracking
- Handle reversals and refunds
- Notify sender and receiver on completion
- Maintain complete audit trail

### Non-Functional Requirements
- **Throughput**: 10,000 payments/second
- **Latency**: Payment initiation ACK < 500ms; settlement T+0 (wire) / T+1 (ACH)
- **Availability**: 99.999% (Five nines — ~5 min downtime/year)
- **Consistency**: Strong — no double payments, no lost payments
- **Idempotency**: Retrying same payment must not double-charge
- **Compliance**: PCI-DSS, SOX, AML (Anti-Money Laundering) checks

---

## Step 2 — Capacity Estimation

```
Peak QPS:       10,000 payments/second
Daily volume:   ~500M payments/day
Payment size:   ~2 KB per record
Storage:        500M × 2 KB = 1 TB/day (before audit records)
Audit log:      ~5× storage = 5 TB/day
Read:Write:     5:1 (status checks, reconciliation)
```

---

## Step 3 — API Design

```
POST /payments
  Body: { idempotencyKey, senderId, receiverId, amount, currency, type (ACH/WIRE/SWIFT), memo? }
  Returns: { paymentId, status: INITIATED, estimatedSettlement }

GET /payments/{paymentId}
  Returns: { paymentId, status, amount, currency, createdAt, settledAt?, failureReason? }

POST /payments/{paymentId}/cancel
  Returns: { paymentId, status: CANCELLATION_REQUESTED }

GET /accounts/{accountId}/payments?from=&to=&status=
```

---

## Step 4 — High-Level Architecture

```
  Client Apps / Partner Banks
           │
           ▼
  ┌─────────────────┐
  │   API Gateway   │  Auth (OAuth2), rate limiting, TLS
  └────────┬────────┘
           │
           ▼
  ┌─────────────────┐    ┌──────────────────┐
  │  Payment        │───▶│  Idempotency     │ (Redis: idempotencyKey → paymentId)
  │  Service        │    │  Store           │
  └────────┬────────┘    └──────────────────┘
           │
     ┌─────▼──────────────┐
     │                    │
     ▼                    ▼
┌─────────────┐   ┌───────────────┐
│  Fraud &    │   │  Balance      │
│  AML Check  │   │  Validation   │
└──────┬──────┘   └───────┬───────┘
       │                  │
       └────────┬──────────┘
                │
                ▼
  ┌─────────────────────┐
  │   Payment Ledger    │  (Double-entry bookkeeping DB)
  │   (Write to DB)     │
  └──────────┬──────────┘
             │
             ▼
  ┌─────────────────────┐
  │   Message Queue     │  (Kafka: payments.pending)
  └──────────┬──────────┘
             │
     ┌────────────────────────────┐
     │                            │
     ▼                            ▼
┌──────────┐              ┌───────────────┐
│  ACH     │              │  SWIFT/Wire   │
│  Processor│              │  Processor    │
└──────────┘              └───────────────┘
     │                            │
     ▼                            ▼
┌──────────────────────────────────────┐
│         Settlement Engine            │
│  (Updates ledger on confirmation)    │
└──────────────┬───────────────────────┘
               │
       ┌───────▼───────┐
       │  Notification │  (Email, SMS, WebSocket, Webhook)
       └───────────────┘
```

---

## Step 5 — Component Deep Dive

### Double-Entry Ledger (Core of the System)
Every payment creates TWO ledger entries (debit source, credit destination):
```sql
CREATE TABLE ledger_entries (
    entry_id      UUID PRIMARY KEY,
    payment_id    UUID NOT NULL,
    account_id    VARCHAR(50) NOT NULL,
    entry_type    CHAR(6) NOT NULL,      -- DEBIT / CREDIT
    amount        DECIMAL(20, 2) NOT NULL,
    currency      CHAR(3) NOT NULL,
    balance_after DECIMAL(20, 2) NOT NULL,
    created_at    TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    CONSTRAINT positive_amount CHECK (amount > 0)
);
-- Both entries written in a single ACID transaction
```

### Idempotency Layer
```
Client sends: POST /payments with idempotencyKey = "uuid-xyz"
Server:
  1. Check Redis: GET idempotency:uuid-xyz
  2. If found → return cached response (payment already processed)
  3. If not found → process payment
  4. On success: SET idempotency:uuid-xyz paymentId EX 86400 (24h TTL)
```

### Fraud & AML Check
- Rule-based: amount > $10,000 → flag for AML review (Bank Secrecy Act)
- ML-based: velocity checks, unusual country destinations
- Synchronous call with 200ms timeout; if timeout → allow with async review flag

### Payment State Machine
```
INITIATED → VALIDATING → PENDING → PROCESSING → SETTLED
                 │                      │
                 ▼                      ▼
              FAILED               FAILED / REVERSED
```

### Database — PostgreSQL with Partitioning
```sql
-- Partition payments by created_at (monthly partitions)
CREATE TABLE payments (
    payment_id    UUID NOT NULL,
    sender_id     VARCHAR(50) NOT NULL,
    receiver_id   VARCHAR(50) NOT NULL,
    amount        DECIMAL(20, 2) NOT NULL,
    currency      CHAR(3) NOT NULL,
    type          VARCHAR(10) NOT NULL,   -- ACH, WIRE, SWIFT
    status        VARCHAR(20) NOT NULL,
    created_at    TIMESTAMPTZ NOT NULL,
    settled_at    TIMESTAMPTZ
) PARTITION BY RANGE (created_at);
```

---

## Step 6 — Handling Failures & Exactly-Once

### Network Failure Between Services
- Use **Outbox Pattern**: write payment to DB + outbox table in same transaction
- Separate process reads outbox and publishes to Kafka
- Guarantees no message is lost even if Kafka is briefly unavailable

### Exchange/Bank Failure
- Retry with exponential backoff (1s, 2s, 4s... max 5 retries)
- After max retries → move to FAILED, notify sender, reverse ledger entries
- Dead letter queue for manual investigation

### Distributed Transaction (Saga Pattern)
For cross-bank payments needing multiple steps:
```
Step 1: Debit sender account
Step 2: Send to correspondent bank
Step 3: Credit receiver account

Compensating transactions (if step 3 fails):
  → Reverse Step 1 (credit sender back)
  → Send cancellation to correspondent bank
```

---

## Step 7 — Compliance & Security

| Requirement | Implementation |
|------------|---------------|
| PCI-DSS | Encrypt card data at rest (AES-256) and in transit (TLS 1.3) |
| SOX | Immutable audit log — S3 with Object Lock (WORM) |
| AML | Flag transactions > $10K, unusual patterns → compliance review queue |
| Data Residency | Route EU payments through EU datacenters only |
| SWIFT | ISO 20022 message format, SWIFT GPI for tracking |

---

## Step 8 — Trade-offs

| Decision | Chosen | Reason | Trade-off |
|----------|--------|--------|-----------|
| Consistency | Strong (ACID) | No double payments | Lower write throughput |
| Processing | Async (Kafka) | Decouple from bank networks | Eventual settlement |
| Ledger | Double-entry | Regulatory standard | More complex queries |
| Idempotency store | Redis | Sub-ms lookup | Risk of data loss (mitigated with persistence) |
| Saga vs 2PC | Saga | More resilient to failures | Eventual consistency between banks |
