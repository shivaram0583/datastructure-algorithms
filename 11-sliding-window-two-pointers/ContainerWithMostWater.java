/**
 * Problem: Container With Most Water (LeetCode #11)
 * Difficulty: Medium
 * Topics: Array, Two Pointers, Greedy
 * Frequently asked at: Microsoft
 *
 * Description:
 *   Given n non-negative integers representing heights, find two lines
 *   that together with the x-axis form a container that holds the most water.
 *
 * Approach 1 – Brute Force: check all pairs. TC: O(n²) | SC: O(1)
 * Approach 2 – Two Pointers (Optimal/Best): TC: O(n) | SC: O(1)
 * Approach 3 – Two Pointers with skip optimization: TC: O(n) | SC: O(1)
 */

public class ContainerWithMostWater {

    // =========================================================================
    // Approach 1: Brute Force — O(n²)
    // =========================================================================
    public static int maxAreaBrute(int[] height) {
        int max = 0;
        for (int i = 0; i < height.length; i++) {
            for (int j = i + 1; j < height.length; j++) {
                max = Math.max(max, Math.min(height[i], height[j]) * (j - i));
            }
        }
        return max;
    }

    // =========================================================================
    // Approach 2: Two Pointers — O(n)
    // =========================================================================
    public static int maxAreaOptimal(int[] height) {
        int left = 0, right = height.length - 1, max = 0;
        while (left < right) {
            int area = Math.min(height[left], height[right]) * (right - left);
            max = Math.max(max, area);
            if (height[left] < height[right]) left++;
            else right--;
        }
        return max;
    }

    // =========================================================================
    // Approach 3: Two Pointers with Skip — O(n) faster in practice
    // =========================================================================
    public static int maxAreaBest(int[] height) {
        int left = 0, right = height.length - 1, max = 0;
        while (left < right) {
            int h = Math.min(height[left], height[right]);
            max = Math.max(max, h * (right - left));
            // Skip shorter or equal heights
            while (left < right && height[left] <= h) left++;
            while (left < right && height[right] <= h) right--;
        }
        return max;
    }

    public static void main(String[] args) {
        System.out.println("=== Container With Most Water ===");
        int[] h = {1, 8, 6, 2, 5, 4, 8, 3, 7};
        System.out.println("Brute: " + maxAreaBrute(h));      // 49
        System.out.println("2Ptr:  " + maxAreaOptimal(h));     // 49
        System.out.println("Skip:  " + maxAreaBest(h));        // 49
    }
}
