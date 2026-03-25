/**
 * Problem: Reverse Linked List (LeetCode #206)
 * Difficulty: Medium
 * Topics: Linked List, Recursion
 * Frequently asked at: Microsoft
 *
 * Description:
 *   Given the head of a singly linked list, reverse the list, and return
 *   the reversed list.
 *
 * Example:
 *   Input:  1 -> 2 -> 3 -> 4 -> 5
 *   Output: 5 -> 4 -> 3 -> 2 -> 1
 *
 * Approach 1 – Brute Force (Stack)
 *   Push all values onto a stack, then rebuild. TC: O(n) | SC: O(n)
 *
 * Approach 2 – Recursive
 *   Reverse the rest, then fix pointers. TC: O(n) | SC: O(n) call stack
 *
 * Approach 3 – Iterative (Best)
 *   Three-pointer in-place reversal. TC: O(n) | SC: O(1)
 */

import java.util.*;

public class ReverseLinkedList {

    static class ListNode {
        int val;
        ListNode next;
        ListNode(int val) { this.val = val; }
    }

    // =========================================================================
    // Approach 1: Brute Force (Stack) — O(n) time, O(n) space
    // =========================================================================
    public static ListNode reverseBrute(ListNode head) {
        Stack<Integer> stack = new Stack<>();
        ListNode curr = head;
        while (curr != null) {
            stack.push(curr.val);
            curr = curr.next;
        }
        curr = head;
        while (curr != null) {
            curr.val = stack.pop();
            curr = curr.next;
        }
        return head;
    }

    // =========================================================================
    // Approach 2: Recursive — O(n) time, O(n) space (call stack)
    // =========================================================================
    public static ListNode reverseRecursive(ListNode head) {
        if (head == null || head.next == null) return head;
        ListNode newHead = reverseRecursive(head.next);
        head.next.next = head;
        head.next = null;
        return newHead;
    }

    // =========================================================================
    // Approach 3: Iterative (Best) — O(n) time, O(1) space
    // =========================================================================
    public static ListNode reverseBest(ListNode head) {
        ListNode prev = null, curr = head;
        while (curr != null) {
            ListNode next = curr.next;
            curr.next = prev;
            prev = curr;
            curr = next;
        }
        return prev;
    }

    // =========================================================================
    // Helpers & Main
    // =========================================================================
    private static ListNode build(int... vals) {
        ListNode dummy = new ListNode(0);
        ListNode curr = dummy;
        for (int v : vals) { curr.next = new ListNode(v); curr = curr.next; }
        return dummy.next;
    }

    private static String toString(ListNode head) {
        StringBuilder sb = new StringBuilder();
        while (head != null) {
            sb.append(head.val);
            if (head.next != null) sb.append(" -> ");
            head = head.next;
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println("=== Reverse Linked List ===");

        System.out.println("Brute:     " + toString(reverseBrute(build(1, 2, 3, 4, 5))));
        System.out.println("Recursive: " + toString(reverseRecursive(build(1, 2, 3, 4, 5))));
        System.out.println("Best:      " + toString(reverseBest(build(1, 2, 3, 4, 5))));
    }
}
