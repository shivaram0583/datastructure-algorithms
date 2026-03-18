/**
 * Problem: Sort Colors / Dutch National Flag (LeetCode #75)
 * Difficulty: Medium
 * Topics: Array, Two Pointers, Sorting
 * Frequently asked at: JP Morgan Chase (Round 1)
 *
 * Description:
 *   Given an array with n objects colored red(0), white(1), or blue(2),
 *   sort them in-place so that same colors are adjacent in order 0,1,2.
 *   Must be done in one pass and constant space.
 *
 * Example:
 *   Input:  [2,0,2,1,1,0]
 *   Output: [0,0,1,1,2,2]
 *
 * Constraints:
 *   n == nums.length
 *   1 <= n <= 300
 *   nums[i] is 0, 1, or 2.
 *
 * JP Morgan Context:
 *   Dutch National Flag is a classic algorithmic pattern. Tests:
 *   - Three-way partition (also the backbone of QuickSort's 3-way variant)
 *   - In-place single-pass reasoning with 3 pointers
 *
 * ============================================================
 * Approach 1 — Count & Fill (Two-pass)
 * ============================================================
 *   Count occurrences of 0, 1, 2. Rewrite array.
 *   TC: O(n)  SC: O(1)  — Two passes, not single pass.
 *
 * ============================================================
 * Approach 2 — Dutch National Flag (Optimal, Single Pass)
 * ============================================================
 *   Three pointers:
 *     low  = boundary of 0s (all before low are 0s)
 *     mid  = current element under examination
 *     high = boundary of 2s (all after high are 2s)
 *
 *   Loop while mid <= high:
 *     nums[mid] == 0 → swap(low, mid), low++, mid++
 *     nums[mid] == 1 → mid++
 *     nums[mid] == 2 → swap(mid, high), high--  (don't advance mid, new element unexamined)
 *
 *   Step-by-step ([2,0,2,1,1,0]):
 *     low=0, mid=0, high=5
 *     nums[0]=2 → swap(0,5)=[0,0,2,1,1,2], high=4
 *     nums[0]=0 → swap(0,0)=[0,0,2,1,1,2], low=1, mid=1
 *     nums[1]=0 → swap(1,1)=[0,0,2,1,1,2], low=2, mid=2
 *     nums[2]=2 → swap(2,4)=[0,0,1,1,2,2], high=3
 *     nums[2]=1 → mid=3
 *     nums[3]=1 → mid=4
 *     mid(4) > high(3) → done
 *   Result: [0,0,1,1,2,2]
 *
 *   TC: O(n)  SC: O(1)
 */

import java.util.Arrays;

public class SortColors {

    // =========================================================================
    // Approach 1: Count & Fill — O(n) time, O(1) space (two passes)
    // =========================================================================
    public static void sortColorsTwoPass(int[] nums) {
        int count0 = 0, count1 = 0, count2 = 0;
        for (int n : nums) {
            if (n == 0) count0++;
            else if (n == 1) count1++;
            else count2++;
        }
        int i = 0;
        while (count0-- > 0) nums[i++] = 0;
        while (count1-- > 0) nums[i++] = 1;
        while (count2-- > 0) nums[i++] = 2;
    }

    // =========================================================================
    // Approach 2: Dutch National Flag — O(n) time, O(1) space (single pass)
    // =========================================================================
    public static void sortColorsDNF(int[] nums) {
        int low = 0, mid = 0, high = nums.length - 1;
        while (mid <= high) {
            if (nums[mid] == 0) {
                swap(nums, low++, mid++);
            } else if (nums[mid] == 1) {
                mid++;
            } else {
                swap(nums, mid, high--);
            }
        }
    }

    private static void swap(int[] nums, int i, int j) {
        int tmp = nums[i]; nums[i] = nums[j]; nums[j] = tmp;
    }

    // =========================================================================
    // Main — Test Cases
    // =========================================================================
    public static void main(String[] args) {
        System.out.println("=== Sort Colors (Dutch National Flag) ===\n");

        int[] t1a = {2, 0, 2, 1, 1, 0};
        int[] t1b = {2, 0, 2, 1, 1, 0};
        System.out.println("Test 1: [2,0,2,1,1,0]  Expected: [0,0,1,1,2,2]");
        sortColorsTwoPass(t1a);
        System.out.println("  TwoPass: " + Arrays.toString(t1a));
        sortColorsDNF(t1b);
        System.out.println("  DNF:     " + Arrays.toString(t1b));

        int[] t2 = {2, 0, 1};
        System.out.println("\nTest 2: [2,0,1]  Expected: [0,1,2]");
        sortColorsDNF(t2);
        System.out.println("  DNF: " + Arrays.toString(t2));

        int[] t3 = {0};
        System.out.println("\nTest 3: [0]  Expected: [0]");
        sortColorsDNF(t3);
        System.out.println("  DNF: " + Arrays.toString(t3));

        int[] t4 = {1, 2, 0};
        System.out.println("\nTest 4: [1,2,0]  Expected: [0,1,2]");
        sortColorsDNF(t4);
        System.out.println("  DNF: " + Arrays.toString(t4));
    }
}
