/**
 * CODE REVIEW EXERCISE 03 — Memory & Resource Leaks
 * ============================================================
 * Scenario:
 *   A developer wrote code to read trade data from a file and
 *   cache results. Review for resource and memory leaks.
 *
 * BUGS FOUND:
 *
 *   BUG 1 — Unclosed InputStream (Resource Leak)
 *     FileInputStream is opened but never closed. If an exception
 *     is thrown during reading, the stream leaks a file descriptor.
 *     FIX: Use try-with-resources.
 *
 *   BUG 2 — Unclosed Database Connection (Resource Leak)
 *     Connection is opened but never closed in the finally block.
 *     Each call leaks a DB connection; connection pool exhausted quickly.
 *     FIX: try-with-resources or explicit close in finally.
 *
 *   BUG 3 — Static Cache Without Eviction (Memory Leak)
 *     A static HashMap grows indefinitely as trade IDs are added.
 *     In a long-running JVM, this fills the heap.
 *     FIX: Use a bounded LRU cache, WeakHashMap, or Caffeine/Guava Cache.
 *
 *   BUG 4 — Catching and Swallowing Exception
 *     catch (Exception e) {} silently discards errors. The caller
 *     gets no indication that reading failed. Extremely dangerous in
 *     financial code — silent failures = incorrect balances/positions.
 *     FIX: At minimum log the exception; better, rethrow or wrap.
 *
 *   BUG 5 — String Concatenation in Loop (Performance, not leak)
 *     Building a large String with += in a loop creates O(n²) temporary
 *     String objects, stressing GC.
 *     FIX: Use StringBuilder.
 */

import java.io.*;
import java.sql.*;
import java.util.*;

// ============================================================
// BUGGY VERSION (for review)
// ============================================================
class BuggyTradeReader {

    // BUG 3: static unbounded cache — memory leak
    private static Map<String, String> tradeCache = new HashMap<>();

    // BUG 1: stream never closed; BUG 4: exception swallowed
    public String readTradeFile(String path) {
        try {
            FileInputStream fis = new FileInputStream(path); // BUG 1
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            String result = "";
            String line;
            while ((line = reader.readLine()) != null) {
                result += line + "\n"; // BUG 5: String concat in loop
            }
            return result;
        } catch (Exception e) {
            // BUG 4: silently swallowed
        }
        return null;
    }

    // BUG 2: connection never closed
    public void saveToDb(String tradeId, String data) throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:h2:mem:test"); // BUG 2
        PreparedStatement ps = conn.prepareStatement(
            "INSERT INTO trades (id, data) VALUES (?, ?)");
        ps.setString(1, tradeId);
        ps.setString(2, data);
        ps.executeUpdate();
        // BUG 2: conn and ps never closed
    }

    // BUG 3: grows unbounded
    public void cacheResult(String tradeId, String result) {
        tradeCache.put(tradeId, result);
    }
}

// ============================================================
// FIXED VERSION
// ============================================================
class FixedTradeReader {

    private static final int MAX_CACHE_SIZE = 1000;
    // FIX 3: bounded LRU cache using LinkedHashMap
    private static final Map<String, String> tradeCache =
        Collections.synchronizedMap(
            new LinkedHashMap<String, String>(MAX_CACHE_SIZE, 0.75f, true) {
                @Override
                protected boolean removeEldestEntry(Map.Entry<String, String> eldest) {
                    return size() > MAX_CACHE_SIZE;
                }
            });

    // FIX 1: try-with-resources closes stream automatically
    // FIX 4: exception is logged and rethrown (or wrapped)
    // FIX 5: StringBuilder instead of String concatenation
    public String readTradeFile(String path) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            StringBuilder sb = new StringBuilder(); // FIX 5
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
            return sb.toString();
        } catch (IOException e) {
            System.err.println("Failed to read trade file: " + path + " — " + e.getMessage()); // FIX 4
            throw e; // rethrow so caller knows it failed
        }
    }

    // FIX 2: try-with-resources closes connection and statement
    public void saveToDb(String tradeId, String data) throws SQLException {
        try (Connection conn = DriverManager.getConnection("jdbc:h2:mem:test");
             PreparedStatement ps = conn.prepareStatement(
                 "INSERT INTO trades (id, data) VALUES (?, ?)")) {
            ps.setString(1, tradeId);
            ps.setString(2, data);
            ps.executeUpdate();
        } // FIX 2: conn and ps auto-closed
    }

    // FIX 3: bounded cache with eviction
    public void cacheResult(String tradeId, String result) {
        tradeCache.put(tradeId, result);
    }
}

// ============================================================
// Main
// ============================================================
public class CodeReview_MemoryLeaks {
    public static void main(String[] args) {
        System.out.println("=== Code Review: Memory & Resource Leaks ===\n");
        System.out.println("Key fixes summary:");
        System.out.println("  BUG 1: FileInputStream → try-with-resources BufferedReader");
        System.out.println("  BUG 2: DB Connection   → try-with-resources Connection+Statement");
        System.out.println("  BUG 3: Static HashMap  → Bounded LRU LinkedHashMap (max=1000)");
        System.out.println("  BUG 4: Silent catch    → Log + rethrow");
        System.out.println("  BUG 5: String+= loop   → StringBuilder.append()");

        FixedTradeReader reader = new FixedTradeReader();
        reader.cacheResult("TRADE-001", "BUY 100 AAPL @ 150.00");
        reader.cacheResult("TRADE-002", "SELL 50 MSFT @ 300.00");
        System.out.println("\nCached 2 trades successfully (bounded LRU cache).");
    }
}
