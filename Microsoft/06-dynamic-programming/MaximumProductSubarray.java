/**
 * Problem: Maximum Product Subarray (LeetCode #152)
 * Difficulty: Medium
 * Topics: Array, DP
 * Frequently asked at: Microsoft
 *
 * Description:
 *   Given an integer array nums, find a contiguous subarray that has the
 *   largest product, and return the product.
 *
 * Approach 1 – Brute Force
 *   Check all subarrays. TC: O(n²) | SC: O(1)
 *
 * Approach 2 – DP tracking min & max (Optimal/Best)
 *   Track running max and min (because negatives flip sign).
 *   TC: O(n) | SC: O(1)
 *
 * Approach 3 – Left-Right product sweep
 *   Sweep from left and right; reset on zero.
 *   TC: O(n) | SC: O(1)
 */

public class MaximumProductSubarray {

    // =========================================================================
    // Approach 1: Brute Force — O(n²) time, O(1) space
    // =========================================================================
    public static int maxProductBrute(int[] nums) {
        int maxProd = Integer.MIN_VALUE;
        for (int i = 0; i < nums.length; i++) {
            int prod = 1;
            for (int j = i; j < nums.length; j++) {
                prod *= nums[j];
                maxProd = Math.max(maxProd, prod);
            }
        }
        return maxProd;
    }

    // =========================================================================
    // Approach 2: Track Min/Max DP — O(n) time, O(1) space
    // =========================================================================
    public static int maxProductOptimal(int[] nums) {
        int maxSoFar = nums[0], minSoFar = nums[0], result = nums[0];

        for (int i = 1; i < nums.length; i++) {
            int temp = maxSoFar;
            maxSoFar = Math.max(nums[i], Math.max(maxSoFar * nums[i], minSoFar * nums[i]));
            minSoFar = Math.min(nums[i], Math.min(temp * nums[i], minSoFar * nums[i]));
            result = Math.max(result, maxSoFar);
        }
        return result;
    }

    // =========================================================================
    // Approach 3: Left-Right Sweep (Best) — O(n) time, O(1) space
    // =========================================================================
    public static int maxProductBest(int[] nums) {
        int n = nums.length;
        int leftProd = 0, rightProd = 0, result = Integer.MIN_VALUE;

        for (int i = 0; i < n; i++) {
            leftProd  = (leftProd == 0 ? 1 : leftProd) * nums[i];
            rightProd = (rightProd == 0 ? 1 : rightProd) * nums[n - 1 - i];
            result = Math.max(result, Math.max(leftProd, rightProd));
        }
        return result;
    }

    // =========================================================================
    // Main
    // =========================================================================
    public static void main(String[] args) {
        System.out.println("=== Maximum Product Subarray ===");

        int[] nums = {2, 3, -2, 4};
        System.out.println("Input: [2,3,-2,4]");
        System.out.println("Brute:    " + maxProductBrute(nums));    // 6
        System.out.println("Min/Max:  " + maxProductOptimal(nums));  // 6
        System.out.println("LR Sweep: " + maxProductBest(nums));     // 6

        int[] nums2 = {-2, 0, -1};
        System.out.println("\nInput: [-2,0,-1]");
        System.out.println("Brute:    " + maxProductBrute(nums2));   // 0
        System.out.println("Min/Max:  " + maxProductOptimal(nums2)); // 0
        System.out.println("LR Sweep: " + maxProductBest(nums2));    // 0
    }
}
