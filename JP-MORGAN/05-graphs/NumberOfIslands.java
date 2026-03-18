/**
 * Problem: Number of Islands (LeetCode #200)
 * Difficulty: Medium
 * Topics: Graph, DFS, BFS, Union-Find
 * Frequently asked at: JP Morgan Chase (Round 1)
 *
 * Description:
 *   Given a 2D grid of '1's (land) and '0's (water), count the number
 *   of islands. An island is surrounded by water and formed by connecting
 *   adjacent lands horizontally or vertically.
 *
 * Example:
 *   grid = [
 *     ["1","1","0","0","0"],
 *     ["1","1","0","0","0"],
 *     ["0","0","1","0","0"],
 *     ["0","0","0","1","1"]
 *   ]
 *   Output: 3
 *
 * Constraints:
 *   1 <= m, n <= 300
 *   grid[i][j] is '0' or '1'
 *
 * JP Morgan Context:
 *   Models connected component analysis (e.g., connected accounts in a
 *   fraud detection system, connected bank branches in a network).
 *
 * ============================================================
 * Approach 1 — DFS (Mark and Flood)
 * ============================================================
 *   For every unvisited '1', increment count, then DFS to mark
 *   all connected '1's as visited (flood with '0' or a visited marker).
 *
 *   Step-by-step (simplified 3x3):
 *     grid = [1,1,0; 0,1,0; 0,0,1]
 *     (0,0)=1 → count=1, DFS floods (0,0),(0,1),(1,1) to '0'
 *     (2,2)=1 → count=2, DFS floods (2,2)
 *   Result: 2
 *
 *   TC: O(m*n)  SC: O(m*n) recursion stack worst case
 *
 * ============================================================
 * Approach 2 — BFS
 * ============================================================
 *   Same idea but use a queue for BFS expansion.
 *   TC: O(m*n)  SC: O(min(m,n)) — queue size bounded by grid diagonal
 *
 * ============================================================
 * Approach 3 — Union-Find (Disjoint Set Union)
 * ============================================================
 *   Initialize each '1' cell as its own component.
 *   For each '1', union it with adjacent '1's.
 *   Count distinct components at the end.
 *   TC: O(m*n * α(m*n)) ≈ O(m*n)  SC: O(m*n)
 */

import java.util.LinkedList;
import java.util.Queue;

public class NumberOfIslands {

    // =========================================================================
    // Approach 1: DFS — O(m*n) time, O(m*n) space
    // =========================================================================
    public static int numIslandsDFS(char[][] grid) {
        if (grid == null || grid.length == 0) return 0;
        int count = 0;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == '1') {
                    count++;
                    dfs(grid, i, j);
                }
            }
        }
        return count;
    }

    private static void dfs(char[][] grid, int i, int j) {
        if (i < 0 || i >= grid.length || j < 0 || j >= grid[0].length || grid[i][j] != '1') return;
        grid[i][j] = '0';
        dfs(grid, i + 1, j);
        dfs(grid, i - 1, j);
        dfs(grid, i, j + 1);
        dfs(grid, i, j - 1);
    }

    // =========================================================================
    // Approach 2: BFS — O(m*n) time, O(min(m,n)) space
    // =========================================================================
    public static int numIslandsBFS(char[][] grid) {
        if (grid == null || grid.length == 0) return 0;
        int rows = grid.length, cols = grid[0].length;
        int count = 0;
        int[][] dirs = {{1,0},{-1,0},{0,1},{0,-1}};

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (grid[i][j] == '1') {
                    count++;
                    Queue<int[]> queue = new LinkedList<>();
                    queue.offer(new int[]{i, j});
                    grid[i][j] = '0';
                    while (!queue.isEmpty()) {
                        int[] cell = queue.poll();
                        for (int[] d : dirs) {
                            int ni = cell[0] + d[0], nj = cell[1] + d[1];
                            if (ni >= 0 && ni < rows && nj >= 0 && nj < cols && grid[ni][nj] == '1') {
                                queue.offer(new int[]{ni, nj});
                                grid[ni][nj] = '0';
                            }
                        }
                    }
                }
            }
        }
        return count;
    }

    // =========================================================================
    // Approach 3: Union-Find — O(m*n) time, O(m*n) space
    // =========================================================================
    static class UnionFind {
        int[] parent, rank;
        int count;

        UnionFind(char[][] grid) {
            int m = grid.length, n = grid[0].length;
            parent = new int[m * n];
            rank = new int[m * n];
            count = 0;
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    if (grid[i][j] == '1') {
                        parent[i * n + j] = i * n + j;
                        count++;
                    }
                }
            }
        }

        int find(int x) {
            if (parent[x] != x) parent[x] = find(parent[x]);
            return parent[x];
        }

        void union(int x, int y) {
            int rx = find(x), ry = find(y);
            if (rx == ry) return;
            if (rank[rx] < rank[ry]) parent[rx] = ry;
            else if (rank[rx] > rank[ry]) parent[ry] = rx;
            else { parent[ry] = rx; rank[rx]++; }
            count--;
        }
    }

    public static int numIslandsUF(char[][] grid) {
        if (grid == null || grid.length == 0) return 0;
        int rows = grid.length, cols = grid[0].length;
        UnionFind uf = new UnionFind(grid);
        int[][] dirs = {{1,0},{0,1}};

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (grid[i][j] == '1') {
                    grid[i][j] = '0';
                    for (int[] d : dirs) {
                        int ni = i + d[0], nj = j + d[1];
                        if (ni < rows && nj < cols && grid[ni][nj] == '1') {
                            uf.union(i * cols + j, ni * cols + nj);
                        }
                    }
                }
            }
        }
        return uf.count;
    }

    // =========================================================================
    // Main — Test Cases
    // =========================================================================
    public static void main(String[] args) {
        System.out.println("=== Number of Islands ===\n");

        char[][] g1 = {
            {'1','1','0','0','0'},
            {'1','1','0','0','0'},
            {'0','0','1','0','0'},
            {'0','0','0','1','1'}
        };
        char[][] g1b = {
            {'1','1','0','0','0'},
            {'1','1','0','0','0'},
            {'0','0','1','0','0'},
            {'0','0','0','1','1'}
        };
        char[][] g1c = {
            {'1','1','0','0','0'},
            {'1','1','0','0','0'},
            {'0','0','1','0','0'},
            {'0','0','0','1','1'}
        };
        System.out.println("Test 1:  Expected: 3");
        System.out.println("  DFS: " + numIslandsDFS(g1));
        System.out.println("  BFS: " + numIslandsBFS(g1b));
        System.out.println("  UF:  " + numIslandsUF(g1c));

        char[][] g2 = {
            {'1','1','1','1','0'},
            {'1','1','0','1','0'},
            {'1','1','0','0','0'},
            {'0','0','0','0','0'}
        };
        System.out.println("\nTest 2:  Expected: 1");
        System.out.println("  DFS: " + numIslandsDFS(g2));
    }
}
