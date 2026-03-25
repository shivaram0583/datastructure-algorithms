/**
 * Problem: Letter Combinations of a Phone Number (LeetCode #17)
 * Difficulty: Medium
 * Topics: Hash Table, String, Backtracking
 * Frequently asked at: Microsoft
 *
 * Description:
 *   Given a string containing digits from 2-9 inclusive, return all possible
 *   letter combinations that the number could represent.
 *
 * Approach 1 – Iterative (BFS-style)
 *   Build combinations level by level. TC: O(4^n * n) | SC: O(4^n * n)
 *
 * Approach 2 – Backtracking (Optimal/Best)
 *   DFS with backtracking. TC: O(4^n * n) | SC: O(n) call stack
 *
 * Approach 3 – Queue-based BFS
 *   Use a queue to build combinations. TC: O(4^n * n) | SC: O(4^n * n)
 */

import java.util.*;

public class LetterCombinations {

    static final String[] MAPPING = {"", "", "abc", "def", "ghi", "jkl", "mno", "pqrs", "tuv", "wxyz"};

    // =========================================================================
    // Approach 1: Iterative — O(4^n * n)
    // =========================================================================
    public static List<String> combineIterative(String digits) {
        if (digits.isEmpty()) return new ArrayList<>();
        List<String> result = new ArrayList<>();
        result.add("");

        for (char digit : digits.toCharArray()) {
            List<String> next = new ArrayList<>();
            String letters = MAPPING[digit - '0'];
            for (String prev : result) {
                for (char c : letters.toCharArray()) {
                    next.add(prev + c);
                }
            }
            result = next;
        }
        return result;
    }

    // =========================================================================
    // Approach 2: Backtracking (Optimal/Best) — O(4^n * n)
    // =========================================================================
    public static List<String> combineBest(String digits) {
        List<String> result = new ArrayList<>();
        if (digits.isEmpty()) return result;
        backtrack(digits, 0, new StringBuilder(), result);
        return result;
    }

    private static void backtrack(String digits, int idx, StringBuilder sb, List<String> result) {
        if (idx == digits.length()) { result.add(sb.toString()); return; }
        for (char c : MAPPING[digits.charAt(idx) - '0'].toCharArray()) {
            sb.append(c);
            backtrack(digits, idx + 1, sb, result);
            sb.deleteCharAt(sb.length() - 1);
        }
    }

    // =========================================================================
    // Approach 3: Queue-based BFS — O(4^n * n)
    // =========================================================================
    public static List<String> combineQueue(String digits) {
        LinkedList<String> queue = new LinkedList<>();
        if (digits.isEmpty()) return queue;
        queue.offer("");

        for (int i = 0; i < digits.length(); i++) {
            String letters = MAPPING[digits.charAt(i) - '0'];
            while (queue.peek().length() == i) {
                String curr = queue.poll();
                for (char c : letters.toCharArray()) {
                    queue.offer(curr + c);
                }
            }
        }
        return queue;
    }

    // =========================================================================
    // Main
    // =========================================================================
    public static void main(String[] args) {
        System.out.println("=== Letter Combinations ===");

        System.out.println("\"23\":");
        System.out.println("Iterative:  " + combineIterative("23"));
        System.out.println("Backtrack:  " + combineBest("23"));
        System.out.println("Queue:      " + combineQueue("23"));
        // [ad, ae, af, bd, be, bf, cd, ce, cf]
    }
}
