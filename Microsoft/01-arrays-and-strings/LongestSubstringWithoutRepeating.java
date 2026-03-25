/**
 * Problem: Longest Substring Without Repeating Characters (LeetCode #3)
 * Difficulty: Medium
 * Topics: String, Hash Table, Sliding Window
 * Frequently asked at: Microsoft
 *
 * Description:
 *   Given a string s, find the length of the longest substring without
 *   repeating characters.
 *
 * Example:
 *   Input:  s = "abcabcbb"
 *   Output: 3  ("abc")
 *
 * Approach 1 – Brute Force
 *   Check all substrings with a set. TC: O(n³) | SC: O(min(n,m))
 *
 * Approach 2 – Sliding Window + Set (Optimal)
 *   Expand right, shrink left when duplicate found.
 *   TC: O(2n) = O(n) | SC: O(min(n,m))
 *
 * Approach 3 – Sliding Window + HashMap (Best)
 *   Jump left pointer directly past the last occurrence.
 *   TC: O(n) | SC: O(min(n,m))
 */

import java.util.*;

public class LongestSubstringWithoutRepeating {

    // =========================================================================
    // Approach 1: Brute Force — O(n³) time, O(min(n,m)) space
    // =========================================================================
    public static int lengthBrute(String s) {
        int maxLen = 0;
        for (int i = 0; i < s.length(); i++) {
            for (int j = i; j < s.length(); j++) {
                if (allUnique(s, i, j)) {
                    maxLen = Math.max(maxLen, j - i + 1);
                } else {
                    break; // further extending will still have a duplicate
                }
            }
        }
        return maxLen;
    }

    private static boolean allUnique(String s, int start, int end) {
        Set<Character> set = new HashSet<>();
        for (int i = start; i <= end; i++) {
            if (!set.add(s.charAt(i))) return false;
        }
        return true;
    }

    // =========================================================================
    // Approach 2: Sliding Window + HashSet — O(n) time, O(min(n,m)) space
    // =========================================================================
    public static int lengthOptimal(String s) {
        Set<Character> set = new HashSet<>();
        int left = 0, maxLen = 0;

        for (int right = 0; right < s.length(); right++) {
            while (set.contains(s.charAt(right))) {
                set.remove(s.charAt(left));
                left++;
            }
            set.add(s.charAt(right));
            maxLen = Math.max(maxLen, right - left + 1);
        }
        return maxLen;
    }

    // =========================================================================
    // Approach 3: Sliding Window + HashMap (Best) — O(n) time, O(min(n,m)) space
    // =========================================================================
    public static int lengthBest(String s) {
        Map<Character, Integer> map = new HashMap<>();
        int left = 0, maxLen = 0;

        for (int right = 0; right < s.length(); right++) {
            char c = s.charAt(right);
            if (map.containsKey(c)) {
                // Jump left past the previous occurrence
                left = Math.max(left, map.get(c) + 1);
            }
            map.put(c, right);
            maxLen = Math.max(maxLen, right - left + 1);
        }
        return maxLen;
    }

    // =========================================================================
    // Main — Test Cases
    // =========================================================================
    public static void main(String[] args) {
        System.out.println("=== Longest Substring Without Repeating Characters ===");

        String s1 = "abcabcbb";
        System.out.println("Input: \"abcabcbb\"");
        System.out.println("Brute:   " + lengthBrute(s1));    // 3
        System.out.println("Optimal: " + lengthOptimal(s1));  // 3
        System.out.println("Best:    " + lengthBest(s1));      // 3

        String s2 = "bbbbb";
        System.out.println("\nInput: \"bbbbb\"");
        System.out.println("Brute:   " + lengthBrute(s2));    // 1
        System.out.println("Optimal: " + lengthOptimal(s2));  // 1
        System.out.println("Best:    " + lengthBest(s2));      // 1

        String s3 = "pwwkew";
        System.out.println("\nInput: \"pwwkew\"");
        System.out.println("Brute:   " + lengthBrute(s3));    // 3
        System.out.println("Optimal: " + lengthOptimal(s3));  // 3
        System.out.println("Best:    " + lengthBest(s3));      // 3
    }
}
