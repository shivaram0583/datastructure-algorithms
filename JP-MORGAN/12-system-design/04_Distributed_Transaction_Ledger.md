# System Design: Distributed Transaction Ledger

## Problem Statement
Design a distributed ledger system that tracks all financial transactions across multiple accounts with strong consistency, complete audit history, and the ability to reconstruct any account balance at any point in time.

---

## Step 1 — Clarify Requirements

### Functional Requirements
- Record all financial transactions (debit/credit)
- Query current balance for any account
- Query balance at any historical point in time (time travel)
- Generate account statements (list of transactions)
- Support reconciliation between accounts
- Immutable transaction log — entries can never be modified or deleted

### Non-Functional Requirements
- **Consistency**: Strong — balance must always be accurate
- **Durability**: Zero data loss — committed transactions survive failures
- **Throughput**: 50,000 transactions/second
- **Latency**: Transaction commit < 50ms
- **Availability**: 99.999%
- **Compliance**: 7-year retention, SOX audit trail

---

## Step 2 — Core Design Principle: Event Sourcing + CQRS

### Event Sourcing
Instead of storing current state (balance), store every event that changed state.
Current balance = replay all events for that account.

```
Traditional: accounts table → balance = 5000
Event Sourced: 
  Event 1: CREDIT  +10000  (balance=10000)
  Event 2: DEBIT   -3000   (balance=7000)
  Event 3: DEBIT   -2000   (balance=5000)
  Current balance = sum of all events = 5000
```

**Benefits for JP Morgan**:
- Complete audit history by design
- Time-travel queries (balance on any date)
- Replay for recovery, analytics, debugging
- No UPDATE or DELETE — only INSERT → safe for compliance

### CQRS (Command Query Responsibility Segregation)
- **Write side (Commands)**: append-only ledger, optimized for writes
- **Read side (Queries)**: materialized views of balances, optimized for reads

---

## Step 3 — Architecture

```
         Client (Payment Service, Trade System)
                          │
                          ▼
                 ┌─────────────────┐
                 │  Ledger API     │
                 └────────┬────────┘
                          │
              ┌───────────┴────────────┐
              ▼                        ▼
    ┌──────────────────┐    ┌──────────────────────┐
    │  Command Handler │    │  Query Handler       │
    │  (Write Side)    │    │  (Read Side)         │
    └────────┬─────────┘    └────────┬─────────────┘
             │                       │
             ▼                       ▼
    ┌──────────────────┐    ┌──────────────────────┐
    │  Transaction Log │    │  Balance Cache       │
    │  (Append-Only DB)│    │  (Redis / Read DB)   │
    └────────┬─────────┘    └──────────────────────┘
             │
             ▼
    ┌──────────────────┐
    │  Kafka           │  (event stream)
    └────────┬─────────┘
             │
             ▼
    ┌──────────────────┐
    │  Projection      │  (builds read models)
    │  Workers         │
    └──────────────────┘
```

---

## Step 4 — Database Schema

### Ledger Table (Write Side — Append-Only)
```sql
CREATE TABLE ledger_entries (
    entry_id        UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    account_id      VARCHAR(50) NOT NULL,
    transaction_id  UUID NOT NULL,
    entry_type      VARCHAR(6) NOT NULL,        -- CREDIT or DEBIT
    amount          BIGINT NOT NULL,            -- in cents, always positive
    currency        CHAR(3) NOT NULL DEFAULT 'USD',
    balance_after   BIGINT NOT NULL,            -- running balance snapshot
    description     VARCHAR(255),
    reference_id    VARCHAR(100),               -- payment_id, trade_id, etc.
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    created_by      VARCHAR(50) NOT NULL,       -- service that created entry

    CONSTRAINT positive_amount CHECK (amount > 0),
    CONSTRAINT valid_entry_type CHECK (entry_type IN ('CREDIT', 'DEBIT'))
);

-- Immutability enforced at DB level
CREATE RULE no_update_ledger AS ON UPDATE TO ledger_entries DO INSTEAD NOTHING;
CREATE RULE no_delete_ledger AS ON DELETE TO ledger_entries DO INSTEAD NOTHING;

-- Indexes
CREATE INDEX idx_ledger_account_time ON ledger_entries(account_id, created_at DESC);
CREATE INDEX idx_ledger_txn ON ledger_entries(transaction_id);
```

