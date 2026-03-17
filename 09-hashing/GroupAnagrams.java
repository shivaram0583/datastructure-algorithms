/**
 * Problem: Group Anagrams (LeetCode #49)
 * Difficulty: Medium
 * Topics: Array, Hash Table, String, Sorting
 * Frequently asked at: Microsoft
 *
 * Description:
 *   Given an array of strings, group the anagrams together.
 *
 * Approach 1 – Brute Force (Compare all pairs)
 *   Compare each pair of strings. TC: O(n² * m log m) | SC: O(n*m)
 *
 * Approach 2 – Sort Key (Optimal)
 *   Sort each word → use as hash key. TC: O(n * m log m) | SC: O(n*m)
 *
 * Approach 3 – Count Key (Best)
 *   Use char frequency as key (avoids sorting each word).
 *   TC: O(n * m) | SC: O(n * m)
 */

import java.util.*;

public class GroupAnagrams {

    // =========================================================================
    // Approach 1: Brute Force — O(n² * m log m)
    // =========================================================================
    public static List<List<String>> groupBrute(String[] strs) {
        boolean[] used = new boolean[strs.length];
        List<List<String>> result = new ArrayList<>();

        for (int i = 0; i < strs.length; i++) {
            if (used[i]) continue;
            List<String> group = new ArrayList<>();
            group.add(strs[i]);
            used[i] = true;
            char[] sorted_i = strs[i].toCharArray();
            Arrays.sort(sorted_i);
            for (int j = i + 1; j < strs.length; j++) {
                if (used[j]) continue;
                char[] sorted_j = strs[j].toCharArray();
                Arrays.sort(sorted_j);
                if (Arrays.equals(sorted_i, sorted_j)) {
                    group.add(strs[j]);
                    used[j] = true;
                }
            }
            result.add(group);
        }
        return result;
    }

    // =========================================================================
    // Approach 2: Sort Key — O(n * m log m)
    // =========================================================================
    public static List<List<String>> groupOptimal(String[] strs) {
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
    // Approach 3: Count Key (Best) — O(n * m)
    // =========================================================================
    public static List<List<String>> groupBest(String[] strs) {
        Map<String, List<String>> map = new HashMap<>();
        for (String s : strs) {
            int[] count = new int[26];
            for (char c : s.toCharArray()) count[c - 'a']++;
            String key = Arrays.toString(count); // e.g. "[1, 0, 0, ...]"
            map.computeIfAbsent(key, k -> new ArrayList<>()).add(s);
        }
        return new ArrayList<>(map.values());
    }

    // =========================================================================
    // Main
    // =========================================================================
    public static void main(String[] args) {
        System.out.println("=== Group Anagrams ===");
        String[] strs = {"eat","tea","tan","ate","nat","bat"};

        System.out.println("Sort Key: " + groupOptimal(strs));
        System.out.println("Count Key:" + groupBest(strs));
        // [[eat, tea, ate], [tan, nat], [bat]]
    }
}
