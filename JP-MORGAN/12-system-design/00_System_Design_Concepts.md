# System Design Core Concepts — JP Morgan Interview

## How to Approach a System Design Interview

### Framework (use every time)
```
1. Clarify Requirements      (5 min)
2. Estimate Scale            (2 min)
3. Define APIs               (3 min)
4. High-Level Design         (10 min)
5. Deep Dive Key Components  (15 min)
6. Identify Bottlenecks      (5 min)
7. Trade-offs & Alternatives (5 min)
```

### Step 1 — Clarify Requirements
Always ask before drawing anything:
- **Functional**: What are the core features?
- **Non-Functional**: Latency, consistency, availability requirements?
- **Scale**: QPS, DAU, data volume?
- **Constraints**: Any existing systems to integrate with?

---

## Foundational Concepts

### CAP Theorem
A distributed system can guarantee at most 2 of 3:
| Property | Meaning |
|----------|---------|
| **C**onsistency | Every read receives the most recent write |
| **A**vailability | Every request receives a response (not necessarily latest) |
| **P**artition Tolerance | System operates despite network partitions |

**JP Morgan context**: Financial systems typically choose **CP** (e.g., order books, ledgers) — consistency over availability. A duplicate trade is far worse than temporary unavailability.

---

### ACID vs BASE
| | ACID (traditional DB) | BASE (NoSQL) |
|--|--|--|
| Consistency | Strong | Eventual |
| Availability | Lower | Higher |
| Use case | Bank transactions, ledgers | Session data, analytics |

---

### Consistency Levels (from strongest to weakest)
1. **Linearizability** — reads always see the latest write
2. **Sequential** — operations appear in some sequential order
3. **Causal** — causally related operations are ordered
4. **Eventual** — all replicas converge eventually

---

## Scalability Patterns

### Horizontal vs Vertical Scaling
- **Vertical**: Bigger machine. Simple but has a ceiling.
- **Horizontal**: More machines. Complex but unlimited scale.

### Load Balancing
- **Round Robin** — distribute evenly
- **Least Connections** — route to least-loaded server
- **IP Hash** — same client → same server (session stickiness)
- **Consistent Hashing** — used in distributed caches to minimize remapping on node add/remove

### Database Scaling
| Technique | When to use |
|-----------|------------|
| **Read Replicas** | Read-heavy workloads |
| **Sharding** | Write-heavy, too large for one DB |
| **Partitioning** | Divide by range (date), hash (user ID), or list (region) |
| **Caching** | Reduce DB load for repeated reads |

---

## Caching

### Cache Strategies
| Strategy | Description | When |
|----------|-------------|------|
| **Cache-Aside** (Lazy) | App reads cache, on miss reads DB and fills cache | General purpose |
| **Write-Through** | Write to cache AND DB synchronously | Low write latency needed |
| **Write-Behind** (Write-Back) | Write to cache, async write to DB | High write throughput |
| **Read-Through** | Cache handles DB reads on miss | Simpler app code |

### Cache Eviction Policies
- **LRU** (Least Recently Used) — evict oldest accessed
- **LFU** (Least Frequently Used) — evict least accessed overall
- **TTL** — expire after fixed time

### Common Tools
- **Redis** — in-memory, supports data structures, pub/sub, persistence
- **Memcached** — simpler, pure in-memory key-value

---

## Messaging & Async Processing

### Message Queue vs Event Streaming
| | Message Queue (RabbitMQ) | Event Streaming (Kafka) |
|--|--|--|
| Message retention | Deleted after consumption | Retained for configurable period |
| Consumers | One consumer per message | Multiple consumers, independent offsets |
| Use case | Task queues, work distribution | Event log, audit trail, replay |
| JP Morgan use | Trade order routing | Market data feed, audit log |

### Patterns
- **Fan-out**: One message → multiple consumers
- **Dead Letter Queue (DLQ)**: Failed messages go here for inspection
- **Idempotency**: Process the same message multiple times safely

---

## Data Storage

### SQL vs NoSQL Decision
| Situation | Choice |
|-----------|--------|
| ACID transactions required | SQL (PostgreSQL, Oracle) |
| Schema flexibility | NoSQL (MongoDB, DynamoDB) |
| Time-series data | InfluxDB, TimescaleDB |
| Graph relationships | Neo4j |
| Wide-column, high write | Cassandra, HBase |

---

## Networking Fundamentals

### API Styles
| Style | Protocol | Use case |
|-------|----------|---------|
| **REST** | HTTP | General CRUD, external APIs |
| **gRPC** | HTTP/2 | Internal microservices, low latency |
| **WebSocket** | TCP | Real-time bidirectional (market data) |
| **FIX Protocol** | TCP | Financial industry standard for trade messages |

### Important Numbers to Know
| Latency | Value |
|---------|-------|
| L1 cache | ~1 ns |
| L2 cache | ~10 ns |
| RAM access | ~100 ns |
| SSD read | ~100 µs |
| HDD read | ~10 ms |
| Network (same DC) | ~1 ms |
| Network (cross-region) | ~100 ms |

---

## JP Morgan-Specific Patterns

### Financial System Requirements
- **Exactly-once processing**: No duplicate trades, no missed payments
- **Audit trail**: Every action logged immutably
- **High availability**: 99.99% (≤52 min downtime/year)
- **Low latency**: Market data < 1ms, order acknowledgment < 10ms
- **Strong consistency**: Ledger entries must be ACID

### Common JP Morgan System Types
1. **Order Management System (OMS)** — receives, routes, executes orders
2. **Risk Management System** — real-time position and P&L tracking
3. **Settlement System** — post-trade processing (T+1 or T+2)
4. **Market Data Feed** — distributes prices to trading systems
5. **Payment Rails** — cross-border and domestic payment processing

---

## Estimation Cheat Sheet

### Common Calculations
```
1 million requests/day  = ~12 requests/second
1 billion requests/day  = ~12,000 requests/second

1 KB  = 10^3  bytes
1 MB  = 10^6  bytes
1 GB  = 10^9  bytes
1 TB  = 10^12 bytes

1 char  = 1 byte  (ASCII)
1 int   = 4 bytes
1 long  = 8 bytes
UUID    = 36 bytes

Image  = ~300 KB
Tweet  = ~280 bytes
Trade  = ~1 KB
```

### Bandwidth
```
10,000 RPS × 1 KB/request = 10 MB/s = 80 Mbps
```
