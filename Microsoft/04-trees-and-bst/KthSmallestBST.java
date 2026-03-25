/**
 * Problem: Kth Smallest Element in a BST (LeetCode #230)
 * Difficulty: Medium
 * Topics: Tree, DFS, BST
 * Frequently asked at: Microsoft
 *
 * Description:
 *   Given the root of a BST and an integer k, return the kth smallest value
 *   (1-indexed) of all the values in the BST.
 *
 * Approach 1 – Brute Force (Inorder to list)
 *   Full inorder traversal into list, return list[k-1].
 *   TC: O(n) | SC: O(n)
 *
 * Approach 2 – Recursive Early Stop (Optimal)
 *   Count during inorder; stop when count == k.
 *   TC: O(H + k) | SC: O(H)
 *
 * Approach 3 – Iterative Inorder (Best)
 *   Stack-based inorder, return when kth element popped.
 *   TC: O(H + k) | SC: O(H)
 */

import java.util.*;

public class KthSmallestBST {

    static class TreeNode {
        int val;
        TreeNode left, right;
        TreeNode(int val) { this.val = val; }
    }

    // =========================================================================
    // Approach 1: Inorder to List — O(n) time, O(n) space
    // =========================================================================
    public static int kthSmallestBrute(TreeNode root, int k) {
        List<Integer> inorder = new ArrayList<>();
        inorderCollect(root, inorder);
        return inorder.get(k - 1);
    }

    private static void inorderCollect(TreeNode node, List<Integer> list) {
        if (node == null) return;
        inorderCollect(node.left, list);
        list.add(node.val);
        inorderCollect(node.right, list);
    }

    // =========================================================================
    // Approach 2: Recursive Early Stop — O(H + k) time, O(H) space
    // =========================================================================
    static int count, result;

    public static int kthSmallestOptimal(TreeNode root, int k) {
        count = 0;
        result = -1;
        inorderEarlyStop(root, k);
        return result;
    }

    private static void inorderEarlyStop(TreeNode node, int k) {
        if (node == null || count >= k) return;
        inorderEarlyStop(node.left, k);
        count++;
        if (count == k) { result = node.val; return; }
        inorderEarlyStop(node.right, k);
    }

    // =========================================================================
    // Approach 3: Iterative Inorder (Best) — O(H + k) time, O(H) space
    // =========================================================================
    public static int kthSmallestBest(TreeNode root, int k) {
        Stack<TreeNode> stack = new Stack<>();
        TreeNode curr = root;

        while (curr != null || !stack.isEmpty()) {
            while (curr != null) {
                stack.push(curr);
                curr = curr.left;
            }
            curr = stack.pop();
            k--;
            if (k == 0) return curr.val;
            curr = curr.right;
        }
        return -1; // should not reach
    }

    // =========================================================================
    // Main — Test Cases
    // =========================================================================
    public static void main(String[] args) {
        System.out.println("=== Kth Smallest Element in BST ===");

        //        5
        //       / \
        //      3   6
        //     / \
        //    2   4
        //   /
        //  1
        TreeNode root = new TreeNode(5);
        root.left = new TreeNode(3);
        root.right = new TreeNode(6);
        root.left.left = new TreeNode(2);
        root.left.right = new TreeNode(4);
        root.left.left.left = new TreeNode(1);

        int k = 3;
        System.out.println("k = " + k);
        System.out.println("Brute:     " + kthSmallestBrute(root, k));    // 3
        System.out.println("Recursive: " + kthSmallestOptimal(root, k));  // 3
        System.out.println("Iterative: " + kthSmallestBest(root, k));      // 3
    }
}
