/**
 * Problem: Word Search (LeetCode #79)
 * Difficulty: Medium
 * Topics: Array, Backtracking, Matrix
 * Frequently asked at: Microsoft
 *
 * Description:
 *   Given an m x n grid of characters and a string word, return true if word
 *   exists in the grid. Word can be constructed from sequentially adjacent cells.
 *
 * Approach 1 – Brute Force DFS (standard backtracking)
 *   Try every cell as start, DFS with visited marking.
 *   TC: O(m * n * 4^L) | SC: O(L)
 *
 * Approach 2 – Optimized with pruning
 *   Count character frequencies; start from rarer end; early termination.
 *   TC: O(m * n * 4^L) but much faster in practice | SC: O(L)
 *
 * Approach 3 – In-place marking (Best space)
 *   Use board itself to mark visited (xor/flip character).
 *   TC: O(m * n * 4^L) | SC: O(L) stack only
 */

public class WordSearch {

    // =========================================================================
    // Approach 1: Standard DFS Backtracking — O(m*n*4^L)
    // =========================================================================
    public static boolean existBrute(char[][] board, String word) {
        boolean[][] visited = new boolean[board.length][board[0].length];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (dfsBrute(board, word, 0, i, j, visited)) return true;
            }
        }
        return false;
    }

    private static boolean dfsBrute(char[][] board, String word, int idx,
                                     int r, int c, boolean[][] visited) {
        if (idx == word.length()) return true;
        if (r < 0 || r >= board.length || c < 0 || c >= board[0].length) return false;
        if (visited[r][c] || board[r][c] != word.charAt(idx)) return false;

        visited[r][c] = true;
        boolean found = dfsBrute(board, word, idx+1, r+1, c, visited)
                      || dfsBrute(board, word, idx+1, r-1, c, visited)
                      || dfsBrute(board, word, idx+1, r, c+1, visited)
                      || dfsBrute(board, word, idx+1, r, c-1, visited);
        visited[r][c] = false;
        return found;
    }

    // =========================================================================
    // Approach 2: Optimized with Frequency Pruning
    // =========================================================================
    public static boolean existOptimal(char[][] board, String word) {
        // Count frequencies for pruning
        int[] boardFreq = new int[128], wordFreq = new int[128];
        for (char[] row : board) for (char c : row) boardFreq[c]++;
        for (char c : word.toCharArray()) wordFreq[c]++;
        for (char c : word.toCharArray()) {
            if (boardFreq[c] < wordFreq[c]) return false;
        }

        // If last char is rarer, reverse the word for efficiency
        String w = word;
        if (boardFreq[word.charAt(0)] > boardFreq[word.charAt(word.length()-1)]) {
            w = new StringBuilder(word).reverse().toString();
        }

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (dfsInPlace(board, w, 0, i, j)) return true;
            }
        }
        return false;
    }

    // =========================================================================
    // Approach 3: In-place Marking (Best) — O(L) stack space
    // =========================================================================
    public static boolean existBest(char[][] board, String word) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (dfsInPlace(board, word, 0, i, j)) return true;
            }
        }
        return false;
    }

    private static boolean dfsInPlace(char[][] board, String word, int idx, int r, int c) {
        if (idx == word.length()) return true;
        if (r < 0 || r >= board.length || c < 0 || c >= board[0].length) return false;
        if (board[r][c] != word.charAt(idx)) return false;

        board[r][c] ^= 256; // mark visited by flipping
        boolean found = dfsInPlace(board, word, idx+1, r+1, c)
                      || dfsInPlace(board, word, idx+1, r-1, c)
                      || dfsInPlace(board, word, idx+1, r, c+1)
                      || dfsInPlace(board, word, idx+1, r, c-1);
        board[r][c] ^= 256; // unmark
        return found;
    }

    // =========================================================================
    // Main
    // =========================================================================
    public static void main(String[] args) {
        System.out.println("=== Word Search ===");
        char[][] board = {
            {'A','B','C','E'},
            {'S','F','C','S'},
            {'A','D','E','E'}
        };

        System.out.println("\"ABCCED\": " + existBest(board, "ABCCED")); // true
        System.out.println("\"SEE\":    " + existBest(board, "SEE"));     // true
        System.out.println("\"ABCB\":   " + existBest(board, "ABCB"));   // false
    }
}
