/**
 * Problem: Reorganize String (LeetCode #767)
 * Difficulty: Medium
 * Topics: Hash Table, String, Greedy, Heap
 * Frequently asked at: Microsoft
 *
 * Description:
 *   Given a string s, rearrange characters so no two adjacent characters
 *   are the same. Return "" if not possible.
 *
 * Approach 1 – Brute Force (Permutation check)
 *   Try all permutations — impractical. TC: O(n!) | SC: O(n)
 *
 * Approach 2 – Max Heap (Optimal)
 *   Greedily pick most frequent char; alternate.
 *   TC: O(n log A) where A=26 | SC: O(A)
 *
 * Approach 3 – Counting + Greedy Fill (Best)
 *   Place most frequent at even indices, fill rest at odd indices.
 *   TC: O(n) | SC: O(A)
 */

import java.util.*;

public class ReorganizeString {

    // =========================================================================
    // Approach 1: Simple Greedy with Sort — O(n * 26 log 26) ≈ O(n)
    // =========================================================================
    public static String reorganizeBrute(String s) {
        int[] count = new int[26];
        for (char c : s.toCharArray()) count[c - 'a']++;

        int n = s.length();
        for (int c : count) if (c > (n + 1) / 2) return "";

        char[] result = new char[n];
        // Sort indices by count descending, place alternately
        Integer[] idx = new Integer[26];
        for (int i = 0; i < 26; i++) idx[i] = i;
        Arrays.sort(idx, (a, b) -> count[b] - count[a]);

        int pos = 0;
        for (int i : idx) {
            while (count[i] > 0) {
                if (pos >= n) pos = 1; // wrap to odd positions
                result[pos] = (char) ('a' + i);
                pos += 2;
                count[i]--;
            }
        }
        return new String(result);
    }

    // =========================================================================
    // Approach 2: Max Heap — O(n log 26) ≈ O(n)
    // =========================================================================
    public static String reorganizeOptimal(String s) {
        int[] count = new int[26];
        for (char c : s.toCharArray()) count[c - 'a']++;

        PriorityQueue<int[]> maxHeap = new PriorityQueue<>((a, b) -> b[1] - a[1]);
        for (int i = 0; i < 26; i++) {
            if (count[i] > 0) maxHeap.offer(new int[]{i, count[i]});
        }

        StringBuilder sb = new StringBuilder();
        while (maxHeap.size() >= 2) {
            int[] first = maxHeap.poll();
            int[] second = maxHeap.poll();
            sb.append((char) ('a' + first[0]));
            sb.append((char) ('a' + second[0]));
            if (--first[1] > 0) maxHeap.offer(first);
            if (--second[1] > 0) maxHeap.offer(second);
        }

        if (!maxHeap.isEmpty()) {
            int[] last = maxHeap.poll();
            if (last[1] > 1) return "";
            sb.append((char) ('a' + last[0]));
        }
        return sb.toString();
    }

    // =========================================================================
    // Approach 3: Counting + Greedy Fill (Best) — O(n)
    // =========================================================================
    public static String reorganizeBest(String s) {
        int[] count = new int[26];
        int maxCount = 0, maxChar = 0, n = s.length();
        for (char c : s.toCharArray()) {
            count[c - 'a']++;
            if (count[c - 'a'] > maxCount) {
                maxCount = count[c - 'a'];
                maxChar = c - 'a';
            }
        }
        if (maxCount > (n + 1) / 2) return "";

        char[] result = new char[n];
        int idx = 0;

        // Place the most frequent character at even indices first
        while (count[maxChar] > 0) {
            result[idx] = (char) ('a' + maxChar);
            idx += 2;
            count[maxChar]--;
        }

        // Place remaining characters
        for (int i = 0; i < 26; i++) {
            while (count[i] > 0) {
                if (idx >= n) idx = 1;
                result[idx] = (char) ('a' + i);
                idx += 2;
                count[i]--;
            }
        }
        return new String(result);
    }

    // =========================================================================
    // Main
    // =========================================================================
    public static void main(String[] args) {
        System.out.println("=== Reorganize String ===");

        System.out.println("\"aab\" → Heap:  " + reorganizeOptimal("aab"));  // "aba"
        System.out.println("\"aab\" → Greedy:" + reorganizeBest("aab"));     // "aba"
        System.out.println("\"aaab\" → Heap: " + reorganizeOptimal("aaab")); // ""
    }
}
