/**
 * Problem: Merge Intervals (LeetCode #56)
 * Difficulty: Medium
 * Topics: Array, Sorting
 * Frequently asked at: Microsoft
 *
 * Description:
 *   Given an array of intervals, merge all overlapping intervals.
 *
 * Approach 1 – Brute Force
 *   Compare every pair; merge if overlap. Repeat until stable.
 *   TC: O(n²) | SC: O(n)
 *
 * Approach 2 – Sort + Linear Merge (Optimal/Best)
 *   Sort by start, linear scan to merge.
 *   TC: O(n log n) | SC: O(n)
 *
 * Approach 3 – Connected Components (alternative)
 *   Build graph of overlapping intervals, BFS/DFS per component.
 *   TC: O(n² + V+E) | SC: O(n²)
 */

import java.util.*;

public class MergeIntervals {

    // =========================================================================
    // Approach 1: Brute Force — O(n²)
    // =========================================================================
    public static int[][] mergeBrute(int[][] intervals) {
        List<int[]> merged = new ArrayList<>(Arrays.asList(intervals));
        boolean changed = true;
        while (changed) {
            changed = false;
            for (int i = 0; i < merged.size(); i++) {
                for (int j = i + 1; j < merged.size(); j++) {
                    if (merged.get(i)[0] <= merged.get(j)[1] &&
                        merged.get(j)[0] <= merged.get(i)[1]) {
                        merged.get(i)[0] = Math.min(merged.get(i)[0], merged.get(j)[0]);
                        merged.get(i)[1] = Math.max(merged.get(i)[1], merged.get(j)[1]);
                        merged.remove(j);
                        changed = true;
                        break;
                    }
                }
                if (changed) break;
            }
        }
        return merged.toArray(new int[0][]);
    }

    // =========================================================================
    // Approach 2: Sort + Merge (Best) — O(n log n) time
    // =========================================================================
    public static int[][] mergeBest(int[][] intervals) {
        Arrays.sort(intervals, (a, b) -> a[0] - b[0]);
        List<int[]> merged = new ArrayList<>();
        merged.add(intervals[0]);

        for (int i = 1; i < intervals.length; i++) {
            int[] last = merged.get(merged.size() - 1);
            if (intervals[i][0] <= last[1]) {
                last[1] = Math.max(last[1], intervals[i][1]);
            } else {
                merged.add(intervals[i]);
            }
        }
        return merged.toArray(new int[0][]);
    }

    // =========================================================================
    // Approach 3: Sort + Stack based
    // =========================================================================
    public static int[][] mergeStack(int[][] intervals) {
        Arrays.sort(intervals, (a, b) -> a[0] - b[0]);
        Stack<int[]> stack = new Stack<>();
        stack.push(intervals[0]);

        for (int i = 1; i < intervals.length; i++) {
            int[] top = stack.peek();
            if (intervals[i][0] <= top[1]) {
                top[1] = Math.max(top[1], intervals[i][1]);
            } else {
                stack.push(intervals[i]);
            }
        }
        return stack.toArray(new int[0][]);
    }

    // =========================================================================
    // Main
    // =========================================================================
    public static void main(String[] args) {
        System.out.println("=== Merge Intervals ===");
        int[][] intervals = {{1,3},{2,6},{8,10},{15,18}};

        System.out.println("Best: " + Arrays.deepToString(
            mergeBest(new int[][]{{1,3},{2,6},{8,10},{15,18}})));
        // [[1,6],[8,10],[15,18]]

        System.out.println("Stack:" + Arrays.deepToString(
            mergeStack(new int[][]{{1,4},{4,5}})));
        // [[1,5]]
    }
}
