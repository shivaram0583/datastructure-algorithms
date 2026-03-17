/**
 * Problem: Kth Largest Element in an Array (LeetCode #215)
 * Difficulty: Medium
 * Topics: Array, Divide and Conquer, Sorting, Heap
 * Frequently asked at: Microsoft
 *
 * Description:
 *   Given an integer array nums and an integer k, return the kth largest element.
 *
 * Approach 1 – Brute Force (Sort)
 *   Sort and return nums[n-k]. TC: O(n log n) | SC: O(1)
 *
 * Approach 2 – Min Heap (Optimal)
 *   Maintain a min-heap of size k. TC: O(n log k) | SC: O(k)
 *
 * Approach 3 – Quickselect (Best)
 *   Randomized partition. TC: O(n) avg, O(n²) worst | SC: O(1)
 */

import java.util.*;

public class KthLargestElement {

    // =========================================================================
    // Approach 1: Sort — O(n log n)
    // =========================================================================
    public static int findKthBrute(int[] nums, int k) {
        Arrays.sort(nums);
        return nums[nums.length - k];
    }

    // =========================================================================
    // Approach 2: Min Heap — O(n log k)
    // =========================================================================
    public static int findKthOptimal(int[] nums, int k) {
        PriorityQueue<Integer> minHeap = new PriorityQueue<>();
        for (int num : nums) {
            minHeap.offer(num);
            if (minHeap.size() > k) minHeap.poll();
        }
        return minHeap.peek();
    }

    // =========================================================================
    // Approach 3: Quickselect (Best) — O(n) average
    // =========================================================================
    public static int findKthBest(int[] nums, int k) {
        int target = nums.length - k; // convert to kth smallest
        return quickselect(nums, 0, nums.length - 1, target);
    }

    private static int quickselect(int[] nums, int lo, int hi, int k) {
        if (lo == hi) return nums[lo];

        // Random pivot
        Random rand = new Random();
        int pivotIdx = lo + rand.nextInt(hi - lo + 1);
        int pivot = nums[pivotIdx];
        // Move pivot to end
        swap(nums, pivotIdx, hi);

        int storeIdx = lo;
        for (int i = lo; i < hi; i++) {
            if (nums[i] < pivot) {
                swap(nums, i, storeIdx);
                storeIdx++;
            }
        }
        swap(nums, storeIdx, hi);

        if (storeIdx == k) return nums[storeIdx];
        else if (storeIdx < k) return quickselect(nums, storeIdx + 1, hi, k);
        else return quickselect(nums, lo, storeIdx - 1, k);
    }

    private static void swap(int[] nums, int i, int j) {
        int tmp = nums[i]; nums[i] = nums[j]; nums[j] = tmp;
    }

    // =========================================================================
    // Main
    // =========================================================================
    public static void main(String[] args) {
        System.out.println("=== Kth Largest Element ===");

        int[] nums = {3, 2, 1, 5, 6, 4};
        int k = 2;
        System.out.println("Input: [3,2,1,5,6,4], k=2");
        System.out.println("Sort:        " + findKthBrute(nums.clone(), k));   // 5
        System.out.println("Min Heap:    " + findKthOptimal(nums.clone(), k)); // 5
        System.out.println("Quickselect: " + findKthBest(nums.clone(), k));    // 5
    }
}
