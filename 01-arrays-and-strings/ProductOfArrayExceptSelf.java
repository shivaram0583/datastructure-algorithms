/**
 * Problem: Product of Array Except Self (LeetCode #238)
 * Difficulty: Medium
 * Topics: Array, Prefix Sum
 * Frequently asked at: Microsoft
 *
 * Description:
 *   Given an integer array nums, return an array answer such that answer[i]
 *   is equal to the product of all the elements of nums except nums[i].
 *   You must write an algorithm that runs in O(n) time and without using
 *   the division operation.
 *
 * Example:
 *   Input:  nums = [1,2,3,4]
 *   Output: [24,12,8,6]
 *
 * Approach 1 – Brute Force
 *   For each element, multiply all others. TC: O(n²) | SC: O(1) extra
 *
 * Approach 2 – Prefix & Suffix Arrays (Optimal)
 *   Build left-product and right-product arrays.
 *   TC: O(n) | SC: O(n)
 *
 * Approach 3 – Single Output Array (Best)
 *   Use the output array for left products, then sweep right.
 *   TC: O(n) | SC: O(1) extra (output array doesn't count)
 */

import java.util.*;

public class ProductOfArrayExceptSelf {

    // =========================================================================
    // Approach 1: Brute Force — O(n²) time, O(1) extra space
    // =========================================================================
    public static int[] productBrute(int[] nums) {
        int n = nums.length;
        int[] result = new int[n];
        for (int i = 0; i < n; i++) {
            int product = 1;
            for (int j = 0; j < n; j++) {
                if (j != i) product *= nums[j];
            }
            result[i] = product;
        }
        return result;
    }

    // =========================================================================
    // Approach 2: Prefix & Suffix Arrays — O(n) time, O(n) space
    // =========================================================================
    public static int[] productOptimal(int[] nums) {
        int n = nums.length;
        int[] left = new int[n];
        int[] right = new int[n];
        int[] result = new int[n];

        left[0] = 1;
        for (int i = 1; i < n; i++) {
            left[i] = left[i - 1] * nums[i - 1];
        }

        right[n - 1] = 1;
        for (int i = n - 2; i >= 0; i--) {
            right[i] = right[i + 1] * nums[i + 1];
        }

        for (int i = 0; i < n; i++) {
            result[i] = left[i] * right[i];
        }
        return result;
    }

    // =========================================================================
    // Approach 3: Single Output Array (Best) — O(n) time, O(1) extra space
    // =========================================================================
    public static int[] productBest(int[] nums) {
        int n = nums.length;
        int[] result = new int[n];

        // Build left products into result
        result[0] = 1;
        for (int i = 1; i < n; i++) {
            result[i] = result[i - 1] * nums[i - 1];
        }

        // Sweep from right, multiplying right product in-place
        int rightProduct = 1;
        for (int i = n - 1; i >= 0; i--) {
            result[i] *= rightProduct;
            rightProduct *= nums[i];
        }
        return result;
    }

    // =========================================================================
    // Main — Test Cases
    // =========================================================================
    public static void main(String[] args) {
        System.out.println("=== Product of Array Except Self ===");

        int[] nums1 = {1, 2, 3, 4};
        System.out.println("Input: [1,2,3,4]");
        System.out.println("Brute:   " + Arrays.toString(productBrute(nums1)));   // [24,12,8,6]
        System.out.println("Optimal: " + Arrays.toString(productOptimal(nums1))); // [24,12,8,6]
        System.out.println("Best:    " + Arrays.toString(productBest(nums1)));     // [24,12,8,6]

        int[] nums2 = {-1, 1, 0, -3, 3};
        System.out.println("\nInput: [-1,1,0,-3,3]");
        System.out.println("Brute:   " + Arrays.toString(productBrute(nums2)));   // [0,0,9,0,0]
        System.out.println("Optimal: " + Arrays.toString(productOptimal(nums2))); // [0,0,9,0,0]
        System.out.println("Best:    " + Arrays.toString(productBest(nums2)));     // [0,0,9,0,0]
    }
}
