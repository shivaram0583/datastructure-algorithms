/**
 * Problem: Binary Tree Level Order Traversal (LeetCode #102)
 * Difficulty: Medium
 * Topics: Tree, BFS, Queue
 * Frequently asked at: JP Morgan Chase (Round 1)
 *
 * Description:
 *   Given root of a binary tree, return the level order traversal
 *   of its nodes' values (i.e., left to right, level by level).
 *
 * Example:
 *       3
 *      / \
 *     9  20
 *       /  \
 *      15   7
 *   Output: [[3],[9,20],[15,7]]
 *
 * Constraints:
 *   0 <= number of nodes <= 2000
 *   -1000 <= Node.val <= 1000
 *
 * JP Morgan Context:
 *   BFS/level-order is the most common tree traversal asked.
 *   Follow-ups: "Print in zigzag order" (#103), "Right side view" (#199).
 *
 * ============================================================
 * Approach 1 — BFS with Queue (Optimal)
 * ============================================================
 *   Use a queue. At each level, process all nodes currently in queue.
 *   Add their children to queue for the next level.
 *
 *   Step-by-step (tree above):
 *     Queue=[3]
 *     Level 1: process 3 → add 9,20 → Queue=[9,20] → result=[[3]]
 *     Level 2: process 9 (no children), 20 → add 15,7 → Queue=[15,7]
 *              result=[[3],[9,20]]
 *     Level 3: process 15,7 → no children → Queue=[]
 *              result=[[3],[9,20],[15,7]]
 *
 *   TC: O(n)  SC: O(n) — queue holds at most n/2 nodes (last level)
 *
 * ============================================================
 * Approach 2 — DFS / Recursive
 * ============================================================
 *   DFS with a level parameter. Add node value to result[level].
 *   TC: O(n)  SC: O(h) recursion stack (h = tree height)
 */

import java.util.*;

public class BinaryTreeLevelOrder {

    static class TreeNode {
        int val;
        TreeNode left, right;
        TreeNode(int val) { this.val = val; }
    }

    // =========================================================================
    // Approach 1: BFS with Queue — O(n) time, O(n) space
    // =========================================================================
    public static List<List<Integer>> levelOrderBFS(TreeNode root) {
        List<List<Integer>> result = new ArrayList<>();
        if (root == null) return result;

        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);

        while (!queue.isEmpty()) {
            int levelSize = queue.size();
            List<Integer> level = new ArrayList<>();

            for (int i = 0; i < levelSize; i++) {
                TreeNode node = queue.poll();
                level.add(node.val);
                if (node.left != null)  queue.offer(node.left);
                if (node.right != null) queue.offer(node.right);
            }
            result.add(level);
        }
        return result;
    }

    // =========================================================================
    // Approach 2: DFS Recursive — O(n) time, O(h) space
    // =========================================================================
    public static List<List<Integer>> levelOrderDFS(TreeNode root) {
        List<List<Integer>> result = new ArrayList<>();
        dfs(root, 0, result);
        return result;
    }

    private static void dfs(TreeNode node, int level, List<List<Integer>> result) {
        if (node == null) return;
        if (result.size() == level) result.add(new ArrayList<>());
        result.get(level).add(node.val);
        dfs(node.left, level + 1, result);
        dfs(node.right, level + 1, result);
    }

    // =========================================================================
    // Helper: Build tree
    // =========================================================================
    public static TreeNode build() {
        TreeNode root = new TreeNode(3);
        root.left = new TreeNode(9);
        root.right = new TreeNode(20);
        root.right.left = new TreeNode(15);
        root.right.right = new TreeNode(7);
        return root;
    }

    // =========================================================================
    // Main — Test Cases
    // =========================================================================
    public static void main(String[] args) {
        System.out.println("=== Binary Tree Level Order Traversal ===\n");

        TreeNode root = build();
        System.out.println("Tree: 3, children: 9 and 20; 20's children: 15,7");
        System.out.println("  BFS: " + levelOrderBFS(root));
        System.out.println("  DFS: " + levelOrderDFS(root));

        System.out.println("\nNull tree:");
        System.out.println("  BFS: " + levelOrderBFS(null));

        TreeNode single = new TreeNode(1);
        System.out.println("\nSingle node [1]:");
        System.out.println("  BFS: " + levelOrderBFS(single));
    }
}
