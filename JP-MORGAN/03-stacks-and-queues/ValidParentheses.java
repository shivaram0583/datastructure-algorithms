/**
 * Problem: Valid Parentheses (LeetCode #20)
 * Difficulty: Easy
 * Topics: Stack, String
 * Frequently asked at: JP Morgan Chase (Round 1)
 *
 * Description:
 *   Given a string s containing '(', ')', '{', '}', '[', ']',
 *   determine if the input string is valid.
 *   Valid means: open brackets are closed by the same type in correct order.
 *
 * Example 1: s = "()"    → true
 * Example 2: s = "()[]{}" → true
 * Example 3: s = "(]"    → false
 * Example 4: s = "([)]"  → false
 * Example 5: s = "{[]}"  → true
 *
 * Constraints:
 *   1 <= s.length <= 10^4
 *   s consists of parentheses only.
 *
 * JP Morgan Context:
 *   Models expression validation in financial formula parsers.
 *   Follow-up: "Validate nested XML/JSON tags" — same stack pattern.
 *
 * ============================================================
 * Approach 1 — Brute Force (Repeated Replacement)
 * ============================================================
 *   Repeatedly replace "()", "[]", "{}" with "" until no change.
 *   If final string is empty → valid.
 *
 *   Step-by-step ("()[]{}")):
 *     Replace "()" → "[]{})"  → "[]{}"  → "{}"  → ""  → true
 *
 *   TC: O(n²)  SC: O(n) (string copies)
 *
 * ============================================================
 * Approach 2 — Stack (Optimal)
 * ============================================================
 *   Push every opening bracket onto the stack.
 *   For every closing bracket, check if it matches the top of the stack.
 *   If mismatch or stack empty → false.
 *   At end, stack must be empty.
 *
 *   Step-by-step ("{[]}"):
 *     '{' → push → stack=[{]
 *     '[' → push → stack=[{, []
 *     ']' → top='[', match! → pop → stack=[{]
 *     '}' → top='{', match! → pop → stack=[]
 *     stack empty → true
 *
 *   TC: O(n)  SC: O(n)
 */

import java.util.Stack;

public class ValidParentheses {

    // =========================================================================
    // Approach 1: Brute Force — O(n²) time, O(n) space
    // =========================================================================
    public static boolean isValidBrute(String s) {
        while (s.contains("()") || s.contains("[]") || s.contains("{}")) {
            s = s.replace("()", "").replace("[]", "").replace("{}", "");
        }
        return s.isEmpty();
    }

    // =========================================================================
    // Approach 2: Stack (Optimal) — O(n) time, O(n) space
    // =========================================================================
    public static boolean isValidStack(String s) {
        Stack<Character> stack = new Stack<>();
        for (char c : s.toCharArray()) {
            if (c == '(' || c == '[' || c == '{') {
                stack.push(c);
            } else {
                if (stack.isEmpty()) return false;
                char top = stack.pop();
                if (c == ')' && top != '(') return false;
                if (c == ']' && top != '[') return false;
                if (c == '}' && top != '{') return false;
            }
        }
        return stack.isEmpty();
    }

    // =========================================================================
    // Main — Test Cases
    // =========================================================================
    public static void main(String[] args) {
        System.out.println("=== Valid Parentheses ===\n");

        String[] tests = {"()", "()[]{}", "(]", "([)]", "{[]}", "", "{"};
        boolean[] expected = {true, true, false, false, true, true, false};

        for (int i = 0; i < tests.length; i++) {
            boolean brute = isValidBrute(tests[i]);
            boolean stack = isValidStack(tests[i]);
            System.out.printf("s=\"%s\"  Expected:%b  Brute:%b  Stack:%b%n",
                tests[i], expected[i], brute, stack);
        }
    }
}
