/**
 * Problem: Merge K Sorted Lists (LeetCode #23)
 * Difficulty: Hard
 * Topics: Linked List, Divide and Conquer, Heap
 * Frequently asked at: Microsoft
 *
 * Description:
 *   You are given an array of k linked-lists, each sorted in ascending order.
 *   Merge all the linked-lists into one sorted linked-list and return it.
 *
 * Example:
 *   Input:  [[1,4,5],[1,3,4],[2,6]]
 *   Output: [1,1,2,3,4,4,5,6]
 *
 * Approach 1 – Brute Force
 *   Collect all values, sort, rebuild list. TC: O(N log N) | SC: O(N)
 *
 * Approach 2 – Min Heap (Optimal)
 *   Use a priority queue of size k. TC: O(N log k) | SC: O(k)
 *
 * Approach 3 – Divide and Conquer (Best)
 *   Merge pairs in log k rounds. TC: O(N log k) | SC: O(1) extra
 */

import java.util.*;

public class MergeKSortedLists {

    static class ListNode {
        int val;
        ListNode next;
        ListNode(int val) { this.val = val; }
    }

    // =========================================================================
    // Approach 1: Brute Force — O(N log N) time, O(N) space
    // =========================================================================
    public static ListNode mergeBrute(ListNode[] lists) {
        List<Integer> vals = new ArrayList<>();
        for (ListNode node : lists) {
            while (node != null) {
                vals.add(node.val);
                node = node.next;
            }
        }
        Collections.sort(vals);

        ListNode dummy = new ListNode(0);
        ListNode curr = dummy;
        for (int v : vals) {
            curr.next = new ListNode(v);
            curr = curr.next;
        }
        return dummy.next;
    }

    // =========================================================================
    // Approach 2: Min Heap — O(N log k) time, O(k) space
    // =========================================================================
    public static ListNode mergeOptimal(ListNode[] lists) {
        PriorityQueue<ListNode> pq = new PriorityQueue<>((a, b) -> a.val - b.val);
        for (ListNode node : lists) {
            if (node != null) pq.offer(node);
        }

        ListNode dummy = new ListNode(0);
        ListNode curr = dummy;
        while (!pq.isEmpty()) {
            ListNode min = pq.poll();
            curr.next = min;
            curr = curr.next;
            if (min.next != null) pq.offer(min.next);
        }
        return dummy.next;
    }

    // =========================================================================
    // Approach 3: Divide and Conquer (Best) — O(N log k) time, O(1) extra
    // =========================================================================
    public static ListNode mergeBest(ListNode[] lists) {
        if (lists == null || lists.length == 0) return null;
        int interval = 1;
        while (interval < lists.length) {
            for (int i = 0; i + interval < lists.length; i += interval * 2) {
                lists[i] = mergeTwoLists(lists[i], lists[i + interval]);
            }
            interval *= 2;
        }
        return lists[0];
    }

    private static ListNode mergeTwoLists(ListNode l1, ListNode l2) {
        ListNode dummy = new ListNode(0);
        ListNode curr = dummy;
        while (l1 != null && l2 != null) {
            if (l1.val <= l2.val) { curr.next = l1; l1 = l1.next; }
            else                  { curr.next = l2; l2 = l2.next; }
            curr = curr.next;
        }
        curr.next = (l1 != null) ? l1 : l2;
        return dummy.next;
    }

    // =========================================================================
    // Helpers & Main
    // =========================================================================
    private static ListNode build(int... vals) {
        ListNode dummy = new ListNode(0); ListNode c = dummy;
        for (int v : vals) { c.next = new ListNode(v); c = c.next; }
        return dummy.next;
    }

    private static String toString(ListNode head) {
        StringBuilder sb = new StringBuilder("[");
        while (head != null) {
            sb.append(head.val);
            if (head.next != null) sb.append(",");
            head = head.next;
        }
        return sb.append("]").toString();
    }

    public static void main(String[] args) {
        System.out.println("=== Merge K Sorted Lists ===");

        ListNode[] lists = {build(1, 4, 5), build(1, 3, 4), build(2, 6)};
        System.out.println("Brute:   " + toString(mergeBrute(
                new ListNode[]{build(1,4,5), build(1,3,4), build(2,6)})));
        System.out.println("Optimal: " + toString(mergeOptimal(
                new ListNode[]{build(1,4,5), build(1,3,4), build(2,6)})));
        System.out.println("Best:    " + toString(mergeBest(
                new ListNode[]{build(1,4,5), build(1,3,4), build(2,6)})));
    }
}
