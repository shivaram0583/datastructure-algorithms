/**
 * Problem: Largest Rectangle in Histogram (LeetCode #84)
 * Difficulty: Hard
 * Topics: Array, Stack, Monotonic Stack
 * Frequently asked at: Microsoft
 *
 * Description:
 *   Given an array of integers heights representing the histogram's bar heights
 *   where the width of each bar is 1, return the area of the largest rectangle.
 *
 * Example:
 *   Input:  heights = [2,1,5,6,2,3]
 *   Output: 10
 *
 * Approach 1 – Brute Force
 *   For each bar, expand left/right while height >= current.
 *   TC: O(n²) | SC: O(1)
 *
 * Approach 2 – Prefix Arrays (Optimal)
 *   Pre-compute left-smaller and right-smaller indices.
 *   TC: O(n) | SC: O(n)
 *
 * Approach 3 – Monotonic Stack (Best)
 *   Use a stack to track increasing-height indices.
 *   TC: O(n) | SC: O(n)
 */

import java.util.*;

public class LargestRectangleHistogram {

    // =========================================================================
    // Approach 1: Brute Force — O(n²) time, O(1) space
    // =========================================================================
    public static int largestBrute(int[] heights) {
        int maxArea = 0;
        for (int i = 0; i < heights.length; i++) {
            int minH = heights[i];
            for (int j = i; j < heights.length; j++) {
                minH = Math.min(minH, heights[j]);
                maxArea = Math.max(maxArea, minH * (j - i + 1));
            }
        }
        return maxArea;
    }

    // =========================================================================
    // Approach 2: Left/Right Smaller Arrays — O(n) time, O(n) space
    // =========================================================================
    public static int largestOptimal(int[] heights) {
        int n = heights.length;
        int[] leftSmaller = new int[n];
        int[] rightSmaller = new int[n];

        Stack<Integer> stack = new Stack<>();
        for (int i = 0; i < n; i++) {
            while (!stack.isEmpty() && heights[stack.peek()] >= heights[i]) stack.pop();
            leftSmaller[i] = stack.isEmpty() ? -1 : stack.peek();
            stack.push(i);
        }

        stack.clear();
        for (int i = n - 1; i >= 0; i--) {
            while (!stack.isEmpty() && heights[stack.peek()] >= heights[i]) stack.pop();
            rightSmaller[i] = stack.isEmpty() ? n : stack.peek();
            stack.push(i);
        }

        int maxArea = 0;
        for (int i = 0; i < n; i++) {
            maxArea = Math.max(maxArea, heights[i] * (rightSmaller[i] - leftSmaller[i] - 1));
        }
        return maxArea;
    }

    // =========================================================================
    // Approach 3: Monotonic Stack (Best) — O(n) time, O(n) space
    // =========================================================================
    public static int largestBest(int[] heights) {
        Stack<Integer> stack = new Stack<>();
        int maxArea = 0;
        int n = heights.length;

        for (int i = 0; i <= n; i++) {
            int currH = (i == n) ? 0 : heights[i];
            while (!stack.isEmpty() && currH < heights[stack.peek()]) {
                int h = heights[stack.pop()];
                int w = stack.isEmpty() ? i : i - stack.peek() - 1;
                maxArea = Math.max(maxArea, h * w);
            }
            stack.push(i);
        }
        return maxArea;
    }

    // =========================================================================
    // Main — Test Cases
    // =========================================================================
    public static void main(String[] args) {
        System.out.println("=== Largest Rectangle in Histogram ===");

        int[] h1 = {2, 1, 5, 6, 2, 3};
        System.out.println("Input: [2,1,5,6,2,3]");
        System.out.println("Brute:   " + largestBrute(h1));    // 10
        System.out.println("Optimal: " + largestOptimal(h1));  // 10
        System.out.println("Best:    " + largestBest(h1));      // 10

        int[] h2 = {2, 4};
        System.out.println("\nInput: [2,4]");
        System.out.println("Brute:   " + largestBrute(h2));    // 4
        System.out.println("Optimal: " + largestOptimal(h2));  // 4
        System.out.println("Best:    " + largestBest(h2));      // 4
    }
}
