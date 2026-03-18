/**
 * Problem: Reverse a Linked List (LeetCode #206)
 * Difficulty: Easy
 * Topics: Linked List, Recursion
 * Frequently asked at: JP Morgan Chase (Round 1)
 *
 * Description:
 *   Given the head of a singly linked list, reverse the list and return
 *   the reversed list's head.
 *
 * Example:
 *   Input:  1 -> 2 -> 3 -> 4 -> 5
 *   Output: 5 -> 4 -> 3 -> 2 -> 1
 *
 * Constraints:
 *   0 <= number of nodes <= 5000
 *   -5000 <= Node.val <= 5000
 *
 * JP Morgan Context:
 *   Foundational pointer manipulation. Follow-ups:
 *   - "Reverse in groups of k" → LeetCode #25
 *   - "Reverse only between positions m and n" → LeetCode #92
 *
 * ============================================================
 * Approach 1 — Iterative (Three Pointer)
 * ============================================================
 *   Maintain prev, current, next pointers.
 *   At each step: save next, point current.next to prev, advance.
 *
 *   Step-by-step (1->2->3->4->5):
 *     prev=null, curr=1
 *     iter 1: next=2, 1->null, prev=1, curr=2
 *     iter 2: next=3, 2->1,   prev=2, curr=3
 *     iter 3: next=4, 3->2,   prev=3, curr=4
 *     iter 4: next=5, 4->3,   prev=4, curr=5
 *     iter 5: next=null, 5->4, prev=5, curr=null
 *   Return prev (5)
 *
 *   TC: O(n)  SC: O(1)
 *
 * ============================================================
 * Approach 2 — Recursive
 * ============================================================
 *   Recurse to the end of the list. On the way back, reverse each link.
 *
 *   Step-by-step (1->2->3):
 *     reverse(1): calls reverse(2)
 *       reverse(2): calls reverse(3)
 *         reverse(3): base case, return 3 (new head)
 *       3.next = 2, 2.next = null → 3->2
 *       return 3
 *     3->2->1, 1.next = null → 3->2->1
 *     return 3
 *
 *   TC: O(n)  SC: O(n) stack frames
 *
 * ============================================================
 * Approach 3 — Stack-based
 * ============================================================
 *   Push all nodes to a stack (LIFO), pop to rebuild.
 *   TC: O(n)  SC: O(n)
 */

import java.util.Stack;

public class ReverseLinkedList {

    static class ListNode {
        int val;
        ListNode next;
        ListNode(int val) { this.val = val; }
    }

    // =========================================================================
    // Approach 1: Iterative — O(n) time, O(1) space
    // =========================================================================
    public static ListNode reverseIterative(ListNode head) {
        ListNode prev = null;
        ListNode curr = head;
        while (curr != null) {
            ListNode next = curr.next;
            curr.next = prev;
            prev = curr;
            curr = next;
        }
        return prev;
    }

    // =========================================================================
    // Approach 2: Recursive — O(n) time, O(n) space
    // =========================================================================
    public static ListNode reverseRecursive(ListNode head) {
        if (head == null || head.next == null) return head;
        ListNode newHead = reverseRecursive(head.next);
        head.next.next = head;
        head.next = null;
        return newHead;
    }

    // =========================================================================
    // Approach 3: Stack-based — O(n) time, O(n) space
    // =========================================================================
    public static ListNode reverseStack(ListNode head) {
        if (head == null) return null;
        Stack<ListNode> stack = new Stack<>();
        ListNode curr = head;
        while (curr != null) {
            stack.push(curr);
            curr = curr.next;
        }
        ListNode dummy = new ListNode(0);
        curr = dummy;
        while (!stack.isEmpty()) {
            curr.next = stack.pop();
            curr = curr.next;
        }
        curr.next = null;
        return dummy.next;
    }

    // =========================================================================
    // Helper: Build list from array
    // =========================================================================
    public static ListNode build(int[] arr) {
        ListNode dummy = new ListNode(0);
        ListNode curr = dummy;
        for (int val : arr) {
            curr.next = new ListNode(val);
            curr = curr.next;
        }
        return dummy.next;
    }

    // =========================================================================
    // Helper: Print list
    // =========================================================================
    public static String print(ListNode head) {
        StringBuilder sb = new StringBuilder();
        while (head != null) {
            sb.append(head.val);
            if (head.next != null) sb.append(" -> ");
            head = head.next;
        }
        return sb.toString();
    }

    // =========================================================================
    // Main — Test Cases
    // =========================================================================
    public static void main(String[] args) {
        System.out.println("=== Reverse Linked List ===\n");

        System.out.println("Test 1: 1->2->3->4->5  Expected: 5->4->3->2->1");
        System.out.println("  Iterative: " + print(reverseIterative(build(new int[]{1,2,3,4,5}))));
        System.out.println("  Recursive: " + print(reverseRecursive(build(new int[]{1,2,3,4,5}))));
        System.out.println("  Stack:     " + print(reverseStack(build(new int[]{1,2,3,4,5}))));

        System.out.println("\nTest 2: 1->2  Expected: 2->1");
        System.out.println("  Iterative: " + print(reverseIterative(build(new int[]{1,2}))));

        System.out.println("\nTest 3: []  Expected: (empty)");
        System.out.println("  Iterative: " + print(reverseIterative(null)));
    }
}
