/**
 * Problem: Course Schedule (LeetCode #207)
 * Difficulty: Medium
 * Topics: DFS, BFS, Graph, Topological Sort
 * Frequently asked at: Microsoft
 *
 * Description:
 *   Given numCourses and prerequisites pairs, determine if you can
 *   finish all courses (i.e., no cycle in the directed graph).
 *
 * Approach 1 – BFS (Kahn's Algorithm / Topological Sort)
 *   Compute in-degrees; process nodes with 0 in-degree.
 *   TC: O(V+E) | SC: O(V+E)
 *
 * Approach 2 – DFS Cycle Detection (Optimal)
 *   Color nodes: WHITE/GRAY/BLACK. Cycle if we visit GRAY.
 *   TC: O(V+E) | SC: O(V+E)
 *
 * Approach 3 – DFS with Memo (Best)
 *   Same as DFS but with explicit visited states for clarity.
 *   TC: O(V+E) | SC: O(V+E)
 */

import java.util.*;

public class CourseSchedule {

    // =========================================================================
    // Approach 1: BFS / Kahn's — O(V+E) time, O(V+E) space
    // =========================================================================
    public static boolean canFinishBFS(int numCourses, int[][] prereqs) {
        List<List<Integer>> adj = new ArrayList<>();
        int[] inDegree = new int[numCourses];
        for (int i = 0; i < numCourses; i++) adj.add(new ArrayList<>());

        for (int[] p : prereqs) {
            adj.get(p[1]).add(p[0]);
            inDegree[p[0]]++;
        }

        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < numCourses; i++) {
            if (inDegree[i] == 0) queue.offer(i);
        }

        int processed = 0;
        while (!queue.isEmpty()) {
            int course = queue.poll();
            processed++;
            for (int next : adj.get(course)) {
                inDegree[next]--;
                if (inDegree[next] == 0) queue.offer(next);
            }
        }
        return processed == numCourses;
    }

    // =========================================================================
    // Approach 2: DFS Cycle Detection — O(V+E) time, O(V+E) space
    // =========================================================================
    public static boolean canFinishDFS(int numCourses, int[][] prereqs) {
        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < numCourses; i++) adj.add(new ArrayList<>());
        for (int[] p : prereqs) adj.get(p[1]).add(p[0]);

        int[] color = new int[numCourses]; // 0=WHITE, 1=GRAY, 2=BLACK
        for (int i = 0; i < numCourses; i++) {
            if (color[i] == 0 && hasCycle(adj, i, color)) return false;
        }
        return true;
    }

    private static boolean hasCycle(List<List<Integer>> adj, int u, int[] color) {
        color[u] = 1; // GRAY - in progress
        for (int v : adj.get(u)) {
            if (color[v] == 1) return true;  // back edge → cycle
            if (color[v] == 0 && hasCycle(adj, v, color)) return true;
        }
        color[u] = 2; // BLACK - done
        return false;
    }

    // =========================================================================
    // Approach 3: DFS with visited/inStack arrays — O(V+E)
    // =========================================================================
    public static boolean canFinishBest(int numCourses, int[][] prereqs) {
        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < numCourses; i++) adj.add(new ArrayList<>());
        for (int[] p : prereqs) adj.get(p[1]).add(p[0]);

        boolean[] visited = new boolean[numCourses];
        boolean[] inStack = new boolean[numCourses];

        for (int i = 0; i < numCourses; i++) {
            if (!visited[i] && dfsCycle(adj, i, visited, inStack)) return false;
        }
        return true;
    }

    private static boolean dfsCycle(List<List<Integer>> adj, int u,
                                     boolean[] visited, boolean[] inStack) {
        visited[u] = true;
        inStack[u] = true;
        for (int v : adj.get(u)) {
            if (inStack[v]) return true;
            if (!visited[v] && dfsCycle(adj, v, visited, inStack)) return true;
        }
        inStack[u] = false;
        return false;
    }

    // =========================================================================
    // Main
    // =========================================================================
    public static void main(String[] args) {
        System.out.println("=== Course Schedule ===");

        int[][] p1 = {{1,0}};
        System.out.println("2 courses, [[1,0]]:");
        System.out.println("BFS: " + canFinishBFS(2, p1));   // true
        System.out.println("DFS: " + canFinishDFS(2, p1));   // true
        System.out.println("Best:" + canFinishBest(2, p1));   // true

        int[][] p2 = {{1,0},{0,1}};
        System.out.println("\n2 courses, [[1,0],[0,1]]:");
        System.out.println("BFS: " + canFinishBFS(2, p2));   // false
        System.out.println("DFS: " + canFinishDFS(2, p2));   // false
        System.out.println("Best:" + canFinishBest(2, p2));   // false
    }
}
