# JP Morgan Chase — Interview Preparation (Java)

A complete, round-wise preparation guide for **JP Morgan Chase SDE** interviews.
Every section is mapped directly to the 3-round interview structure.

---

## Interview Structure

| Round | Type | Focus |
|-------|------|-------|
| Round 1 | DSA | Problem Solving (1 question) + Code Review (1 question) |
| Round 2 | System Design | Design financial/distributed systems |
| Round 3 | Behavioural | Leadership, conflict, decision-making (STAR format) |

---

## DSA Topics

| # | Topic | Problems |
|---|-------|----------|
| 01 | [Arrays & Strings](./01-arrays-and-strings) | 5 |
| 02 | [Linked Lists](./02-linked-lists) | 4 |
| 03 | [Stacks & Queues](./03-stacks-and-queues) | 3 |
| 04 | [Trees & BST](./04-trees-and-bst) | 4 |
| 05 | [Graphs](./05-graphs) | 3 |
| 06 | [Dynamic Programming](./06-dynamic-programming) | 4 |
| 07 | [Hashing](./07-hashing) | 3 |
| 08 | [Sliding Window & Two Pointers](./08-sliding-window-two-pointers) | 3 |
| 09 | [Heap & Priority Queue](./09-heap-and-priority-queue) | 3 |
| 10 | [Sorting & Searching](./10-sorting-and-searching) | 3 |

**Total: 35 DSA problems**

---

## Round 1 — Code Review

| # | Scenario | Key Issues |
|---|----------|-----------|
| 01 | [Buggy Bank Transaction](./11-code-review/CodeReview_BuggyBankTransaction.java) | Off-by-one, null check, race condition |
| 02 | [Thread Safety Issues](./11-code-review/CodeReview_ThreadSafetyIssues.java) | Shared state, deadlock, visibility |
| 03 | [Memory & Resource Leaks](./11-code-review/CodeReview_MemoryLeaks.java) | Unclosed streams, static collections |
| 04 | [Security Vulnerabilities](./11-code-review/CodeReview_SecurityVulnerabilities.java) | SQL injection, hardcoded secrets |

---

## Round 2 — System Design

| # | Problem | Domain |
|---|---------|--------|
| 01 | [Trading Order Management System](./12-system-design/01_Trading_Order_Management_System.md) | Finance |
| 02 | [Payment Processing System](./12-system-design/02_Payment_Processing_System.md) | Finance |
| 03 | [Real-Time Market Data Feed](./12-system-design/03_Real_Time_Market_Data_Feed.md) | Finance |
| 04 | [Distributed Transaction Ledger](./12-system-design/04_Distributed_Transaction_Ledger.md) | Finance |
| 05 | [Rate Limiter](./12-system-design/05_Rate_Limiter.md) | Infrastructure |
| 06 | [URL Shortener](./12-system-design/06_URL_Shortener.md) | Classic |
| **Concepts** | [System Design Core Concepts](./12-system-design/00_System_Design_Concepts.md) | Theory |

---

## Round 3 — Behavioural

| File | Content |
|------|---------|
| [Behavioural Guide](./13-behavioural/Behavioural_Questions_STAR.md) | 20+ questions with STAR-format answers |

---

## Solution Format

Every `.java` DSA file follows:

```
1. Problem statement & constraints
2. JP Morgan relevance note
3. Approach 1 — Brute Force      (TC / SC)
4. Approach 2 — Better           (TC / SC)
5. Approach 3 — Optimal / Best   (TC / SC)
6. Step-by-step dry run
7. main() with test cases
```

---

## Difficulty Legend

| Tag | Meaning |
|-----|---------|
| 🟢 | Easy |
| 🟡 | Medium |
| 🔴 | Hard |

---

## Study Plan

| Week | Focus |
|------|-------|
| 1 | Arrays, Strings, Hashing, Sliding Window |
| 2 | Linked Lists, Stacks, Queues, Heap |
| 3 | Trees, BST, Graphs |
| 4 | Dynamic Programming, Sorting & Searching |
| 5 | Code Review practice + System Design |
| 6 | Behavioural prep + Mock Interviews |

Good luck with your JP Morgan Chase interview!
