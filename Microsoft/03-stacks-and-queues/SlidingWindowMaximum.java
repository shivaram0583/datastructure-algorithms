/**
 * Problem: Sliding Window Maximum (LeetCode #239)
 * Difficulty: Hard
 * Topics: Array, Queue, Sliding Window, Monotonic Deque
 * Frequently asked at: Microsoft
 *
 * Description:
 *   Given an array nums and a sliding window of size k moving from left to right,
 *   return the max in each window position.
 *
 * Example:
 *   Input:  nums = [1,3,-1,-3,5,3,6,7], k = 3
 *   Output: [3,3,5,5,6,7]
 *
 * Approach 1 – Brute Force
 *   For each window, scan for max. TC: O(n*k) | SC: O(1)
 *
 * Approach 2 – Max Heap (Optimal)
 *   Use a max-heap; lazily remove out-of-window elements.
 *   TC: O(n log n) | SC: O(n)
 *
 * Approach 3 – Monotonic Deque (Best)
 *   Maintain a decreasing deque of indices.
 *   TC: O(n) | SC: O(k)
 */

import java.util.*;

public class SlidingWindowMaximum {

    // =========================================================================
    // Approach 1: Brute Force — O(n*k) time, O(1) extra space
    // =========================================================================
    public static int[] maxSlidingBrute(int[] nums, int k) {
        int n = nums.length;
        int[] result = new int[n - k + 1];
        for (int i = 0; i <= n - k; i++) {
            int max = Integer.MIN_VALUE;
            for (int j = i; j < i + k; j++) {
                max = Math.max(max, nums[j]);
            }
            result[i] = max;
        }
        return result;
    }

    // =========================================================================
    // Approach 2: Max Heap — O(n log n) time, O(n) space
    // =========================================================================
    public static int[] maxSlidingOptimal(int[] nums, int k) {
        int n = nums.length;
        int[] result = new int[n - k + 1];
        // Max-heap of {value, index}
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> b[0] - a[0]);

        for (int i = 0; i < n; i++) {
            pq.offer(new int[]{nums[i], i});
            // Remove elements outside the window
            while (pq.peek()[1] < i - k + 1) pq.poll();
            if (i >= k - 1) {
                result[i - k + 1] = pq.peek()[0];
            }
        }
        return result;
    }

    // =========================================================================
    // Approach 3: Monotonic Deque (Best) — O(n) time, O(k) space
    // =========================================================================
    public static int[] maxSlidingBest(int[] nums, int k) {
        int n = nums.length;
        int[] result = new int[n - k + 1];
        Deque<Integer> deque = new ArrayDeque<>(); // stores indices

        for (int i = 0; i < n; i++) {
            // Remove indices outside the window
            while (!deque.isEmpty() && deque.peekFirst() < i - k + 1) {
                deque.pollFirst();
            }
            // Remove smaller elements from back
            while (!deque.isEmpty() && nums[deque.peekLast()] < nums[i]) {
                deque.pollLast();
            }
            deque.offerLast(i);
            if (i >= k - 1) {
                result[i - k + 1] = nums[deque.peekFirst()];
            }
        }
        return result;
    }

    // =========================================================================
    // Main — Test Cases
    // =========================================================================
    public static void main(String[] args) {
        System.out.println("=== Sliding Window Maximum ===");

        int[] nums = {1, 3, -1, -3, 5, 3, 6, 7};
        int k = 3;
        System.out.println("Input: [1,3,-1,-3,5,3,6,7], k=3");
        System.out.println("Brute:   " + Arrays.toString(maxSlidingBrute(nums, k)));
        System.out.println("Optimal: " + Arrays.toString(maxSlidingOptimal(nums, k)));
        System.out.println("Best:    " + Arrays.toString(maxSlidingBest(nums, k)));
        // Expected: [3,3,5,5,6,7]
    }
}
