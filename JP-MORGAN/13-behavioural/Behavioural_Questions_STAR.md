# JP Morgan Chase — Behavioural Round Preparation

## STAR Format
Every answer must follow this structure:
- **S**ituation — Set the context (1-2 sentences)
- **T**ask — What was your responsibility?
- **A**ction — What specific steps did YOU take? (use "I", not "we")
- **R**esult — Quantifiable outcome. What did you learn?

**Time limit**: 2-3 minutes per answer. Practice out loud.

---

## JP Morgan Core Values to Align With
- **Integrity** — Do the right thing even when no one is watching
- **Client focus** — Put clients' long-term interests first
- **Accountability** — Own your mistakes, fix them, prevent recurrence
- **Collaboration** — Work across teams, functions, geographies
- **Excellence** — High-quality code, thorough testing, rigorous thinking

---

## Leadership & Influence

### Q1: Tell me about a time you led a project without formal authority.

**STAR Example**:
- **S**: Our team needed to migrate a monolithic Java service to microservices. I was a mid-level developer with no direct reports.
- **T**: I needed to get 4 senior engineers on board with a new architectural approach they were skeptical about.
- **A**: I created a proof-of-concept over a weekend demonstrating 40% latency reduction. I presented it in a team meeting with benchmarks, addressed each concern individually, and proposed a phased approach to reduce risk. I organized weekly design reviews to keep everyone aligned.
- **R**: The team adopted the approach. We completed the migration in 3 months with zero production incidents. Latency dropped 38% and deployment frequency increased 5×.
- **JP Morgan angle**: "I believe in leading through data and collaboration — the same approach I'd use to influence cross-functional teams here."

---

### Q2: Describe a situation where you had to make a decision with incomplete information.

**STAR Example**:
- **S**: During a major data center migration, a database replication lag alarm fired at 11 PM on a Friday. On-call senior engineers were unavailable.
- **T**: I had to decide whether to halt the migration (costly delay) or continue (risk of data inconsistency).
- **A**: I analyzed the lag trends — lag was increasing but at a decelerating rate. I cross-referenced with historical patterns and determined it was likely a temporary network blip, not a fundamental issue. I set automated monitoring alerts at threshold intervals and documented my decision rationale. I continued the migration but prepared a rollback plan with 15-minute checkpoints.
- **R**: Lag normalized within 20 minutes. Migration completed successfully. I wrote a post-incident review establishing decision criteria for future on-call scenarios.

---

## Conflict & Collaboration

### Q3: Tell me about a time you disagreed with a senior colleague or manager.

**STAR Example**:
- **S**: My tech lead wanted to use a NoSQL document store for a payment processing feature. I believed we needed ACID transactions that NoSQL couldn't provide reliably.
- **T**: I needed to present my concerns without damaging the relationship or appearing insubordinate.
- **A**: I requested a 30-minute meeting. I came prepared with a document comparing both approaches on our specific requirements: transaction isolation, rollback support, and regulatory audit requirements. I proposed a compromise: use PostgreSQL for the core payment ledger and Redis for caching non-critical session data. I framed it as risk reduction, not personal preference.
- **R**: The team adopted the hybrid approach. We had zero data inconsistency incidents in production. The tech lead appreciated the structured analysis and now invites me to architectural reviews.
- **JP Morgan angle**: "In financial systems, I always tie technical decisions back to risk — that's a language every senior stakeholder understands."

---

### Q4: Give an example of a time you had to collaborate with a difficult team member.

**STAR Example**:
- **S**: A QA engineer on my team consistently rejected pull requests with minor stylistic issues rather than functional bugs, blocking releases.
- **T**: I needed to work effectively with them without creating tension or escalating to management.
- **A**: I set up a 1:1 coffee chat (non-confrontational setting) and asked about their quality standards and concerns. I learned they had experienced a production incident caused by "trivial" code issues in the past. Together, we created a pre-commit linting checklist that automated style checks. I also proposed separating style issues (advisory) from functional issues (blocking) in PR reviews.
- **R**: PR cycle time dropped from 5 days to 1.5 days. The QA engineer became one of my strongest collaborators. The linting process was adopted team-wide.

---

## Problem Solving & Ownership

### Q5: Tell me about the most challenging technical problem you've solved.

**STAR Example**:
- **S**: Our payment processing service was experiencing random 5-second latency spikes affecting 0.3% of transactions. This was invisible in average latency metrics but impacting high-value clients.
- **T**: Diagnose and fix the issue with no reproduction in staging.
- **A**: I added percentile-level latency tracking (P95, P99, P999) to our dashboards. I correlated spike timestamps with GC logs and found full GC events aligning perfectly. I profiled heap usage and traced it to a cache that was retaining large deserialized payment objects. I implemented object pooling and reduced cache TTL. I also switched from CMS to G1GC for more predictable pause times.
- **R**: P99 latency dropped from 5200ms to 280ms. No further latency spike complaints. Presented findings at the company's tech talk series.

---

### Q6: Describe a time you failed and what you learned.

