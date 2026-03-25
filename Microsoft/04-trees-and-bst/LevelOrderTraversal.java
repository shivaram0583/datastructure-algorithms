/**
 * Problem: Binary Tree Level Order Traversal (LeetCode #102)
 * Difficulty: Medium
 * Topics: Tree, BFS
 * Frequently asked at: Microsoft
 *
 * Description:
 *   Given the root of a binary tree, return the level order traversal
 *   (i.e., from left to right, level by level).
 *
 * Approach 1 – Brute Force (DFS with level tracking)
 *   DFS, pass depth and insert into list-of-lists by index.
 *   TC: O(n) | SC: O(n)
 *
 * Approach 2 – BFS Queue (Optimal/Best)
 *   Classic BFS with level-size loop. TC: O(n) | SC: O(n)
 *
 * Approach 3 – Iterative DFS with Stack
 *   Stack-based DFS storing (node, depth) pairs.
 *   TC: O(n) | SC: O(n)
 */

import java.util.*;

public class LevelOrderTraversal {

    static class TreeNode {
        int val;
        TreeNode left, right;
        TreeNode(int val) { this.val = val; }
    }

    // =========================================================================
    // Approach 1: DFS with Level — O(n) time, O(n) space
    // =========================================================================
    public static List<List<Integer>> levelOrderDFS(TreeNode root) {
        List<List<Integer>> result = new ArrayList<>();
        dfs(root, 0, result);
        return result;
    }

    private static void dfs(TreeNode node, int level, List<List<Integer>> result) {
        if (node == null) return;
        if (level == result.size()) result.add(new ArrayList<>());
        result.get(level).add(node.val);
        dfs(node.left, level + 1, result);
        dfs(node.right, level + 1, result);
    }

    // =========================================================================
    // Approach 2: BFS Queue (Optimal/Best) — O(n) time, O(n) space
    // =========================================================================
    public static List<List<Integer>> levelOrderBFS(TreeNode root) {
        List<List<Integer>> result = new ArrayList<>();
        if (root == null) return result;

        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);

        while (!queue.isEmpty()) {
            int size = queue.size();
            List<Integer> level = new ArrayList<>();
            for (int i = 0; i < size; i++) {
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
    // Approach 3: Iterative DFS with Stack — O(n) time, O(n) space
    // =========================================================================
    public static List<List<Integer>> levelOrderStack(TreeNode root) {
        List<List<Integer>> result = new ArrayList<>();
        if (root == null) return result;

        Stack<Object[]> stack = new Stack<>(); // {TreeNode, depth}
        stack.push(new Object[]{root, 0});

        while (!stack.isEmpty()) {
            Object[] pair = stack.pop();
            TreeNode node = (TreeNode) pair[0];
            int depth = (int) pair[1];

            if (depth == result.size()) result.add(new ArrayList<>());
            result.get(depth).add(node.val);

            // Push right first so left is processed first
            if (node.right != null) stack.push(new Object[]{node.right, depth + 1});
            if (node.left != null)  stack.push(new Object[]{node.left, depth + 1});
        }
        return result;
    }

    // =========================================================================
    // Main — Test Cases
    // =========================================================================
    public static void main(String[] args) {
        System.out.println("=== Binary Tree Level Order Traversal ===");

        //      3
        //     / \
        //    9  20
        //      /  \
        //     15   7
        TreeNode root = new TreeNode(3);
        root.left = new TreeNode(9);
        root.right = new TreeNode(20);
        root.right.left = new TreeNode(15);
        root.right.right = new TreeNode(7);

        System.out.println("DFS:   " + levelOrderDFS(root));   // [[3],[9,20],[15,7]]
        System.out.println("BFS:   " + levelOrderBFS(root));   // [[3],[9,20],[15,7]]
        System.out.println("Stack: " + levelOrderStack(root)); // [[3],[9,20],[15,7]]
    }
}
