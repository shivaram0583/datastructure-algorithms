/**
 * Problem: Spiral Matrix (LeetCode #54)
 * Difficulty: Medium
 * Topics: Array, Matrix, Simulation
 * Frequently asked at: Microsoft
 *
 * Description:
 *   Given an m x n matrix, return all elements in spiral order.
 *
 * Approach 1 – Simulation with visited: TC: O(m*n) | SC: O(m*n)
 * Approach 2 – Layer-by-layer (Optimal/Best): TC: O(m*n) | SC: O(1)
 * Approach 3 – Direction array: TC: O(m*n) | SC: O(1)
 */

import java.util.*;

public class SpiralMatrix {

    // =========================================================================
    // Approach 1: Simulation with visited — O(m*n)
    // =========================================================================
    public static List<Integer> spiralBrute(int[][] matrix) {
        List<Integer> result = new ArrayList<>();
        int m = matrix.length, n = matrix[0].length;
        boolean[][] visited = new boolean[m][n];
        int[][] dirs = {{0,1},{1,0},{0,-1},{-1,0}};
        int r = 0, c = 0, d = 0;

        for (int i = 0; i < m * n; i++) {
            result.add(matrix[r][c]);
            visited[r][c] = true;
            int nr = r + dirs[d][0], nc = c + dirs[d][1];
            if (nr < 0 || nr >= m || nc < 0 || nc >= n || visited[nr][nc]) {
                d = (d + 1) % 4;
                nr = r + dirs[d][0];
                nc = c + dirs[d][1];
            }
            r = nr; c = nc;
        }
        return result;
    }

    // =========================================================================
    // Approach 2: Layer by Layer (Best) — O(m*n) time, O(1) extra
    // =========================================================================
    public static List<Integer> spiralBest(int[][] matrix) {
        List<Integer> result = new ArrayList<>();
        int top = 0, bottom = matrix.length - 1;
        int left = 0, right = matrix[0].length - 1;

        while (top <= bottom && left <= right) {
            for (int c = left; c <= right; c++) result.add(matrix[top][c]);
            top++;
            for (int r = top; r <= bottom; r++) result.add(matrix[r][right]);
            right--;
            if (top <= bottom) {
                for (int c = right; c >= left; c--) result.add(matrix[bottom][c]);
                bottom--;
            }
            if (left <= right) {
                for (int r = bottom; r >= top; r--) result.add(matrix[r][left]);
                left++;
            }
        }
        return result;
    }

    public static void main(String[] args) {
        System.out.println("=== Spiral Matrix ===");
        int[][] matrix = {{1,2,3},{4,5,6},{7,8,9}};
        System.out.println("Simulation: " + spiralBrute(matrix)); // [1,2,3,6,9,8,7,4,5]
        System.out.println("Layer:      " + spiralBest(matrix));   // [1,2,3,6,9,8,7,4,5]
    }
}
