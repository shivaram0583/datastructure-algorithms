/**
 * Problem: Single Number II (LeetCode #137)
 * Difficulty: Medium
 * Topics: Array, Bit Manipulation
 * Frequently asked at: Microsoft
 *
 * Description:
 *   Every element appears three times except one. Find the single one.
 *   Algorithm should have linear time and constant extra space.
 *
 * Approach 1 – HashMap (Brute): TC: O(n) | SC: O(n)
 * Approach 2 – Bit counting (Optimal): Count bits mod 3. TC: O(32n) | SC: O(1)
 * Approach 3 – State machine (Best): Two variables track bit counts mod 3.
 *   TC: O(n) | SC: O(1)
 */

import java.util.*;

public class SingleNumberII {

    public static int singleNumberBrute(int[] nums) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int n : nums) map.merge(n, 1, Integer::sum);
        for (var e : map.entrySet()) if (e.getValue() == 1) return e.getKey();
        return -1;
    }

    public static int singleNumberOptimal(int[] nums) {
        int result = 0;
        for (int i = 0; i < 32; i++) {
            int sum = 0;
            for (int num : nums) sum += (num >> i) & 1;
            if (sum % 3 != 0) result |= (1 << i);
        }
        return result;
    }

    public static int singleNumberBest(int[] nums) {
        int ones = 0, twos = 0;
        for (int num : nums) {
            ones = (ones ^ num) & ~twos;
            twos = (twos ^ num) & ~ones;
        }
        return ones;
    }

    public static void main(String[] args) {
        System.out.println("=== Single Number II ===");
        int[] nums = {2, 2, 3, 2};
        System.out.println("Map:      " + singleNumberBrute(nums));    // 3
        System.out.println("BitCount: " + singleNumberOptimal(nums));  // 3
        System.out.println("State:    " + singleNumberBest(nums));     // 3
    }
}
