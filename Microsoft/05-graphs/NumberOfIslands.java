/**
 * Problem: Number of Islands (LeetCode #200)
 * Difficulty: Medium
 * Topics: Array, DFS, BFS, Union Find
 * Frequently asked at: Microsoft
 *
 * Description:
 *   Given an m x n 2D binary grid which represents a map of '1's (land) and
 *   '0's (water), return the number of islands.
 *
 * Approach 1 – BFS
 *   For each unvisited '1', BFS to mark all connected land.
 *   TC: O(m*n) | SC: O(min(m,n))
 *
 * Approach 2 – DFS (Optimal)
 *   For each unvisited '1', DFS recursively to mark connected land.
 *   TC: O(m*n) | SC: O(m*n) worst case stack
 *
 * Approach 3 – Union Find (Best for follow-ups)
 *   Union adjacent land cells. Count distinct components.
 *   TC: O(m*n * α(m*n)) ≈ O(m*n) | SC: O(m*n)
 */

public class NumberOfIslands {

    // =========================================================================
    // Approach 1: BFS — O(m*n) time
    // =========================================================================
    public static int numIslandsBFS(char[][] grid) {
        int count = 0, m = grid.length, n = grid[0].length;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == '1') {
                    count++;
                    bfs(grid, i, j, m, n);
                }
            }
        }
        return count;
    }

    private static void bfs(char[][] grid, int r, int c, int m, int n) {
        java.util.Queue<int[]> q = new java.util.LinkedList<>();
        q.offer(new int[]{r, c});
        grid[r][c] = '0';
        int[][] dirs = {{0,1},{0,-1},{1,0},{-1,0}};
        while (!q.isEmpty()) {
            int[] cell = q.poll();
            for (int[] d : dirs) {
                int nr = cell[0]+d[0], nc = cell[1]+d[1];
                if (nr >= 0 && nr < m && nc >= 0 && nc < n && grid[nr][nc] == '1') {
                    grid[nr][nc] = '0';
                    q.offer(new int[]{nr, nc});
                }
            }
        }
    }

    // =========================================================================
    // Approach 2: DFS — O(m*n) time
    // =========================================================================
    public static int numIslandsDFS(char[][] grid) {
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
        if (i < 0 || i >= grid.length || j < 0 || j >= grid[0].length || grid[i][j] == '0') return;
        grid[i][j] = '0';
        dfs(grid, i+1, j); dfs(grid, i-1, j);
        dfs(grid, i, j+1); dfs(grid, i, j-1);
    }

    // =========================================================================
    // Approach 3: Union Find — O(m*n * α) time
    // =========================================================================
    public static int numIslandsUF(char[][] grid) {
        int m = grid.length, n = grid[0].length;
        int[] parent = new int[m * n];
        int[] rank = new int[m * n];
        int count = 0;

        for (int i = 0; i < m * n; i++) parent[i] = i;
        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++)
                if (grid[i][j] == '1') count++;

        int[][] dirs = {{0,1},{1,0}};
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == '1') {
                    for (int[] d : dirs) {
                        int ni = i+d[0], nj = j+d[1];
                        if (ni < m && nj < n && grid[ni][nj] == '1') {
                            int a = find(parent, i*n+j), b = find(parent, ni*n+nj);
                            if (a != b) { union(parent, rank, a, b); count--; }
                        }
                    }
                }
            }
        }
        return count;
    }

    private static int find(int[] p, int x) { return p[x] == x ? x : (p[x] = find(p, p[x])); }
    private static void union(int[] p, int[] r, int a, int b) {
        if (r[a] < r[b]) p[a] = b;
        else if (r[a] > r[b]) p[b] = a;
        else { p[b] = a; r[a]++; }
    }

    // =========================================================================
    // Main
    // =========================================================================
    public static void main(String[] args) {
        System.out.println("=== Number of Islands ===");
        char[][] g1 = {
            {'1','1','0','0','0'},
            {'1','1','0','0','0'},
            {'0','0','1','0','0'},
            {'0','0','0','1','1'}
        };
        // Need fresh copies since approaches modify the grid
        System.out.println("BFS: " + numIslandsBFS(copy(g1)));  // 3
        System.out.println("DFS: " + numIslandsDFS(copy(g1)));  // 3
        System.out.println("UF:  " + numIslandsUF(copy(g1)));   // 3
    }

    private static char[][] copy(char[][] g) {
        char[][] c = new char[g.length][];
        for (int i = 0; i < g.length; i++) c[i] = g[i].clone();
        return c;
    }
}
