/**
 * CODE REVIEW EXERCISE 04 — Security Vulnerabilities
 * ============================================================
 * Scenario:
 *   A developer wrote a trade lookup service that queries the database
 *   and logs results. Review for security vulnerabilities.
 *
 * BUGS FOUND:
 *
 *   BUG 1 — SQL Injection
 *     String concatenation is used to build SQL queries with user input.
 *     Attacker can input: "'; DROP TABLE trades; --"
 *     This destroys the trades table — catastrophic for a bank.
 *     FIX: Always use PreparedStatement with parameterized queries.
 *
 *   BUG 2 — Hardcoded Credentials
 *     Database password is hardcoded in source code. If this file is
 *     committed to version control, credentials are permanently exposed.
 *     FIX: Load from environment variables or a secrets manager
 *          (e.g., AWS Secrets Manager, HashiCorp Vault).
 *
 *   BUG 3 — Sensitive Data in Logs
 *     Account number and full trade details are logged in plaintext.
 *     Log files are often accessible to many people or systems.
 *     FIX: Mask PII/sensitive data in logs. Log a reference ID instead.
 *
 *   BUG 4 — Exposing Stack Traces to Users
 *     e.printStackTrace() sends stack trace to stderr which may appear
 *     in user-facing error messages in some frameworks.
 *     FIX: Log internally; return a generic error message to the user.
 *
 *   BUG 5 — Missing Input Validation
 *     No validation on accountId length or format. An attacker could
 *     send an extremely long string causing performance degradation.
 *     FIX: Validate input format and length before processing.
 *
 *   BUG 6 — Returning Raw Internal Error to Client
 *     The catch block returns the exception message directly to the caller.
 *     Internal system details (DB structure, class names) leak to the client.
 *     FIX: Return a generic message; log the full error internally.
 */

import java.sql.*;

// ============================================================
// BUGGY VERSION (for review)
// ============================================================
class BuggyTradeService {

    // BUG 2: hardcoded credentials
    private static final String DB_URL  = "jdbc:postgresql://prod-db:5432/trading";
    private static final String DB_USER = "admin";
    private static final String DB_PASS = "Sup3rSecretP@ss!";  // BUG 2

    // BUG 1: SQL injection; BUG 3: sensitive data in logs;
    // BUG 4: stack trace exposed; BUG 5: no input validation;
    // BUG 6: internal error returned to caller
    public String getTrade(String accountId) {
        // BUG 5: no validation of accountId
        // BUG 3: logs sensitive account ID
        System.out.println("Looking up trades for account: " + accountId);

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            // BUG 1: SQL injection via string concat
            String sql = "SELECT * FROM trades WHERE account_id = '" + accountId + "'";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            if (rs.next()) {
                String result = rs.getString("trade_data");
                // BUG 3: logs full trade data including PII
                System.out.println("Found trade: " + result);
                return result;
            }
        } catch (SQLException e) {
            e.printStackTrace(); // BUG 4: exposes stack trace
            return "Error: " + e.getMessage(); // BUG 6: internal detail leak
        }
        return null;
    }
}

// ============================================================
// FIXED VERSION
// ============================================================
class FixedTradeService {

    // FIX 2: load from environment / secrets manager
    private static final String DB_URL  = System.getenv("TRADING_DB_URL");
    private static final String DB_USER = System.getenv("TRADING_DB_USER");
    private static final String DB_PASS = System.getenv("TRADING_DB_PASS");

    // Simple logger placeholder (use SLF4J/Logback in production)
    private static void logInfo(String msg)  { System.out.println("[INFO]  " + msg); }
    private static void logError(String msg) { System.err.println("[ERROR] " + msg); }

    public String getTrade(String accountId) {
        // FIX 5: validate input
        if (accountId == null || !accountId.matches("^[A-Z0-9]{6,20}$")) {
            logInfo("Invalid accountId format received (masked for security)");
            return "Invalid account ID format";
        }

        // FIX 3: log a masked reference, not the raw account ID
        String maskedId = accountId.substring(0, 2) + "****" + accountId.substring(accountId.length() - 2);
        logInfo("Trade lookup requested for account: " + maskedId);

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             // FIX 1: parameterized query prevents SQL injection
             PreparedStatement ps = conn.prepareStatement(
                 "SELECT trade_data FROM trades WHERE account_id = ?")) {

            ps.setString(1, accountId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                logInfo("Trade record found for account: " + maskedId);
                return rs.getString("trade_data");
            }
            return "No trades found";

        } catch (SQLException e) {
            // FIX 4+6: log internally with detail, return generic message to caller
            logError("DB error during trade lookup for " + maskedId + ": " + e.getMessage());
            return "An error occurred. Please contact support.";
        }
    }
}

// ============================================================
// Main
// ============================================================
public class CodeReview_SecurityVulnerabilities {
    public static void main(String[] args) {
        System.out.println("=== Code Review: Security Vulnerabilities ===\n");

        System.out.println("Demonstrating SQL Injection attack string:");
        String maliciousInput = "'; DROP TABLE trades; --";
        System.out.println("  Malicious input: " + maliciousInput);
        System.out.println("  Buggy SQL would execute: SELECT * FROM trades WHERE account_id = '"
            + maliciousInput + "'");
        System.out.println("  → This drops the trades table!");
        System.out.println("\n  Fixed SQL uses PreparedStatement with '?' placeholder.");
        System.out.println("  Malicious input is treated as a literal string value, not SQL code.");

        System.out.println("\nInput validation test:");
        String[] inputs = {"ACC12345", "'; DROP TABLE;--", "", null, "A".repeat(100)};
        for (String input : inputs) {
            boolean valid = input != null && input.matches("^[A-Z0-9]{6,20}$");
            System.out.println("  \"" + (input != null ? input.substring(0, Math.min(15, input.length())) : "null")
                + "\" → valid: " + valid);
        }

        System.out.println("\nKey security fixes summary:");
        System.out.println("  BUG 1 → PreparedStatement (parameterized query)");
        System.out.println("  BUG 2 → System.getenv() / secrets manager");
        System.out.println("  BUG 3 → Mask PII in logs (first2****last2)");
        System.out.println("  BUG 4 → Internal log only, no stack trace to user");
        System.out.println("  BUG 5 → Regex input validation before DB query");
        System.out.println("  BUG 6 → Generic error message to caller");
    }
}
