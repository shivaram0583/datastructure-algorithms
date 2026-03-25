/**
 * Problem: Meeting Rooms II (LeetCode #253)
 * Difficulty: Medium
 * Topics: Array, Two Pointers, Greedy, Sorting, Heap
 * Frequently asked at: Microsoft
 *
 * Description:
 *   Given an array of meeting time intervals, find the minimum number
 *   of conference rooms required.
 *
 * Approach 1 – Brute Force (check all overlaps): TC: O(n²) | SC: O(n)
 * Approach 2 – Min Heap (Optimal): TC: O(n log n) | SC: O(n)
 * Approach 3 – Chronological Ordering (Best): TC: O(n log n) | SC: O(n)
 */

import java.util.*;

public class MeetingRoomsII {

    // =========================================================================
    // Approach 1: Brute Force — O(n²)
    // =========================================================================
    public static int minRoomsBrute(int[][] intervals) {
        if (intervals.length == 0) return 0;
        int maxRooms = 0;
        for (int[] interval : intervals) {
            int count = 0;
            for (int[] other : intervals) {
                if (other[0] < interval[1] && other[1] > interval[0]) count++;
            }
            maxRooms = Math.max(maxRooms, count);
        }
        return maxRooms;
    }

    // =========================================================================
    // Approach 2: Min Heap — O(n log n)
    // =========================================================================
    public static int minRoomsOptimal(int[][] intervals) {
        Arrays.sort(intervals, (a, b) -> a[0] - b[0]);
        PriorityQueue<Integer> minHeap = new PriorityQueue<>();

        for (int[] interval : intervals) {
            if (!minHeap.isEmpty() && minHeap.peek() <= interval[0]) {
                minHeap.poll(); // reuse room
            }
            minHeap.offer(interval[1]);
        }
        return minHeap.size();
    }

    // =========================================================================
    // Approach 3: Chronological Ordering (Best) — O(n log n)
    // =========================================================================
    public static int minRoomsBest(int[][] intervals) {
        int n = intervals.length;
        int[] starts = new int[n], ends = new int[n];
        for (int i = 0; i < n; i++) {
            starts[i] = intervals[i][0];
            ends[i] = intervals[i][1];
        }
        Arrays.sort(starts);
        Arrays.sort(ends);

        int rooms = 0, endPtr = 0;
        for (int i = 0; i < n; i++) {
            if (starts[i] < ends[endPtr]) {
                rooms++;
            } else {
                endPtr++;
            }
        }
        return rooms;
    }

    public static void main(String[] args) {
        System.out.println("=== Meeting Rooms II ===");
        int[][] intervals = {{0,30},{5,10},{15,20}};
        System.out.println("Brute: " + minRoomsBrute(intervals));    // 2
        System.out.println("Heap:  " + minRoomsOptimal(intervals));  // 2
        System.out.println("Chrono:" + minRoomsBest(intervals));     // 2
    }
}