### Balance Table (Read Side — Materialized View)
```sql
CREATE TABLE account_balances (
    account_id      VARCHAR(50) PRIMARY KEY,
    current_balance BIGINT NOT NULL DEFAULT 0,
    currency        CHAR(3) NOT NULL DEFAULT 'USD',
    last_entry_id   UUID REFERENCES ledger_entries(entry_id),
    updated_at      TIMESTAMPTZ NOT NULL
);
```

---

## Step 5 — Consistency: Optimistic Locking

**Problem**: Two concurrent debit operations on the same account.

```
Thread A reads balance=5000, plans to debit 3000
Thread B reads balance=5000, plans to debit 4000
Thread A writes: balance=2000 ✓
Thread B writes: balance=1000 ✗ (should have been rejected: 5000-3000=2000, not enough)
```

**Solution — Version Number (Optimistic Locking)**:
```sql
-- account_balances has a version column
UPDATE account_balances
   SET current_balance = current_balance - 3000,
       version = version + 1
 WHERE account_id = 'ACC123'
   AND version = 42          -- must match expected version
   AND current_balance >= 3000;

-- If 0 rows updated → version mismatch → retry
```

---

## Step 6 — Double-Entry Bookkeeping

Every transaction creates exactly TWO ledger entries:
```
Payment of $1000 from Alice to Bob:

Entry 1: account=ALICE, type=DEBIT,  amount=1000, balance_after=4000
Entry 2: account=BOB,   type=CREDIT, amount=1000, balance_after=6000

Both entries share the same transaction_id.
Sum of all CREDITS - Sum of all DEBITS = 0 (fundamental invariant).
```

**Reconciliation check (runs nightly)**:
```sql
SELECT SUM(CASE WHEN entry_type='CREDIT' THEN amount ELSE -amount END) AS net
FROM ledger_entries;
-- Must equal 0 in a balanced ledger
```

---

## Step 7 — Time Travel Queries

```sql
-- Balance of account ACC123 at any point in time
SELECT balance_after
FROM ledger_entries
WHERE account_id = 'ACC123'
  AND created_at <= '2024-06-15 09:30:00'
ORDER BY created_at DESC
LIMIT 1;

-- Full statement for ACC123 in June 2024
SELECT entry_type, amount, balance_after, description, created_at
FROM ledger_entries
WHERE account_id = 'ACC123'
  AND created_at BETWEEN '2024-06-01' AND '2024-06-30'
ORDER BY created_at;
```

---

## Step 8 — Handling Scale

### Sharding Strategy
- Shard ledger_entries by `account_id` hash
- 256 shards → each shard = manageable DB size
- Cross-shard transactions use Saga pattern (not 2PC — avoids distributed locks)

### Compaction / Snapshots
- Replaying all events from the beginning becomes slow over years
- Solution: periodic snapshots
```
Every midnight: record balance snapshot for each account
Query = last snapshot + events after snapshot timestamp
```

### Partitioning for Retention
```sql
-- Monthly partitions, auto-pruning after 7 years
CREATE TABLE ledger_entries_2024_06
    PARTITION OF ledger_entries
    FOR VALUES FROM ('2024-06-01') TO ('2024-07-01');
```

---

## Step 9 — Trade-offs

| Decision | Chosen | Reason | Trade-off |
|----------|--------|--------|-----------|
| Append-only | Yes | Compliance, audit, replay | Storage grows forever |
| Event Sourcing | Yes | Full history, time-travel | Complex query model |
| CQRS | Yes | Separate read/write optimization | Eventual read consistency |
| Sharding | By account_id hash | Uniform distribution | Cross-account queries need scatter-gather |
| Consistency | Optimistic locking | Better than pessimistic for throughput | Retry on conflict |
