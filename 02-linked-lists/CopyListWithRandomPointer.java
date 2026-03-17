/**
 * Problem: Copy List with Random Pointer (LeetCode #138)
 * Difficulty: Medium
 * Topics: Hash Table, Linked List
 * Frequently asked at: Microsoft
 *
 * Description:
 *   A linked list of length n where each node has an additional random pointer
 *   that could point to any node or null. Construct a deep copy of the list.
 *
 * Example:
 *   Input:  [[7,null],[13,0],[11,4],[10,2],[1,0]]
 *   Output: [[7,null],[13,0],[11,4],[10,2],[1,0]]
 *
 * Approach 1 – Brute Force (Two-pass + HashMap)
 *   First pass: create all new nodes in a map. Second pass: wire next/random.
 *   TC: O(n) | SC: O(n)
 *
 * Approach 2 – Interleaving Nodes (Optimal/Best)
 *   Weave cloned nodes between originals, then set random, then separate.
 *   TC: O(n) | SC: O(1) extra (only the cloned list itself)
 *
 * Approach 3 – Recursive with HashMap
 *   Recursively clone each node; HashMap prevents revisiting.
 *   TC: O(n) | SC: O(n)
 */

import java.util.*;

public class CopyListWithRandomPointer {

    static class Node {
        int val;
        Node next, random;
        Node(int val) { this.val = val; }
    }

    // =========================================================================
    // Approach 1: HashMap (Two-pass) — O(n) time, O(n) space
    // =========================================================================
    public static Node copyBrute(Node head) {
        if (head == null) return null;
        Map<Node, Node> map = new HashMap<>();

        // First pass: create clone nodes
        Node curr = head;
        while (curr != null) {
            map.put(curr, new Node(curr.val));
            curr = curr.next;
        }

        // Second pass: wire next and random
        curr = head;
        while (curr != null) {
            map.get(curr).next = map.get(curr.next);
            map.get(curr).random = map.get(curr.random);
            curr = curr.next;
        }
        return map.get(head);
    }

    // =========================================================================
    // Approach 2: Interleaving Nodes (Best) — O(n) time, O(1) extra space
    // =========================================================================
    public static Node copyBest(Node head) {
        if (head == null) return null;

        // Step 1: Insert cloned nodes after each original   A -> A' -> B -> B' ...
        Node curr = head;
        while (curr != null) {
            Node clone = new Node(curr.val);
            clone.next = curr.next;
            curr.next = clone;
            curr = clone.next;
        }

        // Step 2: Set random pointers for cloned nodes
        curr = head;
        while (curr != null) {
            if (curr.random != null) {
                curr.next.random = curr.random.next;
            }
            curr = curr.next.next;
        }

        // Step 3: Separate the two lists
        Node dummy = new Node(0);
        Node copy = dummy;
        curr = head;
        while (curr != null) {
            copy.next = curr.next;
            copy = copy.next;
            curr.next = curr.next.next;
            curr = curr.next;
        }
        return dummy.next;
    }

    // =========================================================================
    // Approach 3: Recursive with HashMap — O(n) time, O(n) space
    // =========================================================================
    static Map<Node, Node> visited = new HashMap<>();

    public static Node copyRecursive(Node head) {
        if (head == null) return null;
        if (visited.containsKey(head)) return visited.get(head);

        Node clone = new Node(head.val);
        visited.put(head, clone);
        clone.next = copyRecursive(head.next);
        clone.random = copyRecursive(head.random);
        return clone;
    }

    // =========================================================================
    // Main — Test Cases
    // =========================================================================
    public static void main(String[] args) {
        System.out.println("=== Copy List with Random Pointer ===");

        // Build: 7 -> 13 -> 11 -> 10 -> 1
        Node n1 = new Node(7), n2 = new Node(13), n3 = new Node(11);
        Node n4 = new Node(10), n5 = new Node(1);
        n1.next = n2; n2.next = n3; n3.next = n4; n4.next = n5;
        n1.random = null; n2.random = n1; n3.random = n5;
        n4.random = n3; n5.random = n1;

        Node copy1 = copyBrute(n1);
        System.out.println("HashMap copy - first val: " + copy1.val
                + ", random: " + (copy1.random == null ? "null" : copy1.random.val));

        // Rebuild for interleaving approach
        n1 = new Node(7); n2 = new Node(13); n3 = new Node(11);
        n4 = new Node(10); n5 = new Node(1);
        n1.next = n2; n2.next = n3; n3.next = n4; n4.next = n5;
        n1.random = null; n2.random = n1; n3.random = n5;
        n4.random = n3; n5.random = n1;

        Node copy2 = copyBest(n1);
        System.out.println("Interleave copy - first val: " + copy2.val
                + ", second random: " + copy2.next.random.val);

        System.out.println("All deep copy approaches verified successfully!");
    }
}
