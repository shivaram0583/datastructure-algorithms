/**
 * Problem: Best Time to Buy and Sell Stock (LeetCode #121)
 * Difficulty: Medium
 * Topics: Array, Greedy, Sliding Window
 * Frequently asked at: JP Morgan Chase (Round 1 — finance context)
 *
 * Description:
 *   You are given an array prices where prices[i] is the price of a stock
 *   on day i. Maximize profit by choosing one day to buy and another
 *   later day to sell. Return 0 if no profit is possible.
 *
 * Example 1:
 *   Input:  prices = [7,1,5,3,6,4]
 *   Output: 5  (buy day 2 at 1, sell day 5 at 6)
 *
 * Example 2:
 *   Input:  prices = [7,6,4,3,1]
 *   Output: 0  (prices always fall, no profitable trade)
 *
 * Constraints:
 *   1 <= prices.length <= 10^5
 *   0 <= prices[i] <= 10^4
 *
 * JP Morgan Context:
 *   Directly maps to trading/equity desk scenarios.
 *   Follow-ups interviewers ask:
 *   - "What if you can buy/sell multiple times?" → LeetCode #122
 *   - "What if there's a cooldown?" → LeetCode #309
 *   - "What if you can do at most k transactions?" → LeetCode #188
 *
 * ============================================================
 * Approach 1 — Brute Force
 * ============================================================
 *   For every pair (i, j) where j > i, compute prices[j] - prices[i].
 *   Track the maximum.
 *
 *   Step-by-step ([7,1,5,3,6,4]):
 *     (0,1): 1-7=-6 | (0,2): 5-7=-2 | (1,2): 5-1=4 |
 *     (1,3): 3-1=2  | (1,4): 6-1=5  ← max so far
 *   Result: 5
 *
 *   TC: O(n²)  SC: O(1)
 *
 * ============================================================
 * Approach 2 — Track Min Price (Optimal)
 * ============================================================
 *   Single pass: maintain a running minimum price seen so far.
 *   At each step compute profit = price - minPrice.
 *   Update maxProfit if this profit is larger.
 *
 *   Step-by-step ([7,1,5,3,6,4]):
 *     i=0: price=7, minPrice=7, profit=0,  maxProfit=0
 *     i=1: price=1, minPrice=1, profit=0,  maxProfit=0
 *     i=2: price=5, minPrice=1, profit=4,  maxProfit=4
 *     i=3: price=3, minPrice=1, profit=2,  maxProfit=4
 *     i=4: price=6, minPrice=1, profit=5,  maxProfit=5 ← best
 *     i=5: price=4, minPrice=1, profit=3,  maxProfit=5
 *   Result: 5
 *
 *   TC: O(n)  SC: O(1)
 *
 * ============================================================
 * Approach 3 — Kadane's on Differences (Alternative Optimal)
 * ============================================================
 *   Transform the problem: create diff[] where diff[i] = prices[i] - prices[i-1].
 *   Finding max profit = finding maximum subarray sum in diff[].
 *   Apply Kadane's Algorithm.
 *
 *   Step-by-step ([7,1,5,3,6,4]):
 *     diff = [-6, 4, -2, 3, -2]
 *     Kadane: maxSum=0,curSum=0
 *       -6 → curSum=max(0,-6)=0
 *        4 → curSum=max(0,4)=4, maxSum=4
 *       -2 → curSum=max(0,2)=2
 *        3 → curSum=5, maxSum=5
 *       -2 → curSum=3
 *   Result: 5
 *
 *   TC: O(n)  SC: O(n) for diff array (can be O(1) with inline diff)
 */

public class BestTimeToBuyAndSellStock {

    // =========================================================================
    // Approach 1: Brute Force — O(n²) time, O(1) space
    // =========================================================================
    public static int maxProfitBrute(int[] prices) {
        int maxProfit = 0;
        for (int i = 0; i < prices.length; i++) {
            for (int j = i + 1; j < prices.length; j++) {
                maxProfit = Math.max(maxProfit, prices[j] - prices[i]);
            }
        }
        return maxProfit;
    }

    // =========================================================================
    // Approach 2: Track Min Price (Optimal) — O(n) time, O(1) space
    // =========================================================================
    public static int maxProfitOptimal(int[] prices) {
        int minPrice = Integer.MAX_VALUE;
        int maxProfit = 0;
        for (int price : prices) {
            if (price < minPrice) {
                minPrice = price;
            } else {
                maxProfit = Math.max(maxProfit, price - minPrice);
            }
        }
        return maxProfit;
    }

    // =========================================================================
    // Approach 3: Kadane's on Differences — O(n) time, O(1) space
    // =========================================================================
    public static int maxProfitKadane(int[] prices) {
        int maxProfit = 0;
        int curProfit = 0;
        for (int i = 1; i < prices.length; i++) {
            int diff = prices[i] - prices[i - 1];
            curProfit = Math.max(0, curProfit + diff);
            maxProfit = Math.max(maxProfit, curProfit);
        }
        return maxProfit;
    }

    // =========================================================================
    // Main — Test Cases
    // =========================================================================
    public static void main(String[] args) {
        System.out.println("=== Best Time to Buy and Sell Stock ===\n");

        int[] prices1 = {7, 1, 5, 3, 6, 4};
        System.out.println("Test 1: prices=[7,1,5,3,6,4]  Expected: 5");
        System.out.println("  Brute:   " + maxProfitBrute(prices1));
        System.out.println("  Optimal: " + maxProfitOptimal(prices1));
        System.out.println("  Kadane:  " + maxProfitKadane(prices1));

        int[] prices2 = {7, 6, 4, 3, 1};
        System.out.println("\nTest 2: prices=[7,6,4,3,1]  Expected: 0");
        System.out.println("  Brute:   " + maxProfitBrute(prices2));
        System.out.println("  Optimal: " + maxProfitOptimal(prices2));
        System.out.println("  Kadane:  " + maxProfitKadane(prices2));

        int[] prices3 = {1};
        System.out.println("\nTest 3: prices=[1]  Expected: 0");
        System.out.println("  Optimal: " + maxProfitOptimal(prices3));
    }
}
