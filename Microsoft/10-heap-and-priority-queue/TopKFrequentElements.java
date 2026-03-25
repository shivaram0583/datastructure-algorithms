/**
 * Problem: Top K Frequent Elements (LeetCode #347)
 * Difficulty: Medium
 * Topics: Array, Hash Table, Heap, Bucket Sort
 * Frequently asked at: Microsoft
 *
 * Description:
 *   Given an integer array nums and an integer k, return the k most frequent elements.
 *
 * Approach 1 – Sort by Frequency (Brute)
 *   Count freq, sort entries. TC: O(n log n) | SC: O(n)
 *
 * Approach 2 – Min Heap (Optimal)
 *   Heap of size k. TC: O(n log k) | SC: O(n)
 *
 * Approach 3 – Bucket Sort (Best)
 *   Buckets indexed by frequency. TC: O(n) | SC: O(n)
 */

import java.util.*;

public class TopKFrequentElements {

    // =========================================================================
    // Approach 1: Sort — O(n log n)
    // =========================================================================
    public static int[] topKBrute(int[] nums, int k) {
        Map<Integer, Integer> freq = new HashMap<>();
        for (int n : nums) freq.merge(n, 1, Integer::sum);

        List<Map.Entry<Integer, Integer>> entries = new ArrayList<>(freq.entrySet());
        entries.sort((a, b) -> b.getValue() - a.getValue());

        int[] result = new int[k];
        for (int i = 0; i < k; i++) result[i] = entries.get(i).getKey();
        return result;
    }

    // =========================================================================
    // Approach 2: Min Heap — O(n log k)
    // =========================================================================
    public static int[] topKOptimal(int[] nums, int k) {
        Map<Integer, Integer> freq = new HashMap<>();
        for (int n : nums) freq.merge(n, 1, Integer::sum);

        PriorityQueue<Integer> minHeap = new PriorityQueue<>((a, b) -> freq.get(a) - freq.get(b));
        for (int key : freq.keySet()) {
            minHeap.offer(key);
            if (minHeap.size() > k) minHeap.poll();
        }

        int[] result = new int[k];
        for (int i = 0; i < k; i++) result[i] = minHeap.poll();
        return result;
    }

    // =========================================================================
    // Approach 3: Bucket Sort (Best) — O(n)
    // =========================================================================
    @SuppressWarnings("unchecked")
    public static int[] topKBest(int[] nums, int k) {
        Map<Integer, Integer> freq = new HashMap<>();
        for (int n : nums) freq.merge(n, 1, Integer::sum);

        List<Integer>[] buckets = new List[nums.length + 1];
        for (int i = 0; i < buckets.length; i++) buckets[i] = new ArrayList<>();

        for (var entry : freq.entrySet()) {
            buckets[entry.getValue()].add(entry.getKey());
        }

        int[] result = new int[k];
        int idx = 0;
        for (int i = buckets.length - 1; i >= 0 && idx < k; i--) {
            for (int num : buckets[i]) {
                result[idx++] = num;
                if (idx == k) break;
            }
        }
        return result;
    }

    // =========================================================================
    // Main
    // =========================================================================
    public static void main(String[] args) {
        System.out.println("=== Top K Frequent Elements ===");
        int[] nums = {1, 1, 1, 2, 2, 3};

        System.out.println("Sort:   " + Arrays.toString(topKBrute(nums, 2)));
        System.out.println("Heap:   " + Arrays.toString(topKOptimal(nums, 2)));
        System.out.println("Bucket: " + Arrays.toString(topKBest(nums, 2)));
        // [1, 2]
    }
}
