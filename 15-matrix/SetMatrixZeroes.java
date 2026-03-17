/**
 * Problem: Set Matrix Zeroes (LeetCode #73)
 * Difficulty: Medium
 * Topics: Array, Hash Table, Matrix
 * Frequently asked at: Microsoft
 *
 * Description:
 *   If an element is 0, set its entire row and column to 0. Do it in-place.
 *
 * Approach 1 – Extra sets: TC: O(m*n) | SC: O(m+n)
 * Approach 2 – First row/col as markers (Best): TC: O(m*n) | SC: O(1)
 * Approach 3 – Copy matrix: TC: O(m*n) | SC: O(m*n)
 */

import java.util.*;

public class SetMatrixZeroes {

    // =========================================================================
    // Approach 1: Extra Sets — O(m*n) time, O(m+n) space
    // =========================================================================
    public static void setZeroesBrute(int[][] matrix) {
        Set<Integer> rows = new HashSet<>(), cols = new HashSet<>();
        for (int i = 0; i < matrix.length; i++)
            for (int j = 0; j < matrix[0].length; j++)
                if (matrix[i][j] == 0) { rows.add(i); cols.add(j); }
        for (int i = 0; i < matrix.length; i++)
            for (int j = 0; j < matrix[0].length; j++)
                if (rows.contains(i) || cols.contains(j)) matrix[i][j] = 0;
    }

    // =========================================================================
    // Approach 2: First row/col as markers (Best) — O(m*n), O(1) space
    // =========================================================================
    public static void setZeroesBest(int[][] matrix) {
        int m = matrix.length, n = matrix[0].length;
        boolean firstRowZero = false, firstColZero = false;

        for (int j = 0; j < n; j++) if (matrix[0][j] == 0) firstRowZero = true;
        for (int i = 0; i < m; i++) if (matrix[i][0] == 0) firstColZero = true;

        // Mark zeros in first row/col
        for (int i = 1; i < m; i++)
            for (int j = 1; j < n; j++)
                if (matrix[i][j] == 0) { matrix[i][0] = 0; matrix[0][j] = 0; }

        // Zero cells based on markers
        for (int i = 1; i < m; i++)
            for (int j = 1; j < n; j++)
                if (matrix[i][0] == 0 || matrix[0][j] == 0) matrix[i][j] = 0;

        // Handle first row/col
        if (firstRowZero) for (int j = 0; j < n; j++) matrix[0][j] = 0;
        if (firstColZero) for (int i = 0; i < m; i++) matrix[i][0] = 0;
    }

    public static void main(String[] args) {
        System.out.println("=== Set Matrix Zeroes ===");

        int[][] m1 = {{1,1,1},{1,0,1},{1,1,1}};
        setZeroesBest(m1);
        System.out.println(Arrays.deepToString(m1)); // [[1,0,1],[0,0,0],[1,0,1]]

        int[][] m2 = {{0,1,2,0},{3,4,5,2},{1,3,1,5}};
        setZeroesBest(m2);
        System.out.println(Arrays.deepToString(m2)); // [[0,0,0,0],[0,4,5,0],[0,3,1,0]]
    }
}
