/**
 * Problem: Product of Array Except Self (LeetCode #238)
 * Difficulty: Medium
 * Topics: Array, Prefix Product
 * Frequently asked at: JP Morgan Chase (Round 1)
 *
 * Description:
 *   Given an integer array nums, return an array answer such that
 *   answer[i] = product of all elements of nums except nums[i].
 *   You must solve it in O(n) without using the division operator.
 *
 * Example:
 *   Input:  nums = [1,2,3,4]
 *   Output: [24,12,8,6]
 *
 * Constraints:
 *   2 <= nums.length <= 10^5
 *   -30 <= nums[i] <= 30
 *   The product of any prefix or suffix fits in a 32-bit integer.
 *
 * JP Morgan Context:
 *   Tests prefix/suffix thinking — common in financial calculations
 *   (e.g., cumulative returns, portfolio weights).
 *
 * ============================================================
 * Approach 1 — Brute Force
 * ============================================================
 *   For each index i, multiply all elements except nums[i].
 *
 *   Step-by-step ([1,2,3,4]):
 *     i=0: 2*3*4=24
 *     i=1: 1*3*4=12
 *     i=2: 1*2*4=8
 *     i=3: 1*2*3=6
 *
 *   TC: O(n²)  SC: O(1) (output array doesn't count)
 *
 * ============================================================
 * Approach 2 — Prefix + Suffix Arrays
 * ============================================================
 *   Build two arrays:
 *     prefix[i] = product of all elements to the LEFT of i
 *     suffix[i] = product of all elements to the RIGHT of i
 *   answer[i] = prefix[i] * suffix[i]
 *
 *   Step-by-step ([1,2,3,4]):
 *     prefix = [1, 1, 2, 6]
 *     suffix = [24,12, 4, 1]
 *     answer = [1*24, 1*12, 2*4, 6*1] = [24, 12, 8, 6]
 *
 *   TC: O(n)  SC: O(n)
 *
 * ============================================================
 * Approach 3 — Single Output Array (Optimal)
 * ============================================================
 *   Use the output array itself to store prefix products.
 *   Then do a right-to-left pass with a running suffix variable.
 *
 *   Step-by-step ([1,2,3,4]):
 *     Left pass (store prefix in result):
 *       result = [1, 1, 2, 6]
 *     Right pass (multiply suffix into result):
 *       suffix=1
 *       i=3: result[3]=6*1=6,   suffix=1*4=4
 *       i=2: result[2]=2*4=8,   suffix=4*3=12
 *       i=1: result[1]=1*12=12, suffix=12*2=24
 *       i=0: result[0]=1*24=24, suffix=24*1=24
 *     result = [24, 12, 8, 6]
 *
 *   TC: O(n)  SC: O(1) (output array excluded from space complexity)
 */

import java.util.*;

public class ProductOfArrayExceptSelf {

    // =========================================================================
    // Approach 1: Brute Force — O(n²) time, O(1) space
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
    // Approach 2: Prefix + Suffix Arrays — O(n) time, O(n) space
    // =========================================================================
    public static int[] productPrefixSuffix(int[] nums) {
        int n = nums.length;
        int[] prefix = new int[n];
        int[] suffix = new int[n];
        int[] result = new int[n];

        prefix[0] = 1;
        for (int i = 1; i < n; i++) {
            prefix[i] = prefix[i - 1] * nums[i - 1];
        }

        suffix[n - 1] = 1;
        for (int i = n - 2; i >= 0; i--) {
            suffix[i] = suffix[i + 1] * nums[i + 1];
        }

        for (int i = 0; i < n; i++) {
            result[i] = prefix[i] * suffix[i];
        }
        return result;
    }

    // =========================================================================
    // Approach 3: Single Output Array (Optimal) — O(n) time, O(1) space
    // =========================================================================
    public static int[] productOptimal(int[] nums) {
        int n = nums.length;
        int[] result = new int[n];

        result[0] = 1;
        for (int i = 1; i < n; i++) {
            result[i] = result[i - 1] * nums[i - 1];
        }

        int suffix = 1;
        for (int i = n - 1; i >= 0; i--) {
            result[i] *= suffix;
            suffix *= nums[i];
        }
        return result;
    }

    // =========================================================================
    // Main — Test Cases
    // =========================================================================
    public static void main(String[] args) {
        System.out.println("=== Product of Array Except Self ===\n");

        int[] nums1 = {1, 2, 3, 4};
        System.out.println("Test 1: nums=[1,2,3,4]  Expected: [24,12,8,6]");
        System.out.println("  Brute:         " + Arrays.toString(productBrute(nums1)));
        System.out.println("  PrefixSuffix:  " + Arrays.toString(productPrefixSuffix(nums1)));
        System.out.println("  Optimal:       " + Arrays.toString(productOptimal(nums1)));

        int[] nums2 = {-1, 1, 0, -3, 3};
        System.out.println("\nTest 2: nums=[-1,1,0,-3,3]  Expected: [0,0,9,0,0]");
        System.out.println("  Optimal:       " + Arrays.toString(productOptimal(nums2)));
    }
}
