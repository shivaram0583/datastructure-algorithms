/**
 * Problem: Validate Binary Search Tree (LeetCode #98)
 * Difficulty: Medium
 * Topics: Tree, DFS, BST
 * Frequently asked at: Microsoft
 *
 * Description:
 *   Given the root of a binary tree, determine if it is a valid BST.
 *
 * Approach 1 – Brute Force (Inorder → check sorted)
 *   Collect inorder traversal, verify strictly ascending.
 *   TC: O(n) | SC: O(n)
 *
 * Approach 2 – Recursive with Range (Optimal)
 *   Pass valid (min, max) range down. TC: O(n) | SC: O(h)
 *
 * Approach 3 – Iterative Inorder (Best)
 *   Inorder using a stack; check each node > previous.
 *   TC: O(n) | SC: O(h)
 */

import java.util.*;

public class ValidateBST {

    static class TreeNode {
        int val;
        TreeNode left, right;
        TreeNode(int val) { this.val = val; }
    }

    // =========================================================================
    // Approach 1: Inorder + Check Sorted — O(n) time, O(n) space
    // =========================================================================
    public static boolean isValidBrute(TreeNode root) {
        List<Integer> inorder = new ArrayList<>();
        inorderCollect(root, inorder);
        for (int i = 1; i < inorder.size(); i++) {
            if (inorder.get(i) <= inorder.get(i - 1)) return false;
        }
        return true;
    }

    private static void inorderCollect(TreeNode node, List<Integer> list) {
        if (node == null) return;
        inorderCollect(node.left, list);
        list.add(node.val);
        inorderCollect(node.right, list);
    }

    // =========================================================================
    // Approach 2: Recursive with Range — O(n) time, O(h) space
    // =========================================================================
    public static boolean isValidOptimal(TreeNode root) {
        return validate(root, Long.MIN_VALUE, Long.MAX_VALUE);
    }

    private static boolean validate(TreeNode node, long min, long max) {
        if (node == null) return true;
        if (node.val <= min || node.val >= max) return false;
        return validate(node.left, min, node.val) &&
               validate(node.right, node.val, max);
    }

    // =========================================================================
    // Approach 3: Iterative Inorder (Best) — O(n) time, O(h) space
    // =========================================================================
    public static boolean isValidBest(TreeNode root) {
        Stack<TreeNode> stack = new Stack<>();
        long prev = Long.MIN_VALUE;
        TreeNode curr = root;

        while (curr != null || !stack.isEmpty()) {
            while (curr != null) {
                stack.push(curr);
                curr = curr.left;
            }
            curr = stack.pop();
            if (curr.val <= prev) return false;
            prev = curr.val;
            curr = curr.right;
        }
        return true;
    }

    // =========================================================================
    // Main — Test Cases
    // =========================================================================
    public static void main(String[] args) {
        System.out.println("=== Validate BST ===");

        // Valid BST: [2, 1, 3]
        TreeNode valid = new TreeNode(2);
        valid.left = new TreeNode(1);
        valid.right = new TreeNode(3);

        System.out.println("Tree [2,1,3] (valid BST):");
        System.out.println("Brute:     " + isValidBrute(valid));    // true
        System.out.println("Recursive: " + isValidOptimal(valid));  // true
        System.out.println("Iterative: " + isValidBest(valid));      // true

        // Invalid BST: [5, 1, 4, null, null, 3, 6]
        TreeNode invalid = new TreeNode(5);
        invalid.left = new TreeNode(1);
        invalid.right = new TreeNode(4);
        invalid.right.left = new TreeNode(3);
        invalid.right.right = new TreeNode(6);

        System.out.println("\nTree [5,1,4,null,null,3,6] (invalid BST):");
        System.out.println("Brute:     " + isValidBrute(invalid));    // false
        System.out.println("Recursive: " + isValidOptimal(invalid));  // false
        System.out.println("Iterative: " + isValidBest(invalid));      // false
    }
}
