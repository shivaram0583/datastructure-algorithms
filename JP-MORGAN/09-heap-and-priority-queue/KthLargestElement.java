/**
 * Problem: Kth Largest Element in an Array (LeetCode #215)
 * Difficulty: Medium
 * Topics: Array, Heap, QuickSelect
 * Frequently asked at: JP Morgan Chase (Round 1)
 *
 * Description:
 *   Given an integer array nums and an integer k, return the kth largest
 *   element in the array (not the kth distinct).
 *
 * Example 1: nums=[3,2,1,5,6,4], k=2 → 5
 * Example 2: nums=[3,2,3,1,2,4,5,5,6], k=4 → 4
 *
 * Constraints:
 *   1 <= k <= nums.length <= 10^5
 *   -10^4 <= nums[i] <= 10^4
 *
 * JP Morgan Context:
 *   Finding the k-th largest trade size or k-th highest risk exposure.
 *   Follow-up: "Kth largest in a stream?" → use a fixed min-heap of size k.
 *
 * ============================================================
 * Approach 1 — Sort (Baseline)
 * ============================================================
 *   Sort descending, return index k-1.
 *   TC: O(n log n)  SC: O(1)
 *
 * ============================================================
 * Approach 2 — Min-Heap of Size k
 * ============================================================
 *   Maintain a min-heap of size k. After processing all elements,
 *   the heap top is the kth largest.
 *
 *   Why min-heap? The smallest of the top-k elements sits at the top,
 *   so we can quickly evict smaller elements.
 *
 *   Step-by-step ([3,2,1,5,6,4], k=2):
 *     Push 3:  heap=[3]
 *     Push 2:  heap=[2,3]
 *     Push 1:  heap size>2 → poll min(1), heap=[2,3]
 *     Push 5:  heap=[3,5], poll 2
 *     Push 6:  heap=[5,6], poll 3
 *     Push 4:  heap=[5,6], poll 4 (4<5, evict)
 *   Heap top = 5 ← 2nd largest
 *
 *   TC: O(n log k)  SC: O(k)
 *
 * ============================================================
 * Approach 3 — QuickSelect (Optimal Average)
 * ============================================================
 *   Partition around a pivot (like QuickSort). If pivot lands at
 *   position n-k (0-indexed from start), it's the answer.
 *   Otherwise recurse on left or right partition.
 *
 *   Average TC: O(n)  Worst TC: O(n²)  SC: O(1)
 */

import java.util.*;

public class KthLargestElement {

    // =========================================================================
    // Approach 1: Sort — O(n log n) time, O(1) space
    // =========================================================================
    public static int findKthLargestSort(int[] nums, int k) {
        Arrays.sort(nums);
        return nums[nums.length - k];
    }

    // =========================================================================
    // Approach 2: Min-Heap of Size k — O(n log k) time, O(k) space
    // =========================================================================
    public static int findKthLargestHeap(int[] nums, int k) {
        PriorityQueue<Integer> minHeap = new PriorityQueue<>();
        for (int num : nums) {
            minHeap.offer(num);
            if (minHeap.size() > k) minHeap.poll();
        }
        return minHeap.peek();
    }

    // =========================================================================
    // Approach 3: QuickSelect — O(n) average time, O(1) space
    // =========================================================================
    public static int findKthLargestQuickSelect(int[] nums, int k) {
        int target = nums.length - k;
        int left = 0, right = nums.length - 1;
        while (left < right) {
            int pivot = partition(nums, left, right);
            if (pivot == target) return nums[pivot];
            else if (pivot < target) left = pivot + 1;
            else right = pivot - 1;
        }
        return nums[left];
    }

    private static int partition(int[] nums, int left, int right) {
        int pivot = nums[right];
        int i = left;
        for (int j = left; j < right; j++) {
            if (nums[j] <= pivot) {
                int tmp = nums[i]; nums[i] = nums[j]; nums[j] = tmp;
                i++;
            }
        }
        int tmp = nums[i]; nums[i] = nums[right]; nums[right] = tmp;
        return i;
    }

    // =========================================================================
    // Main — Test Cases
    // =========================================================================
    public static void main(String[] args) {
        System.out.println("=== Kth Largest Element ===\n");

        System.out.println("Test 1: [3,2,1,5,6,4], k=2  Expected: 5");
        System.out.println("  Sort:        " + findKthLargestSort(new int[]{3,2,1,5,6,4}, 2));
        System.out.println("  Heap:        " + findKthLargestHeap(new int[]{3,2,1,5,6,4}, 2));
        System.out.println("  QuickSelect: " + findKthLargestQuickSelect(new int[]{3,2,1,5,6,4}, 2));

        System.out.println("\nTest 2: [3,2,3,1,2,4,5,5,6], k=4  Expected: 4");
        System.out.println("  Heap:        " + findKthLargestHeap(new int[]{3,2,3,1,2,4,5,5,6}, 4));
        System.out.println("  QuickSelect: " + findKthLargestQuickSelect(new int[]{3,2,3,1,2,4,5,5,6}, 4));
    }
}
