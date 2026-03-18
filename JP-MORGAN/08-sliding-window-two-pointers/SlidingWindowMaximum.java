/**
 * Problem: Sliding Window Maximum (LeetCode #239)
 * Difficulty: Hard
 * Topics: Array, Queue, Sliding Window, Monotonic Deque
 * Frequently asked at: JP Morgan Chase (Round 1)
 *
 * Description:
 *   Given an array nums and a sliding window of size k, return the max
 *   value in the window as it slides from left to right.
 *
 * Example:
 *   nums=[3,1,-1,-3,5,3,6,7], k=3
 *   Windows:  [3,1,-1]→3, [1,-1,-3]→1, [-1,-3,5]→5,
 *             [-3,5,3]→5, [5,3,6]→6, [3,6,7]→7
 *   Output:   [3,1,5,5,6,7]
 *
 * Constraints:
 *   1 <= nums.length <= 10^5
 *   -10^4 <= nums[i] <= 10^4
 *   1 <= k <= nums.length
 *
 * JP Morgan Context:
 *   Rolling maximum price over a window — used in technical analysis
 *   (resistance levels), risk VaR windows, and order book snapshots.
 *
 * ============================================================
 * Approach 1 — Brute Force
 * ============================================================
 *   For each window position, find the max by scanning k elements.
 *   TC: O(n*k)  SC: O(1)
 *
 * ============================================================
 * Approach 2 — Monotonic Deque (Optimal)
 * ============================================================
 *   Maintain a deque of indices in decreasing order of their values.
 *   The front of the deque always holds the index of the current window max.
 *
 *   Rules:
 *   - Before adding index i: pop from back all indices whose values are
 *     <= nums[i]  (they can never be the max while nums[i] is in window).
 *   - After adding: pop from front if that index is out of window (< i-k+1).
 *   - If window is full (i >= k-1): record deque.front value as answer.
 *
 *   Step-by-step ([3,1,-1,-3,5,3,6,7], k=3):
 *     i=0: deque=[0]
 *     i=1: nums[1]=1 < nums[0]=3, deque=[0,1]
 *     i=2: nums[2]=-1 < nums[1]=1, deque=[0,1,2]
 *           i>=k-1: result=[nums[0]]=[ 3]
 *     i=3: nums[3]=-3 < nums[2]=-1, deque=[0,1,2,3]
 *           front=0 < i-k+1=1 → remove front, deque=[1,2,3]
 *           result=[3, nums[1]]=[3,1]
 *     i=4: nums[4]=5 > all → pop 3,2,1, deque=[4]
 *           front=4 >= 2, result=[3,1, nums[4]]=[3,1,5]
 *     i=5: nums[5]=3 < 5, deque=[4,5]
 *           front=4 >= 3, result=[3,1,5,5]
 *     i=6: nums[6]=6>3 → pop 5, deque=[4,6]
 *           front=4 < 4 → remove, deque=[6]
 *           result=[3,1,5,5,6]
 *     i=7: nums[7]=7>6 → pop 6, deque=[7]
 *           result=[3,1,5,5,6,7]
 *
 *   TC: O(n)  SC: O(k)
 */

import java.util.*;

public class SlidingWindowMaximum {

    // =========================================================================
    // Approach 1: Brute Force — O(n*k) time, O(1) space
    // =========================================================================
    public static int[] maxSlidingWindowBrute(int[] nums, int k) {
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
    // Approach 2: Monotonic Deque (Optimal) — O(n) time, O(k) space
    // =========================================================================
    public static int[] maxSlidingWindowDeque(int[] nums, int k) {
        int n = nums.length;
        int[] result = new int[n - k + 1];
        Deque<Integer> deque = new ArrayDeque<>();

        for (int i = 0; i < n; i++) {
            while (!deque.isEmpty() && nums[deque.peekLast()] <= nums[i]) {
                deque.pollLast();
            }
            deque.offerLast(i);

            if (deque.peekFirst() < i - k + 1) {
                deque.pollFirst();
            }

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
        System.out.println("=== Sliding Window Maximum ===\n");

        int[] nums1 = {3, 1, -1, -3, 5, 3, 6, 7};
        System.out.println("Test 1: [3,1,-1,-3,5,3,6,7], k=3  Expected: [3,1,5,5,6,7]");
        System.out.println("  Brute: " + Arrays.toString(maxSlidingWindowBrute(nums1, 3)));
        System.out.println("  Deque: " + Arrays.toString(maxSlidingWindowDeque(nums1, 3)));

        int[] nums2 = {1};
        System.out.println("\nTest 2: [1], k=1  Expected: [1]");
        System.out.println("  Deque: " + Arrays.toString(maxSlidingWindowDeque(nums2, 1)));

        int[] nums3 = {9, 11};
        System.out.println("\nTest 3: [9,11], k=2  Expected: [11]");
        System.out.println("  Deque: " + Arrays.toString(maxSlidingWindowDeque(nums3, 2)));

        int[] nums4 = {4, -2};
        System.out.println("\nTest 4: [4,-2], k=2  Expected: [4]");
        System.out.println("  Deque: " + Arrays.toString(maxSlidingWindowDeque(nums4, 2)));
    }
}
