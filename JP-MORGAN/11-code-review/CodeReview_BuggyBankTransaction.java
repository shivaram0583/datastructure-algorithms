/**
 * CODE REVIEW EXERCISE 01 — Buggy Bank Transaction
 * ============================================================
 * Scenario:
 *   A junior developer wrote a BankAccount class that handles deposits,
 *   withdrawals, and transaction history. Review it, find all bugs,
 *   and provide a corrected version.
 *
 * BUGS FOUND (read after attempting review yourself):
 *
 *   BUG 1 — Race Condition (Critical)
 *     withdraw() checks balance and deducts in two separate steps
 *     without synchronization. Two threads can both pass the balance
 *     check and both deduct, resulting in negative balance.
 *     FIX: Make withdraw() synchronized or use AtomicLong for balance.
 *
 *   BUG 2 — Null Pointer Dereference
 *     getLastTransaction() returns transactions.get(transactions.size()-1)
 *     without checking if the list is empty. Throws IndexOutOfBoundsException.
 *     FIX: Check if transactions.isEmpty() first.
 *
 *   BUG 3 — Off-by-One in printLast()
 *     Loop runs `i <= transactions.size()` → throws IndexOutOfBoundsException
 *     at the last iteration.
 *     FIX: Change <= to <
 *
 *   BUG 4 — Memory Leak / Unbounded Growth
 *     transactions list grows forever. In a long-running system, this
 *     will exhaust heap memory.
 *     FIX: Cap the list size or use a circular buffer / rolling log.
 *
 *   BUG 5 — Integer Overflow
 *     balance is stored as int. For a bank system, amounts can exceed
 *     Integer.MAX_VALUE (2.1 billion cents).
 *     FIX: Use long or BigDecimal.
 *
 *   BUG 6 — Negative Deposit Allowed
 *     deposit() does not validate that amount > 0. A negative deposit
 *     effectively reduces the balance, bypassing withdraw() controls.
 *     FIX: Add validation.
 *
 *   BUG 7 — toString in Logging Exposes Sensitive Data
 *     printAccount() logs full account details including balance to stdout.
 *     In production, this would appear in logs — security violation.
 *     FIX: Use a proper logging framework with appropriate log levels;
 *          mask sensitive data.
 */

import java.util.ArrayList;
import java.util.List;

// ============================================================
// BUGGY VERSION (for review)
// ============================================================
class BuggyBankAccount {
    private int balance;                         // BUG 5: should be long
    private List<String> transactions = new ArrayList<>(); // BUG 4: unbounded

    public BuggyBankAccount(int initialBalance) {
        this.balance = initialBalance;
    }

    // BUG 1: not synchronized — race condition
    // BUG 6: no validation on amount
    public void deposit(int amount) {
        balance += amount;
        transactions.add("DEPOSIT: " + amount);
    }

    // BUG 1: not synchronized — race condition between check and deduct
    public boolean withdraw(int amount) {
        if (balance >= amount) {
            balance -= amount;                   // another thread may have changed balance
            transactions.add("WITHDRAW: " + amount);
            return true;
        }
        return false;
    }

    // BUG 2: no empty check — IndexOutOfBoundsException if list is empty
    public String getLastTransaction() {
        return transactions.get(transactions.size() - 1);
    }

    // BUG 3: off-by-one — i <= size causes IndexOutOfBoundsException
    public void printLast(int n) {
        int start = Math.max(0, transactions.size() - n);
        for (int i = start; i <= transactions.size(); i++) { // BUG: should be <
            System.out.println(transactions.get(i));
        }
    }

    // BUG 7: exposes sensitive balance data to logs
    public void printAccount() {
        System.out.println("Account balance: " + balance); // security risk
    }
}

// ============================================================
// FIXED VERSION
// ============================================================
class FixedBankAccount {
    private static final int MAX_TRANSACTION_LOG = 1000; // FIX 4: cap size
    private long balance;                                  // FIX 5: use long
    private final List<String> transactions = new ArrayList<>();

    public FixedBankAccount(long initialBalance) {
        if (initialBalance < 0) throw new IllegalArgumentException("Initial balance cannot be negative");
        this.balance = initialBalance;
    }

    // FIX 1+6: synchronized + validation
    public synchronized void deposit(long amount) {
        if (amount <= 0) throw new IllegalArgumentException("Deposit amount must be positive");
        balance += amount;
        addTransaction("DEPOSIT: " + amount);
    }

    // FIX 1: synchronized — atomicity between check and deduct
    public synchronized boolean withdraw(long amount) {
        if (amount <= 0) throw new IllegalArgumentException("Withdrawal amount must be positive");
        if (balance >= amount) {
            balance -= amount;
            addTransaction("WITHDRAW: " + amount);
            return true;
        }
        addTransaction("WITHDRAW_FAILED: " + amount + " (insufficient funds)");
        return false;
    }

    // FIX 2: empty check
    public String getLastTransaction() {
        if (transactions.isEmpty()) return "No transactions yet";
        return transactions.get(transactions.size() - 1);
    }

    // FIX 3: i < size (not <=)
    public void printLast(int n) {
        int start = Math.max(0, transactions.size() - n);
        for (int i = start; i < transactions.size(); i++) { // FIX
            System.out.println(transactions.get(i));
        }
    }

    // FIX 4: cap transaction log
    private void addTransaction(String entry) {
        if (transactions.size() >= MAX_TRANSACTION_LOG) {
            transactions.remove(0);
        }
        transactions.add(entry);
    }

    // FIX 7: no sensitive data exposed; use logger in real code
    public void printAccountSummary() {
        System.out.println("Account has " + transactions.size() + " transactions.");
    }

    public long getBalance() { return balance; }
}

// ============================================================
// Main — Demonstrate fixes
// ============================================================
public class CodeReview_BuggyBankTransaction {
    public static void main(String[] args) {
        System.out.println("=== Code Review: Bank Transaction ===\n");

        System.out.println("--- Fixed Account ---");
        FixedBankAccount acc = new FixedBankAccount(1000L);
        acc.deposit(500L);
        System.out.println("After deposit(500): balance=" + acc.getBalance());
        boolean ok = acc.withdraw(200L);
        System.out.println("withdraw(200): " + ok + ", balance=" + acc.getBalance());
        boolean fail = acc.withdraw(5000L);
        System.out.println("withdraw(5000): " + fail + " (insufficient)");
        System.out.println("Last tx: " + acc.getLastTransaction());
        System.out.println("Last 3:");
        acc.printLast(3);

        System.out.println("\n--- Edge case: empty account ---");
        FixedBankAccount empty = new FixedBankAccount(0L);
        System.out.println("Last tx: " + empty.getLastTransaction());
    }
}
