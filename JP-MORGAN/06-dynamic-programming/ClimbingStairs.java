/**
 * Problem: Climbing Stairs (LeetCode #70)
 * Difficulty: Easy
 * Topics: Dynamic Programming, Math (Fibonacci)
 * Frequently asked at: JP Morgan Chase (Round 1 — DP warm-up)
 *
 * Description:
 *   You are climbing a staircase with n steps. Each time you can climb
 *   either 1 or 2 steps. In how many distinct ways can you climb to the top?
 *
 * Example 1: n=2 → 2  (1+1, 2)
 * Example 2: n=3 → 3  (1+1+1, 1+2, 2+1)
 *
 * Constraints: 1 <= n <= 45
 *
 * JP Morgan Context:
 *   Entry-level DP. Tests whether you recognize the Fibonacci recurrence
 *   and can optimize space. Follow-up: "k steps allowed?" → generalized DP.
 *
 * ============================================================
 * Approach 1 — Recursion (Naive)
 * ============================================================
 *   f(n) = f(n-1) + f(n-2), base cases: f(1)=1, f(2)=2
 *   TC: O(2^n)  SC: O(n) stack
 *
 * ============================================================
 * Approach 2 — Memoization (Top-Down DP)
 * ============================================================
 *   Cache subproblem results to avoid recomputation.
 *   TC: O(n)  SC: O(n)
 *
 * ============================================================
 * Approach 3 — Tabulation (Bottom-Up DP)
 * ============================================================
 *   dp[i] = number of ways to reach step i.
 *   dp[i] = dp[i-1] + dp[i-2]
 *
 *   Step-by-step (n=5):
 *     dp[1]=1, dp[2]=2
 *     dp[3]=dp[2]+dp[1]=3
 *     dp[4]=dp[3]+dp[2]=5
 *     dp[5]=dp[4]+dp[3]=8
 *   TC: O(n)  SC: O(n)
 *
 * ============================================================
 * Approach 4 — Space Optimized (Two Variables)
 * ============================================================
 *   Only need previous two values at each step.
 *   TC: O(n)  SC: O(1)
 */

import java.util.HashMap;
import java.util.Map;

public class ClimbingStairs {

    // =========================================================================
    // Approach 1: Naive Recursion — O(2^n) time, O(n) space
    // =========================================================================
    public static int climbRecursive(int n) {
        if (n <= 2) return n;
        return climbRecursive(n - 1) + climbRecursive(n - 2);
    }

    // =========================================================================
    // Approach 2: Memoization — O(n) time, O(n) space
    // =========================================================================
    public static int climbMemo(int n) {
        return memoHelper(n, new HashMap<>());
    }

    private static int memoHelper(int n, Map<Integer, Integer> memo) {
        if (n <= 2) return n;
        if (memo.containsKey(n)) return memo.get(n);
        int result = memoHelper(n - 1, memo) + memoHelper(n - 2, memo);
        memo.put(n, result);
        return result;
    }

    // =========================================================================
    // Approach 3: Tabulation — O(n) time, O(n) space
    // =========================================================================
    public static int climbDP(int n) {
        if (n <= 2) return n;
        int[] dp = new int[n + 1];
        dp[1] = 1;
        dp[2] = 2;
        for (int i = 3; i <= n; i++) {
            dp[i] = dp[i - 1] + dp[i - 2];
        }
        return dp[n];
    }

    // =========================================================================
    // Approach 4: Space Optimized — O(n) time, O(1) space
    // =========================================================================
    public static int climbOptimal(int n) {
        if (n <= 2) return n;
        int prev2 = 1, prev1 = 2;
        for (int i = 3; i <= n; i++) {
            int curr = prev1 + prev2;
            prev2 = prev1;
            prev1 = curr;
        }
        return prev1;
    }

    // =========================================================================
    // Main — Test Cases
    // =========================================================================
    public static void main(String[] args) {
        System.out.println("=== Climbing Stairs ===\n");

        int[] tests = {1, 2, 3, 5, 10, 45};
        for (int n : tests) {
            System.out.printf("n=%-3d  Recursive:%-8d  Memo:%-8d  DP:%-8d  Optimal:%d%n",
                n,
                n <= 30 ? climbRecursive(n) : -1,
                climbMemo(n),
                climbDP(n),
                climbOptimal(n));
        }
    }
}
