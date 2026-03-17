/**
 * Problem: Task Scheduler (LeetCode #621)
 * Difficulty: Medium
 * Topics: Array, Hash Table, Greedy, Sorting, Heap
 * Frequently asked at: Microsoft
 *
 * Description:
 *   Given tasks and a cooldown interval n, return the least number of
 *   units of time the CPU will take to finish all tasks.
 *
 * Approach 1 – Simulation with Heap (Brute)
 *   Use max-heap + cooldown queue. TC: O(totalTime * log26) | SC: O(26)
 *
 * Approach 2 – Math Formula (Optimal/Best)
 *   (maxFreq - 1) * (n + 1) + countOfMaxFreq. TC: O(n) | SC: O(1)
 *
 * Approach 3 – Greedy with Sorting
 *   Sort frequencies, fill slots from most frequent. TC: O(n) | SC: O(1)
 */

import java.util.*;

public class TaskScheduler {

    // =========================================================================
    // Approach 1: Max Heap Simulation
    // =========================================================================
    public static int leastIntervalHeap(char[] tasks, int n) {
        int[] freq = new int[26];
        for (char t : tasks) freq[t - 'A']++;
        PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Collections.reverseOrder());
        for (int f : freq) if (f > 0) maxHeap.offer(f);

        int time = 0;
        while (!maxHeap.isEmpty()) {
            List<Integer> tmp = new ArrayList<>();
            int cycle = n + 1;
            while (cycle > 0 && !maxHeap.isEmpty()) {
                int f = maxHeap.poll();
                if (f > 1) tmp.add(f - 1);
                time++;
                cycle--;
            }
            for (int f : tmp) maxHeap.offer(f);
            if (!maxHeap.isEmpty()) time += cycle; // idle time
        }
        return time;
    }

    // =========================================================================
    // Approach 2: Math Formula (Best) — O(n)
    // =========================================================================
    public static int leastIntervalBest(char[] tasks, int n) {
        int[] freq = new int[26];
        for (char t : tasks) freq[t - 'A']++;

        int maxFreq = 0;
        for (int f : freq) maxFreq = Math.max(maxFreq, f);

        int maxCount = 0;
        for (int f : freq) if (f == maxFreq) maxCount++;

        // Formula: (maxFreq-1) * (n+1) + maxCount
        int result = (maxFreq - 1) * (n + 1) + maxCount;
        return Math.max(result, tasks.length);
    }

    // =========================================================================
    // Approach 3: Sort + Greedy
    // =========================================================================
    public static int leastIntervalSort(char[] tasks, int n) {
        int[] freq = new int[26];
        for (char t : tasks) freq[t - 'A']++;
        Arrays.sort(freq);

        int maxFreq = freq[25];
        int idle = (maxFreq - 1) * n;

        for (int i = 24; i >= 0 && idle > 0; i--) {
            idle -= Math.min(freq[i], maxFreq - 1);
        }
        return Math.max(0, idle) + tasks.length;
    }

    public static void main(String[] args) {
        System.out.println("=== Task Scheduler ===");
        char[] tasks = {'A','A','A','B','B','B'};
        System.out.println("n=2: Heap=" + leastIntervalHeap(tasks.clone(), 2)
            + " Math=" + leastIntervalBest(tasks.clone(), 2)
            + " Sort=" + leastIntervalSort(tasks.clone(), 2)); // 8
    }
}
