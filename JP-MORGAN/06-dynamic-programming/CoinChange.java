/**
 * Problem: Coin Change (LeetCode #322)
 * Difficulty: Medium
 * Topics: Dynamic Programming, BFS
 * Frequently asked at: JP Morgan Chase (Round 1 — finance context)
 *
 * Description:
 *   Given an integer array coins representing coin denominations and an
 *   integer amount, return the fewest number of coins to make up that amount.
 *   If not possible, return -1. You have infinite coins of each denomination.
 *
 * Example 1:
 *   coins=[1,5,10,25], amount=41 → 4  (25+10+5+1)
 * Example 2:
 *   coins=[2], amount=3 → -1
 *
 * Constraints:
 *   1 <= coins.length <= 12
 *   1 <= coins[i] <= 2^31 - 1
 *   0 <= amount <= 10^4
 *
 * JP Morgan Context:
 *   Directly models currency denomination optimization.
 *   Follow-up: "How many ways to make the amount?" → LeetCode #518 (Coin Change II).
 *
 * ============================================================
 * Approach 1 — Recursion (Naive)
 * ============================================================
 *   Try every coin, recurse on amount-coin. Take minimum.
 *   TC: O(S^n) where S=amount, n=coins  SC: O(S) stack
 *
 * ============================================================
 * Approach 2 — Top-Down DP (Memoization)
 * ============================================================
 *   Cache results of subproblems.
 *   TC: O(S * n)  SC: O(S)
 *
 * ============================================================
 * Approach 3 — Bottom-Up DP (Optimal)
 * ============================================================
 *   dp[i] = minimum coins needed for amount i.
 *   dp[0] = 0.
 *   For each amount i from 1 to S:
 *     For each coin c: if i >= c, dp[i] = min(dp[i], dp[i-c] + 1)
 *
 *   Step-by-step (coins=[1,2,5], amount=11):
 *     dp[0]=0
 *     dp[1]=min(dp[0]+1)=1             (use coin 1)
 *     dp[2]=min(dp[1]+1, dp[0]+1)=1   (use coin 2)
 *     dp[3]=min(dp[2]+1, dp[1]+1)=2   (5>3, skip)
 *     dp[4]=min(dp[3]+1, dp[2]+1)=2
 *     dp[5]=min(dp[4]+1, dp[3]+1, dp[0]+1)=1  (use coin 5)
 *     dp[6]=min(dp[5]+1, dp[4]+1, dp[1]+1)=2
 *     dp[7]=2, dp[8]=3, dp[9]=3, dp[10]=2, dp[11]=3
 *   Result: 3  (5+5+1)
 *
 *   TC: O(S * n)  SC: O(S)
 *
 * ============================================================
 * Approach 4 — BFS (Level = Number of Coins)
 * ============================================================
 *   Treat as shortest path problem. Each level = 1 more coin used.
 *   TC: O(S * n)  SC: O(S)
 */

import java.util.*;

public class CoinChange {

    // =========================================================================
    // Approach 2: Top-Down DP — O(S*n) time, O(S) space
    // =========================================================================
    public static int coinChangeMemo(int[] coins, int amount) {
        int[] memo = new int[amount + 1];
        Arrays.fill(memo, -2);
        return dpHelper(coins, amount, memo);
    }

    private static int dpHelper(int[] coins, int rem, int[] memo) {
        if (rem < 0) return -1;
        if (rem == 0) return 0;
        if (memo[rem] != -2) return memo[rem];

        int min = Integer.MAX_VALUE;
        for (int coin : coins) {
            int result = dpHelper(coins, rem - coin, memo);
            if (result >= 0) min = Math.min(min, result + 1);
        }
        memo[rem] = (min == Integer.MAX_VALUE) ? -1 : min;
        return memo[rem];
    }

    // =========================================================================
    // Approach 3: Bottom-Up DP (Optimal) — O(S*n) time, O(S) space
    // =========================================================================
    public static int coinChangeDP(int[] coins, int amount) {
        int[] dp = new int[amount + 1];
        Arrays.fill(dp, amount + 1);
        dp[0] = 0;

        for (int i = 1; i <= amount; i++) {
            for (int coin : coins) {
                if (coin <= i) {
                    dp[i] = Math.min(dp[i], dp[i - coin] + 1);
                }
            }
        }
        return dp[amount] > amount ? -1 : dp[amount];
    }

    // =========================================================================
    // Approach 4: BFS — O(S*n) time, O(S) space
    // =========================================================================
    public static int coinChangeBFS(int[] coins, int amount) {
        if (amount == 0) return 0;
        boolean[] visited = new boolean[amount + 1];
        Queue<Integer> queue = new LinkedList<>();
        queue.offer(0);
        visited[0] = true;
        int level = 0;

        while (!queue.isEmpty()) {
            level++;
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                int curr = queue.poll();
                for (int coin : coins) {
                    int next = curr + coin;
                    if (next == amount) return level;
                    if (next < amount && !visited[next]) {
                        visited[next] = true;
                        queue.offer(next);
                    }
                }
            }
        }
        return -1;
    }

    // =========================================================================
    // Main — Test Cases
    // =========================================================================
    public static void main(String[] args) {
        System.out.println("=== Coin Change ===\n");

        int[] coins1 = {1, 5, 10, 25};
        System.out.println("Test 1: coins=[1,5,10,25], amount=41  Expected: 4");
        System.out.println("  Memo: " + coinChangeMemo(coins1, 41));
        System.out.println("  DP:   " + coinChangeDP(coins1, 41));
        System.out.println("  BFS:  " + coinChangeBFS(coins1, 41));

        int[] coins2 = {1, 2, 5};
        System.out.println("\nTest 2: coins=[1,2,5], amount=11  Expected: 3");
        System.out.println("  DP:   " + coinChangeDP(coins2, 11));

        int[] coins3 = {2};
        System.out.println("\nTest 3: coins=[2], amount=3  Expected: -1");
        System.out.println("  DP:   " + coinChangeDP(coins3, 3));

        int[] coins4 = {1};
        System.out.println("\nTest 4: coins=[1], amount=0  Expected: 0");
        System.out.println("  DP:   " + coinChangeDP(coins4, 0));
    }
}
