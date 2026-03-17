/**
 * Problem: Min Stack (LeetCode #155)
 * Difficulty: Medium
 * Topics: Stack, Design
 * Frequently asked at: Microsoft
 *
 * Description:
 *   Design a stack that supports push, pop, top, and retrieving the
 *   minimum element in constant time.
 *
 * Approach 1 – Brute Force
 *   Single stack; getMin scans entire stack. TC: O(n) getMin | SC: O(n)
 *
 * Approach 2 – Two Stacks (Optimal)
 *   Auxiliary min-stack tracks current min. TC: O(1) all ops | SC: O(n)
 *
 * Approach 3 – Single Stack with Encoding (Best)
 *   Store encoded values; derive min without extra stack.
 *   TC: O(1) all ops | SC: O(n) but only one stack
 */

import java.util.*;

public class MinStack {

    // =========================================================================
    // Approach 1: Brute Force — O(1) push/pop/top, O(n) getMin
    // =========================================================================
    static class MinStackBrute {
        Stack<Integer> stack = new Stack<>();

        void push(int val) { stack.push(val); }
        void pop() { stack.pop(); }
        int top() { return stack.peek(); }

        int getMin() {
            int min = Integer.MAX_VALUE;
            for (int v : stack) min = Math.min(min, v);
            return min;
        }
    }

    // =========================================================================
    // Approach 2: Two Stacks — O(1) all operations
    // =========================================================================
    static class MinStackOptimal {
        Stack<Integer> stack = new Stack<>();
        Stack<Integer> minStack = new Stack<>();

        void push(int val) {
            stack.push(val);
            int min = minStack.isEmpty() ? val : Math.min(val, minStack.peek());
            minStack.push(min);
        }

        void pop() { stack.pop(); minStack.pop(); }
        int top() { return stack.peek(); }
        int getMin() { return minStack.peek(); }
    }

    // =========================================================================
    // Approach 3: Single Stack with Encoding (Best) — O(1) all ops, 1 stack
    // =========================================================================
    static class MinStackBest {
        Stack<Long> stack = new Stack<>();
        long min;

        void push(int val) {
            if (stack.isEmpty()) {
                stack.push(0L);
                min = val;
            } else {
                // Store difference from current min
                stack.push((long) val - min);
                if (val < min) min = val;
            }
        }

        void pop() {
            long top = stack.pop();
            if (top < 0) {
                // Current min was pushed; restore previous min
                min = min - top;
            }
        }

        int top() {
            long top = stack.peek();
            return (int) (top < 0 ? min : top + min);
        }

        int getMin() { return (int) min; }
    }

    // =========================================================================
    // Main — Test Cases
    // =========================================================================
    public static void main(String[] args) {
        System.out.println("=== Min Stack ===");

        MinStackBest ms = new MinStackBest();
        ms.push(-2);
        ms.push(0);
        ms.push(-3);
        System.out.println("getMin() = " + ms.getMin()); // -3
        ms.pop();
        System.out.println("top()    = " + ms.top());    // 0
        System.out.println("getMin() = " + ms.getMin()); // -2

        System.out.println("\n--- Two Stack approach ---");
        MinStackOptimal ms2 = new MinStackOptimal();
        ms2.push(-2); ms2.push(0); ms2.push(-3);
        System.out.println("getMin() = " + ms2.getMin()); // -3
        ms2.pop();
        System.out.println("top()    = " + ms2.top());    // 0
        System.out.println("getMin() = " + ms2.getMin()); // -2
    }
}
