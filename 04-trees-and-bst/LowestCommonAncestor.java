/**
 * Problem: Lowest Common Ancestor of a Binary Tree (LeetCode #236)
 * Difficulty: Medium
 * Topics: Tree, DFS
 * Frequently asked at: Microsoft
 *
 * Description:
 *   Given a binary tree, find the lowest common ancestor (LCA) of two given nodes.
 *
 * Approach 1 – Brute Force (Store paths)
 *   Find root-to-node paths for both nodes, compare.
 *   TC: O(n) | SC: O(n)
 *
 * Approach 2 – Recursive (Optimal & Best)
 *   Post-order: if both subtrees return non-null, current node is LCA.
 *   TC: O(n) | SC: O(h)
 *
 * Approach 3 – Iterative with Parent Pointers
 *   BFS to build parent map, then trace ancestors.
 *   TC: O(n) | SC: O(n)
 */

import java.util.*;

public class LowestCommonAncestor {

    static class TreeNode {
        int val;
        TreeNode left, right;
        TreeNode(int val) { this.val = val; }
    }

    // =========================================================================
    // Approach 1: Store Paths — O(n) time, O(n) space
    // =========================================================================
    public static TreeNode lcaBrute(TreeNode root, TreeNode p, TreeNode q) {
        List<TreeNode> pathP = new ArrayList<>(), pathQ = new ArrayList<>();
        findPath(root, p, pathP);
        findPath(root, q, pathQ);

        TreeNode lca = null;
        for (int i = 0; i < Math.min(pathP.size(), pathQ.size()); i++) {
            if (pathP.get(i) == pathQ.get(i)) lca = pathP.get(i);
            else break;
        }
        return lca;
    }

    private static boolean findPath(TreeNode root, TreeNode target, List<TreeNode> path) {
        if (root == null) return false;
        path.add(root);
        if (root == target) return true;
        if (findPath(root.left, target, path) || findPath(root.right, target, path)) return true;
        path.remove(path.size() - 1);
        return false;
    }

    // =========================================================================
    // Approach 2: Recursive (Optimal/Best) — O(n) time, O(h) space
    // =========================================================================
    public static TreeNode lcaBest(TreeNode root, TreeNode p, TreeNode q) {
        if (root == null || root == p || root == q) return root;
        TreeNode left = lcaBest(root.left, p, q);
        TreeNode right = lcaBest(root.right, p, q);
        if (left != null && right != null) return root; // both found → LCA
        return left != null ? left : right;
    }

    // =========================================================================
    // Approach 3: Iterative with Parent Map — O(n) time, O(n) space
    // =========================================================================
    public static TreeNode lcaIterative(TreeNode root, TreeNode p, TreeNode q) {
        Map<TreeNode, TreeNode> parent = new HashMap<>();
        parent.put(root, null);

        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        while (!parent.containsKey(p) || !parent.containsKey(q)) {
            TreeNode node = queue.poll();
            if (node.left != null)  { parent.put(node.left, node);  queue.offer(node.left); }
            if (node.right != null) { parent.put(node.right, node); queue.offer(node.right); }
        }

        Set<TreeNode> ancestors = new HashSet<>();
        while (p != null) { ancestors.add(p); p = parent.get(p); }
        while (!ancestors.contains(q)) q = parent.get(q);
        return q;
    }

    // =========================================================================
    // Main — Test Cases
    // =========================================================================
    public static void main(String[] args) {
        System.out.println("=== Lowest Common Ancestor ===");

        //        3
        //       / \
        //      5   1
        //     / \
        //    6   2
        TreeNode root = new TreeNode(3);
        TreeNode n5 = new TreeNode(5), n1 = new TreeNode(1);
        TreeNode n6 = new TreeNode(6), n2 = new TreeNode(2);
        root.left = n5; root.right = n1;
        n5.left = n6; n5.right = n2;

        System.out.println("LCA(5,1) Path:      " + lcaBrute(root, n5, n1).val);     // 3
        System.out.println("LCA(5,1) Recursive:  " + lcaBest(root, n5, n1).val);      // 3
        System.out.println("LCA(5,1) Iterative:  " + lcaIterative(root, n5, n1).val); // 3

        System.out.println("LCA(5,6) Recursive:  " + lcaBest(root, n5, n6).val);      // 5
    }
}
