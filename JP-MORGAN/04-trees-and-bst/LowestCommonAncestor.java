/**
 * Problem: Lowest Common Ancestor of a Binary Tree (LeetCode #236)
 * Difficulty: Medium
 * Topics: Tree, DFS, Recursion
 * Frequently asked at: JP Morgan Chase (Round 1)
 *
 * Description:
 *   Given a binary tree, find the lowest common ancestor (LCA) of
 *   two given nodes p and q.
 *   LCA: the lowest node in the tree that has both p and q as descendants
 *   (a node can be a descendant of itself).
 *
 * Example:
 *           3
 *          / \
 *         5   1
 *        / \ / \
 *       6  2 0  8
 *         / \
 *        7   4
 *   LCA(5,1) = 3,  LCA(5,4) = 5
 *
 * JP Morgan Context:
 *   Models finding a common authority in org charts or account hierarchies.
 *
 * ============================================================
 * Approach 1 — Parent Pointer + HashSet
 * ============================================================
 *   BFS/DFS to build a parent map for all nodes.
 *   Traverse ancestors of p using parent map → store in a HashSet.
 *   Traverse ancestors of q until one is in the set → that's the LCA.
 *
 *   TC: O(n)  SC: O(n)
 *
 * ============================================================
 * Approach 2 — Recursive DFS (Optimal)
 * ============================================================
 *   If current node is null, p, or q → return current node.
 *   Recurse left and right.
 *   If both sides return non-null → current node is LCA.
 *   If only one side returns non-null → propagate that result up.
 *
 *   Step-by-step (LCA of 5 and 4):
 *     dfs(3): recurse left(5) and right(1)
 *       dfs(5): returns 5 (p found, stop searching this subtree)
 *       dfs(1): left=null, right=null → returns null
 *     left=5, right=null → return 5 (LCA is 5)
 *
 *   Step-by-step (LCA of 5 and 1):
 *     dfs(3): recurse left(5) and right(1)
 *       dfs(5): returns 5
 *       dfs(1): returns 1
 *     left=5, right=1 → both non-null → return 3 (LCA is 3)
 *
 *   TC: O(n)  SC: O(h) recursion stack
 */

import java.util.*;

public class LowestCommonAncestor {

    static class TreeNode {
        int val;
        TreeNode left, right;
        TreeNode(int val) { this.val = val; }
    }

    // =========================================================================
    // Approach 1: Parent Pointer + HashSet — O(n) time, O(n) space
    // =========================================================================
    public static TreeNode lcaParentMap(TreeNode root, TreeNode p, TreeNode q) {
        Map<TreeNode, TreeNode> parent = new HashMap<>();
        Deque<TreeNode> stack = new ArrayDeque<>();
        parent.put(root, null);
        stack.push(root);

        while (!parent.containsKey(p) || !parent.containsKey(q)) {
            TreeNode node = stack.pop();
            if (node.left != null) {
                parent.put(node.left, node);
                stack.push(node.left);
            }
            if (node.right != null) {
                parent.put(node.right, node);
                stack.push(node.right);
            }
        }

        Set<TreeNode> ancestors = new HashSet<>();
        while (p != null) {
            ancestors.add(p);
            p = parent.get(p);
        }
        while (!ancestors.contains(q)) {
            q = parent.get(q);
        }
        return q;
    }

    // =========================================================================
    // Approach 2: Recursive DFS (Optimal) — O(n) time, O(h) space
    // =========================================================================
    public static TreeNode lcaRecursive(TreeNode root, TreeNode p, TreeNode q) {
        if (root == null || root == p || root == q) return root;

        TreeNode left  = lcaRecursive(root.left,  p, q);
        TreeNode right = lcaRecursive(root.right, p, q);

        if (left != null && right != null) return root;
        return (left != null) ? left : right;
    }

    // =========================================================================
    // Main — Test Cases
    // =========================================================================
    public static void main(String[] args) {
        System.out.println("=== Lowest Common Ancestor ===\n");

        TreeNode root = new TreeNode(3);
        TreeNode n5 = new TreeNode(5);
        TreeNode n1 = new TreeNode(1);
        TreeNode n6 = new TreeNode(6);
        TreeNode n2 = new TreeNode(2);
        TreeNode n0 = new TreeNode(0);
        TreeNode n8 = new TreeNode(8);
        TreeNode n7 = new TreeNode(7);
        TreeNode n4 = new TreeNode(4);

        root.left = n5; root.right = n1;
        n5.left = n6; n5.right = n2;
        n1.left = n0; n1.right = n8;
        n2.left = n7; n2.right = n4;

        System.out.println("LCA(5, 1)  Expected: 3");
        System.out.println("  Recursive:   " + lcaRecursive(root, n5, n1).val);
        System.out.println("  ParentMap:   " + lcaParentMap(root, n5, n1).val);

        System.out.println("\nLCA(5, 4)  Expected: 5");
        System.out.println("  Recursive:   " + lcaRecursive(root, n5, n4).val);

        System.out.println("\nLCA(6, 4)  Expected: 5");
        System.out.println("  Recursive:   " + lcaRecursive(root, n6, n4).val);
    }
}
