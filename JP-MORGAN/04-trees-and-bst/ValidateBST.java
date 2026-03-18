/**
 * Problem: Validate Binary Search Tree (LeetCode #98)
 * Difficulty: Medium
 * Topics: Tree, DFS, BST
 * Frequently asked at: JP Morgan Chase (Round 1)
 *
 * Description:
 *   Given the root of a binary tree, determine if it is a valid BST.
 *   A valid BST: left subtree has only nodes with keys < root.key;
 *   right subtree has only nodes with keys > root.key;
 *   both subtrees are also valid BSTs.
 *
 * Example 1:
 *       2          → valid BST
 *      / \
 *     1   3
 *
 * Example 2:
 *       5          → INVALID (3 is in right subtree of 5, should be > 5)
 *      / \
 *     1   4
 *        / \
 *       3   6
 *
 * Constraints:
 *   -2^31 <= Node.val <= 2^31 - 1
 *
 * JP Morgan Context:
 *   BSTs model sorted financial instrument lookups (e.g., order price tree).
 *   Common trap: checking only parent-child pairs misses global constraints.
 *
 * ============================================================
 * Approach 1 — Wrong: Check Only Parent-Child (Common Mistake)
 * ============================================================
 *   Only checking left.val < root.val < right.val is WRONG.
 *   Counter-example: 5 → {1, {4,3,6}} — 3 passes local check at node 4
 *   but 3 < 5 violates the global BST property.
 *
 * ============================================================
 * Approach 2 — Inorder Traversal
 * ============================================================
 *   An inorder traversal of a valid BST produces a strictly increasing sequence.
 *   Do inorder and check if each element > previous.
 *
 *   TC: O(n)  SC: O(n) (for the list) or O(h) (iterative with stack)
 *
 * ============================================================
 * Approach 3 — Bounds Checking / Min-Max (Optimal)
 * ============================================================
 *   Pass allowed range [min, max] down the recursion.
 *   Root has range (-∞, +∞).
 *   Going left: max narrows to parent.val.
 *   Going right: min widens to parent.val.
 *
 *   Step-by-step (tree above, root=5):
 *     validate(5, -∞, +∞): 5 in range ✓
 *       validate(1, -∞, 5):  1 in range ✓
 *         validate(null) → true
 *       validate(4, 5, +∞):  4 < 5 → INVALID! return false
 *
 *   TC: O(n)  SC: O(h) recursion stack
 */

import java.util.*;

public class ValidateBST {

    static class TreeNode {
        int val;
        TreeNode left, right;
        TreeNode(int val) { this.val = val; }
    }

    // =========================================================================
    // Approach 2: Inorder Traversal — O(n) time, O(n) space
    // =========================================================================
    public static boolean isValidBSTInorder(TreeNode root) {
        List<Integer> inorder = new ArrayList<>();
        inorderTraversal(root, inorder);
        for (int i = 1; i < inorder.size(); i++) {
            if (inorder.get(i) <= inorder.get(i - 1)) return false;
        }
        return true;
    }

    private static void inorderTraversal(TreeNode node, List<Integer> list) {
        if (node == null) return;
        inorderTraversal(node.left, list);
        list.add(node.val);
        inorderTraversal(node.right, list);
    }

    // =========================================================================
    // Approach 2b: Inorder with running prev (no extra list) — O(n), O(h)
    // =========================================================================
    private static long prev = Long.MIN_VALUE;

    public static boolean isValidBSTInorderOptimal(TreeNode root) {
        prev = Long.MIN_VALUE;
        return inorderCheck(root);
    }

    private static boolean inorderCheck(TreeNode node) {
        if (node == null) return true;
        if (!inorderCheck(node.left)) return false;
        if (node.val <= prev) return false;
        prev = node.val;
        return inorderCheck(node.right);
    }

    // =========================================================================
    // Approach 3: Bounds Checking (Optimal) — O(n) time, O(h) space
    // =========================================================================
    public static boolean isValidBSTBounds(TreeNode root) {
        return validate(root, Long.MIN_VALUE, Long.MAX_VALUE);
    }

    private static boolean validate(TreeNode node, long min, long max) {
        if (node == null) return true;
        if (node.val <= min || node.val >= max) return false;
        return validate(node.left, min, node.val) &&
               validate(node.right, node.val, max);
    }

    // =========================================================================
    // Main — Test Cases
    // =========================================================================
    public static void main(String[] args) {
        System.out.println("=== Validate Binary Search Tree ===\n");

        TreeNode valid = new TreeNode(2);
        valid.left = new TreeNode(1);
        valid.right = new TreeNode(3);
        System.out.println("Test 1: [2,1,3]  Expected: true");
        System.out.println("  Inorder: " + isValidBSTInorder(valid));
        System.out.println("  Bounds:  " + isValidBSTBounds(valid));

        TreeNode invalid = new TreeNode(5);
        invalid.left = new TreeNode(1);
        invalid.right = new TreeNode(4);
        invalid.right.left = new TreeNode(3);
        invalid.right.right = new TreeNode(6);
        System.out.println("\nTest 2: [5,1,4,null,null,3,6]  Expected: false");
        System.out.println("  Inorder: " + isValidBSTInorder(invalid));
        System.out.println("  Bounds:  " + isValidBSTBounds(invalid));

        TreeNode edgeCase = new TreeNode(Integer.MAX_VALUE);
        System.out.println("\nTest 3: [MAX_INT]  Expected: true");
        System.out.println("  Bounds:  " + isValidBSTBounds(edgeCase));
    }
}
