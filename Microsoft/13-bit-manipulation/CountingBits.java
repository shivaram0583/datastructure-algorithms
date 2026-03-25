/**
 * Problem: Counting Bits (LeetCode #338)
 * Difficulty: Medium
 * Topics: DP, Bit Manipulation
 * Frequently asked at: Microsoft
 *
 * Description:
 *   For every number i in [0, n], count the number of 1's in its binary representation.
 *
 * Approach 1 – Brute (count each): TC: O(n log n) | SC: O(1) extra
 * Approach 2 – DP with last set bit: dp[i] = dp[i & (i-1)] + 1. TC: O(n) | SC: O(1) extra
 * Approach 3 – DP with right shift: dp[i] = dp[i >> 1] + (i & 1). TC: O(n) | SC: O(1) extra
 */

public class CountingBits {

    public static int[] countBitsBrute(int n) {
        int[] ans = new int[n + 1];
        for (int i = 0; i <= n; i++) ans[i] = Integer.bitCount(i);
        return ans;
    }

    public static int[] countBitsOptimal(int n) {
        int[] dp = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            dp[i] = dp[i & (i - 1)] + 1; // turn off last set bit
        }
        return dp;
    }

    public static int[] countBitsBest(int n) {
        int[] dp = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            dp[i] = dp[i >> 1] + (i & 1); // right shift + parity
        }
        return dp;
    }

    public static void main(String[] args) {
        System.out.println("=== Counting Bits ===");
        System.out.println("n=5: " + java.util.Arrays.toString(countBitsBest(5)));
        // [0, 1, 1, 2, 1, 2]
    }
}
