/**
 * Problem: Subarray Sum Equals K (LeetCode #560)
 * Difficulty: Medium
 * Topics: Array, Hash Table, Prefix Sum
 * Frequently asked at: JP Morgan Chase (Round 1)
 *
 * Description:
 *   Given an integer array nums and an integer k, return the total number
 *   of subarrays whose sum equals k.
 *
 * Example 1:
 *   nums=[1,1,1], k=2 → 2  (subarrays [1,1] at indices [0,1] and [1,2])
 * Example 2:
 *   nums=[1,2,3], k=3 → 2  (subarrays [3] and [1,2])
 *
 * Constraints:
 *   1 <= nums.length <= 2 * 10^4
 *   -1000 <= nums[i] <= 1000
 *   -10^7 <= k <= 10^7
 *
 * JP Morgan Context:
 *   Directly models: "How many consecutive trading days have a net P&L
 *   of exactly k?" — core reconciliation problem.
 *
 * ============================================================
 * Approach 1 — Brute Force
 * ============================================================
 *   Try all subarrays [i..j] and compute their sum.
 *   TC: O(n²)  SC: O(1)
 *
 * ============================================================
 * Approach 2 — Prefix Sum + HashMap (Optimal)
 * ============================================================
 *   Key insight: sum(i..j) = prefix[j] - prefix[i-1]
 *   If sum(i..j) == k → prefix[j] - prefix[i-1] == k → prefix[i-1] == prefix[j] - k
 *
 *   Maintain a running prefix sum and a HashMap of (prefixSum → frequency).
 *   For each element:
 *     1. Add nums[i] to running sum.
 *     2. Check if (runningSum - k) exists in map. If yes, add its count to result.
 *     3. Increment map[runningSum] by 1.
 *   Initialize map with {0: 1} to handle subarrays starting from index 0.
 *
 *   Step-by-step ([1,1,1], k=2):
 *     map={0:1}, sum=0, count=0
 *     i=0: sum=1, check map[1-2]=-1? No. map={0:1, 1:1}
 *     i=1: sum=2, check map[2-2]=0? YES, count+=1. map={0:1,1:1,2:1}
 *     i=2: sum=3, check map[3-2]=1? YES, count+=1. map={0:1,1:1,2:1,3:1}
 *   Result: 2
 *
 *   TC: O(n)  SC: O(n)
 */

import java.util.HashMap;
import java.util.Map;

public class SubarraySumEqualsK {

    // =========================================================================
    // Approach 1: Brute Force — O(n²) time, O(1) space
    // =========================================================================
    public static int subarraySumBrute(int[] nums, int k) {
        int count = 0;
        for (int i = 0; i < nums.length; i++) {
            int sum = 0;
            for (int j = i; j < nums.length; j++) {
                sum += nums[j];
                if (sum == k) count++;
            }
        }
        return count;
    }

    // =========================================================================
    // Approach 2: Prefix Sum + HashMap (Optimal) — O(n) time, O(n) space
    // =========================================================================
    public static int subarraySumOptimal(int[] nums, int k) {
        Map<Integer, Integer> prefixCount = new HashMap<>();
        prefixCount.put(0, 1);
        int runningSum = 0, count = 0;

        for (int num : nums) {
            runningSum += num;
            count += prefixCount.getOrDefault(runningSum - k, 0);
            prefixCount.merge(runningSum, 1, Integer::sum);
        }
        return count;
    }

    // =========================================================================
    // Main — Test Cases
    // =========================================================================
    public static void main(String[] args) {
        System.out.println("=== Subarray Sum Equals K ===\n");

        System.out.println("Test 1: [1,1,1], k=2  Expected: 2");
        System.out.println("  Brute:   " + subarraySumBrute(new int[]{1,1,1}, 2));
        System.out.println("  Optimal: " + subarraySumOptimal(new int[]{1,1,1}, 2));

        System.out.println("\nTest 2: [1,2,3], k=3  Expected: 2");
        System.out.println("  Brute:   " + subarraySumBrute(new int[]{1,2,3}, 3));
        System.out.println("  Optimal: " + subarraySumOptimal(new int[]{1,2,3}, 3));

        System.out.println("\nTest 3: [1,-1,1], k=0  Expected: 3");
        System.out.println("  Brute:   " + subarraySumBrute(new int[]{1,-1,1}, 0));
        System.out.println("  Optimal: " + subarraySumOptimal(new int[]{1,-1,1}, 0));

        System.out.println("\nTest 4: [3,4,7,2,-3,1,4,2], k=7  Expected: 4");
        System.out.println("  Optimal: " + subarraySumOptimal(new int[]{3,4,7,2,-3,1,4,2}, 7));
    }
}
