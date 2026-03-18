/**
 * Problem: Min Stack (LeetCode #155)
 * Difficulty: Medium
 * Topics: Stack, Design
 * Frequently asked at: JP Morgan Chase (Round 1)
 *
 * Description:
 *   Design a stack that supports push, pop, top, and retrieving
 *   the minimum element in constant time.
 *
 *   Implement MinStack:
 *     void push(int val)   — push element onto stack
 *     void pop()           — remove top element
 *     int top()            — get top element
 *     int getMin()         — retrieve minimum element in O(1)
 *
 * Example:
 *   MinStack s = new MinStack();
 *   s.push(-2); s.push(0); s.push(-3);
 *   s.getMin(); // -3
 *   s.pop();
 *   s.top();    // 0
 *   s.getMin(); // -2
 *
 * JP Morgan Context:
 *   Models real-time minimum tracking for financial metrics
 *   (e.g., minimum bid price in an order book window).
 *
 * ============================================================
 * Approach 1 — Two Stacks
 * ============================================================
 *   Main stack stores all values.
 *   Min stack stores current minimum at every level.
 *   When pushing, push to min stack if val <= current min.
 *   When popping, if popped val == current min, pop from min stack too.
 *
 *   Step-by-step (push -2, push 0, push -3):
 *     push(-2): main=[-2], minStack=[-2]
 *     push(0):  main=[-2,0], minStack=[-2]  (0 > -2, don't push to min)
 *     push(-3): main=[-2,0,-3], minStack=[-2,-3]  (-3 <= -2, push to min)
 *     getMin(): top of minStack = -3
 *     pop():    pop -3 from main; -3 == top of minStack → pop from minStack too
 *     getMin(): top of minStack = -2
 *
 *   TC: O(1) all operations  SC: O(n)
 *
 * ============================================================
 * Approach 2 — Single Stack with Pairs (value, currentMin)
 * ============================================================
 *   Each stack entry stores (value, minSoFar).
 *   No second stack needed.
 *
 *   TC: O(1) all operations  SC: O(n)
 */

import java.util.Stack;

public class MinStack {

    // =========================================================================
    // Approach 1: Two Stacks
    // =========================================================================
    static class MinStackTwoStacks {
        private Stack<Integer> main = new Stack<>();
        private Stack<Integer> minStack = new Stack<>();

        public void push(int val) {
            main.push(val);
            if (minStack.isEmpty() || val <= minStack.peek()) {
                minStack.push(val);
            }
        }

        public void pop() {
            int val = main.pop();
            if (val == minStack.peek()) {
                minStack.pop();
            }
        }

        public int top() {
            return main.peek();
        }

        public int getMin() {
            return minStack.peek();
        }
    }

    // =========================================================================
    // Approach 2: Single Stack with Pairs
    // =========================================================================
    static class MinStackPairs {
        private Stack<int[]> stack = new Stack<>();

        public void push(int val) {
            int currentMin = stack.isEmpty() ? val : Math.min(val, stack.peek()[1]);
            stack.push(new int[]{val, currentMin});
        }

        public void pop() {
            stack.pop();
        }

        public int top() {
            return stack.peek()[0];
        }

        public int getMin() {
            return stack.peek()[1];
        }
    }

    // =========================================================================
    // Main — Test Cases
    // =========================================================================
    public static void main(String[] args) {
        System.out.println("=== Min Stack ===\n");

        System.out.println("--- Two Stacks approach ---");
        MinStackTwoStacks s1 = new MinStackTwoStacks();
        s1.push(-2); s1.push(0); s1.push(-3);
        System.out.println("After push(-2),push(0),push(-3):");
        System.out.println("  getMin(): " + s1.getMin()); // -3
        s1.pop();
        System.out.println("After pop():");
        System.out.println("  top():    " + s1.top());    // 0
        System.out.println("  getMin(): " + s1.getMin()); // -2

        System.out.println("\n--- Pairs approach ---");
        MinStackPairs s2 = new MinStackPairs();
        s2.push(5); s2.push(3); s2.push(7); s2.push(2); s2.push(4);
        System.out.println("After push(5,3,7,2,4):");
        System.out.println("  getMin(): " + s2.getMin()); // 2
        s2.pop(); s2.pop();
        System.out.println("After pop(), pop():");
        System.out.println("  getMin(): " + s2.getMin()); // 3
    }
}
