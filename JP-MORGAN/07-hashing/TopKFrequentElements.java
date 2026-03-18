/**
 * Problem: Top K Frequent Elements (LeetCode #347)
 * Difficulty: Medium
 * Topics: Array, Hash Table, Heap, Bucket Sort
 * Frequently asked at: JP Morgan Chase (Round 1)
 *
 * Description:
 *   Given an integer array nums and an integer k, return the k most
 *   frequent elements. You may return the answer in any order.
 *
 * Example 1:
 *   nums=[1,1,1,2,2,3], k=2 → [1,2]
 * Example 2:
 *   nums=[1], k=1 → [1]
 *
 * Constraints:
 *   1 <= nums.length <= 10^5
 *   k is in range [1, number of unique elements]
 *   Answer is unique
 *
 * JP Morgan Context:
 *   Models finding top-k traded instruments, most active accounts,
 *   or most frequent transaction types in a given time window.
 *
 * ============================================================
 * Approach 1 — HashMap + Sort
 * ============================================================
 *   Count frequencies. Sort by frequency descending. Take first k.
 *   TC: O(n log n)  SC: O(n)
 *
 * ============================================================
 * Approach 2 — HashMap + Min-Heap of Size k
 * ============================================================
 *   Count frequencies. Use a min-heap of size k.
 *   If heap size exceeds k, remove the element with lowest frequency.
 *   This keeps the k most frequent elements in the heap.
 *
 *   TC: O(n log k)  SC: O(n + k)
 *
 * ============================================================
 * Approach 3 — Bucket Sort (Optimal)
 * ============================================================
 *   Count frequencies (max frequency = n).
 *   Create buckets[freq] = list of numbers with that frequency.
 *   Scan buckets from high frequency to low, collect k elements.
 *
 *   Step-by-step ([1,1,1,2,2,3], k=2):
 *     freq: {1:3, 2:2, 3:1}
 *     buckets: [[], [3], [2], [1], [], [], []]
 *     Scan from high: bucket[3]=[1] → add 1, bucket[2]=[2] → add 2
 *     Result: [1, 2]
 *
 *   TC: O(n)  SC: O(n)
 */

import java.util.*;

public class TopKFrequentElements {

    // =========================================================================
    // Approach 1: HashMap + Sort — O(n log n) time, O(n) space
    // =========================================================================
    public static int[] topKSort(int[] nums, int k) {
        Map<Integer, Integer> freq = new HashMap<>();
        for (int n : nums) freq.merge(n, 1, Integer::sum);

        return freq.entrySet().stream()
            .sorted((a, b) -> b.getValue() - a.getValue())
            .limit(k)
            .mapToInt(Map.Entry::getKey)
            .toArray();
    }

    // =========================================================================
    // Approach 2: HashMap + Min-Heap — O(n log k) time, O(n+k) space
    // =========================================================================
    public static int[] topKHeap(int[] nums, int k) {
        Map<Integer, Integer> freq = new HashMap<>();
        for (int n : nums) freq.merge(n, 1, Integer::sum);

        PriorityQueue<Integer> minHeap = new PriorityQueue<>(
            (a, b) -> freq.get(a) - freq.get(b)
        );

        for (int num : freq.keySet()) {
            minHeap.offer(num);
            if (minHeap.size() > k) minHeap.poll();
        }

        int[] result = new int[k];
        for (int i = k - 1; i >= 0; i--) result[i] = minHeap.poll();
        return result;
    }

    // =========================================================================
    // Approach 3: Bucket Sort (Optimal) — O(n) time, O(n) space
    // =========================================================================
    @SuppressWarnings("unchecked")
    public static int[] topKBucket(int[] nums, int k) {
        Map<Integer, Integer> freq = new HashMap<>();
        for (int n : nums) freq.merge(n, 1, Integer::sum);

        List<Integer>[] buckets = new List[nums.length + 1];
        for (int num : freq.keySet()) {
            int f = freq.get(num);
            if (buckets[f] == null) buckets[f] = new ArrayList<>();
            buckets[f].add(num);
        }

        int[] result = new int[k];
        int idx = 0;
        for (int i = buckets.length - 1; i >= 0 && idx < k; i--) {
            if (buckets[i] != null) {
                for (int num : buckets[i]) {
                    result[idx++] = num;
                    if (idx == k) break;
                }
            }
        }
        return result;
    }

    // =========================================================================
    // Main — Test Cases
    // =========================================================================
    public static void main(String[] args) {
        System.out.println("=== Top K Frequent Elements ===\n");

        int[] nums1 = {1, 1, 1, 2, 2, 3};
        System.out.println("Test 1: [1,1,1,2,2,3], k=2  Expected: [1,2]");
        System.out.println("  Sort:   " + Arrays.toString(topKSort(nums1, 2)));
        System.out.println("  Heap:   " + Arrays.toString(topKHeap(nums1, 2)));
        System.out.println("  Bucket: " + Arrays.toString(topKBucket(nums1, 2)));

        int[] nums2 = {1};
        System.out.println("\nTest 2: [1], k=1  Expected: [1]");
        System.out.println("  Bucket: " + Arrays.toString(topKBucket(nums2, 1)));

        int[] nums3 = {4, 1, -1, 2, -1, 2, 3};
        System.out.println("\nTest 3: [4,1,-1,2,-1,2,3], k=2  Expected: [-1,2]");
        System.out.println("  Bucket: " + Arrays.toString(topKBucket(nums3, 2)));
    }
}