**STAR Example**:
- **S**: I deployed a database schema change during peak trading hours without proper rollback planning, causing a 12-minute outage.
- **T**: I was responsible for the deployment and had to manage the incident.
- **A**: I immediately rolled back the schema change (had a backup), communicated proactively to stakeholders every 5 minutes, and worked with the DBA to restore service. Afterward, I drafted a new deployment protocol: all schema changes in off-hours, with an automated rollback script pre-tested in staging, and a sign-off checklist requiring two senior engineers.
- **R**: The protocol was adopted company-wide. In the 18 months since, zero schema-related outages. I turned the failure into a process improvement that protected the entire organization.
- **JP Morgan angle**: "I take ownership of my mistakes. The measure of a professional isn't whether they fail — it's what they do immediately after."

---

## Pressure & Time Management

### Q7: Tell me about a time you worked under extreme time pressure.

**STAR Example**:
- **S**: Three days before a major regulatory deadline, a critical data transformation pipeline failed silently — we discovered the output data was corrupted.
- **T**: Fix the pipeline and re-process 6 months of data in 72 hours.
- **A**: I immediately triaged: identified the corruption root cause (a null coalescing bug introduced in a recent update), fixed it in 2 hours. Then I parallelized the backfill job across 20 EC2 instances, reducing reprocessing time from 96 hours to 18 hours. I communicated status to compliance and legal teams every 6 hours, with realistic ETAs.
- **R**: Data was delivered to regulators 8 hours before the deadline. I created automated data validation checks post-run to prevent silent failures in future.

---

### Q8: How do you prioritize when you have multiple urgent tasks?

**Framework to use**:
1. **Impact**: Which task blocks the most people or has the highest business risk?
2. **Urgency**: Regulatory deadlines > client-facing outages > internal requests
3. **Effort**: Can I unblock someone in 10 minutes? Do that first.
4. **Communication**: Tell stakeholders of other tasks your ETA immediately

**Example at JP Morgan**: "If I simultaneously had a production payment outage and a non-critical report due, I'd escalate the outage to the team, take the lead on the incident, and proactively update the report stakeholder with a revised ETA. Revenue impact always takes priority over deadlines that can be rescheduled."

---

## JP Morgan-Specific Questions

### Q9: Why JP Morgan Chase specifically?

**Key points to hit**:
- Scale: JP Morgan processes $10 trillion/day — engineering problems here are uniquely challenging
- Impact: Systems I build will directly affect millions of clients and global financial stability
- Culture of excellence: Known for rigorous engineering standards and strong mentorship
- Specific team: "I'm particularly excited about [Trading Technology / Payments / Digital Banking] because..."
- Personal connection: Any genuine interaction with JP Morgan products or technology

**Sample answer**: "JP Morgan's scale is unmatched — engineering here means building systems that must be correct by financial definition, not just functionally correct. I want to work on problems where the bar is higher, where exactly-once semantics aren't optional, and where system design decisions are driven by regulatory requirements as much as by technical elegance. The opportunity to contribute to infrastructure that underpins global financial markets is genuinely motivating."

---

### Q10: Where do you see yourself in 5 years?

**Answer framework for JP Morgan**:
- Short-term (Year 1-2): Master the domain, contribute meaningfully to the team, understand the business deeply
- Medium-term (Year 3-4): Lead technical design decisions, mentor junior engineers, drive cross-team initiatives
- Long-term (Year 5+): Architect-level thinking, build systems that define industry standards

**Sample**: "In 5 years I see myself as a technical lead — someone who can bridge the gap between business requirements and engineering solutions. JP Morgan's rotation programs and scale mean I'd gain exposure to trading technology, risk systems, and payments — that breadth is exactly what I need to grow into that role."

---

### Q11: Tell me about a time you improved a process or system.

**STAR Example**:
- **S**: Our team's code review process had no SLA — PRs sat unreviewed for 3-7 days, blocking feature delivery.
- **T**: Improve review turnaround without adding burden to senior engineers.
- **A**: I analyzed 3 months of PR data. Found 70% of comments were on the same 5 recurring issues (missing null checks, non-atomic operations, missing tests). I created a PR template with a self-review checklist covering these points. I also introduced a 24-hour review SLA rule — any PR open >24h gets auto-assigned to an on-call reviewer.
- **R**: Median review time dropped from 5.2 days to 18 hours. Recurring code issues reduced by 60%. Feature delivery frequency increased 40%.

---

## Questions to Ask JP Morgan Interviewer

Always prepare 3-5 thoughtful questions:

1. "What does the on-call rotation look like for this team, and how do you balance engineering time between new features and reliability work?"
2. "What's the biggest technical challenge this team is currently tackling?"
3. "How does the team approach technical debt — is there structured time allocated for it?"
4. "What does the career progression look like from this role, and what distinguishes the top performers?"
5. "How closely do engineers interact with traders or business stakeholders? Is there an opportunity to understand the domain deeply?"

---

## Behavioural Question Bank (Practice These)

| Category | Questions |
|----------|-----------|
| Leadership | Lead without authority, influence upward, mentor someone |
| Conflict | Disagree with manager, difficult teammate, competing priorities |
| Failure | Biggest mistake, missed deadline, production incident |
| Achievement | Most proud of, exceeded expectations, innovative solution |
| Pressure | Tight deadline, ambiguous requirements, multiple priorities |
| Growth | Skill you developed, feedback you received, career pivot |
| JP Morgan | Why JPMC, why this role, 5-year plan, work-life philosophy |
| Ethics | Discovered a compliance issue, pressure to cut corners, data privacy |
