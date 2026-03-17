/**
 * Problem: N-Queens (LeetCode #51)
 * Difficulty: Hard
 * Topics: Array, Backtracking
 * Frequently asked at: Microsoft
 *
 * Description:
 *   Place n queens on an n×n chessboard such that no two queens
 *   attack each other. Return all distinct solutions.
 *
 * Approach 1 – Brute Force
 *   Generate all permutations, check validity by diagonals.
 *   TC: O(n! * n) | SC: O(n²)
 *
 * Approach 2 – Backtracking with Sets (Optimal/Best)
 *   Place row by row; use sets for cols, diag, anti-diag.
 *   TC: O(n!) | SC: O(n²)
 *
 * Approach 3 – Backtracking with Bit Masking
 *   Use bitmasks instead of sets for O(1) checks.
 *   TC: O(n!) | SC: O(n)
 */

import java.util.*;

public class NQueens {

    // =========================================================================
    // Approach 1: Brute (Permutation check) — O(n! * n)
    // =========================================================================
    public static List<List<String>> solveNQueensBrute(int n) {
        List<List<String>> result = new ArrayList<>();
        int[] queens = new int[n]; // queens[row] = col
        solvePermutation(queens, 0, n, result);
        return result;
    }

    private static void solvePermutation(int[] queens, int row, int n, List<List<String>> result) {
        if (row == n) {
            if (isValid(queens, n)) result.add(buildBoard(queens, n));
            return;
        }
        for (int col = 0; col < n; col++) {
            queens[row] = col;
            solvePermutation(queens, row + 1, n, result);
        }
    }

    private static boolean isValid(int[] queens, int n) {
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (queens[i] == queens[j]) return false;
                if (Math.abs(queens[i] - queens[j]) == j - i) return false;
            }
        }
        return true;
    }

    // =========================================================================
    // Approach 2: Backtracking with Sets — O(n!)
    // =========================================================================
    public static List<List<String>> solveNQueensOptimal(int n) {
        List<List<String>> result = new ArrayList<>();
        Set<Integer> cols = new HashSet<>(), diags = new HashSet<>(), antiDiags = new HashSet<>();
        int[] queens = new int[n];
        backtrack(0, n, queens, cols, diags, antiDiags, result);
        return result;
    }

    private static void backtrack(int row, int n, int[] queens,
            Set<Integer> cols, Set<Integer> diags, Set<Integer> antiDiags,
            List<List<String>> result) {
        if (row == n) { result.add(buildBoard(queens, n)); return; }
        for (int col = 0; col < n; col++) {
            int d = row - col, ad = row + col;
            if (cols.contains(col) || diags.contains(d) || antiDiags.contains(ad)) continue;
            queens[row] = col;
            cols.add(col); diags.add(d); antiDiags.add(ad);
            backtrack(row + 1, n, queens, cols, diags, antiDiags, result);
            cols.remove(col); diags.remove(d); antiDiags.remove(ad);
        }
    }

    // =========================================================================
    // Approach 3: Bitmask Backtracking — O(n!)
    // =========================================================================
    static List<List<String>> resultBit;

    public static List<List<String>> solveNQueensBest(int n) {
        resultBit = new ArrayList<>();
        int[] queens = new int[n];
        solveBit(0, n, 0, 0, 0, queens);
        return resultBit;
    }

    private static void solveBit(int row, int n, int cols, int diags, int antiDiags, int[] queens) {
        if (row == n) { resultBit.add(buildBoard(queens, n)); return; }
        int available = ((1 << n) - 1) & ~(cols | diags | antiDiags);
        while (available != 0) {
            int bit = available & (-available); // lowest set bit
            int col = Integer.bitCount(bit - 1);
            queens[row] = col;
            solveBit(row + 1, n, cols | bit, (diags | bit) << 1, (antiDiags | bit) >> 1, queens);
            available &= available - 1;
        }
    }

    private static List<String> buildBoard(int[] queens, int n) {
        List<String> board = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            char[] row = new char[n];
            Arrays.fill(row, '.');
            row[queens[i]] = 'Q';
            board.add(new String(row));
        }
        return board;
    }

    // =========================================================================
    // Main
    // =========================================================================
    public static void main(String[] args) {
        System.out.println("=== N-Queens ===");
        int n = 4;

        List<List<String>> r1 = solveNQueensOptimal(n);
        System.out.println("n=4, solutions found: " + r1.size()); // 2
        for (List<String> board : r1) {
            board.forEach(System.out::println);
            System.out.println();
        }

        List<List<String>> r2 = solveNQueensBest(n);
        System.out.println("Bitmask solutions: " + r2.size()); // 2
    }
}
