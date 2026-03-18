/**
 * Problem: Group Anagrams (LeetCode #49)
 * Difficulty: Medium
 * Topics: Array, Hash Table, String, Sorting
 * Frequently asked at: JP Morgan Chase (Round 1)
 *
 * Description:
 *   Given an array of strings strs, group the anagrams together.
 *   You can return the answer in any order.
 *
 * Example:
 *   Input:  strs = ["eat","tea","tan","ate","nat","bat"]
 *   Output: [["bat"],["nat","tan"],["ate","eat","tea"]]
 *
 * Constraints:
 *   1 <= strs.length <= 10^4
 *   0 <= strs[i].length <= 100
 *   strs[i] consists of lowercase English letters.
 *
 * JP Morgan Context:
 *   Tests HashMap key design — a fundamental skill for building
 *   efficient lookup tables in financial data processing.
 *
 * ============================================================
 * Approach 1 — Sort Each String as Map Key
 * ============================================================
 *   Anagrams have the same sorted form. Use sorted string as key.
 *
 *   Step-by-step:
 *     "eat" → sorted="aet" → map{"aet": ["eat"]}
 *     "tea" → sorted="aet" → map{"aet": ["eat","tea"]}
 *     "tan" → sorted="ant" → map{"ant": ["tan"]}
 *     "ate" → sorted="aet" → map{"aet": ["eat","tea","ate"]}
 *     etc.
 *
 *   TC: O(n * k log k) where n=strings, k=max string length
 *   SC: O(n * k)
 *
 * ============================================================
 * Approach 2 — Character Count Array as Key (Optimal)
 * ============================================================
 *   Use a 26-length frequency array as the key (serialize to string).
 *   Avoids the O(k log k) sorting cost.
 *
 *   "eat" → count=[1,0,0,0,1,0,...,1,...] → key="#1#0#0#...#1#..."
 *
 *   TC: O(n * k)  SC: O(n * k)
 */

import java.util.*;

public class GroupAnagrams {

    // =========================================================================
    // Approach 1: Sort as Key — O(n*k*log k) time, O(n*k) space
    // =========================================================================
    public static List<List<String>> groupAnagramsSort(String[] strs) {
        Map<String, List<String>> map = new HashMap<>();
        for (String s : strs) {
            char[] chars = s.toCharArray();
            Arrays.sort(chars);
            String key = new String(chars);
            map.computeIfAbsent(key, k -> new ArrayList<>()).add(s);
        }
        return new ArrayList<>(map.values());
    }

    // =========================================================================
    // Approach 2: Count Array as Key — O(n*k) time, O(n*k) space
    // =========================================================================
    public static List<List<String>> groupAnagramsCount(String[] strs) {
        Map<String, List<String>> map = new HashMap<>();
        for (String s : strs) {
            int[] count = new int[26];
            for (char c : s.toCharArray()) count[c - 'a']++;
            StringBuilder sb = new StringBuilder();
            for (int c : count) sb.append('#').append(c);
            String key = sb.toString();
            map.computeIfAbsent(key, k -> new ArrayList<>()).add(s);
        }
        return new ArrayList<>(map.values());
    }

    // =========================================================================
    // Main — Test Cases
    // =========================================================================
    public static void main(String[] args) {
        System.out.println("=== Group Anagrams ===\n");

        String[] strs1 = {"eat", "tea", "tan", "ate", "nat", "bat"};
        System.out.println("Test 1: [eat,tea,tan,ate,nat,bat]");
        System.out.println("  Sort:  " + groupAnagramsSort(strs1));
        System.out.println("  Count: " + groupAnagramsCount(strs1));

        String[] strs2 = {""};
        System.out.println("\nTest 2: [\"\"]  Expected: [[\"\"]]");
        System.out.println("  Sort:  " + groupAnagramsSort(strs2));

        String[] strs3 = {"a"};
        System.out.println("\nTest 3: [\"a\"]  Expected: [[\"a\"]]");
        System.out.println("  Sort:  " + groupAnagramsSort(strs3));
    }
}
