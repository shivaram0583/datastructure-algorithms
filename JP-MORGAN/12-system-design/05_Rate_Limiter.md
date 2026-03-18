# System Design: Rate Limiter

## Problem Statement
Design a distributed rate limiter that limits the number of API requests a client can make per time window. Used to protect JP Morgan's internal APIs from abuse, DDoS attacks, and runaway trading algorithms.

---

## Step 1 — Clarify Requirements

### Functional Requirements
- Limit requests per client (by API key, IP, user ID)
- Different rate limits per endpoint / tier (e.g., premium vs. standard)
- Return HTTP 429 Too Many Requests when limit exceeded
- Support multiple time windows (per second, per minute, per hour)
- Minimal latency overhead (< 5ms per check)

### Non-Functional Requirements
- **Distributed**: Consistent limits across multiple API server instances
- **Availability**: Rate limiter failure should fail open (don't block legitimate traffic)
- **Throughput**: 1 million requests/second
- **Accuracy**: < 1% error rate on limit enforcement

---

## Step 2 — Rate Limiting Algorithms

### Algorithm 1 — Fixed Window Counter
```
Divide time into fixed windows (e.g., every 60 seconds).
Count requests per window. Reset at window boundary.

Window: 00:00 → 00:59, 01:00 → 01:59, ...
Client makes 99 requests at 00:59 and 99 requests at 01:00 → 198 in 2 seconds!
Problem: Boundary burst (2× the limit possible at window edge).
```

### Algorithm 2 — Sliding Window Log
```
Store timestamps of all requests in a sorted set.
On each request: remove entries older than windowSize, count remaining.
If count < limit → allow, add current timestamp.

Pros: Accurate
Cons: High memory (stores all timestamps)
```

### Algorithm 3 — Sliding Window Counter (Recommended)
```
Hybrid: combines fixed window accuracy with sliding window smoothness.

current_window_count + (prev_window_count × overlap_fraction)

Example (limit=100, window=60s):
  It is 01:15 (25% into current window, 75% overlap with previous)
  prev_window_count = 84, current_window_count = 36
  effective_count = 36 + (84 × 0.75) = 36 + 63 = 99 → allow

Pros: Accurate, memory efficient
Cons: Slightly approximate (but < 1% error)
```

### Algorithm 4 — Token Bucket
```
Bucket holds up to `capacity` tokens. Tokens refill at `rate` per second.
Each request consumes 1 token. If bucket empty → reject.

Pros: Allows controlled bursting (send capacity tokens at once)
Cons: Complex distributed implementation
```

### Algorithm 5 — Leaky Bucket
```
Requests enter a queue; processed at a fixed rate.
Overflow → reject.

Pros: Smooth output rate
Cons: Queue adds latency; bursts delayed not rejected
```

---

## Step 3 — Architecture

```
          API Gateway / Load Balancer
                    │
                    ▼
         ┌─────────────────────┐
         │   Rate Limiter      │
         │   Middleware        │◀── Rules Config Service
         └──────────┬──────────┘
                    │  (check limit)
                    ▼
         ┌─────────────────────┐
         │   Redis Cluster     │  (shared state across all servers)
         │   (counters)        │
         └──────────┬──────────┘
                    │
         ┌──────────┴──────────┐
         ▼                     ▼
    Allow request          Reject (429)
    (pass to API)          Return headers:
                             X-RateLimit-Limit: 100
                             X-RateLimit-Remaining: 0
                             X-RateLimit-Reset: 1704067200
                             Retry-After: 45
```

---

## Step 4 — Implementation: Sliding Window Counter with Redis

### Redis Data Structure
```
Key format: rate_limit:{clientId}:{windowStart}
Type: Integer (INCR)
TTL: 2 × windowSize (keep prev window)

Keys:
  rate_limit:client123:1704067200  (current minute)  = 36
  rate_limit:client123:1704067140  (previous minute) = 84
```

### Lua Script (Atomic Check + Increment)
```lua
-- KEYS[1] = current window key
-- KEYS[2] = previous window key
-- ARGV[1] = limit
-- ARGV[2] = current window start (unix seconds)
-- ARGV[3] = overlap fraction (0.0 to 1.0)
-- ARGV[4] = window size in seconds

local curr = tonumber(redis.call('GET', KEYS[1]) or 0)
local prev = tonumber(redis.call('GET', KEYS[2]) or 0)
local limit = tonumber(ARGV[1])
local overlap = tonumber(ARGV[3])

local effective = curr + (prev * overlap)

if effective < limit then
    redis.call('INCR', KEYS[1])
    redis.call('EXPIRE', KEYS[1], tonumber(ARGV[4]) * 2)
    return {1, limit - math.floor(effective) - 1}  -- allowed, remaining
else
    return {0, 0}  -- rejected
end
```

**Why Lua?** Redis executes Lua scripts atomically — no race condition between read and increment.

---

## Step 5 — Distributed Considerations

### Problem: Multiple Redis nodes
- Use Redis Cluster with consistent hashing
- `clientId` maps to a specific shard → all requests from same client go to same Redis node

### Problem: Redis node failure
- Fail open: if Redis is unreachable, allow the request (availability > strict limiting)
- Alert ops team immediately
- Fallback to in-process approximate limiter

### Problem: Clock skew between servers
- Use Redis server clock (via `TIME` command) — single source of truth
- Do not use application server clocks (they can drift)

---

## Step 6 — Rate Limit Rules Configuration

```json
{
  "rules": [
    { "tier": "free",     "endpoint": "/api/v1/orders",   "limit": 10,   "window": "1s" },
    { "tier": "premium",  "endpoint": "/api/v1/orders",   "limit": 1000, "window": "1s" },
    { "tier": "internal", "endpoint": "/api/v1/orders",   "limit": 10000,"window": "1s" },
    { "tier": "all",      "endpoint": "/api/v1/market",   "limit": 100,  "window": "1m" }
  ]
}
```

Rules stored in Redis (hot config) + database (source of truth).
Config service pushes updates; rate limiters cache locally with 30s TTL.

---

## Step 7 — Trade-offs

| Algorithm | Accuracy | Memory | Burst Handling | Complexity |
|-----------|----------|--------|---------------|------------|
| Fixed Window | Low (boundary burst) | Low | Poor | Simple |
| Sliding Log | Exact | High | Good | Medium |
| Sliding Counter | ~99% | Low | Good | Medium |
| Token Bucket | Exact | Low | Excellent | High (distributed) |
| Leaky Bucket | Exact | Medium | Smoothed | Medium |

**Recommended**: Sliding Window Counter for most APIs. Token Bucket for burst-tolerant endpoints.

| Decision | Chosen | Trade-off |
|----------|--------|-----------|
| State store | Redis | Fast, distributed — but single point (mitigated by cluster) |
| Atomicity | Lua script | Correct — slight complexity |
| Failure mode | Fail open | High availability — brief over-limit possible |
| Config | Centralized + local cache | Consistent — brief config staleness |
