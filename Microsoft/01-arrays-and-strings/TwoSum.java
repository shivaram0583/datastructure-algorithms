/**
 * Problem: Two Sum (LeetCode #1)
 * Difficulty: Medium
 * Topics: Array, Hash Table
 * Frequently asked at: Microsoft
 *
 * Description:
 *   Given an array of integers nums and an integer target, return indices
 *   of the two numbers such that they add up to target.
 *   You may assume that each input would have exactly one solution,
 *   and you may not use the same element twice.
 *
 * Example:
 *   Input:  nums = [2,7,11,15], target = 9
 *   Output: [0,1]  (because nums[0] + nums[1] == 9)
 *
 * Approach 1 – Brute Force
 *   Check every pair (i,j). TC: O(n²) | SC: O(1)
 *
 * Approach 2 – Sorting + Two Pointers
 *   Sort a copy, use two pointers, then map back original indices.
 *   TC: O(n log n) | SC: O(n)
 *
 * Approach 3 – Hash Map (Best)
 *   Single-pass hash map storing complement → index.
 *   TC: O(n) | SC: O(n)
 */

import java.util.*;

public class TwoSum {

    // =========================================================================
    // Approach 1: Brute Force — O(n²) time, O(1) space
    // =========================================================================
    public static int[] twoSumBrute(int[] nums, int target) {
        for (int i = 0; i < nums.length; i++) {
            for (int j = i + 1; j < nums.length; j++) {
                if (nums[i] + nums[j] == target) {
                    return new int[]{i, j};
                }
            }
        }
        return new int[]{};
    }

    // =========================================================================
    // Approach 2: Sorting + Two Pointers — O(n log n) time, O(n) space
    // =========================================================================
    public static int[] twoSumSorting(int[] nums, int target) {
        int n = nums.length;
        int[][] indexed = new int[n][2]; // {value, original index}
        for (int i = 0; i < n; i++) {
            indexed[i][0] = nums[i];
            indexed[i][1] = i;
        }
        Arrays.sort(indexed, (a, b) -> a[0] - b[0]);

        int left = 0, right = n - 1;
        while (left < right) {
            int sum = indexed[left][0] + indexed[right][0];
            if (sum == target) {
                return new int[]{indexed[left][1], indexed[right][1]};
            } else if (sum < target) {
                left++;
            } else {
                right--;
            }
        }
        return new int[]{};
    }

    // =========================================================================
    // Approach 3: Hash Map (Best) — O(n) time, O(n) space
    // =========================================================================
    public static int[] twoSumBest(int[] nums, int target) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            int complement = target - nums[i];
            if (map.containsKey(complement)) {
                return new int[]{map.get(complement), i};
            }
            map.put(nums[i], i);
        }
        return new int[]{};
    }

    // =========================================================================
    // Main — Test Cases
    // =========================================================================
    public static void main(String[] args) {
        int[] nums1 = {2, 7, 11, 15};
        int target1 = 9;

        System.out.println("=== Two Sum ===");
        System.out.println("Input: nums = [2,7,11,15], target = 9");
        System.out.println("Brute:   " + Arrays.toString(twoSumBrute(nums1, target1)));
        System.out.println("Sorting: " + Arrays.toString(twoSumSorting(nums1, target1)));
        System.out.println("Best:    " + Arrays.toString(twoSumBest(nums1, target1)));

        int[] nums2 = {3, 2, 4};
        int target2 = 6;
        System.out.println("\nInput: nums = [3,2,4], target = 6");
        System.out.println("Brute:   " + Arrays.toString(twoSumBrute(nums2, target2)));
        System.out.println("Sorting: " + Arrays.toString(twoSumSorting(nums2, target2)));
        System.out.println("Best:    " + Arrays.toString(twoSumBest(nums2, target2)));
    }
}
