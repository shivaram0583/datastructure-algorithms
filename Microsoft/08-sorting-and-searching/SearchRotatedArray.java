/**
 * Problem: Search in Rotated Sorted Array (LeetCode #33)
 * Difficulty: Medium
 * Topics: Array, Binary Search
 * Frequently asked at: Microsoft
 *
 * Description:
 *   Given a rotated sorted array and a target, return the index of target
 *   or -1 if not found. You must write an algorithm with O(log n) runtime.
 *
 * Approach 1 – Brute Force (Linear Scan)
 *   TC: O(n) | SC: O(1)
 *
 * Approach 2 – Find Pivot + Two Binary Searches (Optimal)
 *   Find rotation point, then binary search the correct half.
 *   TC: O(log n) | SC: O(1)
 *
 * Approach 3 – Modified Binary Search (Best)
 *   Single binary search with partition logic.
 *   TC: O(log n) | SC: O(1)
 */

public class SearchRotatedArray {

    // =========================================================================
    // Approach 1: Linear Scan — O(n)
    // =========================================================================
    public static int searchBrute(int[] nums, int target) {
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] == target) return i;
        }
        return -1;
    }

    // =========================================================================
    // Approach 2: Find Pivot + Binary Search — O(log n)
    // =========================================================================
    public static int searchOptimal(int[] nums, int target) {
        int n = nums.length;
        int pivot = findPivot(nums, n);

        // Determine which half to search
        if (target >= nums[pivot] && target <= nums[n - 1]) {
            return binarySearch(nums, pivot, n - 1, target);
        }
        return binarySearch(nums, 0, pivot - 1, target);
    }

    private static int findPivot(int[] nums, int n) {
        int lo = 0, hi = n - 1;
        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            if (nums[mid] > nums[hi]) lo = mid + 1;
            else hi = mid;
        }
        return lo;
    }

    private static int binarySearch(int[] nums, int lo, int hi, int target) {
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            if (nums[mid] == target) return mid;
            else if (nums[mid] < target) lo = mid + 1;
            else hi = mid - 1;
        }
        return -1;
    }

    // =========================================================================
    // Approach 3: Modified Single Binary Search (Best) — O(log n)
    // =========================================================================
    public static int searchBest(int[] nums, int target) {
        int lo = 0, hi = nums.length - 1;

        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            if (nums[mid] == target) return mid;

            // Left half is sorted
            if (nums[lo] <= nums[mid]) {
                if (target >= nums[lo] && target < nums[mid]) hi = mid - 1;
                else lo = mid + 1;
            }
            // Right half is sorted
            else {
                if (target > nums[mid] && target <= nums[hi]) lo = mid + 1;
                else hi = mid - 1;
            }
        }
        return -1;
    }

    // =========================================================================
    // Main
    // =========================================================================
    public static void main(String[] args) {
        System.out.println("=== Search in Rotated Sorted Array ===");
        int[] nums = {4, 5, 6, 7, 0, 1, 2};

        System.out.println("target=0: Brute=" + searchBrute(nums, 0)
            + " Pivot=" + searchOptimal(nums, 0)
            + " Best=" + searchBest(nums, 0)); // 4

        System.out.println("target=3: Brute=" + searchBrute(nums, 3)
            + " Pivot=" + searchOptimal(nums, 3)
            + " Best=" + searchBest(nums, 3)); // -1
    }
}
