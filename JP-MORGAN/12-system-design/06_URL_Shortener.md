# System Design: URL Shortener (Classic)

## Problem Statement
Design a URL shortening service (like bit.ly). This is a **classic system design warm-up** often asked to test fundamentals before moving to financial domain problems.

---

## Step 1 — Clarify Requirements

### Functional Requirements
- Shorten a long URL to a short URL (e.g., jpmorgan.com/s/abc123)
- Redirect short URL to the original long URL
- Custom aliases (optional, e.g., jpmorgan.com/s/q4-report)
- URL expiration (optional TTL)
- Analytics: click counts, geographic data (optional)

### Non-Functional Requirements
- **Read-heavy**: 100:1 read-to-write ratio
- **Throughput**: 1,000 writes/second, 100,000 reads/second
- **Latency**: Redirect < 10ms (P99)
- **Availability**: 99.99%
- **Short URL length**: 6-8 characters (alphanumeric)

---

## Step 2 — Capacity Estimation

```
Writes:    1,000 URLs/second = 86M URLs/day
Reads:     100,000 redirects/second
Storage:   1 URL record ≈ 500 bytes
           86M × 365 days × 5 years × 500B ≈ 78 TB

Short URL space:
  Characters: [a-z, A-Z, 0-9] = 62
  Length 7: 62^7 = 3.5 trillion unique URLs ← more than enough
```

---

## Step 3 — API Design

```
POST /api/shorten
  Body: { longUrl, customAlias?, expiresAt? }
  Returns: { shortUrl: "https://jpmorgan.com/s/abc1234", expiresAt }

GET /s/{shortCode}
  Returns: HTTP 301 Redirect to longUrl
  (Use 302 if analytics tracking needed — bypasses browser cache)

DELETE /api/urls/{shortCode}
  Returns: 204 No Content

GET /api/urls/{shortCode}/stats
  Returns: { shortCode, clickCount, createdAt, lastAccessed }
```

---

## Step 4 — Short Code Generation

### Option A — Hash (MD5/SHA256 + truncate)
```
MD5(longUrl) = "550e8400e29b41d4a716446655440000"
Take first 7 chars: "550e840"

Problem: Collisions — two different URLs can hash to same 7 chars.
Fix: On collision, append counter and rehash.
TC: O(1) average but collision handling is complex.
```

### Option B — Counter + Base62 Encoding (Recommended)
```
Maintain a global auto-increment counter (start at 1).
Encode counter in base62.

1        → "1"
61       → "Z"
62       → "10"  (base62)
238,328  → "abc"
3.5T     → 7 chars max

Step-by-step (counter=125):
  125 ÷ 62 = 2 remainder 1 → digits = [1, 2]  (read in reverse)
  Base62 alphabet[2] = '2', alphabet[1] = '1'
  shortCode = "21"
```

**Problem with single counter**: Single point of failure.

**Solution — Ticket Server or Range-based counters**:
```
Each application server requests a range: [1000000, 1001000]
Uses range locally. Requests next range when exhausted.
Ticket Server: a dedicated service (or ZooKeeper) manages ranges.
```

### Option C — Random + Check DB
```
Generate random 7-char string. Check DB for collision. Insert if unique.
Simple but requires a DB round-trip per write.
```

---

## Step 5 — High-Level Architecture

```
         Client
           │
           │ POST /api/shorten
           ▼
  ┌─────────────────┐
  │   API Servers   │  (stateless, horizontally scalable)
  │   (Write Path)  │
  └────────┬────────┘
           │
           ▼
  ┌─────────────────┐     ┌──────────────────┐
  │  Counter/       │────▶│  URL Database    │
  │  ID Generator   │     │  (MySQL/Postgres) │
  │  (ZooKeeper /   │     └──────────────────┘
  │   Ticket Server)│
  └─────────────────┘


         Client
           │
           │ GET /s/abc1234
           ▼
  ┌─────────────────┐
  │   CDN / Edge    │  (cache popular URLs at edge — near-zero latency)
  │   Nodes         │
  └────────┬────────┘
           │ (cache miss)
           ▼
  ┌─────────────────┐
  │   Read Servers  │
  └────────┬────────┘
           │ (cache miss)
           ▼
  ┌─────────────────┐
  │   Redis Cache   │  shortCode → longUrl (TTL = 24h)
  └────────┬────────┘
           │ (cache miss)
           ▼
  ┌─────────────────┐
  │   URL Database  │  (read replicas for scale)
  └─────────────────┘
```

---

## Step 6 — Database Schema

```sql
CREATE TABLE urls (
    id          BIGINT PRIMARY KEY,          -- auto-increment counter
    short_code  VARCHAR(10) UNIQUE NOT NULL, -- base62(id)
    long_url    TEXT NOT NULL,
    user_id     VARCHAR(50),
    created_at  TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    expires_at  TIMESTAMPTZ,
    click_count BIGINT DEFAULT 0
);

CREATE INDEX idx_urls_short_code ON urls(short_code);

-- Analytics table (separate for write performance)
CREATE TABLE url_clicks (
    id          BIGSERIAL PRIMARY KEY,
    short_code  VARCHAR(10) NOT NULL,
    clicked_at  TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    ip_address  INET,
    user_agent  TEXT,
    country     CHAR(2)
);
```

---

## Step 7 — Redirect Flow (Critical Path)

```
1. Browser hits CDN with GET /s/abc1234
2. CDN checks edge cache → HIT → 301 redirect instantly (< 1ms)
3. CDN cache MISS → forward to Read Server
4. Read Server checks Redis → HIT → 301 redirect (< 5ms)
5. Redis MISS → query MySQL read replica → cache in Redis → redirect (< 20ms)

301 (Permanent) vs 302 (Temporary):
  301: Browser caches → no future server hits → lower load, no analytics
  302: No browser cache → every click hits server → accurate analytics
  JP Morgan internal use → 301 (reduce load)
  Analytics product → 302 (track every click)
```

---

## Step 8 — Trade-offs

| Decision | Chosen | Reason | Trade-off |
|----------|--------|--------|-----------|
| ID generation | Counter + Base62 | No collisions, predictable | Sequential → guessable (add random salt if needed) |
| Caching | Redis + CDN | Sub-ms reads | Stale cache after URL deletion |
| Redirect type | 301 | Lower server load | No per-click analytics |
| DB | MySQL (relational) | Simple, mature | Less scalable than NoSQL at extreme scale |
| Analytics | Async (Kafka) | Don't block redirect | Eventual consistency |
