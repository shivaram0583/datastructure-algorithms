/**
 * Problem: Maximum Subarray (LeetCode #53)
 * Difficulty: Medium
 * Topics: Array, Dynamic Programming, Kadane's Algorithm
 * Frequently asked at: JP Morgan Chase (Round 1)
 *
 * Description:
 *   Given an integer array nums, find the subarray with the largest sum,
 *   and return its sum.
 *
 * Example 1:
 *   Input:  nums = [-2,1,-3,4,-1,2,1,-5,4]
 *   Output: 6  (subarray [4,-1,2,1])
 *
 * Example 2:
 *   Input:  nums = [1]
 *   Output: 1
 *
 * Constraints:
 *   1 <= nums.length <= 10^5
 *   -10^4 <= nums[i] <= 10^4
 *
 * JP Morgan Context:
 *   Directly models "find the best consecutive trading window"
 *   (maximum profit period). Follow-ups:
 *   - "Return the actual subarray indices, not just the sum"
 *   - "What if the array is circular?" → LeetCode #918
 *
 * ============================================================
 * Approach 1 — Brute Force
 * ============================================================
 *   Try every subarray [i..j] and compute its sum.
 *
 *   TC: O(n²)  SC: O(1)
 *
 * ============================================================
 * Approach 2 — Prefix Sum
 * ============================================================
 *   prefix[i] = sum of nums[0..i].
 *   Sum of subarray [i..j] = prefix[j] - prefix[i-1].
 *   Track minPrefix seen so far and compute maxSum = prefix[j] - minPrefix.
 *
 *   TC: O(n)  SC: O(n)
 *
 * ============================================================
 * Approach 3 — Kadane's Algorithm (Optimal)
 * ============================================================
 *   Maintain currentSum = max subarray sum ending at index i.
 *   If currentSum goes negative, reset it to 0 (start fresh).
 *   Track globalMax.
 *
 *   Step-by-step ([-2,1,-3,4,-1,2,1,-5,4]):
 *     i=0: cur=max(-2,0)=-2→reset→cur=0  NO, Kadane keeps negative if we track global
 *     Actually classic Kadane:
 *       cur=-2, max=-2
 *       cur=max(1,-2+1)=1,     max=1
 *       cur=max(-3,1-3)=-2,    max=1
 *       cur=max(4,-2+4)=4,     max=4
 *       cur=max(-1,4-1)=3,     max=4
 *       cur=max(2,3+2)=5,      max=5
 *       cur=max(1,5+1)=6,      max=6
 *       cur=max(-5,6-5)=1,     max=6
 *       cur=max(4,1+4)=5,      max=6
 *   Result: 6
 *
 *   TC: O(n)  SC: O(1)
 *
 * ============================================================
 * Approach 4 — Kadane's with Index Tracking
 * ============================================================
 *   Same as Approach 3 but also track start/end indices of the subarray.
 *   TC: O(n)  SC: O(1)
 */

public class MaximumSubarray {

    // =========================================================================
    // Approach 1: Brute Force — O(n²) time, O(1) space
    // =========================================================================
    public static int maxSubarrayBrute(int[] nums) {
        int maxSum = Integer.MIN_VALUE;
        for (int i = 0; i < nums.length; i++) {
            int currentSum = 0;
            for (int j = i; j < nums.length; j++) {
                currentSum += nums[j];
                maxSum = Math.max(maxSum, currentSum);
            }
        }
        return maxSum;
    }

    // =========================================================================
    // Approach 2: Prefix Sum — O(n) time, O(n) space
    // =========================================================================
    public static int maxSubarrayPrefix(int[] nums) {
        int n = nums.length;
        int[] prefix = new int[n + 1];
        for (int i = 0; i < n; i++) {
            prefix[i + 1] = prefix[i] + nums[i];
        }
        int maxSum = Integer.MIN_VALUE;
        int minPrefix = 0;
        for (int i = 1; i <= n; i++) {
            maxSum = Math.max(maxSum, prefix[i] - minPrefix);
            minPrefix = Math.min(minPrefix, prefix[i]);
        }
        return maxSum;
    }

    // =========================================================================
    // Approach 3: Kadane's Algorithm (Optimal) — O(n) time, O(1) space
    // =========================================================================
    public static int maxSubarrayKadane(int[] nums) {
        int currentSum = nums[0];
        int maxSum = nums[0];
        for (int i = 1; i < nums.length; i++) {
            currentSum = Math.max(nums[i], currentSum + nums[i]);
            maxSum = Math.max(maxSum, currentSum);
        }
        return maxSum;
    }

    // =========================================================================
    // Approach 4: Kadane's with Index Tracking — O(n) time, O(1) space
    // =========================================================================
    public static int[] maxSubarrayWithIndices(int[] nums) {
        int currentSum = nums[0];
        int maxSum = nums[0];
        int start = 0, end = 0, tempStart = 0;

        for (int i = 1; i < nums.length; i++) {
            if (nums[i] > currentSum + nums[i]) {
                currentSum = nums[i];
                tempStart = i;
            } else {
                currentSum += nums[i];
            }
            if (currentSum > maxSum) {
                maxSum = currentSum;
                start = tempStart;
                end = i;
            }
        }
        return new int[]{maxSum, start, end};
    }

    // =========================================================================
    // Main — Test Cases
    // =========================================================================
    public static void main(String[] args) {
        System.out.println("=== Maximum Subarray ===\n");

        int[] nums1 = {-2, 1, -3, 4, -1, 2, 1, -5, 4};
        System.out.println("Test 1: [-2,1,-3,4,-1,2,1,-5,4]  Expected: 6");
        System.out.println("  Brute:   " + maxSubarrayBrute(nums1));
        System.out.println("  Prefix:  " + maxSubarrayPrefix(nums1));
        System.out.println("  Kadane:  " + maxSubarrayKadane(nums1));
        int[] res1 = maxSubarrayWithIndices(nums1);
        System.out.println("  Indices: sum=" + res1[0] + ", start=" + res1[1] + ", end=" + res1[2]);

        int[] nums2 = {1};
        System.out.println("\nTest 2: [1]  Expected: 1");
        System.out.println("  Kadane:  " + maxSubarrayKadane(nums2));

        int[] nums3 = {-2, -1};
        System.out.println("\nTest 3: [-2,-1]  Expected: -1");
        System.out.println("  Kadane:  " + maxSubarrayKadane(nums3));

        int[] nums4 = {5, 4, -1, 7, 8};
        System.out.println("\nTest 4: [5,4,-1,7,8]  Expected: 23");
        System.out.println("  Kadane:  " + maxSubarrayKadane(nums4));
    }
}
