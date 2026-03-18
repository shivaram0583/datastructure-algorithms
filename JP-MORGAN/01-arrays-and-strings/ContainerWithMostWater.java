/**
 * Problem: Container With Most Water (LeetCode #11)
 * Difficulty: Medium
 * Topics: Array, Two Pointers, Greedy
 * Frequently asked at: JP Morgan Chase (Round 1)
 *
 * Description:
 *   Given n non-negative integers height[i] representing vertical lines,
 *   find two lines that together with the x-axis form a container that
 *   holds the most water. Return the maximum amount of water.
 *
 * Example:
 *   Input:  height = [1,8,6,2,5,4,8,3,7]
 *   Output: 49  (lines at index 1 and 8, width=7, height=min(8,7)=7, area=49)
 *
 * Constraints:
 *   n == height.length
 *   2 <= n <= 10^5
 *   0 <= height[i] <= 10^4
 *
 * JP Morgan Context:
 *   Tests two-pointer greedy thinking. Follow-up: "Why move the shorter
 *   pointer and not the taller one?" — tests analytical reasoning.
 *
 * ============================================================
 * Approach 1 — Brute Force
 * ============================================================
 *   Try every pair (i, j): area = (j - i) * min(height[i], height[j]).
 *   Track maximum area.
 *
 *   TC: O(n²)  SC: O(1)
 *
 * ============================================================
 * Approach 2 — Two Pointers (Optimal)
 * ============================================================
 *   Start with left=0 (leftmost) and right=n-1 (rightmost).
 *   Width is maximized at the start. To potentially increase area,
 *   we need a taller line. Moving the taller pointer can only decrease
 *   width without guaranteeing a height increase. So always move the
 *   SHORTER pointer inward.
 *
 *   Step-by-step ([1,8,6,2,5,4,8,3,7]):
 *     l=0(1), r=8(7): area=1*(8-0)=8,   move left (height[l]=1 < height[r]=7)
 *     l=1(8), r=8(7): area=7*(8-1)=49,  move right (height[r]=7 < height[l]=8)
 *     l=1(8), r=7(3): area=3*(7-1)=18,  move right
 *     l=1(8), r=6(8): area=8*(6-1)=40,  move either (equal, move right)
 *     l=1(8), r=5(4): area=4*(5-1)=16,  move right
 *     l=1(8), r=4(5): area=5*(4-1)=15,  move right
 *     l=1(8), r=3(2): area=2*(3-1)=4,   move right
 *     l=1(8), r=2(6): area=6*(2-1)=6,   move right
 *     l=1(8), r=1(8): l >= r → stop
 *   Max area = 49
 *
 *   Why does this work?
 *   Moving the shorter pointer is the only way we could possibly
 *   find a larger area (the width decreases but height might increase).
 *   Moving the taller pointer would only decrease width without any
 *   chance of height improvement (bounded by the shorter side).
 *
 *   TC: O(n)  SC: O(1)
 */

public class ContainerWithMostWater {

    // =========================================================================
    // Approach 1: Brute Force — O(n²) time, O(1) space
    // =========================================================================
    public static int maxAreaBrute(int[] height) {
        int maxArea = 0;
        for (int i = 0; i < height.length; i++) {
            for (int j = i + 1; j < height.length; j++) {
                int area = (j - i) * Math.min(height[i], height[j]);
                maxArea = Math.max(maxArea, area);
            }
        }
        return maxArea;
    }

    // =========================================================================
    // Approach 2: Two Pointers (Optimal) — O(n) time, O(1) space
    // =========================================================================
    public static int maxAreaOptimal(int[] height) {
        int left = 0, right = height.length - 1;
        int maxArea = 0;

        while (left < right) {
            int area = (right - left) * Math.min(height[left], height[right]);
            maxArea = Math.max(maxArea, area);

            if (height[left] < height[right]) {
                left++;
            } else {
                right--;
            }
        }
        return maxArea;
    }

    // =========================================================================
    // Main — Test Cases
    // =========================================================================
    public static void main(String[] args) {
        System.out.println("=== Container With Most Water ===\n");

        int[] height1 = {1, 8, 6, 2, 5, 4, 8, 3, 7};
        System.out.println("Test 1: [1,8,6,2,5,4,8,3,7]  Expected: 49");
        System.out.println("  Brute:   " + maxAreaBrute(height1));
        System.out.println("  Optimal: " + maxAreaOptimal(height1));

        int[] height2 = {1, 1};
        System.out.println("\nTest 2: [1,1]  Expected: 1");
        System.out.println("  Optimal: " + maxAreaOptimal(height2));

        int[] height3 = {4, 3, 2, 1, 4};
        System.out.println("\nTest 3: [4,3,2,1,4]  Expected: 16");
        System.out.println("  Optimal: " + maxAreaOptimal(height3));
    }
}
