/**
 * Problem: Rotate Image (LeetCode #48)
 * Difficulty: Medium
 * Topics: Array, Math, Matrix
 * Frequently asked at: Microsoft
 *
 * Description:
 *   Rotate the given n x n 2D matrix 90 degrees clockwise in-place.
 *
 * Approach 1 – Extra Matrix (Brute): TC: O(n²) | SC: O(n²)
 * Approach 2 – Transpose + Reverse (Optimal/Best): TC: O(n²) | SC: O(1)
 * Approach 3 – Four-way Swap: TC: O(n²) | SC: O(1)
 */

import java.util.*;

public class RotateImage {

    // =========================================================================
    // Approach 1: Extra Matrix — O(n²) time, O(n²) space
    // =========================================================================
    public static void rotateBrute(int[][] matrix) {
        int n = matrix.length;
        int[][] copy = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                copy[j][n - 1 - i] = matrix[i][j];
        for (int i = 0; i < n; i++)
            System.arraycopy(copy[i], 0, matrix[i], 0, n);
    }

    // =========================================================================
    // Approach 2: Transpose + Reverse Rows (Best) — O(n²), O(1)
    // =========================================================================
    public static void rotateBest(int[][] matrix) {
        int n = matrix.length;
        // Transpose
        for (int i = 0; i < n; i++)
            for (int j = i + 1; j < n; j++) {
                int tmp = matrix[i][j];
                matrix[i][j] = matrix[j][i];
                matrix[j][i] = tmp;
            }
        // Reverse each row
        for (int[] row : matrix) {
            int left = 0, right = n - 1;
            while (left < right) {
                int tmp = row[left]; row[left] = row[right]; row[right] = tmp;
                left++; right--;
            }
        }
    }

    // =========================================================================
    // Approach 3: Four-way swap — O(n²), O(1)
    // =========================================================================
    public static void rotateSwap(int[][] matrix) {
        int n = matrix.length;
        for (int i = 0; i < n / 2; i++) {
            for (int j = i; j < n - 1 - i; j++) {
                int tmp = matrix[i][j];
                matrix[i][j]               = matrix[n-1-j][i];
                matrix[n-1-j][i]           = matrix[n-1-i][n-1-j];
                matrix[n-1-i][n-1-j]       = matrix[j][n-1-i];
                matrix[j][n-1-i]           = tmp;
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Rotate Image ===");

        int[][] m1 = {{1,2,3},{4,5,6},{7,8,9}};
        rotateBest(m1);
        System.out.println("Transpose+Rev: " + Arrays.deepToString(m1));
        // [[7,4,1],[8,5,2],[9,6,3]]

        int[][] m2 = {{1,2,3},{4,5,6},{7,8,9}};
        rotateSwap(m2);
        System.out.println("Four-way swap: " + Arrays.deepToString(m2));
        // [[7,4,1],[8,5,2],[9,6,3]]
    }
}
