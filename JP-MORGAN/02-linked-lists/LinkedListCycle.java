/**
 * Problem: Linked List Cycle Detection (LeetCode #141 & #142)
 * Difficulty: Easy / Medium
 * Topics: Linked List, Two Pointers, Floyd's Algorithm
 * Frequently asked at: JP Morgan Chase (Round 1)
 *
 * Description:
 *   Part 1 (#141): Given head of a linked list, determine if it has a cycle.
 *   Part 2 (#142): If it has a cycle, return the node where the cycle begins.
 *
 * Example:
 *   Input:  3 -> 2 -> 0 -> -4 -> (back to node with val=2)
 *   Output (Part 1): true
 *   Output (Part 2): node with val=2
 *
 * JP Morgan Context:
 *   Interviewers often ask both parts together. Key follow-up:
 *   "Explain mathematically why fast and slow pointers meet at cycle entry."
 *
 * ============================================================
 * Approach 1 — HashSet (Detect Cycle)
 * ============================================================
 *   Store visited nodes. If we see a node twice, there's a cycle.
 *   TC: O(n)  SC: O(n)
 *
 * ============================================================
 * Approach 2 — Floyd's Tortoise and Hare (Optimal)
 * ============================================================
 *   slow moves 1 step, fast moves 2 steps.
 *   If they meet → cycle exists.
 *
 *   Proof: If cycle length = C and cycle starts at distance F from head,
 *   when slow enters cycle, fast is F steps ahead inside cycle.
 *   They meet after C - F steps (since fast gains 1 step per iteration).
 *
 *   To find cycle entry (#142):
 *   After meeting, reset one pointer to head.
 *   Move both pointers 1 step at a time.
 *   They meet exactly at the cycle entry node.
 *
 *   Mathematical proof:
 *   Let distance from head to cycle entry = F
 *   Let distance from cycle entry to meeting point = a
 *   Let remaining cycle = b  (so cycle length C = a + b)
 *   Slow traveled: F + a
 *   Fast traveled: F + a + n*C (n full cycles extra)
 *   Fast = 2 * Slow: F + a + nC = 2(F + a) → nC = F + a → F = nC - a
 *   So after resetting one pointer to head and advancing both 1 step,
 *   after F steps: head pointer is at cycle entry,
 *   meeting-pointer has gone F = nC - a steps from meeting point,
 *   which is exactly the cycle entry.
 *
 *   TC: O(n)  SC: O(1)
 */

import java.util.HashSet;

public class LinkedListCycle {

    static class ListNode {
        int val;
        ListNode next;
        ListNode(int val) { this.val = val; }
    }

    // =========================================================================
    // Approach 1: HashSet — O(n) time, O(n) space
    // =========================================================================
    public static boolean hasCycleHashSet(ListNode head) {
        HashSet<ListNode> visited = new HashSet<>();
        ListNode curr = head;
        while (curr != null) {
            if (visited.contains(curr)) return true;
            visited.add(curr);
            curr = curr.next;
        }
        return false;
    }

    // =========================================================================
    // Approach 2: Floyd's Algorithm — Detect Cycle — O(n) time, O(1) space
    // =========================================================================
    public static boolean hasCycleFloyd(ListNode head) {
        ListNode slow = head, fast = head;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
            if (slow == fast) return true;
        }
        return false;
    }

    // =========================================================================
    // Approach 3: Floyd's Algorithm — Find Cycle Entry — O(n) time, O(1) space
    // =========================================================================
    public static ListNode detectCycleEntry(ListNode head) {
        ListNode slow = head, fast = head;

        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
            if (slow == fast) {
                slow = head;
                while (slow != fast) {
                    slow = slow.next;
                    fast = fast.next;
                }
                return slow;
            }
        }
        return null;
    }

    // =========================================================================
    // Main — Test Cases
    // =========================================================================
    public static void main(String[] args) {
        System.out.println("=== Linked List Cycle ===\n");

        ListNode n1 = new ListNode(3);
        ListNode n2 = new ListNode(2);
        ListNode n3 = new ListNode(0);
        ListNode n4 = new ListNode(-4);
        n1.next = n2; n2.next = n3; n3.next = n4; n4.next = n2;

        System.out.println("Test 1: 3->2->0->-4->(back to 2)");
        System.out.println("  hasCycleHashSet: " + hasCycleHashSet(n1));
        System.out.println("  hasCycleFloyd:   " + hasCycleFloyd(n1));
        ListNode entry = detectCycleEntry(n1);
        System.out.println("  Cycle entry val: " + (entry != null ? entry.val : "null"));

        ListNode m1 = new ListNode(1);
        ListNode m2 = new ListNode(2);
        m1.next = m2; m2.next = m1;
        System.out.println("\nTest 2: 1->2->(back to 1)");
        System.out.println("  hasCycleFloyd:   " + hasCycleFloyd(m1));
        ListNode entry2 = detectCycleEntry(m1);
        System.out.println("  Cycle entry val: " + (entry2 != null ? entry2.val : "null"));

        ListNode p1 = new ListNode(1);
        System.out.println("\nTest 3: [1] no cycle");
        System.out.println("  hasCycleFloyd:   " + hasCycleFloyd(p1));
    }
}
