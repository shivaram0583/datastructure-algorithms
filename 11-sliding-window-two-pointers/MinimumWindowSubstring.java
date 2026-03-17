/**
 * Problem: Minimum Window Substring (LeetCode #76)
 * Difficulty: Hard
 * Topics: Hash Table, String, Sliding Window
 * Frequently asked at: Microsoft
 *
 * Description:
 *   Given strings s and t, return the minimum window substring of s
 *   such that every character in t (including duplicates) is included.
 *
 * Approach 1 – Brute Force
 *   Check all substrings. TC: O(n² * m) | SC: O(m)
 *
 * Approach 2 – Sliding Window with HashMap (Optimal/Best)
 *   Expand right to satisfy, shrink left to minimize.
 *   TC: O(n + m) | SC: O(m)
 *
 * Approach 3 – Sliding Window with array counters (Best perf)
 *   Use int[128] instead of HashMap for speed.
 *   TC: O(n + m) | SC: O(1) — fixed 128 array
 */

import java.util.*;

public class MinimumWindowSubstring {

    // =========================================================================
    // Approach 1: Brute Force — O(n² * m)
    // =========================================================================
    public static String minWindowBrute(String s, String t) {
        int[] tFreq = new int[128];
        for (char c : t.toCharArray()) tFreq[c]++;

        String result = "";
        int minLen = Integer.MAX_VALUE;

        for (int i = 0; i < s.length(); i++) {
            int[] windowFreq = new int[128];
            for (int j = i; j < s.length(); j++) {
                windowFreq[s.charAt(j)]++;
                if (containsAll(windowFreq, tFreq) && (j - i + 1) < minLen) {
                    minLen = j - i + 1;
                    result = s.substring(i, j + 1);
                }
            }
        }
        return result;
    }

    private static boolean containsAll(int[] window, int[] target) {
        for (int i = 0; i < 128; i++) {
            if (window[i] < target[i]) return false;
        }
        return true;
    }

    // =========================================================================
    // Approach 2: Sliding Window + HashMap — O(n + m)
    // =========================================================================
    public static String minWindowOptimal(String s, String t) {
        Map<Character, Integer> tMap = new HashMap<>();
        for (char c : t.toCharArray()) tMap.merge(c, 1, Integer::sum);

        int required = tMap.size(), formed = 0;
        Map<Character, Integer> windowMap = new HashMap<>();
        int left = 0, minLen = Integer.MAX_VALUE, start = 0;

        for (int right = 0; right < s.length(); right++) {
            char c = s.charAt(right);
            windowMap.merge(c, 1, Integer::sum);
            if (tMap.containsKey(c) && windowMap.get(c).intValue() == tMap.get(c).intValue()) {
                formed++;
            }

            while (formed == required) {
                if (right - left + 1 < minLen) {
                    minLen = right - left + 1;
                    start = left;
                }
                char lc = s.charAt(left);
                windowMap.merge(lc, -1, Integer::sum);
                if (tMap.containsKey(lc) && windowMap.get(lc) < tMap.get(lc)) formed--;
                left++;
            }
        }
        return minLen == Integer.MAX_VALUE ? "" : s.substring(start, start + minLen);
    }

    // =========================================================================
    // Approach 3: Sliding Window + Array Counter (Best) — O(n + m)
    // =========================================================================
    public static String minWindowBest(String s, String t) {
        int[] count = new int[128];
        for (char c : t.toCharArray()) count[c]++;

        int required = t.length(), left = 0, minLen = Integer.MAX_VALUE, start = 0;

        for (int right = 0; right < s.length(); right++) {
            if (count[s.charAt(right)]-- > 0) required--;

            while (required == 0) {
                if (right - left + 1 < minLen) {
                    minLen = right - left + 1;
                    start = left;
                }
                if (++count[s.charAt(left)] > 0) required++;
                left++;
            }
        }
        return minLen == Integer.MAX_VALUE ? "" : s.substring(start, start + minLen);
    }

    // =========================================================================
    // Main
    // =========================================================================
    public static void main(String[] args) {
        System.out.println("=== Minimum Window Substring ===");

        System.out.println("s=\"ADOBECODEBANC\", t=\"ABC\"");
        System.out.println("HashMap: " + minWindowOptimal("ADOBECODEBANC", "ABC")); // "BANC"
        System.out.println("Array:   " + minWindowBest("ADOBECODEBANC", "ABC"));     // "BANC"

        System.out.println("\ns=\"a\", t=\"aa\"");
        System.out.println("Array: " + minWindowBest("a", "aa")); // ""
    }
}
