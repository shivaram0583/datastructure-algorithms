/**
 * Problem: Implement Trie (Prefix Tree) (LeetCode #208)
 * Difficulty: Medium
 * Topics: Hash Table, String, Design, Trie
 * Frequently asked at: Microsoft
 *
 * Description:
 *   Implement a trie with insert, search, and startsWith methods.
 *
 * Approach 1 – HashSet/HashMap (Brute)
 *   Store words in set; startsWith via filter. TC: insert O(1), search O(1),
 *   startsWith O(n*m) where n=words, m=avg length
 *
 * Approach 2 – Array-based Trie (Optimal/Best)
 *   Each node has children[26]. TC: O(m) per operation | SC: O(ALPHABET * m * n)
 *
 * Approach 3 – HashMap-based Trie
 *   Each node uses a HashMap for children (memory efficient for sparse tries).
 *   TC: O(m) per operation | SC: O(m * n) amortized
 */

import java.util.*;

public class ImplementTrie {

    // =========================================================================
    // Approach 1: HashSet — O(1) insert/search, O(n*m) startsWith
    // =========================================================================
    static class TrieBrute {
        Set<String> words = new HashSet<>();

        void insert(String word) { words.add(word); }
        boolean search(String word) { return words.contains(word); }
        boolean startsWith(String prefix) {
            for (String w : words) if (w.startsWith(prefix)) return true;
            return false;
        }
    }

    // =========================================================================
    // Approach 2: Array-based Trie (Best) — O(m) per operation
    // =========================================================================
    static class TrieBest {
        private TrieBest[] children = new TrieBest[26];
        private boolean isEnd = false;

        void insert(String word) {
            TrieBest node = this;
            for (char c : word.toCharArray()) {
                int idx = c - 'a';
                if (node.children[idx] == null) node.children[idx] = new TrieBest();
                node = node.children[idx];
            }
            node.isEnd = true;
        }

        boolean search(String word) {
            TrieBest node = findNode(word);
            return node != null && node.isEnd;
        }

        boolean startsWith(String prefix) {
            return findNode(prefix) != null;
        }

        private TrieBest findNode(String s) {
            TrieBest node = this;
            for (char c : s.toCharArray()) {
                int idx = c - 'a';
                if (node.children[idx] == null) return null;
                node = node.children[idx];
            }
            return node;
        }
    }

    // =========================================================================
    // Approach 3: HashMap-based Trie — O(m) per operation
    // =========================================================================
    static class TrieMap {
        Map<Character, TrieMap> children = new HashMap<>();
        boolean isEnd = false;

        void insert(String word) {
            TrieMap node = this;
            for (char c : word.toCharArray()) {
                node.children.putIfAbsent(c, new TrieMap());
                node = node.children.get(c);
            }
            node.isEnd = true;
        }

        boolean search(String word) {
            TrieMap node = findNode(word);
            return node != null && node.isEnd;
        }

        boolean startsWith(String prefix) {
            return findNode(prefix) != null;
        }

        private TrieMap findNode(String s) {
            TrieMap node = this;
            for (char c : s.toCharArray()) {
                if (!node.children.containsKey(c)) return null;
                node = node.children.get(c);
            }
            return node;
        }
    }

    // =========================================================================
    // Main
    // =========================================================================
    public static void main(String[] args) {
        System.out.println("=== Implement Trie ===");

        TrieBest trie = new TrieBest();
        trie.insert("apple");
        System.out.println("search(apple):   " + trie.search("apple"));      // true
        System.out.println("search(app):     " + trie.search("app"));         // false
        System.out.println("startsWith(app): " + trie.startsWith("app"));     // true
        trie.insert("app");
        System.out.println("search(app):     " + trie.search("app"));         // true
    }
}
