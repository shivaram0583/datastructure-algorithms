# System Design: Real-Time Market Data Feed

## Problem Statement
Design a real-time market data distribution system that receives price quotes, trades, and order book updates from multiple exchanges and distributes them to thousands of internal consumers (trading algorithms, risk systems, front-end dashboards) with microsecond-level latency.

---

## Step 1 — Clarify Requirements

### Functional Requirements
- Ingest raw market data from multiple exchanges (NYSE, NASDAQ, CBOE, etc.)
- Normalize data into a unified format
- Distribute to thousands of internal subscribers
- Support filtering by instrument, exchange, data type
- Provide historical tick data for backtesting
- Display last price, bid/ask, volume, OHLCV

### Non-Functional Requirements
- **Latency**: < 1ms from ingestion to consumer delivery (P99)
- **Throughput**: 1 million messages/second peak
- **Fan-out**: 10,000+ concurrent subscribers
- **Availability**: 99.999% during market hours
- **Data integrity**: No gaps, no duplicates, in-order delivery per instrument

---

## Step 2 — Capacity Estimation

```
Exchanges:           20 major exchanges
Instruments:         50,000 (equities, options, futures)
Messages/exchange:   50,000 updates/second peak
Total ingestion:     20 × 50,000 = 1M messages/second
Message size:        ~500 bytes (quote update)
Ingestion bandwidth: 1M × 500 B = 500 MB/s = 4 Gbps

Subscribers:         10,000 consumers
Fan-out bandwidth:   If each gets 10,000 instruments = enormous → must filter
```

---

## Step 3 — API Design

### Subscription (WebSocket / gRPC streaming)
```
SUBSCRIBE { instruments: ["AAPL", "MSFT"], dataTypes: ["QUOTE", "TRADE"] }
Server streams:
  { instrument: "AAPL", type: "QUOTE", bid: 150.10, ask: 150.12, timestamp: ... }
  { instrument: "AAPL", type: "TRADE", price: 150.11, volume: 100, timestamp: ... }

UNSUBSCRIBE { instruments: ["AAPL"] }

GET /snapshot/{instrument}  → last known state (REST)
GET /history/{instrument}?from=&to=&resolution=1m  → historical OHLCV
```

---

## Step 4 — High-Level Architecture

```
   Exchanges (NYSE, NASDAQ, CBOE...)
         │ (FIX, FAST, ITCH protocols)
         ▼
  ┌─────────────────────────────────┐
  │       Feed Handlers             │  (Co-located in exchange DC for ultra-low latency)
  │  (per-exchange decoders)        │
  └──────────────┬──────────────────┘
                 │ (normalized events)
                 ▼
  ┌─────────────────────────────────┐
  │     Aggregator / Normalizer     │  (Unified schema, seq. number assignment)
  └──────────────┬──────────────────┘
                 │
     ┌───────────┴───────────┐
     ▼                       ▼
┌─────────┐         ┌────────────────┐
│  Kafka  │         │  In-Memory     │
│ (durable│         │  Pub/Sub Bus   │  (Chronicle Queue / Aeron for ultra-low latency)
│ replay) │         └───────┬────────┘
└─────────┘                 │
                            │ (topic per instrument group)
                            ▼
              ┌─────────────────────────┐
              │   Distribution Layer    │
              │   (Multicast UDP or     │
              │    WebSocket servers)   │
              └────────────┬────────────┘
                           │
           ┌───────────────┼──────────────────┐
           ▼               ▼                  ▼
    Trading Algos    Risk Systems      Web Dashboards
    (co-located,     (gRPC stream)     (WebSocket)
     UDP multicast)
```

---

## Step 5 — Component Deep Dive

### Feed Handlers (Exchange Adapters)
- One handler per exchange, co-located in exchange data center
- Decodes proprietary protocols: ITCH (NASDAQ), FAST (CME), FIX
- Adds sequence numbers, timestamps (hardware timestamping for nanosecond precision)
- Publishes normalized `MarketEvent` objects

```java
// Normalized event structure
class MarketEvent {
    String   instrument;   // "AAPL.NASDAQ"
    EventType type;        // QUOTE, TRADE, BOOK_UPDATE
    long     bidPrice;     // in basis points (avoid floating point)
    long     askPrice;
    long     lastPrice;
    long     volume;
    long     exchangeTimestamp;  // nanoseconds
    long     receivedTimestamp;  // nanoseconds
    long     sequenceNumber;
}
```

### Why integers for prices?
- Floating point arithmetic is non-deterministic across CPUs
- Store prices as **integer basis points** (150.10 → 15010)
- Prevents rounding errors in financial calculations

### Aggregator / Normalizer
- Merges data from all exchanges
- Computes National Best Bid and Offer (NBBO): best bid across all exchanges
- Deduplication: detect and drop duplicate messages via sequence numbers
- Gap detection: alert if sequence numbers jump (data loss)

### Ultra-Low Latency Transport
For internal trading algorithms:
- **UDP Multicast**: One sender → all receivers simultaneously; no TCP overhead
- **Aeron** or **Chronicle Queue**: Shared-memory IPC for on-box communication (sub-microsecond)
- No garbage collection: use off-heap memory, object pooling

For web dashboards and risk systems:
- **WebSocket** over TLS
- **gRPC server streaming**

### Kafka — Durable Replay Layer
- All events published to Kafka partitioned by `instrument`
- Consumers can replay from any point for backtesting, reconnection
- Kafka retention: 7 days intraday, 1 year for daily OHLCV

### Snapshot Service
- Maintains last known state per instrument in Redis
- New subscribers get immediate snapshot before streaming begins
- Prevents "wait for next update" delay on connection

---

## Step 6 — Handling Key Challenges

### Gap / Out-of-Order Detection
```
Each exchange feed has monotonically increasing sequence numbers.
If gap detected (seq 5 → seq 7, missing seq 6):
  1. Request retransmission from exchange
  2. If retransmission fails → alert, mark data as potentially incomplete
  3. Publish gap event to downstream consumers
```

### Backpressure
- Slow consumers must not block fast consumers
- Solution: per-consumer ring buffer; if buffer full → drop oldest or disconnect slow consumer
- Alert ops team when consumer falls behind

### Clock Synchronization
- Hardware timestamping (GPS-locked NTP, PTP IEEE 1588)
- Precision: ±100 nanoseconds across servers
- All timestamps in UTC nanoseconds

---

## Step 7 — Historical Data (Tick Store)

```
Raw ticks → Kafka → Tick Consumer → KDB+/TimescaleDB/InfluxDB
OHLCV aggregation → precomputed at 1s, 1m, 5m, 15m, 1h, 1d intervals
Stored in: S3 (cold), TimescaleDB (warm, last 6 months)

Query: GET /history/AAPL?from=2024-01-01&to=2024-01-31&resolution=1d
```

---

## Step 8 — Trade-offs

| Decision | Chosen | Reason | Trade-off |
|----------|--------|--------|-----------|
| Transport (internal) | UDP Multicast | Lowest latency | Unreliable (gaps possible) |
| Transport (external) | WebSocket | Bidirectional, browser support | Higher latency than UDP |
| Prices | Integer basis points | Precision, performance | Less human-readable |
| Durability | Kafka | Replay, audit | Additional hop adds ~1ms |
| Snapshot store | Redis | Fast reads | Memory cost |
| Time series DB | KDB+ / TimescaleDB | Purpose-built for tick data | Specialist skills required |
