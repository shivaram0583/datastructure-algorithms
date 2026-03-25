/**
 * Problem: Jump Game (LeetCode #55)
 * Difficulty: Medium
 * Topics: Array, DP, Greedy
 * Frequently asked at: Microsoft
 *
 * Description:
 *   Given an array where each element represents the maximum jump length
 *   from that position, determine if you can reach the last index.
 *
 * Approach 1 – Recursion (Brute): TC: O(2^n) | SC: O(n)
 * Approach 2 – DP bottom-up: TC: O(n²) | SC: O(n)
 * Approach 3 – Greedy (Best): Track farthest reachable. TC: O(n) | SC: O(1)
 */

public class JumpGame {

    public static boolean canJumpBrute(int[] nums) {
        return canJumpHelper(nums, 0);
    }

    private static boolean canJumpHelper(int[] nums, int pos) {
        if (pos >= nums.length - 1) return true;
        for (int i = 1; i <= nums[pos]; i++) {
            if (canJumpHelper(nums, pos + i)) return true;
        }
        return false;
    }

    public static boolean canJumpDP(int[] nums) {
        int n = nums.length;
        boolean[] dp = new boolean[n];
        dp[n - 1] = true;
        for (int i = n - 2; i >= 0; i--) {
            for (int j = 1; j <= nums[i] && i + j < n; j++) {
                if (dp[i + j]) { dp[i] = true; break; }
            }
        }
        return dp[0];
    }

    public static boolean canJumpBest(int[] nums) {
        int farthest = 0;
        for (int i = 0; i < nums.length; i++) {
            if (i > farthest) return false;
            farthest = Math.max(farthest, i + nums[i]);
        }
        return true;
    }

    public static void main(String[] args) {
        System.out.println("=== Jump Game ===");
        int[] nums1 = {2, 3, 1, 1, 4};
        System.out.println("[2,3,1,1,4]: DP=" + canJumpDP(nums1) + " Greedy=" + canJumpBest(nums1)); // true
        int[] nums2 = {3, 2, 1, 0, 4};
        System.out.println("[3,2,1,0,4]: DP=" + canJumpDP(nums2) + " Greedy=" + canJumpBest(nums2)); // false
    }
}
