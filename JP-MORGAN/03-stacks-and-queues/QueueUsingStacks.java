/**
 * Problem: Implement Queue using Stacks (LeetCode #232)
 * Difficulty: Easy
 * Topics: Stack, Queue, Design
 * Frequently asked at: JP Morgan Chase (Round 1)
 *
 * Description:
 *   Implement a FIFO queue using only two stacks.
 *   Implement MyQueue:
 *     void push(int x)  — push element to back of queue
 *     int pop()         — remove element from front of queue
 *     int peek()        — get front element
 *     boolean empty()   — return true if queue is empty
 *
 * Constraints:
 *   1 <= x <= 9
 *   At most 100 calls in total.
 *   All pop/peek calls valid (queue is non-empty).
 *
 * JP Morgan Context:
 *   Tests understanding of amortized complexity and stack/queue tradeoffs.
 *   Follow-up: "What's the amortized time for pop?" → O(1) amortized.
 *
 * ============================================================
 * Approach 1 — Eager Transfer (push is O(n))
 * ============================================================
 *   On each push, move all elements from stack1 to stack2,
 *   push new element to stack2, then move everything back.
 *   pop/peek is O(1) (just pop/peek stack1).
 *
 *   TC: push O(n), pop O(1), peek O(1)  SC: O(n)
 *
 * ============================================================
 * Approach 2 — Lazy Transfer (Optimal, Amortized O(1))
 * ============================================================
 *   inbox (stack1) — receives all pushes.
 *   outbox (stack2) — serves all pops/peeks.
 *   When outbox is empty and pop/peek needed:
 *     Transfer all of inbox to outbox (reverses order = FIFO).
 *
 *   Step-by-step (push 1,2,3 then pop,pop):
 *     push(1): inbox=[1]
 *     push(2): inbox=[1,2]
 *     push(3): inbox=[1,2,3]
 *     pop():  outbox empty → transfer → outbox=[3,2,1] inbox=[]
 *             pop from outbox → 1, outbox=[3,2]
 *     pop():  outbox not empty → pop → 2, outbox=[3]
 *
 *   Each element is moved exactly once (inbox→outbox).
 *   Amortized O(1) per operation.
 *
 *   TC: push O(1), pop O(1) amortized, peek O(1) amortized  SC: O(n)
 */

import java.util.Stack;

public class QueueUsingStacks {

    // =========================================================================
    // Approach 2: Lazy Transfer (Optimal) — Amortized O(1) all operations
    // =========================================================================
    static class MyQueue {
        private Stack<Integer> inbox = new Stack<>();
        private Stack<Integer> outbox = new Stack<>();

        public void push(int x) {
            inbox.push(x);
        }

        public int pop() {
            transfer();
            return outbox.pop();
        }

        public int peek() {
            transfer();
            return outbox.peek();
        }

        public boolean empty() {
            return inbox.isEmpty() && outbox.isEmpty();
        }

        private void transfer() {
            if (outbox.isEmpty()) {
                while (!inbox.isEmpty()) {
                    outbox.push(inbox.pop());
                }
            }
        }
    }

    // =========================================================================
    // Main — Test Cases
    // =========================================================================
    public static void main(String[] args) {
        System.out.println("=== Queue Using Stacks ===\n");

        MyQueue q = new MyQueue();
        q.push(1); q.push(2); q.push(3);
        System.out.println("After push(1), push(2), push(3):");
        System.out.println("  peek():  " + q.peek());  // 1
        System.out.println("  pop():   " + q.pop());   // 1
        System.out.println("  pop():   " + q.pop());   // 2
        q.push(4);
        System.out.println("After push(4):");
        System.out.println("  pop():   " + q.pop());   // 3
        System.out.println("  pop():   " + q.pop());   // 4
        System.out.println("  empty(): " + q.empty()); // true
    }
}
