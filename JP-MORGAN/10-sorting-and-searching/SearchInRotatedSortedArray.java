/**
 * Problem: Search in Rotated Sorted Array (LeetCode #33)
 * Difficulty: Medium
 * Topics: Array, Binary Search
 * Frequently asked at: JP Morgan Chase (Round 1)
 *
 * Description:
 *   Given a rotated sorted array (no duplicates) and a target,
 *   return the index of target, or -1 if not found. Must be O(log n).
 *
 * Example 1: nums=[4,5,6,7,0,1,2], target=0 → 4
 * Example 2: nums=[4,5,6,7,0,1,2], target=3 → -1
 * Example 3: nums=[1], target=0 → -1
 *
 * Constraints:
 *   1 <= nums.length <= 5000
 *   All values are unique.
 *   nums is a rotated version of an ascending sorted array.
 *
 * JP Morgan Context:
 *   Tests deep binary search reasoning. Interviewers ask:
 *   "What if the array has duplicates?" → LeetCode #81 (worst case O(n)).
 *
 * ============================================================
 * Approach 1 — Linear Search (Baseline)
 * ============================================================
 *   TC: O(n)  SC: O(1)  — NOT acceptable, shown for comparison.
 *
 * ============================================================
 * Approach 2 — Find Pivot + Two Binary Searches
 * ============================================================
 *   Step 1: Find pivot (index of minimum element) using binary search.
 *   Step 2: Binary search in [0, pivot-1] or [pivot, n-1].
 *   TC: O(log n)  SC: O(1)
 *
 * ============================================================
 * Approach 3 — Single-Pass Modified Binary Search (Optimal)
 * ============================================================
 *   At each step, at least one half (left or right) is guaranteed sorted.
 *   Key insight: if nums[mid] >= nums[left], the LEFT half is sorted.
 *
 *   If left half sorted:
 *     If target in [nums[left], nums[mid]) → search left half
 *     Else → search right half
 *   If right half sorted:
 *     If target in (nums[mid], nums[right]] → search right half
 *     Else → search left half
 *
 *   Step-by-step ([4,5,6,7,0,1,2], target=0):
 *     l=0, r=6, mid=3: nums[3]=7
 *     nums[0]=4 <= nums[3]=7 → LEFT is sorted
 *     target=0 in [4,7)? NO → search right: l=4
 *     l=4, r=6, mid=5: nums[5]=1
 *     nums[4]=0 <= nums[5]=1 → LEFT is sorted
 *     target=0 in [0,1)? YES → search left: r=4
 *     l=4, r=4: return 4
 *
 *   TC: O(log n)  SC: O(1)
 */

public class SearchInRotatedSortedArray {

    // =========================================================================
    // Approach 1: Linear Search — O(n) time, O(1) space
    // =========================================================================
    public static int searchLinear(int[] nums, int target) {
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] == target) return i;
        }
        return -1;
    }

    // =========================================================================
    // Approach 3: Modified Binary Search (Optimal) — O(log n) time, O(1) space
    // =========================================================================
    public static int searchOptimal(int[] nums, int target) {
        int left = 0, right = nums.length - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2;

            if (nums[mid] == target) return mid;

            if (nums[left] <= nums[mid]) {
                if (nums[left] <= target && target < nums[mid]) {
                    right = mid - 1;
                } else {
                    left = mid + 1;
                }
            } else {
                if (nums[mid] < target && target <= nums[right]) {
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            }
        }
        return -1;
    }

    // =========================================================================
    // Main — Test Cases
    // =========================================================================
    public static void main(String[] args) {
        System.out.println("=== Search in Rotated Sorted Array ===\n");

        int[] nums1 = {4, 5, 6, 7, 0, 1, 2};
        System.out.println("Test 1: [4,5,6,7,0,1,2], target=0  Expected: 4");
        System.out.println("  Linear:  " + searchLinear(nums1, 0));
        System.out.println("  Optimal: " + searchOptimal(nums1, 0));

        System.out.println("\nTest 2: [4,5,6,7,0,1,2], target=3  Expected: -1");
        System.out.println("  Optimal: " + searchOptimal(nums1, 3));

        int[] nums2 = {1};
        System.out.println("\nTest 3: [1], target=0  Expected: -1");
        System.out.println("  Optimal: " + searchOptimal(nums2, 0));

        int[] nums3 = {1, 3};
        System.out.println("\nTest 4: [1,3], target=3  Expected: 1");
        System.out.println("  Optimal: " + searchOptimal(nums3, 3));

        int[] nums4 = {5, 1, 3};
        System.out.println("\nTest 5: [5,1,3], target=5  Expected: 0");
        System.out.println("  Optimal: " + searchOptimal(nums4, 5));
    }
}
