/**
 * Problem: LRU Cache (LeetCode #146)
 * Difficulty: Medium
 * Topics: Hash Table, Linked List, Design
 * Frequently asked at: Microsoft
 *
 * Description:
 *   Design a data structure that follows the constraints of a
 *   Least Recently Used (LRU) cache.
 *   - LRUCache(int capacity)
 *   - int get(int key)
 *   - void put(int key, int value)
 *   Both operations must run in O(1) average time.
 *
 * Approach 1 – Brute Force (ArrayList)
 *   Use an ArrayList to maintain order; get/put scan list.
 *   TC: O(n) per operation | SC: O(capacity)
 *
 * Approach 2 – LinkedHashMap (Optimal, leverages JDK)
 *   Use Java's LinkedHashMap with access-order.
 *   TC: O(1) | SC: O(capacity)
 *
 * Approach 3 – HashMap + Doubly Linked List (Best / Interview answer)
 *   Manual doubly-linked list + HashMap.
 *   TC: O(1) | SC: O(capacity)
 */

import java.util.*;

public class LRUCache {

    // =========================================================================
    // Approach 1: Brute Force (ArrayList) — O(n) per op, O(capacity) space
    // =========================================================================
    static class LRUBrute {
        int capacity;
        List<int[]> list; // [key, value]

        LRUBrute(int capacity) {
            this.capacity = capacity;
            this.list = new ArrayList<>();
        }

        int get(int key) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i)[0] == key) {
                    int[] entry = list.remove(i);
                    list.add(entry); // move to end (most recent)
                    return entry[1];
                }
            }
            return -1;
        }

        void put(int key, int value) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i)[0] == key) {
                    list.remove(i);
                    break;
                }
            }
            if (list.size() == capacity) {
                list.remove(0); // evict LRU
            }
            list.add(new int[]{key, value});
        }
    }

    // =========================================================================
    // Approach 2: LinkedHashMap — O(1) per op, O(capacity) space
    // =========================================================================
    static class LRULinkedHashMap extends LinkedHashMap<Integer, Integer> {
        int capacity;

        LRULinkedHashMap(int capacity) {
            super(capacity, 0.75f, true); // access-order = true
            this.capacity = capacity;
        }

        public int get(int key) {
            return super.getOrDefault(key, -1);
        }

        public void put(int key, int value) {
            super.put(key, value);
        }

        @Override
        protected boolean removeEldestEntry(Map.Entry<Integer, Integer> eldest) {
            return size() > capacity;
        }
    }

    // =========================================================================
    // Approach 3: HashMap + Doubly Linked List (Best) — O(1) per op
    // =========================================================================
    static class LRUBest {
        class DLLNode {
            int key, val;
            DLLNode prev, next;
            DLLNode(int k, int v) { key = k; val = v; }
        }

        int capacity;
        Map<Integer, DLLNode> map;
        DLLNode head, tail; // sentinel nodes

        LRUBest(int capacity) {
            this.capacity = capacity;
            map = new HashMap<>();
            head = new DLLNode(0, 0);
            tail = new DLLNode(0, 0);
            head.next = tail;
            tail.prev = head;
        }

        int get(int key) {
            if (!map.containsKey(key)) return -1;
            DLLNode node = map.get(key);
            moveToHead(node);
            return node.val;
        }

        void put(int key, int value) {
            if (map.containsKey(key)) {
                DLLNode node = map.get(key);
                node.val = value;
                moveToHead(node);
            } else {
                DLLNode node = new DLLNode(key, value);
                map.put(key, node);
                addToHead(node);
                if (map.size() > capacity) {
                    DLLNode lru = tail.prev;
                    removeNode(lru);
                    map.remove(lru.key);
                }
            }
        }

        private void addToHead(DLLNode node) {
            node.next = head.next;
            node.prev = head;
            head.next.prev = node;
            head.next = node;
        }

        private void removeNode(DLLNode node) {
            node.prev.next = node.next;
            node.next.prev = node.prev;
        }

        private void moveToHead(DLLNode node) {
            removeNode(node);
            addToHead(node);
        }
    }

    // =========================================================================
    // Main — Test Cases
    // =========================================================================
    public static void main(String[] args) {
        System.out.println("=== LRU Cache ===");

        // Test Approach 3 (Best)
        LRUBest cache = new LRUBest(2);
        cache.put(1, 1);
        cache.put(2, 2);
        System.out.println("get(1) = " + cache.get(1));  // 1
        cache.put(3, 3);  // evicts key 2
        System.out.println("get(2) = " + cache.get(2));  // -1
        cache.put(4, 4);  // evicts key 1
        System.out.println("get(1) = " + cache.get(1));  // -1
        System.out.println("get(3) = " + cache.get(3));  // 3
        System.out.println("get(4) = " + cache.get(4));  // 4

        System.out.println("\n--- LinkedHashMap approach ---");
        LRULinkedHashMap cache2 = new LRULinkedHashMap(2);
        cache2.put(1, 1);
        cache2.put(2, 2);
        System.out.println("get(1) = " + cache2.get(1));  // 1
        cache2.put(3, 3);
        System.out.println("get(2) = " + cache2.get(2));  // -1
    }
}
