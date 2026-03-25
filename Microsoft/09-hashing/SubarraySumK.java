/**
 * Problem: Subarray Sum Equals K (LeetCode #560)
 * Difficulty: Medium
 * Topics: Array, Hash Table, Prefix Sum
 * Frequently asked at: Microsoft
 *
 * Description:
 *   Given an array of integers nums and an integer k, return the total number
 *   of subarrays whose sum equals to k.
 *
 * Approach 1 – Brute Force
 *   Check all subarrays. TC: O(n²) | SC: O(1)
 *
 * Approach 2 – Prefix Sum + HashMap (Optimal/Best)
 *   Store prefix sum frequencies; check if (prefixSum - k) exists.
 *   TC: O(n) | SC: O(n)
 *
 * Approach 3 – Prefix Sum Array + Two loops
 *   Pre-compute prefix sums, then check all pairs.
 *   TC: O(n²) | SC: O(n)
 */

import java.util.*;

public class SubarraySumK {

    // =========================================================================
    // Approach 1: Brute Force — O(n²)
    // =========================================================================
    public static int subarraySumBrute(int[] nums, int k) {
        int count = 0;
        for (int i = 0; i < nums.length; i++) {
            int sum = 0;
            for (int j = i; j < nums.length; j++) {
                sum += nums[j];
                if (sum == k) count++;
            }
        }
        return count;
    }

    // =========================================================================
    // Approach 2: Prefix Sum + HashMap (Best) — O(n)
    // =========================================================================
    public static int subarraySumBest(int[] nums, int k) {
        Map<Integer, Integer> prefixCount = new HashMap<>();
        prefixCount.put(0, 1); // empty subarray
        int sum = 0, count = 0;

        for (int num : nums) {
            sum += num;
            // If (sum - k) has been seen before, those prefixes form valid subarrays
            count += prefixCount.getOrDefault(sum - k, 0);
            prefixCount.merge(sum, 1, Integer::sum);
        }
        return count;
    }

    // =========================================================================
    // Approach 3: Prefix Sum Array — O(n²) time, O(n) space
    // =========================================================================
    public static int subarraySumPrefix(int[] nums, int k) {
        int n = nums.length;
        int[] prefix = new int[n + 1];
        for (int i = 0; i < n; i++) prefix[i + 1] = prefix[i] + nums[i];

        int count = 0;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j <= n; j++) {
                if (prefix[j] - prefix[i] == k) count++;
            }
        }
        return count;
    }

    // =========================================================================
    // Main
    // =========================================================================
    public static void main(String[] args) {
        System.out.println("=== Subarray Sum Equals K ===");
        int[] nums = {1, 1, 1};

        System.out.println("k=2: Brute=" + subarraySumBrute(nums, 2)
            + " Best=" + subarraySumBest(nums, 2)
            + " Prefix=" + subarraySumPrefix(nums, 2)); // 2

        int[] nums2 = {1, 2, 3};
        System.out.println("k=3: Brute=" + subarraySumBrute(nums2, 3)
            + " Best=" + subarraySumBest(nums2, 3)); // 2
    }
}
