/**
 * Problem: Merge Two Sorted Lists (LeetCode #21)
 * Difficulty: Easy
 * Topics: Linked List, Recursion
 * Frequently asked at: JP Morgan Chase (Round 1)
 *
 * Description:
 *   You are given the heads of two sorted linked lists list1 and list2.
 *   Merge the two lists into one sorted list. Return the head of the merged list.
 *
 * Example:
 *   Input:  list1 = 1->2->4, list2 = 1->3->4
 *   Output: 1->1->2->3->4->4
 *
 * Constraints:
 *   0 <= number of nodes <= 50
 *   -100 <= Node.val <= 100
 *   Both lists are sorted in non-decreasing order.
 *
 * JP Morgan Context:
 *   Tests clean pointer manipulation. Follow-up: "Merge K sorted lists"
 *   → LeetCode #23 (uses a Min-Heap / divide & conquer).
 *
 * ============================================================
 * Approach 1 — Iterative with Dummy Node
 * ============================================================
 *   Use a dummy node as a sentinel. At each step, compare the heads
 *   of both lists and attach the smaller one to the result.
 *
 *   Step-by-step (1->2->4, 1->3->4):
 *     dummy -> (result tail)
 *     l1=1 == l2=1: pick l1, tail->1, l1=2
 *     l1=2 > l2=1:  pick l2, tail->1, l2=3
 *     l1=2 < l2=3:  pick l1, tail->2, l1=4
 *     l1=4 > l2=3:  pick l2, tail->3, l2=4
 *     l1=4 == l2=4: pick l1, tail->4, l1=null
 *     l1=null, attach rest of l2: tail->4
 *   Result: 1->1->2->3->4->4
 *
 *   TC: O(m+n)  SC: O(1)
 *
 * ============================================================
 * Approach 2 — Recursive
 * ============================================================
 *   Base cases: if either list is null, return the other.
 *   Compare heads: attach the smaller one and recurse on its next.
 *
 *   Step-by-step (1->2, 1->3):
 *     merge(1,1): l1.val==l2.val, pick l1
 *       l1.next = merge(2, 1->3)
 *         merge(2,1): l2.val<l1.val, pick l2
 *           l2.next = merge(2->null, 3)
 *             merge(2,3): l1.val<l2.val, pick l1
 *               l1.next = merge(null, 3) = 3
 *             return 2->3
 *           return 1->2->3
 *         return 1->2->3
 *       return 1->1->2->3
 *
 *   TC: O(m+n)  SC: O(m+n) stack frames
 */

public class MergeTwoSortedLists {

    static class ListNode {
        int val;
        ListNode next;
        ListNode(int val) { this.val = val; }
    }

    // =========================================================================
    // Approach 1: Iterative with Dummy — O(m+n) time, O(1) space
    // =========================================================================
    public static ListNode mergeIterative(ListNode list1, ListNode list2) {
        ListNode dummy = new ListNode(0);
        ListNode tail = dummy;

        while (list1 != null && list2 != null) {
            if (list1.val <= list2.val) {
                tail.next = list1;
                list1 = list1.next;
            } else {
                tail.next = list2;
                list2 = list2.next;
            }
            tail = tail.next;
        }
        tail.next = (list1 != null) ? list1 : list2;
        return dummy.next;
    }

    // =========================================================================
    // Approach 2: Recursive — O(m+n) time, O(m+n) space
    // =========================================================================
    public static ListNode mergeRecursive(ListNode list1, ListNode list2) {
        if (list1 == null) return list2;
        if (list2 == null) return list1;

        if (list1.val <= list2.val) {
            list1.next = mergeRecursive(list1.next, list2);
            return list1;
        } else {
            list2.next = mergeRecursive(list1, list2.next);
            return list2;
        }
    }

    // =========================================================================
    // Helpers
    // =========================================================================
    public static ListNode build(int[] arr) {
        ListNode dummy = new ListNode(0);
        ListNode curr = dummy;
        for (int val : arr) { curr.next = new ListNode(val); curr = curr.next; }
        return dummy.next;
    }

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
        System.out.println("=== Merge Two Sorted Lists ===\n");

        System.out.println("Test 1: l1=1->2->4, l2=1->3->4  Expected: 1->1->2->3->4->4");
        System.out.println("  Iterative: " + print(mergeIterative(build(new int[]{1,2,4}), build(new int[]{1,3,4}))));
        System.out.println("  Recursive: " + print(mergeRecursive(build(new int[]{1,2,4}), build(new int[]{1,3,4}))));

        System.out.println("\nTest 2: l1=[], l2=[]  Expected: (empty)");
        System.out.println("  Iterative: " + print(mergeIterative(null, null)));

        System.out.println("\nTest 3: l1=[], l2=0  Expected: 0");
        System.out.println("  Iterative: " + print(mergeIterative(null, build(new int[]{0}))));
    }
}
