/**
 * Problem: Evaluate Reverse Polish Notation (LeetCode #150)
 * Difficulty: Medium
 * Topics: Array, Math, Stack
 * Frequently asked at: Microsoft
 *
 * Description:
 *   Evaluate the value of an arithmetic expression in Reverse Polish Notation.
 *   Valid operators are +, -, *, /. Each operand may be an integer or another expression.
 *   Division truncates toward zero.
 *
 * Example:
 *   Input:  tokens = ["2","1","+","3","*"]
 *   Output: 9  → ((2+1)*3)
 *
 * Approach 1 – Brute Force (Recursion from back)
 *   Recursively process from the end of the array.
 *   TC: O(n) | SC: O(n) call stack
 *
 * Approach 2 – Stack (Optimal & Best)
 *   Classic stack evaluation. TC: O(n) | SC: O(n)
 *
 * Approach 3 – Array as Stack (Best, slightly faster)
 *   Use an array index as stack pointer to avoid Stack overhead.
 *   TC: O(n) | SC: O(n)
 */

import java.util.*;

public class EvalRPN {

    // =========================================================================
    // Approach 1: Recursion — O(n) time, O(n) space (call stack)
    // =========================================================================
    static int idx;

    public static int evalBrute(String[] tokens) {
        idx = tokens.length - 1;
        return evalHelper(tokens);
    }

    private static int evalHelper(String[] tokens) {
        String token = tokens[idx--];
        if (!isOperator(token)) return Integer.parseInt(token);
        int right = evalHelper(tokens);
        int left = evalHelper(tokens);
        return applyOp(token, left, right);
    }

    // =========================================================================
    // Approach 2: Stack — O(n) time, O(n) space
    // =========================================================================
    public static int evalOptimal(String[] tokens) {
        Stack<Integer> stack = new Stack<>();
        for (String token : tokens) {
            if (isOperator(token)) {
                int b = stack.pop(), a = stack.pop();
                stack.push(applyOp(token, a, b));
            } else {
                stack.push(Integer.parseInt(token));
            }
        }
        return stack.pop();
    }

    // =========================================================================
    // Approach 3: Array-as-Stack (Best) — O(n) time, O(n) space, faster
    // =========================================================================
    public static int evalBest(String[] tokens) {
        int[] stack = new int[tokens.length];
        int top = -1;
        for (String token : tokens) {
            if (isOperator(token)) {
                int b = stack[top--], a = stack[top--];
                stack[++top] = applyOp(token, a, b);
            } else {
                stack[++top] = Integer.parseInt(token);
            }
        }
        return stack[0];
    }

    // =========================================================================
    // Helpers
    // =========================================================================
    private static boolean isOperator(String s) {
        return s.equals("+") || s.equals("-") || s.equals("*") || s.equals("/");
    }

    private static int applyOp(String op, int a, int b) {
        switch (op) {
            case "+": return a + b;
            case "-": return a - b;
            case "*": return a * b;
            case "/": return a / b;
        }
        throw new IllegalArgumentException("Unknown operator: " + op);
    }

    // =========================================================================
    // Main — Test Cases
    // =========================================================================
    public static void main(String[] args) {
        System.out.println("=== Evaluate Reverse Polish Notation ===");

        String[] t1 = {"2", "1", "+", "3", "*"};
        System.out.println("Input: [\"2\",\"1\",\"+\",\"3\",\"*\"]");
        System.out.println("Recursive: " + evalBrute(t1));    // 9
        System.out.println("Stack:     " + evalOptimal(t1));   // 9
        System.out.println("Array:     " + evalBest(t1));      // 9

        String[] t2 = {"4", "13", "5", "/", "+"};
        System.out.println("\nInput: [\"4\",\"13\",\"5\",\"/\",\"+\"]");
        System.out.println("Recursive: " + evalBrute(t2));    // 6
        System.out.println("Stack:     " + evalOptimal(t2));   // 6
        System.out.println("Array:     " + evalBest(t2));      // 6

        String[] t3 = {"10", "6", "9", "3", "+", "-11", "*", "/", "*", "17", "+", "5", "+"};
        System.out.println("\nComplex expression:");
        System.out.println("Stack: " + evalOptimal(t3));       // 22
        System.out.println("Array: " + evalBest(t3));           // 22
    }
}
