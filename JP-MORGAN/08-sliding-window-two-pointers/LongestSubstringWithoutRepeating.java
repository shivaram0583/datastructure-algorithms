/**
 * Problem: Longest Substring Without Repeating Characters (LeetCode #3)
 * Difficulty: Medium
 * Topics: String, Sliding Window, Hash Map
 * Frequently asked at: JP Morgan Chase (Round 1)
 *
 * Description:
 *   Given a string s, find the length of the longest substring without
 *   repeating characters.
 *
 * Example 1: s="abcabcbb" → 3  ("abc")
 * Example 2: s="bbbbb"   → 1  ("b")
 * Example 3: s="pwwkew"  → 3  ("wke")
 *
 * Constraints:
 *   0 <= s.length <= 5 * 10^4
 *   s consists of English letters, digits, symbols and spaces.
 *
 * JP Morgan Context:
 *   Sliding window is the key pattern. Follow-up:
 *   "What if we allow at most 2 repeating chars?" → LeetCode #159
 *
 * ============================================================
 * Approach 1 — Brute Force
 * ============================================================
 *   For every pair (i,j), check if substring s[i..j] has all unique chars.
 *   TC: O(n³)  SC: O(min(n, charset))
 *
 * ============================================================
 * Approach 2 — Sliding Window + HashSet
 * ============================================================
 *   Two pointers left and right. Expand right; if char already in set,
 *   shrink from left until the char is removed.
 *
 *   Step-by-step ("abcabcbb"):
 *     l=0, r=0: add 'a', set={a}, max=1
 *     l=0, r=1: add 'b', set={a,b}, max=2
 *     l=0, r=2: add 'c', set={a,b,c}, max=3
 *     l=0, r=3: 'a' in set → remove 'a' (l=1), add 'a', set={b,c,a}, max=3
 *     l=1, r=4: 'b' in set → remove 'b' (l=2), add 'b', set={c,a,b}, max=3
 *     l=2, r=5: 'c' in set → remove 'c' (l=3), add 'c', set={a,b,c}, max=3
 *     l=3, r=6: 'b' in set → remove 'a'(l=4), 'b'(l=5), add 'b', set={c,b}, max=3
 *     l=5, r=7: 'b' in set → remove 'b'(l=6), add 'b', set={b}, max=3
 *   Result: 3
 *
 *   TC: O(n)  SC: O(min(n, charset))
 *
 * ============================================================
 * Approach 3 — Sliding Window + HashMap (Optimal Jump)
 * ============================================================
 *   Store char → last seen index in a map.
 *   When duplicate found, jump left pointer directly past the duplicate.
 *
 *   Step-by-step ("abcabcbb"):
 *     l=0
 *     r=0: 'a' → map={a:0}, max=1
 *     r=1: 'b' → map={a:0,b:1}, max=2
 *     r=2: 'c' → map={a:0,b:1,c:2}, max=3
 *     r=3: 'a' seen at 0 ≥ l=0 → l=1, map={a:3,b:1,c:2}, max=3
 *     r=4: 'b' seen at 1 ≥ l=1 → l=2, map={a:3,b:4,c:2}, max=3
 *     r=5: 'c' seen at 2 ≥ l=2 → l=3, map={a:3,b:4,c:5}, max=3
 *     r=6: 'b' seen at 4 ≥ l=3 → l=5, max=3
 *     r=7: 'b' seen at 6 ≥ l=5 → l=7, max=3
 *   Result: 3
 *
 *   TC: O(n)  SC: O(min(n, charset))
 */

import java.util.*;

public class LongestSubstringWithoutRepeating {

    // =========================================================================
    // Approach 1: Brute Force — O(n³) time, O(charset) space
    // =========================================================================
    public static int lengthOfLongestSubstringBrute(String s) {
        int max = 0;
        for (int i = 0; i < s.length(); i++) {
            for (int j = i + 1; j <= s.length(); j++) {
                if (allUnique(s, i, j)) max = Math.max(max, j - i);
            }
        }
        return max;
    }

    private static boolean allUnique(String s, int start, int end) {
        Set<Character> seen = new HashSet<>();
        for (int i = start; i < end; i++) {
            if (!seen.add(s.charAt(i))) return false;
        }
        return true;
    }

    // =========================================================================
    // Approach 2: Sliding Window + HashSet — O(n) time, O(charset) space
    // =========================================================================
    public static int lengthOfLongestSubstringSet(String s) {
        Set<Character> set = new HashSet<>();
        int max = 0, left = 0;
        for (int right = 0; right < s.length(); right++) {
            while (set.contains(s.charAt(right))) {
                set.remove(s.charAt(left++));
            }
            set.add(s.charAt(right));
            max = Math.max(max, right - left + 1);
        }
        return max;
    }

    // =========================================================================
    // Approach 3: Sliding Window + HashMap (Optimal Jump) — O(n) time
    // =========================================================================
    public static int lengthOfLongestSubstringMap(String s) {
        Map<Character, Integer> map = new HashMap<>();
        int max = 0, left = 0;
        for (int right = 0; right < s.length(); right++) {
            char c = s.charAt(right);
            if (map.containsKey(c) && map.get(c) >= left) {
                left = map.get(c) + 1;
            }
            map.put(c, right);
            max = Math.max(max, right - left + 1);
        }
        return max;
    }

    // =========================================================================
    // Main — Test Cases
    // =========================================================================
    public static void main(String[] args) {
        System.out.println("=== Longest Substring Without Repeating Characters ===\n");

        String[] tests = {"abcabcbb", "bbbbb", "pwwkew", "", " ", "dvdf"};
        int[]    expected = {3, 1, 3, 0, 1, 3};

        for (int i = 0; i < tests.length; i++) {
            System.out.printf("s=\"%s\"  Expected:%d  Set:%d  Map:%d%n",
                tests[i], expected[i],
                lengthOfLongestSubstringSet(tests[i]),
                lengthOfLongestSubstringMap(tests[i]));
        }
    }
}
