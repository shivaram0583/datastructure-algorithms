# Code Review — JP Morgan Round 1, Question 2

## What to Expect
The interviewer presents **buggy or poorly-written code** and asks you to:
1. Identify all bugs (logical, runtime, security, concurrency)
2. Explain the impact of each bug
3. Provide a corrected version
4. Suggest improvements (performance, readability, design)

## Code Review Checklist

### Correctness
- [ ] Off-by-one errors in loops/array access
- [ ] Null pointer dereferences
- [ ] Integer overflow / underflow
- [ ] Incorrect boundary conditions

### Concurrency
- [ ] Shared mutable state without synchronization
- [ ] Deadlock risk (nested locks)
- [ ] Visibility issues (non-volatile shared fields)
- [ ] Race conditions

### Security
- [ ] SQL injection via string concatenation
- [ ] Hardcoded credentials
- [ ] Sensitive data in logs
- [ ] Unchecked user input

### Resource Management
- [ ] Unclosed streams/connections (use try-with-resources)
- [ ] Static collections that grow unbounded (memory leak)
- [ ] Connections not returned to pool

### Design
- [ ] Violation of Single Responsibility Principle
- [ ] Catch Exception/Throwable (too broad)
- [ ] Magic numbers without constants
- [ ] Missing error handling

## Files

| # | Scenario | Key Issues |
|---|----------|-----------|
| 01 | [Buggy Bank Transaction](./CodeReview_BuggyBankTransaction.java) | NPE, off-by-one, race condition |
| 02 | [Thread Safety Issues](./CodeReview_ThreadSafetyIssues.java) | Deadlock, visibility, atomicity |
| 03 | [Memory & Resource Leaks](./CodeReview_MemoryLeaks.java) | Unclosed streams, static collection leak |
| 04 | [Security Vulnerabilities](./CodeReview_SecurityVulnerabilities.java) | SQL injection, hardcoded secrets, info leakage |

## Interview Tips
- Think out loud as you scan — don't go silent.
- Categorize bugs before fixing: "I see a concurrency issue, a null check, and a resource leak."
- Always verify your fix doesn't introduce a new bug.
- Mention the financial impact: "This race condition could result in double-processing of a payment."
