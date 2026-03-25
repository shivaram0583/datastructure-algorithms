/**
 * Problem: Word Search II (LeetCode #212)
 * Difficulty: Hard
 * Topics: Array, String, Backtracking, Trie, Matrix
 * Frequently asked at: Microsoft
 *
 * Description:
 *   Given an m x n board and a list of words, find all words that can be
 *   formed by sequentially adjacent cells. Each cell can be used once per word.
 *
 * Approach 1 – Brute Force (Word Search I for each word)
 *   Run backtracking per word. TC: O(W * m*n * 4^L) | SC: O(L)
 *
 * Approach 2 – Trie + DFS (Optimal/Best)
 *   Build trie from words; single DFS over board.
 *   TC: O(m*n * 4^L) | SC: O(sum of word lengths)
 *
 * Approach 3 – Trie + DFS with pruning (Best)
 *   Same as above but remove found words from trie to prune search.
 *   TC: O(m*n * 4^L) amortized better | SC: O(sum of word lengths)
 */

import java.util.*;

public class WordSearchII {

    // =========================================================================
    // Approach 1: Brute Force — per-word backtracking
    // =========================================================================
    public static List<String> findWordsBrute(char[][] board, String[] words) {
        List<String> result = new ArrayList<>();
        for (String word : words) {
            if (exists(board, word)) result.add(word);
        }
        return result;
    }

    private static boolean exists(char[][] board, String word) {
        for (int i = 0; i < board.length; i++)
            for (int j = 0; j < board[0].length; j++)
                if (dfs(board, word, 0, i, j)) return true;
        return false;
    }

    private static boolean dfs(char[][] board, String word, int idx, int r, int c) {
        if (idx == word.length()) return true;
        if (r < 0 || r >= board.length || c < 0 || c >= board[0].length) return false;
        if (board[r][c] != word.charAt(idx)) return false;
        board[r][c] ^= 256;
        boolean found = dfs(board, word, idx + 1, r + 1, c) ||
                         dfs(board, word, idx + 1, r - 1, c) ||
                         dfs(board, word, idx + 1, r, c + 1) ||
                         dfs(board, word, idx + 1, r, c - 1);
        board[r][c] ^= 256;
        return found;
    }

    // =========================================================================
    // Approach 2 & 3: Trie + DFS with Pruning (Best)
    // =========================================================================
    static class TrieNode {
        TrieNode[] children = new TrieNode[26];
        String word = null; // stores the word at leaf
    }

    public static List<String> findWordsBest(char[][] board, String[] words) {
        // Build trie
        TrieNode root = new TrieNode();
        for (String word : words) {
            TrieNode node = root;
            for (char c : word.toCharArray()) {
                int idx = c - 'a';
                if (node.children[idx] == null) node.children[idx] = new TrieNode();
                node = node.children[idx];
            }
            node.word = word;
        }

        List<String> result = new ArrayList<>();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                trieDFS(board, i, j, root, result);
            }
        }
        return result;
    }

    private static void trieDFS(char[][] board, int r, int c, TrieNode node, List<String> result) {
        if (r < 0 || r >= board.length || c < 0 || c >= board[0].length) return;
        char ch = board[r][c];
        if (ch == '#' || node.children[ch - 'a'] == null) return;

        node = node.children[ch - 'a'];
        if (node.word != null) {
            result.add(node.word);
            node.word = null; // avoid duplicates (also acts as pruning)
        }

        board[r][c] = '#'; // mark visited
        trieDFS(board, r + 1, c, node, result);
        trieDFS(board, r - 1, c, node, result);
        trieDFS(board, r, c + 1, node, result);
        trieDFS(board, r, c - 1, node, result);
        board[r][c] = ch;  // restore
    }

    // =========================================================================
    // Main
    // =========================================================================
    public static void main(String[] args) {
        System.out.println("=== Word Search II ===");

        char[][] board = {
            {'o','a','a','n'},
            {'e','t','a','e'},
            {'i','h','k','r'},
            {'i','f','l','v'}
        };
        String[] words = {"oath", "pea", "eat", "rain"};

        System.out.println("Trie+DFS: " + findWordsBest(board, words));
        // [oath, eat]
    }
}
