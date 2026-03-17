/**
 * Problem: Word Break (LeetCode #139)
 * Difficulty: Medium
 * Topics: Hash Table, String, DP, Trie
 * Frequently asked at: Microsoft
 *
 * Description:
 *   Given a string s and a dictionary of strings wordDict, return true if s
 *   can be segmented into a space-separated sequence of dictionary words.
 *
 * Approach 1 – Recursion (Brute Force)
 *   Try every prefix; recurse on suffix. TC: O(2^n) | SC: O(n)
 *
 * Approach 2 – Memoization (Optimal)
 *   Recursion + Boolean memo. TC: O(n² * m) | SC: O(n)
 *
 * Approach 3 – Bottom-Up DP (Best)
 *   dp[i] = true if s[0..i-1] is breakable. TC: O(n² * m) | SC: O(n)
 */

import java.util.*;

public class WordBreak {

    // =========================================================================
    // Approach 1: Recursion — O(2^n)
    // =========================================================================
    public static boolean wordBreakBrute(String s, List<String> wordDict) {
        Set<String> set = new HashSet<>(wordDict);
        return breakRec(s, 0, set);
    }

    private static boolean breakRec(String s, int start, Set<String> set) {
        if (start == s.length()) return true;
        for (int end = start + 1; end <= s.length(); end++) {
            if (set.contains(s.substring(start, end)) && breakRec(s, end, set)) {
                return true;
            }
        }
        return false;
    }

    // =========================================================================
    // Approach 2: Memoization — O(n² * m)
    // =========================================================================
    public static boolean wordBreakOptimal(String s, List<String> wordDict) {
        Set<String> set = new HashSet<>(wordDict);
        Boolean[] memo = new Boolean[s.length()];
        return breakMemo(s, 0, set, memo);
    }

    private static boolean breakMemo(String s, int start, Set<String> set, Boolean[] memo) {
        if (start == s.length()) return true;
        if (memo[start] != null) return memo[start];
        for (int end = start + 1; end <= s.length(); end++) {
            if (set.contains(s.substring(start, end)) && breakMemo(s, end, set, memo)) {
                return memo[start] = true;
            }
        }
        return memo[start] = false;
    }

    // =========================================================================
    // Approach 3: Bottom-Up DP (Best) — O(n² * m) time, O(n) space
    // =========================================================================
    public static boolean wordBreakBest(String s, List<String> wordDict) {
        Set<String> set = new HashSet<>(wordDict);
        boolean[] dp = new boolean[s.length() + 1];
        dp[0] = true;

        for (int i = 1; i <= s.length(); i++) {
            for (int j = 0; j < i; j++) {
                if (dp[j] && set.contains(s.substring(j, i))) {
                    dp[i] = true;
                    break;
                }
            }
        }
        return dp[s.length()];
    }

    // =========================================================================
    // Main
    // =========================================================================
    public static void main(String[] args) {
        System.out.println("=== Word Break ===");

        List<String> dict = Arrays.asList("leet", "code");
        System.out.println("s=\"leetcode\", dict=[leet,code]");
        System.out.println("Brute: " + wordBreakBrute("leetcode", dict));    // true
        System.out.println("Memo:  " + wordBreakOptimal("leetcode", dict));  // true
        System.out.println("DP:    " + wordBreakBest("leetcode", dict));      // true

        List<String> dict2 = Arrays.asList("cats", "dog", "sand", "and", "cat");
        System.out.println("\ns=\"catsandog\", dict=[cats,dog,sand,and,cat]");
        System.out.println("DP: " + wordBreakBest("catsandog", dict2));       // false
    }
}
