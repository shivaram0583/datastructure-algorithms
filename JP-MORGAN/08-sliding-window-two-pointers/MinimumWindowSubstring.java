/**
 * Problem: Minimum Window Substring (LeetCode #76)
 * Difficulty: Hard
 * Topics: String, Sliding Window, Hash Map
 * Frequently asked at: JP Morgan Chase (Round 1)
 *
 * Description:
 *   Given strings s and t, return the minimum window substring of s that
 *   contains all characters of t. Return "" if no such window exists.
 *
 * Example 1:
 *   s="ADOBECODEBANC", t="ABC" → "BANC"
 * Example 2:
 *   s="a", t="a" → "a"
 * Example 3:
 *   s="a", t="aa" → ""
 *
 * Constraints:
 *   1 <= s.length, t.length <= 10^5
 *
 * JP Morgan Context:
 *   Models finding the shortest sequence of events containing all
 *   required audit checkpoints in a transaction log.
 *
 * ============================================================
 * Approach — Sliding Window + Frequency Maps (Optimal)
 * ============================================================
 *   need    = frequency map of t
 *   window  = frequency map of current window
 *   formed  = count of unique chars in window that match their required count
 *   required = number of unique chars in t
 *
 *   Expand right pointer. When char count in window matches need:
 *     increment formed.
 *   When formed == required (window is valid):
 *     try to shrink left pointer.
 *     Update minimum window if current is smaller.
 *     Shrink left: remove char, update formed if count drops below need.
 *
 *   Step-by-step ("ADOBECODEBANC", t="ABC"):
 *     need={A:1, B:1, C:1}, required=3
 *     Expand until window has A,B,C:
 *       r=0(A): window={A:1}, formed=1
 *       r=1(D): window={A:1,D:1}
 *       r=2(O): window={A:1,D:1,O:1}
 *       r=3(B): window={...,B:1}, formed=2
 *       r=4(E): window={...,E:1}
 *       r=5(C): window={...,C:1}, formed=3 → valid!
 *       minWindow="ADOBEC" (len=6), shrink left:
 *         l=0(A): A:0<1, formed=2 → stop shrinking
 *       Continue expanding...
 *       r=9(B): window={B:2,...} (already have)
 *       r=10(A): A:1, formed=3 → valid!
 *       minWindow="NCODEBA"... → keep shrinking
 *       ...eventually "BANC" is found (len=4) → final answer
 *
 *   TC: O(|s| + |t|)  SC: O(|s| + |t|)
 */

import java.util.HashMap;
import java.util.Map;

public class MinimumWindowSubstring {

    // =========================================================================
    // Sliding Window + Frequency Maps (Optimal) — O(|s|+|t|) time and space
    // =========================================================================
    public static String minWindow(String s, String t) {
        if (s.isEmpty() || t.isEmpty()) return "";

        Map<Character, Integer> need = new HashMap<>();
        for (char c : t.toCharArray()) need.merge(c, 1, Integer::sum);

        Map<Character, Integer> window = new HashMap<>();
        int required = need.size();
        int formed = 0;
        int left = 0;
        int minLen = Integer.MAX_VALUE, minLeft = 0;

        for (int right = 0; right < s.length(); right++) {
            char c = s.charAt(right);
            window.merge(c, 1, Integer::sum);

            if (need.containsKey(c) && window.get(c).equals(need.get(c))) {
                formed++;
            }

            while (formed == required) {
                if (right - left + 1 < minLen) {
                    minLen = right - left + 1;
                    minLeft = left;
                }
                char lc = s.charAt(left);
                window.merge(lc, -1, Integer::sum);
                if (need.containsKey(lc) && window.get(lc) < need.get(lc)) {
                    formed--;
                }
                left++;
            }
        }
        return minLen == Integer.MAX_VALUE ? "" : s.substring(minLeft, minLeft + minLen);
    }

    // =========================================================================
    // Main — Test Cases
    // =========================================================================
    public static void main(String[] args) {
        System.out.println("=== Minimum Window Substring ===\n");

        System.out.println("Test 1: s=\"ADOBECODEBANC\", t=\"ABC\"  Expected: \"BANC\"");
        System.out.println("  Result: " + minWindow("ADOBECODEBANC", "ABC"));

        System.out.println("\nTest 2: s=\"a\", t=\"a\"  Expected: \"a\"");
        System.out.println("  Result: " + minWindow("a", "a"));

        System.out.println("\nTest 3: s=\"a\", t=\"aa\"  Expected: \"\"");
        System.out.println("  Result: \"" + minWindow("a", "aa") + "\"");

        System.out.println("\nTest 4: s=\"aa\", t=\"aa\"  Expected: \"aa\"");
        System.out.println("  Result: " + minWindow("aa", "aa"));
    }
}
