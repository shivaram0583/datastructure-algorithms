/**
 * CODE REVIEW EXERCISE 02 — Thread Safety Issues
 * ============================================================
 * Scenario:
 *   A developer wrote a shared order counter and a payment processor
 *   used by multiple threads in a trading system. Review for
 *   concurrency bugs.
 *
 * BUGS FOUND:
 *
 *   BUG 1 — Non-atomic Increment (Race Condition)
 *     orderCount++ is NOT atomic. It compiles to read-modify-write:
 *       int temp = orderCount;
 *       temp = temp + 1;
 *       orderCount = temp;
 *     Two threads can both read the same value, both increment, and
 *     one increment is lost. Use AtomicInteger or synchronized.
 *
 *   BUG 2 — Visibility Issue (Missing volatile)
 *     isRunning is a plain boolean. The JVM can cache it in a CPU
 *     register; changes by one thread may be invisible to another.
 *     FIX: Declare as volatile boolean isRunning.
 *
 *   BUG 3 — Deadlock Risk
 *     processPayment() acquires accountLock then ledgerLock.
 *     reconcile()      acquires ledgerLock then accountLock.
 *     If both run concurrently: Thread A holds accountLock waiting for
 *     ledgerLock; Thread B holds ledgerLock waiting for accountLock → DEADLOCK.
 *     FIX: Always acquire locks in the same global order everywhere.
 *
 *   BUG 4 — Double-Checked Locking (Broken without volatile)
 *     getInstance() uses double-checked locking but instance is not volatile.
 *     Due to CPU instruction reordering, a partially constructed object
 *     can be seen by another thread.
 *     FIX: Declare instance as volatile, OR use initialization-on-demand holder.
 *
 *   BUG 5 — Using HashMap in Concurrent Context
 *     pendingOrders is a HashMap shared across threads. Concurrent writes
 *     can corrupt internal structure causing infinite loops or data loss.
 *     FIX: Use ConcurrentHashMap.
 */

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

// ============================================================
// BUGGY VERSION (for review)
// ============================================================
class BuggyOrderService {
    private int orderCount = 0;                    // BUG 1: non-atomic
    private boolean isRunning = true;              // BUG 2: not volatile
    private Map<String, String> pendingOrders = new HashMap<>(); // BUG 5: not thread-safe

    private final Object accountLock = new Object();
    private final Object ledgerLock  = new Object();

    // BUG 1: race condition on orderCount
    public void placeOrder(String orderId) {
        orderCount++;                              // not atomic!
        pendingOrders.put(orderId, "PENDING");
    }

    // BUG 3: acquires accountLock → ledgerLock
    public void processPayment(String orderId) {
        synchronized (accountLock) {
            synchronized (ledgerLock) {            // BUG 3: deadlock risk
                pendingOrders.put(orderId, "PROCESSED");
            }
        }
    }

    // BUG 3: acquires ledgerLock → accountLock (opposite order!)
    public void reconcile() {
        synchronized (ledgerLock) {
            synchronized (accountLock) {           // BUG 3: deadlock risk
                System.out.println("Reconciling " + orderCount + " orders");
            }
        }
    }

    // BUG 2: isRunning may never be seen as false by worker thread
    public void stop() { isRunning = false; }

    public void run() {
        while (isRunning) {                        // BUG 2: may spin forever
            // process orders
        }
    }
}

// BUG 4: Broken singleton without volatile
class BrokenSingleton {
    private static BrokenSingleton instance;       // BUG 4: not volatile

    private BrokenSingleton() {}

    public static BrokenSingleton getInstance() {
        if (instance == null) {
            synchronized (BrokenSingleton.class) {
                if (instance == null) {
                    instance = new BrokenSingleton(); // BUG 4: reordering risk
                }
            }
        }
        return instance;
    }
}

// ============================================================
// FIXED VERSION
// ============================================================
class FixedOrderService {
    private final AtomicInteger orderCount = new AtomicInteger(0); // FIX 1
    private volatile boolean isRunning = true;                      // FIX 2
    private final Map<String, String> pendingOrders = new ConcurrentHashMap<>(); // FIX 5

    private final Object lock1 = new Object(); // consistent name: smaller lock ID first
    private final Object lock2 = new Object();

    public void placeOrder(String orderId) {
        orderCount.incrementAndGet();              // FIX 1: atomic
        pendingOrders.put(orderId, "PENDING");
    }

    // FIX 3: always acquire lock1 → lock2
    public void processPayment(String orderId) {
        synchronized (lock1) {
            synchronized (lock2) {
                pendingOrders.put(orderId, "PROCESSED");
            }
        }
    }

    // FIX 3: same order as processPayment
    public void reconcile() {
        synchronized (lock1) {
            synchronized (lock2) {
                System.out.println("Reconciling " + orderCount.get() + " orders");
            }
        }
    }

    public int getOrderCount() { return orderCount.get(); }

    public void stop() { isRunning = false; }      // FIX 2: volatile write visible

    public void run() {
        while (isRunning) {                        // FIX 2: sees updated value
            // process orders
        }
    }
}

// FIX 4: Initialization-on-demand holder — thread-safe, no volatile needed
class FixedSingleton {
    private FixedSingleton() {}

    private static class Holder {
        private static final FixedSingleton INSTANCE = new FixedSingleton();
    }

    public static FixedSingleton getInstance() {
        return Holder.INSTANCE;
    }
}

// ============================================================
// Alternative FIX 4: volatile double-checked locking
// ============================================================
class VolatileSingleton {
    private static volatile VolatileSingleton instance; // FIX 4: volatile

    private VolatileSingleton() {}

    public static VolatileSingleton getInstance() {
        if (instance == null) {
            synchronized (VolatileSingleton.class) {
                if (instance == null) {
                    instance = new VolatileSingleton();
                }
            }
        }
        return instance;
    }
}

// ============================================================
// Main
// ============================================================
public class CodeReview_ThreadSafetyIssues {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Code Review: Thread Safety ===\n");

        FixedOrderService service = new FixedOrderService();

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 500; i++) service.placeOrder("O-T1-" + i);
        });
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 500; i++) service.placeOrder("O-T2-" + i);
        });

        t1.start(); t2.start();
        t1.join();  t2.join();

        System.out.println("Total orders (expected 1000): " + service.getOrderCount());

        System.out.println("\nSingleton test (same instance): " +
            (FixedSingleton.getInstance() == FixedSingleton.getInstance()));
    }
}
