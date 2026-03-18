/**
 * Problem: LRU Cache (LeetCode #146)
 * Difficulty: Hard
 * Topics: Design, Hash Table, Doubly Linked List
 * Frequently asked at: JP Morgan Chase (Round 1 — flagship design question)
 *
 * Description:
 *   Design a data structure that follows the Least Recently Used (LRU) cache
 *   eviction policy.
 *
 *   Implement the LRUCache class:
 *     LRUCache(int capacity) — initialize with positive capacity
 *     int get(int key)       — return value if key exists, else -1
 *     void put(int key, int value) — insert/update key-value pair;
 *                                    if at capacity, evict LRU key first
 *
 *   Both get and put must run in O(1) average time.
 *
 * Example:
 *   LRUCache cache = new LRUCache(2);
 *   cache.put(1, 1);   // cache: {1=1}
 *   cache.put(2, 2);   // cache: {1=1, 2=2}
 *   cache.get(1);      // returns 1, cache: {2=2, 1=1} (1 is now most recent)
 *   cache.put(3, 3);   // evicts key 2, cache: {1=1, 3=3}
 *   cache.get(2);      // returns -1 (not found)
 *   cache.put(4, 4);   // evicts key 1, cache: {3=3, 4=4}
 *   cache.get(1);      // returns -1
 *   cache.get(3);      // returns 3
 *   cache.get(4);      // returns 4
 *
 * JP Morgan Context:
 *   Directly models session caches and order book caches in trading systems.
 *   Interviewers check:
 *   - Why doubly linked list (not singly)?  → Need O(1) node removal.
 *   - Why sentinel/dummy head and tail?      → Eliminate edge cases.
 *   - Thread safety follow-up               → Use ConcurrentHashMap + locks.
 *
 * ============================================================
 * Approach 1 — LinkedHashMap (Java Built-in, Interview Shortcut)
 * ============================================================
 *   Java's LinkedHashMap with accessOrder=true gives LRU behavior.
 *   Override removeEldestEntry for automatic eviction.
 *
 *   TC: O(1) get/put  SC: O(capacity)
 *   NOTE: Only use this as a shortcut after explaining the real approach.
 *
 * ============================================================
 * Approach 2 — HashMap + Custom Doubly Linked List (Proper Solution)
 * ============================================================
 *   HashMap<key, DLL_Node> for O(1) lookup.
 *   Doubly Linked List ordered by recency:
 *     head.next = Most Recently Used (MRU)
 *     tail.prev = Least Recently Used (LRU) — eviction target
 *
 *   Operations:
 *   get(key):
 *     If key not in map → return -1
 *     Move node to front (most recent) → return node.val
 *
 *   put(key, value):
 *     If key exists → update value, move to front
 *     If key new and at capacity → remove LRU (tail.prev), add new node to front
 *     Else → add new node to front
 *
 *   Step-by-step (capacity=2):
 *     put(1,1): DLL=[1], map={1:node1}
 *     put(2,2): DLL=[2,1], map={1,2}
 *     get(1):   DLL=[1,2], return 1
 *     put(3,3): evict LRU=2, DLL=[3,1], map={1,3}
 *     get(2):   -1 (evicted)
 *
 *   TC: O(1) get/put  SC: O(capacity)
 */

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class LRUCache {

    // =========================================================================
    // Approach 1: LinkedHashMap shortcut
    // =========================================================================
    static class LRUCacheSimple {
        private final int capacity;
        private final LinkedHashMap<Integer, Integer> cache;

        public LRUCacheSimple(int capacity) {
            this.capacity = capacity;
            this.cache = new LinkedHashMap<>(capacity, 0.75f, true) {
                @Override
                protected boolean removeEldestEntry(Map.Entry<Integer, Integer> eldest) {
                    return size() > LRUCacheSimple.this.capacity;
                }
            };
        }

        public int get(int key) {
            return cache.getOrDefault(key, -1);
        }

        public void put(int key, int value) {
            cache.put(key, value);
        }
    }

    // =========================================================================
    // Approach 2: HashMap + Custom Doubly Linked List (Proper)
    // =========================================================================
    static class LRUCacheProper {

        private static class Node {
            int key, val;
            Node prev, next;
            Node(int key, int val) { this.key = key; this.val = val; }
        }

        private final int capacity;
        private final HashMap<Integer, Node> map;
        private final Node head, tail;

        public LRUCacheProper(int capacity) {
            this.capacity = capacity;
            this.map = new HashMap<>();
            this.head = new Node(0, 0);
            this.tail = new Node(0, 0);
            head.next = tail;
            tail.prev = head;
        }

        public int get(int key) {
            if (!map.containsKey(key)) return -1;
            Node node = map.get(key);
            remove(node);
            insertFront(node);
            return node.val;
        }

        public void put(int key, int value) {
            if (map.containsKey(key)) {
                remove(map.get(key));
            }
            if (map.size() == capacity) {
                Node lru = tail.prev;
                remove(lru);
                map.remove(lru.key);
            }
            Node node = new Node(key, value);
            insertFront(node);
            map.put(key, node);
        }

        private void remove(Node node) {
            node.prev.next = node.next;
            node.next.prev = node.prev;
        }

        private void insertFront(Node node) {
            node.next = head.next;
            node.prev = head;
            head.next.prev = node;
            head.next = node;
        }
    }

    // =========================================================================
    // Main — Test Cases
    // =========================================================================
    public static void main(String[] args) {
        System.out.println("=== LRU Cache ===\n");

        System.out.println("--- LinkedHashMap approach ---");
        LRUCacheSimple c1 = new LRUCacheSimple(2);
        c1.put(1, 1);
        c1.put(2, 2);
        System.out.println("get(1): " + c1.get(1));   // 1
        c1.put(3, 3);
        System.out.println("get(2): " + c1.get(2));   // -1 (evicted)
        c1.put(4, 4);
        System.out.println("get(1): " + c1.get(1));   // -1 (evicted)
        System.out.println("get(3): " + c1.get(3));   // 3
        System.out.println("get(4): " + c1.get(4));   // 4

        System.out.println("\n--- Proper DLL approach ---");
        LRUCacheProper c2 = new LRUCacheProper(2);
        c2.put(1, 1);
        c2.put(2, 2);
        System.out.println("get(1): " + c2.get(1));   // 1
        c2.put(3, 3);
        System.out.println("get(2): " + c2.get(2));   // -1 (evicted)
        c2.put(4, 4);
        System.out.println("get(1): " + c2.get(1));   // -1 (evicted)
        System.out.println("get(3): " + c2.get(3));   // 3
        System.out.println("get(4): " + c2.get(4));   // 4
    }
}
