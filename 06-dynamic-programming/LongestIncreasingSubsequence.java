/**
 * Problem: Longest Increasing Subsequence (LeetCode #300)
 * Difficulty: Medium
 * Topics: Array, Binary Search, DP
 * Frequently asked at: Microsoft
 *
 * Description:
 *   Given an integer array nums, return the length of the longest strictly
 *   increasing subsequence.
 *
 * Approach 1 – Brute Force (Recursion)
 *   Try include/exclude each element. TC: O(2^n) | SC: O(n)
 *
 * Approach 2 – DP (Optimal)
 *   dp[i] = length of LIS ending at i. TC: O(n²) | SC: O(n)
 *
 * Approach 3 – Patience Sorting / Binary Search (Best)
 *   Maintain tails array with binary search. TC: O(n log n) | SC: O(n)
 */

import java.util.*;

public class LongestIncreasingSubsequence {

    // =========================================================================
    // Approach 1: Recursion — O(2^n) time
    // =========================================================================
    public static int lisBrute(int[] nums) {
        return lisHelper(nums, 0, Integer.MIN_VALUE);
    }

    private static int lisHelper(int[] nums, int idx, int prev) {
        if (idx == nums.length) return 0;
        int skip = lisHelper(nums, idx + 1, prev);
        int take = 0;
        if (nums[idx] > prev) {
            take = 1 + lisHelper(nums, idx + 1, nums[idx]);
        }
        return Math.max(skip, take);
    }

    // =========================================================================
    // Approach 2: DP — O(n²) time, O(n) space
    // =========================================================================
    public static int lisOptimal(int[] nums) {
        int n = nums.length;
        int[] dp = new int[n];
        Arrays.fill(dp, 1);
        int max = 1;

        for (int i = 1; i < n; i++) {
            for (int j = 0; j < i; j++) {
                if (nums[j] < nums[i]) {
                    dp[i] = Math.max(dp[i], dp[j] + 1);
                }
            }
            max = Math.max(max, dp[i]);
        }
        return max;
    }

    // =========================================================================
    // Approach 3: Binary Search (Best) — O(n log n) time, O(n) space
    // =========================================================================
    public static int lisBest(int[] nums) {
        List<Integer> tails = new ArrayList<>();

        for (int num : nums) {
            int pos = Collections.binarySearch(tails, num);
            if (pos < 0) pos = -(pos + 1);
            if (pos == tails.size()) {
                tails.add(num);
            } else {
                tails.set(pos, num);
            }
        }
        return tails.size();
    }

    // =========================================================================
    // Main
    // =========================================================================
    public static void main(String[] args) {
        System.out.println("=== Longest Increasing Subsequence ===");

        int[] nums = {10, 9, 2, 5, 3, 7, 101, 18};
        System.out.println("Input: [10,9,2,5,3,7,101,18]");
        System.out.println("Brute:  " + lisBrute(nums));    // 4
        System.out.println("DP:     " + lisOptimal(nums));   // 4
        System.out.println("Binary: " + lisBest(nums));      // 4
    }
}
