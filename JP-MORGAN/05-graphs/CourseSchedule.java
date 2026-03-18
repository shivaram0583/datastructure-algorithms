/**
 * Problem: Course Schedule (LeetCode #207)
 * Difficulty: Medium
 * Topics: Graph, Topological Sort, Cycle Detection, BFS/DFS
 * Frequently asked at: JP Morgan Chase (Round 1)
 *
 * Description:
 *   There are numCourses courses labeled 0 to numCourses-1.
 *   prerequisites[i] = [ai, bi] means to take course ai you must first take bi.
 *   Return true if you can finish all courses (no cycle in dependency graph).
 *
 * Example 1:
 *   numCourses=2, prerequisites=[[1,0]]  → true  (take 0 then 1)
 * Example 2:
 *   numCourses=2, prerequisites=[[1,0],[0,1]] → false  (circular)
 *
 * JP Morgan Context:
 *   Models task dependency resolution in financial workflows, build pipelines,
 *   and trade settlement chains. Circular dependencies = deadlock.
 *
 * ============================================================
 * Approach 1 — DFS Cycle Detection (White-Gray-Black coloring)
 * ============================================================
 *   0 = unvisited (white), 1 = in current DFS path (gray), 2 = done (black)
 *   If we encounter a gray node during DFS → cycle detected.
 *
 *   Step-by-step (numCourses=4, [[1,0],[2,1],[3,2],[1,3]]):
 *     Build adjacency list: 0→[1], 1→[2,3], 2→[3]? actually [[1,0],[2,1],[3,2],[1,3]]
 *     prereqs of 1 = {0,3}, prereqs of 2 = {1}, prereqs of 3 = {2}
 *     DFS(0): visit 0 (gray), visit 1 (gray), visit 2 (gray), visit 3 (gray)
 *             3's prereq is 2 (gray) → cycle! return false
 *
 *   TC: O(V + E)  SC: O(V + E)
 *
 * ============================================================
 * Approach 2 — Kahn's Algorithm (BFS Topological Sort)
 * ============================================================
 *   Build indegree array. Start BFS from all nodes with indegree=0.
 *   For each processed node, reduce neighbors' indegree.
 *   If neighbor's indegree becomes 0, add to queue.
 *   If all nodes processed → no cycle → return true.
 *
 *   Step-by-step (numCourses=4, [[1,0],[2,1],[3,2]]):
 *     indegree: [0,1,1,1]
 *     Queue: [0]
 *     Process 0 → reduce 1's indegree to 0 → queue: [1]
 *     Process 1 → reduce 2's indegree to 0 → queue: [2]
 *     Process 2 → reduce 3's indegree to 0 → queue: [3]
 *     Process 3 → done. processed=4 == numCourses=4 → true
 *
 *   TC: O(V + E)  SC: O(V + E)
 */

import java.util.*;

public class CourseSchedule {

    // =========================================================================
    // Approach 1: DFS Cycle Detection — O(V+E) time, O(V+E) space
    // =========================================================================
    public static boolean canFinishDFS(int numCourses, int[][] prerequisites) {
        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < numCourses; i++) adj.add(new ArrayList<>());
        for (int[] pre : prerequisites) adj.get(pre[1]).add(pre[0]);

        int[] color = new int[numCourses];
        for (int i = 0; i < numCourses; i++) {
            if (color[i] == 0 && hasCycle(adj, color, i)) return false;
        }
        return true;
    }

    private static boolean hasCycle(List<List<Integer>> adj, int[] color, int node) {
        color[node] = 1;
        for (int neighbor : adj.get(node)) {
            if (color[neighbor] == 1) return true;
            if (color[neighbor] == 0 && hasCycle(adj, color, neighbor)) return true;
        }
        color[node] = 2;
        return false;
    }

    // =========================================================================
    // Approach 2: Kahn's BFS Topological Sort — O(V+E) time, O(V+E) space
    // =========================================================================
    public static boolean canFinishBFS(int numCourses, int[][] prerequisites) {
        List<List<Integer>> adj = new ArrayList<>();
        int[] indegree = new int[numCourses];

        for (int i = 0; i < numCourses; i++) adj.add(new ArrayList<>());
        for (int[] pre : prerequisites) {
            adj.get(pre[1]).add(pre[0]);
            indegree[pre[0]]++;
        }

        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < numCourses; i++) {
            if (indegree[i] == 0) queue.offer(i);
        }

        int processed = 0;
        while (!queue.isEmpty()) {
            int course = queue.poll();
            processed++;
            for (int next : adj.get(course)) {
                if (--indegree[next] == 0) queue.offer(next);
            }
        }
        return processed == numCourses;
    }

    // =========================================================================
    // Main — Test Cases
    // =========================================================================
    public static void main(String[] args) {
        System.out.println("=== Course Schedule ===\n");

        System.out.println("Test 1: numCourses=2, [[1,0]]  Expected: true");
        System.out.println("  DFS: " + canFinishDFS(2, new int[][]{{1,0}}));
        System.out.println("  BFS: " + canFinishBFS(2, new int[][]{{1,0}}));

        System.out.println("\nTest 2: numCourses=2, [[1,0],[0,1]]  Expected: false");
        System.out.println("  DFS: " + canFinishDFS(2, new int[][]{{1,0},{0,1}}));
        System.out.println("  BFS: " + canFinishBFS(2, new int[][]{{1,0},{0,1}}));

        System.out.println("\nTest 3: numCourses=4, [[1,0],[2,1],[3,2]]  Expected: true");
        System.out.println("  DFS: " + canFinishDFS(4, new int[][]{{1,0},{2,1},{3,2}}));
        System.out.println("  BFS: " + canFinishBFS(4, new int[][]{{1,0},{2,1},{3,2}}));
    }
}
