/**
 * Problem: Longest Consecutive Sequence (LeetCode #128)
 * Difficulty: Medium
 * Topics: Array, Hash Table, Union Find
 * Frequently asked at: Microsoft
 *
 * Description:
 *   Given an unsorted array, find the length of the longest consecutive sequence.
 *   Must run in O(n) time.
 *
 * Approach 1 – Brute Force (Sort)
 *   Sort and count consecutive runs. TC: O(n log n) | SC: O(1)
 *
 * Approach 2 – HashSet (Optimal/Best)
 *   Add all to set; for each sequence start, count length.
 *   TC: O(n) | SC: O(n)
 *
 * Approach 3 – Union Find
 *   Union consecutive elements. TC: O(n * α(n)) ≈ O(n) | SC: O(n)
 */

import java.util.*;

public class LongestConsecutiveSequence {

    // =========================================================================
    // Approach 1: Sort — O(n log n)
    // =========================================================================
    public static int longestBrute(int[] nums) {
        if (nums.length == 0) return 0;
        Arrays.sort(nums);
        int longest = 1, current = 1;
        for (int i = 1; i < nums.length; i++) {
            if (nums[i] == nums[i-1]) continue; // skip duplicates
            if (nums[i] == nums[i-1] + 1) {
                current++;
            } else {
                longest = Math.max(longest, current);
                current = 1;
            }
        }
        return Math.max(longest, current);
    }

    // =========================================================================
    // Approach 2: HashSet (Best) — O(n)
    // =========================================================================
    public static int longestBest(int[] nums) {
        Set<Integer> set = new HashSet<>();
        for (int n : nums) set.add(n);

        int longest = 0;
        for (int n : set) {
            // Only start counting from sequence start
            if (!set.contains(n - 1)) {
                int length = 1;
                while (set.contains(n + length)) length++;
                longest = Math.max(longest, length);
            }
        }
        return longest;
    }

    // =========================================================================
    // Approach 3: Union Find — O(n * α(n))
    // =========================================================================
    public static int longestUF(int[] nums) {
        if (nums.length == 0) return 0;
        Map<Integer, Integer> parent = new HashMap<>();
        Map<Integer, Integer> size = new HashMap<>();

        for (int n : nums) {
            if (parent.containsKey(n)) continue;
            parent.put(n, n);
            size.put(n, 1);

            if (parent.containsKey(n - 1)) union(parent, size, n, n - 1);
            if (parent.containsKey(n + 1)) union(parent, size, n, n + 1);
        }

        int max = 0;
        for (int s : size.values()) max = Math.max(max, s);
        return max;
    }

    private static int find(Map<Integer, Integer> parent, int x) {
        while (parent.get(x) != x) {
            parent.put(x, parent.get(parent.get(x)));
            x = parent.get(x);
        }
        return x;
    }

    private static void union(Map<Integer, Integer> parent, Map<Integer, Integer> size, int a, int b) {
        int ra = find(parent, a), rb = find(parent, b);
        if (ra == rb) return;
        if (size.get(ra) < size.get(rb)) { int t = ra; ra = rb; rb = t; }
        parent.put(rb, ra);
        size.put(ra, size.get(ra) + size.get(rb));
    }

    // =========================================================================
    // Main
    // =========================================================================
    public static void main(String[] args) {
        System.out.println("=== Longest Consecutive Sequence ===");
        int[] nums = {100, 4, 200, 1, 3, 2};

        System.out.println("Sort:    " + longestBrute(nums.clone()));  // 4
        System.out.println("HashSet: " + longestBest(nums));           // 4
        System.out.println("UF:      " + longestUF(nums));             // 4
    }
}
