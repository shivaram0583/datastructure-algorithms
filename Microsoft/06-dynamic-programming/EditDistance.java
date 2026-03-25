/**
 * Problem: Edit Distance (LeetCode #72)
 * Difficulty: Medium
 * Topics: String, DP
 * Frequently asked at: Microsoft
 *
 * Description:
 *   Given two strings word1 and word2, return the minimum number of operations
 *   (insert, delete, replace) to convert word1 into word2.
 *
 * Approach 1 – Recursion (Brute Force)
 *   Try all three operations at each mismatch. TC: O(3^max(m,n)) | SC: O(m+n)
 *
 * Approach 2 – 2D DP (Optimal)
 *   dp[i][j] = min ops for word1[0..i-1] to word2[0..j-1].
 *   TC: O(m*n) | SC: O(m*n)
 *
 * Approach 3 – 1D DP (Best)
 *   Space-optimized: only keep previous row.
 *   TC: O(m*n) | SC: O(min(m,n))
 */

public class EditDistance {

    // =========================================================================
    // Approach 1: Recursion — exponential time
    // =========================================================================
    public static int editBrute(String w1, String w2) {
        return editRec(w1, w2, w1.length(), w2.length());
    }

    private static int editRec(String w1, String w2, int i, int j) {
        if (i == 0) return j;
        if (j == 0) return i;
        if (w1.charAt(i-1) == w2.charAt(j-1)) return editRec(w1, w2, i-1, j-1);
        return 1 + Math.min(editRec(w1, w2, i, j-1),       // insert
                   Math.min(editRec(w1, w2, i-1, j),       // delete
                            editRec(w1, w2, i-1, j-1)));   // replace
    }

    // =========================================================================
    // Approach 2: 2D DP — O(m*n) time, O(m*n) space
    // =========================================================================
    public static int editOptimal(String w1, String w2) {
        int m = w1.length(), n = w2.length();
        int[][] dp = new int[m + 1][n + 1];

        for (int i = 0; i <= m; i++) dp[i][0] = i;
        for (int j = 0; j <= n; j++) dp[0][j] = j;

        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (w1.charAt(i-1) == w2.charAt(j-1)) {
                    dp[i][j] = dp[i-1][j-1];
                } else {
                    dp[i][j] = 1 + Math.min(dp[i][j-1],
                                   Math.min(dp[i-1][j], dp[i-1][j-1]));
                }
            }
        }
        return dp[m][n];
    }

    // =========================================================================
    // Approach 3: 1D DP (Best) — O(m*n) time, O(min(m,n)) space
    // =========================================================================
    public static int editBest(String w1, String w2) {
        if (w1.length() < w2.length()) { String t = w1; w1 = w2; w2 = t; }
        int m = w1.length(), n = w2.length();
        int[] prev = new int[n + 1];
        int[] curr = new int[n + 1];

        for (int j = 0; j <= n; j++) prev[j] = j;

        for (int i = 1; i <= m; i++) {
            curr[0] = i;
            for (int j = 1; j <= n; j++) {
                if (w1.charAt(i-1) == w2.charAt(j-1)) {
                    curr[j] = prev[j-1];
                } else {
                    curr[j] = 1 + Math.min(curr[j-1],
                                  Math.min(prev[j], prev[j-1]));
                }
            }
            int[] temp = prev; prev = curr; curr = temp;
        }
        return prev[n];
    }

    // =========================================================================
    // Main
    // =========================================================================
    public static void main(String[] args) {
        System.out.println("=== Edit Distance ===");

        System.out.println("\"horse\" → \"ros\"");
        System.out.println("Brute:  " + editBrute("horse", "ros"));    // 3
        System.out.println("2D DP:  " + editOptimal("horse", "ros"));  // 3
        System.out.println("1D DP:  " + editBest("horse", "ros"));     // 3

        System.out.println("\n\"intention\" → \"execution\"");
        System.out.println("2D DP: " + editOptimal("intention", "execution")); // 5
        System.out.println("1D DP: " + editBest("intention", "execution"));    // 5
    }
}
