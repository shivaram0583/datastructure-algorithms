/**
 * Problem: 3Sum (LeetCode #15)
 * Difficulty: Medium
 * Topics: Array, Two Pointers, Sorting
 * Frequently asked at: Microsoft
 *
 * Description:
 *   Find all unique triplets that sum to zero.
 *
 * Approach 1 – Brute Force: Check all triplets. TC: O(n³) | SC: O(1)
 * Approach 2 – Sort + Two Pointers (Optimal/Best): TC: O(n²) | SC: O(1)
 * Approach 3 – HashSet approach: TC: O(n²) | SC: O(n)
 */

import java.util.*;

public class ThreeSum {

    // =========================================================================
    // Approach 1: Brute Force — O(n³)
    // =========================================================================
    public static List<List<Integer>> threeSumBrute(int[] nums) {
        Set<List<Integer>> result = new HashSet<>();
        Arrays.sort(nums);
        for (int i = 0; i < nums.length; i++)
            for (int j = i + 1; j < nums.length; j++)
                for (int k = j + 1; k < nums.length; k++)
                    if (nums[i] + nums[j] + nums[k] == 0)
                        result.add(Arrays.asList(nums[i], nums[j], nums[k]));
        return new ArrayList<>(result);
    }

    // =========================================================================
    // Approach 2: Sort + Two Pointers (Best) — O(n²)
    // =========================================================================
    public static List<List<Integer>> threeSumBest(int[] nums) {
        Arrays.sort(nums);
        List<List<Integer>> result = new ArrayList<>();

        for (int i = 0; i < nums.length - 2; i++) {
            if (i > 0 && nums[i] == nums[i - 1]) continue; // skip duplicates
            int left = i + 1, right = nums.length - 1;
            while (left < right) {
                int sum = nums[i] + nums[left] + nums[right];
                if (sum == 0) {
                    result.add(Arrays.asList(nums[i], nums[left], nums[right]));
                    while (left < right && nums[left] == nums[left + 1]) left++;
                    while (left < right && nums[right] == nums[right - 1]) right--;
                    left++; right--;
                } else if (sum < 0) left++;
                else right--;
            }
        }
        return result;
    }

    // =========================================================================
    // Approach 3: HashSet — O(n²) time, O(n) space
    // =========================================================================
    public static List<List<Integer>> threeSumHash(int[] nums) {
        Arrays.sort(nums);
        List<List<Integer>> result = new ArrayList<>();

        for (int i = 0; i < nums.length - 2; i++) {
            if (i > 0 && nums[i] == nums[i - 1]) continue;
            Set<Integer> seen = new HashSet<>();
            for (int j = i + 1; j < nums.length; j++) {
                int complement = -nums[i] - nums[j];
                if (seen.contains(complement)) {
                    result.add(Arrays.asList(nums[i], complement, nums[j]));
                    while (j + 1 < nums.length && nums[j] == nums[j + 1]) j++;
                }
                seen.add(nums[j]);
            }
        }
        return result;
    }

    public static void main(String[] args) {
        System.out.println("=== 3Sum ===");
        int[] nums = {-1, 0, 1, 2, -1, -4};
        System.out.println("Two Ptr: " + threeSumBest(nums));
        System.out.println("Hash:    " + threeSumHash(new int[]{-1, 0, 1, 2, -1, -4}));
        // [[-1,-1,2],[-1,0,1]]
    }
}
