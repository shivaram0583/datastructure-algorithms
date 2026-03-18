/**
 * Problem: Longest Common Subsequence (LeetCode #1143)
 * Difficulty: Medium
 * Topics: Dynamic Programming, String
 * Frequently asked at: JP Morgan Chase (Round 1)
 *
 * Description:
 *   Given two strings text1 and text2, return the length of their
 *   longest common subsequence. A subsequence is derived by deleting
 *   some characters (not necessarily contiguous) without changing order.
 *
 * Example 1:
 *   text1="abcde", text2="ace" → 3  (LCS = "ace")
 * Example 2:
 *   text1="abc", text2="abc"   → 3  (LCS = "abc")
 * Example 3:
 *   text1="abc", text2="def"   → 0  (no common subsequence)
 *
 * Constraints:
 *   1 <= text1.length, text2.length <= 1000
 *   Both strings consist of lowercase English letters.
 *
 * JP Morgan Context:
 *   LCS is the basis for diff algorithms used to reconcile trade records,
 *   audit logs, and data migration verification.
 *
 * ============================================================
 * Approach 1 — Recursion (Naive)
 * ============================================================
 *   If chars match: 1 + lcs(i+1, j+1)
 *   Else: max(lcs(i+1, j), lcs(i, j+1))
 *   TC: O(2^(m+n))  SC: O(m+n) stack
 *
 * ============================================================
 * Approach 2 — 2D DP Table (Bottom-Up)
 * ============================================================
 *   dp[i][j] = LCS length of text1[0..i-1] and text2[0..j-1]
 *
 *   Recurrence:
 *     If text1[i-1] == text2[j-1]: dp[i][j] = dp[i-1][j-1] + 1
 *     Else:                         dp[i][j] = max(dp[i-1][j], dp[i][j-1])
 *
 *   Step-by-step (text1="abcde", text2="ace"):
 *         ""  a  c  e
 *     ""   0  0  0  0
 *     a    0  1  1  1
 *     b    0  1  1  1
 *     c    0  1  2  2
 *     d    0  1  2  2
 *     e    0  1  2  3   ← answer
 *
 *   TC: O(m*n)  SC: O(m*n)
 *
 * ============================================================
 * Approach 3 — Space Optimized (Two Rows)
 * ============================================================
 *   Only need previous row to compute current row.
 *   TC: O(m*n)  SC: O(min(m,n))
 */

public class LongestCommonSubsequence {

    // =========================================================================
    // Approach 2: 2D DP Table — O(m*n) time, O(m*n) space
    // =========================================================================
    public static int lcs2D(String text1, String text2) {
        int m = text1.length(), n = text2.length();
        int[][] dp = new int[m + 1][n + 1];

        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (text1.charAt(i - 1) == text2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                } else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }
        return dp[m][n];
    }

    // =========================================================================
    // Approach 3: Space Optimized — O(m*n) time, O(min(m,n)) space
    // =========================================================================
    public static int lcsOptimized(String text1, String text2) {
        if (text1.length() < text2.length()) {
            String temp = text1; text1 = text2; text2 = temp;
        }
        int m = text1.length(), n = text2.length();
        int[] dp = new int[n + 1];

        for (int i = 1; i <= m; i++) {
            int prev = 0;
            for (int j = 1; j <= n; j++) {
                int temp = dp[j];
                if (text1.charAt(i - 1) == text2.charAt(j - 1)) {
                    dp[j] = prev + 1;
                } else {
                    dp[j] = Math.max(dp[j], dp[j - 1]);
                }
                prev = temp;
            }
        }
        return dp[n];
    }

    // =========================================================================
    // Bonus: Reconstruct the LCS string itself
    // =========================================================================
    public static String reconstructLCS(String text1, String text2) {
        int m = text1.length(), n = text2.length();
        int[][] dp = new int[m + 1][n + 1];

        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (text1.charAt(i - 1) == text2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                } else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }

        StringBuilder sb = new StringBuilder();
        int i = m, j = n;
        while (i > 0 && j > 0) {
            if (text1.charAt(i - 1) == text2.charAt(j - 1)) {
                sb.insert(0, text1.charAt(i - 1));
                i--; j--;
            } else if (dp[i - 1][j] > dp[i][j - 1]) {
                i--;
            } else {
                j--;
            }
        }
        return sb.toString();
    }

    // =========================================================================
    // Main — Test Cases
    // =========================================================================
    public static void main(String[] args) {
        System.out.println("=== Longest Common Subsequence ===\n");

        System.out.println("Test 1: \"abcde\", \"ace\"  Expected: 3 (ace)");
        System.out.println("  2D DP:     " + lcs2D("abcde", "ace"));
        System.out.println("  Optimized: " + lcsOptimized("abcde", "ace"));
        System.out.println("  LCS:       " + reconstructLCS("abcde", "ace"));

        System.out.println("\nTest 2: \"abc\", \"abc\"  Expected: 3 (abc)");
        System.out.println("  2D DP:     " + lcs2D("abc", "abc"));
        System.out.println("  LCS:       " + reconstructLCS("abc", "abc"));

        System.out.println("\nTest 3: \"abc\", \"def\"  Expected: 0");
        System.out.println("  2D DP:     " + lcs2D("abc", "def"));

        System.out.println("\nTest 4: \"AGGTAB\", \"GXTXAYB\"  Expected: 4 (GTAB)");
        System.out.println("  2D DP:     " + lcs2D("AGGTAB", "GXTXAYB"));
        System.out.println("  LCS:       " + reconstructLCS("AGGTAB", "GXTXAYB"));
    }
}
