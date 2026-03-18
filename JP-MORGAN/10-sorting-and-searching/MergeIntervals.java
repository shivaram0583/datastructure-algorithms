/**
 * Problem: Merge Intervals (LeetCode #56)
 * Difficulty: Medium
 * Topics: Array, Sorting, Greedy
 * Frequently asked at: JP Morgan Chase (Round 1)
 *
 * Description:
 *   Given an array of intervals where intervals[i] = [start, end],
 *   merge all overlapping intervals and return the non-overlapping intervals.
 *
 * Example 1:
 *   intervals = [[1,3],[2,6],[8,10],[15,18]]
 *   Output:     [[1,6],[8,10],[15,18]]
 *
 * Example 2:
 *   intervals = [[1,4],[4,5]]
 *   Output:     [[1,5]]  (touching intervals merge)
 *
 * Constraints:
 *   1 <= intervals.length <= 10^4
 *   intervals[i].length == 2
 *   0 <= start <= end <= 10^4
 *
 * JP Morgan Context:
 *   Models merging overlapping trading sessions, maintenance windows,
 *   or market halt periods. Follow-up: "Insert a new interval" → LeetCode #57.
 *
 * ============================================================
 * Approach — Sort + Greedy Merge (Optimal)
 * ============================================================
 *   Sort intervals by start time.
 *   Iterate: if current interval overlaps with the last merged interval
 *   (current.start <= last.end), extend last.end to max(last.end, current.end).
 *   Otherwise, add current as new interval.
 *
 *   Step-by-step ([[1,3],[2,6],[8,10],[15,18]]):
 *     Sort: already sorted by start.
 *     result=[[1,3]]
 *     [2,6]: 2<=3 → merge → result=[[1,6]]
 *     [8,10]: 8>6 → add → result=[[1,6],[8,10]]
 *     [15,18]: 15>10 → add → result=[[1,6],[8,10],[15,18]]
 *
 *   TC: O(n log n)  SC: O(n) for output
 */

import java.util.*;

public class MergeIntervals {

    // =========================================================================
    // Optimal: Sort + Greedy Merge — O(n log n) time, O(n) space
    // =========================================================================
    public static int[][] merge(int[][] intervals) {
        if (intervals.length <= 1) return intervals;
        Arrays.sort(intervals, (a, b) -> a[0] - b[0]);

        List<int[]> result = new ArrayList<>();
        int[] current = intervals[0];

        for (int i = 1; i < intervals.length; i++) {
            if (intervals[i][0] <= current[1]) {
                current[1] = Math.max(current[1], intervals[i][1]);
            } else {
                result.add(current);
                current = intervals[i];
            }
        }
        result.add(current);
        return result.toArray(new int[0][]);
    }

    // =========================================================================
    // Bonus: Insert Interval (LeetCode #57)
    // =========================================================================
    public static int[][] insert(int[][] intervals, int[] newInterval) {
        List<int[]> result = new ArrayList<>();
        int i = 0, n = intervals.length;

        while (i < n && intervals[i][1] < newInterval[0]) {
            result.add(intervals[i++]);
        }
        while (i < n && intervals[i][0] <= newInterval[1]) {
            newInterval[0] = Math.min(newInterval[0], intervals[i][0]);
            newInterval[1] = Math.max(newInterval[1], intervals[i][1]);
            i++;
        }
        result.add(newInterval);
        while (i < n) result.add(intervals[i++]);
        return result.toArray(new int[0][]);
    }

    // =========================================================================
    // Main — Test Cases
    // =========================================================================
    public static void main(String[] args) {
        System.out.println("=== Merge Intervals ===\n");

        int[][] t1 = {{1,3},{2,6},{8,10},{15,18}};
        System.out.println("Test 1: [[1,3],[2,6],[8,10],[15,18]]  Expected: [[1,6],[8,10],[15,18]]");
        System.out.println("  Result: " + Arrays.deepToString(merge(t1)));

        int[][] t2 = {{1,4},{4,5}};
        System.out.println("\nTest 2: [[1,4],[4,5]]  Expected: [[1,5]]");
        System.out.println("  Result: " + Arrays.deepToString(merge(t2)));

        int[][] t3 = {{1,5}};
        System.out.println("\nInsert Interval: [[1,5]], new=[2,3]  Expected: [[1,5]]");
        System.out.println("  Result: " + Arrays.deepToString(insert(t3, new int[]{2,3})));

        int[][] t4 = {{1,2},{3,5},{6,7},{8,10},{12,16}};
        System.out.println("\nInsert Interval: [[1,2],[3,5],[6,7],[8,10],[12,16]], new=[4,8]");
        System.out.println("  Expected: [[1,2],[3,10],[12,16]]");
        System.out.println("  Result: " + Arrays.deepToString(insert(t4, new int[]{4,8})));
    }
}
