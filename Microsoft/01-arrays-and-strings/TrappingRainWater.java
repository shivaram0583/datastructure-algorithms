/**
 * Problem: Trapping Rain Water (LeetCode #42)
 * Difficulty: Hard
 * Topics: Array, Two Pointers, Stack, Dynamic Programming
 * Frequently asked at: Microsoft
 *
 * Description:
 *   Given n non-negative integers representing an elevation map where the
 *   width of each bar is 1, compute how much water it can trap after raining.
 *
 * Example:
 *   Input:  height = [0,1,0,2,1,0,1,3,2,1,2,1]
 *   Output: 6
 *
 * Approach 1 – Brute Force
 *   For every element, find max on left and right, water at i = min(lMax,rMax)-h[i].
 *   TC: O(n²) | SC: O(1)
 *
 * Approach 2 – Prefix Max Arrays (Optimal)
 *   Pre-compute leftMax[] and rightMax[] arrays.
 *   TC: O(n) | SC: O(n)
 *
 * Approach 3 – Two Pointers (Best)
 *   Maintain two pointers and running left/right max.
 *   TC: O(n) | SC: O(1)
 */

import java.util.*;

public class TrappingRainWater {

    // =========================================================================
    // Approach 1: Brute Force — O(n²) time, O(1) space
    // =========================================================================
    public static int trapBrute(int[] height) {
        int n = height.length, water = 0;
        for (int i = 0; i < n; i++) {
            int leftMax = 0, rightMax = 0;
            for (int j = 0; j <= i; j++) leftMax = Math.max(leftMax, height[j]);
            for (int j = i; j < n; j++) rightMax = Math.max(rightMax, height[j]);
            water += Math.min(leftMax, rightMax) - height[i];
        }
        return water;
    }

    // =========================================================================
    // Approach 2: Prefix Max Arrays — O(n) time, O(n) space
    // =========================================================================
    public static int trapOptimal(int[] height) {
        int n = height.length;
        if (n == 0) return 0;

        int[] leftMax = new int[n];
        int[] rightMax = new int[n];

        leftMax[0] = height[0];
        for (int i = 1; i < n; i++) {
            leftMax[i] = Math.max(leftMax[i - 1], height[i]);
        }

        rightMax[n - 1] = height[n - 1];
        for (int i = n - 2; i >= 0; i--) {
            rightMax[i] = Math.max(rightMax[i + 1], height[i]);
        }

        int water = 0;
        for (int i = 0; i < n; i++) {
            water += Math.min(leftMax[i], rightMax[i]) - height[i];
        }
        return water;
    }

    // =========================================================================
    // Approach 3: Two Pointers (Best) — O(n) time, O(1) space
    // =========================================================================
    public static int trapBest(int[] height) {
        int left = 0, right = height.length - 1;
        int leftMax = 0, rightMax = 0, water = 0;

        while (left < right) {
            if (height[left] < height[right]) {
                if (height[left] >= leftMax) {
                    leftMax = height[left];
                } else {
                    water += leftMax - height[left];
                }
                left++;
            } else {
                if (height[right] >= rightMax) {
                    rightMax = height[right];
                } else {
                    water += rightMax - height[right];
                }
                right--;
            }
        }
        return water;
    }

    // =========================================================================
    // Main — Test Cases
    // =========================================================================
    public static void main(String[] args) {
        int[] height1 = {0, 1, 0, 2, 1, 0, 1, 3, 2, 1, 2, 1};
        System.out.println("=== Trapping Rain Water ===");
        System.out.println("Input: [0,1,0,2,1,0,1,3,2,1,2,1]");
        System.out.println("Brute:   " + trapBrute(height1));    // 6
        System.out.println("Optimal: " + trapOptimal(height1));  // 6
        System.out.println("Best:    " + trapBest(height1));      // 6

        int[] height2 = {4, 2, 0, 3, 2, 5};
        System.out.println("\nInput: [4,2,0,3,2,5]");
        System.out.println("Brute:   " + trapBrute(height2));    // 9
        System.out.println("Optimal: " + trapOptimal(height2));  // 9
        System.out.println("Best:    " + trapBest(height2));      // 9
    }
}
