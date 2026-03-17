/**
 * Problem: Median of Two Sorted Arrays (LeetCode #4)
 * Difficulty: Hard
 * Topics: Array, Binary Search, Divide and Conquer
 * Frequently asked at: Microsoft
 *
 * Description:
 *   Given two sorted arrays nums1 and nums2 of size m and n respectively,
 *   return the median of the two sorted arrays.
 *   The overall run time complexity should be O(log(m+n)).
 *
 * Example:
 *   Input:  nums1 = [1,3], nums2 = [2]
 *   Output: 2.0
 *
 * Approach 1 – Brute Force (Merge)
 *   Merge both arrays, find median. TC: O(m+n) | SC: O(m+n)
 *
 * Approach 2 – Count to Middle (Optimal)
 *   Merge-style walk without storing, stop at median position.
 *   TC: O(m+n) | SC: O(1)
 *
 * Approach 3 – Binary Search (Best)
 *   Binary search on the shorter array to find the correct partition.
 *   TC: O(log(min(m,n))) | SC: O(1)
 */

public class MedianOfTwoSortedArrays {

    // =========================================================================
    // Approach 1: Brute Force (Merge) — O(m+n) time, O(m+n) space
    // =========================================================================
    public static double medianBrute(int[] nums1, int[] nums2) {
        int m = nums1.length, n = nums2.length;
        int[] merged = new int[m + n];
        int i = 0, j = 0, k = 0;

        while (i < m && j < n) {
            merged[k++] = nums1[i] <= nums2[j] ? nums1[i++] : nums2[j++];
        }
        while (i < m) merged[k++] = nums1[i++];
        while (j < n) merged[k++] = nums2[j++];

        int total = m + n;
        if (total % 2 == 1) {
            return merged[total / 2];
        }
        return (merged[total / 2 - 1] + merged[total / 2]) / 2.0;
    }

    // =========================================================================
    // Approach 2: Count to Middle — O(m+n) time, O(1) space
    // =========================================================================
    public static double medianOptimal(int[] nums1, int[] nums2) {
        int m = nums1.length, n = nums2.length;
        int total = m + n;
        int mid = total / 2;
        int prev = -1, curr = -1;
        int i = 0, j = 0;

        for (int count = 0; count <= mid; count++) {
            prev = curr;
            if (i < m && (j >= n || nums1[i] <= nums2[j])) {
                curr = nums1[i++];
            } else {
                curr = nums2[j++];
            }
        }

        if (total % 2 == 1) return curr;
        return (prev + curr) / 2.0;
    }

    // =========================================================================
    // Approach 3: Binary Search (Best) — O(log(min(m,n))) time, O(1) space
    // =========================================================================
    public static double medianBest(int[] nums1, int[] nums2) {
        // Ensure nums1 is the shorter array
        if (nums1.length > nums2.length) {
            return medianBest(nums2, nums1);
        }

        int m = nums1.length, n = nums2.length;
        int low = 0, high = m;

        while (low <= high) {
            int partX = (low + high) / 2;          // partition in nums1
            int partY = (m + n + 1) / 2 - partX;   // partition in nums2

            int maxLeftX  = (partX == 0) ? Integer.MIN_VALUE : nums1[partX - 1];
            int minRightX = (partX == m) ? Integer.MAX_VALUE : nums1[partX];
            int maxLeftY  = (partY == 0) ? Integer.MIN_VALUE : nums2[partY - 1];
            int minRightY = (partY == n) ? Integer.MAX_VALUE : nums2[partY];

            if (maxLeftX <= minRightY && maxLeftY <= minRightX) {
                // Found the correct partition
                if ((m + n) % 2 == 1) {
                    return Math.max(maxLeftX, maxLeftY);
                }
                return (Math.max(maxLeftX, maxLeftY) + Math.min(minRightX, minRightY)) / 2.0;
            } else if (maxLeftX > minRightY) {
                high = partX - 1;
            } else {
                low = partX + 1;
            }
        }
        throw new IllegalArgumentException("Input arrays are not sorted");
    }

    // =========================================================================
    // Main — Test Cases
    // =========================================================================
    public static void main(String[] args) {
        System.out.println("=== Median of Two Sorted Arrays ===");

        int[] a1 = {1, 3}, b1 = {2};
        System.out.println("Input: [1,3] and [2]");
        System.out.println("Brute:   " + medianBrute(a1, b1));    // 2.0
        System.out.println("Optimal: " + medianOptimal(a1, b1));  // 2.0
        System.out.println("Best:    " + medianBest(a1, b1));      // 2.0

        int[] a2 = {1, 2}, b2 = {3, 4};
        System.out.println("\nInput: [1,2] and [3,4]");
        System.out.println("Brute:   " + medianBrute(a2, b2));    // 2.5
        System.out.println("Optimal: " + medianOptimal(a2, b2));  // 2.5
        System.out.println("Best:    " + medianBest(a2, b2));      // 2.5
    }
}
