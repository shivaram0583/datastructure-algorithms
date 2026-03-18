/**
 * Problem: Merge K Sorted Lists (LeetCode #23)
 * Difficulty: Hard
 * Topics: Linked List, Heap, Divide & Conquer
 * Frequently asked at: JP Morgan Chase (Round 1)
 *
 * Description:
 *   You are given an array of k linked-lists, each sorted in ascending order.
 *   Merge all lists into one sorted linked list and return it.
 *
 * Example:
 *   lists = [1->4->5, 1->3->4, 2->6]
 *   Output: 1->1->2->3->4->4->5->6
 *
 * Constraints:
 *   0 <= k <= 10^4
 *   0 <= lists[i].length <= 500
 *   -10^4 <= lists[i][j] <= 10^4
 *
 * JP Morgan Context:
 *   Merging sorted data streams (e.g., price feeds from k exchanges,
 *   sorted transaction logs from k regional databases).
 *
 * ============================================================
 * Approach 1 — Brute Force (Collect + Sort)
 * ============================================================
 *   Collect all node values, sort them, rebuild linked list.
 *   TC: O(N log N) where N = total nodes  SC: O(N)
 *
 * ============================================================
 * Approach 2 — Min-Heap (Priority Queue)
 * ============================================================
 *   Add head of each list to a min-heap (keyed by node value).
 *   Poll min node, add to result, push its next node to heap.
 *
 *   Step-by-step ([1->4->5, 1->3->4, 2->6]):
 *     heap=[1(L0), 1(L1), 2(L2)]
 *     Poll 1(L0) → result=1, push 4(L0) → heap=[1(L1),2(L2),4(L0)]
 *     Poll 1(L1) → result=1->1, push 3(L1) → heap=[2,3,4]
 *     Poll 2(L2) → result=1->1->2, push 6(L2) → heap=[3,4,6]
 *     Poll 3, push 4: heap=[4,4,6] → result=1->1->2->3
 *     Poll 4(L0), push 5: heap=[4,5,6] → result=...->4
 *     Poll 4(L1), no next: heap=[5,6] → result=...->4
 *     Poll 5(L0), no next: heap=[6] → result=...->5
 *     Poll 6(L2), no next: heap=[] → result=...->6
 *   Final: 1->1->2->3->4->4->5->6
 *
 *   TC: O(N log k)  SC: O(k)
 *
 * ============================================================
 * Approach 3 — Divide & Conquer (Optimal)
 * ============================================================
 *   Pair up and merge adjacent lists. Repeat until 1 list remains.
 *   Round 1: merge (L0,L1), (L2,L3), ... → k/2 lists
 *   Round 2: merge (M0,M1), ... → k/4 lists
 *   ...
 *   Total passes: log k. Each pass processes N nodes.
 *   TC: O(N log k)  SC: O(1) (merge is in-place)
 */

import java.util.PriorityQueue;

public class MergeKSortedLists {

    static class ListNode {
        int val;
        ListNode next;
        ListNode(int val) { this.val = val; }
    }

    // =========================================================================
    // Approach 2: Min-Heap — O(N log k) time, O(k) space
    // =========================================================================
    public static ListNode mergeKListsHeap(ListNode[] lists) {
        PriorityQueue<ListNode> heap = new PriorityQueue<>((a, b) -> a.val - b.val);
        for (ListNode head : lists) {
            if (head != null) heap.offer(head);
        }

        ListNode dummy = new ListNode(0);
        ListNode tail = dummy;
        while (!heap.isEmpty()) {
            ListNode node = heap.poll();
            tail.next = node;
            tail = tail.next;
            if (node.next != null) heap.offer(node.next);
        }
        return dummy.next;
    }

    // =========================================================================
    // Approach 3: Divide & Conquer — O(N log k) time, O(1) space
    // =========================================================================
    public static ListNode mergeKListsDivide(ListNode[] lists) {
        if (lists == null || lists.length == 0) return null;
        int n = lists.length;
        int step = 1;
        while (step < n) {
            for (int i = 0; i + step < n; i += step * 2) {
                lists[i] = mergeTwoLists(lists[i], lists[i + step]);
            }
            step *= 2;
        }
        return lists[0];
    }

    private static ListNode mergeTwoLists(ListNode l1, ListNode l2) {
        ListNode dummy = new ListNode(0);
        ListNode tail = dummy;
        while (l1 != null && l2 != null) {
            if (l1.val <= l2.val) { tail.next = l1; l1 = l1.next; }
            else                  { tail.next = l2; l2 = l2.next; }
            tail = tail.next;
        }
        tail.next = (l1 != null) ? l1 : l2;
        return dummy.next;
    }

    // =========================================================================
    // Helpers
    // =========================================================================
    public static ListNode build(int[] arr) {
        ListNode dummy = new ListNode(0); ListNode curr = dummy;
        for (int v : arr) { curr.next = new ListNode(v); curr = curr.next; }
        return dummy.next;
    }

    public static String print(ListNode head) {
        StringBuilder sb = new StringBuilder();
        while (head != null) {
            sb.append(head.val);
            if (head.next != null) sb.append("->");
            head = head.next;
        }
        return sb.toString();
    }

    // =========================================================================
    // Main — Test Cases
    // =========================================================================
    public static void main(String[] args) {
        System.out.println("=== Merge K Sorted Lists ===\n");

        ListNode[] lists1 = {build(new int[]{1,4,5}), build(new int[]{1,3,4}), build(new int[]{2,6})};
        ListNode[] lists2 = {build(new int[]{1,4,5}), build(new int[]{1,3,4}), build(new int[]{2,6})};
        System.out.println("Test 1: [1->4->5, 1->3->4, 2->6]  Expected: 1->1->2->3->4->4->5->6");
        System.out.println("  Heap:   " + print(mergeKListsHeap(lists1)));
        System.out.println("  Divide: " + print(mergeKListsDivide(lists2)));

        System.out.println("\nTest 2: []  Expected: (empty)");
        System.out.println("  Heap:   " + print(mergeKListsHeap(new ListNode[]{})));

        ListNode[] lists3 = {build(new int[]{1,2,3}), build(new int[]{4,5,6})};
        System.out.println("\nTest 3: [1->2->3, 4->5->6]  Expected: 1->2->3->4->5->6");
        System.out.println("  Divide: " + print(mergeKListsDivide(lists3)));
    }
}
