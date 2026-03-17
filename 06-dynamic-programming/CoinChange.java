/**
 * Problem: Coin Change (LeetCode #322)
 * Difficulty: Medium
 * Topics: Array, DP, BFS
 * Frequently asked at: Microsoft
 *
 * Description:
 *   Given an integer array coins and an amount, return the fewest number of
 *   coins needed to make up that amount. Return -1 if not possible.
 *
 * Approach 1 – Recursion (Brute Force)
 *   Try each coin, recurse on remaining. TC: O(S^n) | SC: O(S)
 *
 * Approach 2 – Top-Down Memoization (Optimal)
 *   Recursion + memo array. TC: O(S * n) | SC: O(S)
 *
 * Approach 3 – Bottom-Up DP (Best)
 *   dp[i] = min coins for amount i. TC: O(S * n) | SC: O(S)
 */

import java.util.*;

public class CoinChange {

    // =========================================================================
    // Approach 1: Recursion — exponential
    // =========================================================================
    public static int coinChangeBrute(int[] coins, int amount) {
        if (amount == 0) return 0;
        if (amount < 0) return -1;
        int min = Integer.MAX_VALUE;
        for (int coin : coins) {
            int res = coinChangeBrute(coins, amount - coin);
            if (res >= 0) min = Math.min(min, res + 1);
        }
        return min == Integer.MAX_VALUE ? -1 : min;
    }

    // =========================================================================
    // Approach 2: Top-Down Memoization — O(S*n)
    // =========================================================================
    public static int coinChangeOptimal(int[] coins, int amount) {
        int[] memo = new int[amount + 1];
        Arrays.fill(memo, Integer.MIN_VALUE);
        return coinChangeMemo(coins, amount, memo);
    }

    private static int coinChangeMemo(int[] coins, int rem, int[] memo) {
        if (rem == 0) return 0;
        if (rem < 0) return -1;
        if (memo[rem] != Integer.MIN_VALUE) return memo[rem];

        int min = Integer.MAX_VALUE;
        for (int coin : coins) {
            int res = coinChangeMemo(coins, rem - coin, memo);
            if (res >= 0) min = Math.min(min, res + 1);
        }
        memo[rem] = (min == Integer.MAX_VALUE) ? -1 : min;
        return memo[rem];
    }

    // =========================================================================
    // Approach 3: Bottom-Up DP (Best) — O(S*n) time, O(S) space
    // =========================================================================
    public static int coinChangeBest(int[] coins, int amount) {
        int[] dp = new int[amount + 1];
        Arrays.fill(dp, amount + 1); // impossible sentinel
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
    // Main
    // =========================================================================
    public static void main(String[] args) {
        System.out.println("=== Coin Change ===");

        int[] coins = {1, 5, 11};
        int amount = 15;
        System.out.println("coins=[1,5,11], amount=15");
        System.out.println("Brute: " + coinChangeBrute(coins, amount));   // 3
        System.out.println("Memo:  " + coinChangeOptimal(coins, amount)); // 3
        System.out.println("DP:    " + coinChangeBest(coins, amount));     // 3

        int[] coins2 = {2};
        System.out.println("\ncoins=[2], amount=3");
        System.out.println("DP: " + coinChangeBest(coins2, 3)); // -1
    }
}
