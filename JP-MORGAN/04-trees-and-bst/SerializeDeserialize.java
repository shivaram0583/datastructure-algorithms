/**
 * Problem: Serialize and Deserialize Binary Tree (LeetCode #297)
 * Difficulty: Hard
 * Topics: Tree, BFS, DFS, Design
 * Frequently asked at: JP Morgan Chase (Round 1)
 *
 * Description:
 *   Design an algorithm to serialize a binary tree to a string,
 *   and deserialize that string back to the original tree.
 *   There is no restriction on your algorithm.
 *
 * Example:
 *       1
 *      / \
 *     2   3
 *        / \
 *       4   5
 *   Serialized: "1,2,null,null,3,4,null,null,5,null,null"  (preorder)
 *
 * JP Morgan Context:
 *   Models storing and reconstructing financial account trees or
 *   org charts to/from a database or message queue.
 *
 * ============================================================
 * Approach 1 — BFS (Level Order) Serialization
 * ============================================================
 *   Serialize: BFS, output each node val or "null".
 *   Deserialize: BFS, use queue to assign children level by level.
 *
 *   Format: "1,2,3,null,null,4,5"
 *   TC: O(n)  SC: O(n)
 *
 * ============================================================
 * Approach 2 — DFS (Preorder) Serialization (Optimal Clarity)
 * ============================================================
 *   Serialize: preorder DFS (root, left, right). Null → "#".
 *   Deserialize: preorder DFS with a global index pointer.
 *
 *   Step-by-step serialize:
 *     visit 1 → "1"
 *     visit 2 → "1,2"
 *     visit null (2.left) → "1,2,#"
 *     visit null (2.right) → "1,2,#,#"
 *     visit 3 → "1,2,#,#,3"
 *     visit 4 → "1,2,#,#,3,4"
 *     visit null, null → "1,2,#,#,3,4,#,#"
 *     visit 5, null, null → "1,2,#,#,3,4,#,#,5,#,#"
 *
 *   Step-by-step deserialize:
 *     tokens = [1,2,#,#,3,4,#,#,5,#,#]
 *     idx=0: create node(1)
 *       idx=1: create node(2)
 *         idx=2: #, return null (2.left=null)
 *         idx=3: #, return null (2.right=null)
 *       1.left = node(2)
 *       idx=4: create node(3)
 *         idx=5: create node(4), ...
 *       1.right = node(3)
 *
 *   TC: O(n)  SC: O(n)
 */

import java.util.*;

public class SerializeDeserialize {

    static class TreeNode {
        int val;
        TreeNode left, right;
        TreeNode(int val) { this.val = val; }
    }

    // =========================================================================
    // Approach 1: BFS Serialization — O(n) time, O(n) space
    // =========================================================================
    static class BFSCodec {
        public String serialize(TreeNode root) {
            if (root == null) return "";
            StringBuilder sb = new StringBuilder();
            Queue<TreeNode> queue = new LinkedList<>();
            queue.offer(root);
            while (!queue.isEmpty()) {
                TreeNode node = queue.poll();
                if (node == null) {
                    sb.append("null,");
                } else {
                    sb.append(node.val).append(",");
                    queue.offer(node.left);
                    queue.offer(node.right);
                }
            }
            return sb.toString();
        }

        public TreeNode deserialize(String data) {
            if (data.isEmpty()) return null;
            String[] tokens = data.split(",");
            TreeNode root = new TreeNode(Integer.parseInt(tokens[0]));
            Queue<TreeNode> queue = new LinkedList<>();
            queue.offer(root);
            int i = 1;
            while (!queue.isEmpty() && i < tokens.length) {
                TreeNode node = queue.poll();
                if (!tokens[i].equals("null")) {
                    node.left = new TreeNode(Integer.parseInt(tokens[i]));
                    queue.offer(node.left);
                }
                i++;
                if (i < tokens.length && !tokens[i].equals("null")) {
                    node.right = new TreeNode(Integer.parseInt(tokens[i]));
                    queue.offer(node.right);
                }
                i++;
            }
            return root;
        }
    }

    // =========================================================================
    // Approach 2: DFS Preorder — O(n) time, O(n) space
    // =========================================================================
    static class DFSCodec {
        public String serialize(TreeNode root) {
            StringBuilder sb = new StringBuilder();
            serializeDFS(root, sb);
            return sb.toString();
        }

        private void serializeDFS(TreeNode node, StringBuilder sb) {
            if (node == null) { sb.append("#,"); return; }
            sb.append(node.val).append(",");
            serializeDFS(node.left, sb);
            serializeDFS(node.right, sb);
        }

        private int idx = 0;

        public TreeNode deserialize(String data) {
            idx = 0;
            String[] tokens = data.split(",");
            return deserializeDFS(tokens);
        }

        private TreeNode deserializeDFS(String[] tokens) {
            if (idx >= tokens.length || tokens[idx].equals("#")) {
                idx++;
                return null;
            }
            TreeNode node = new TreeNode(Integer.parseInt(tokens[idx++]));
            node.left  = deserializeDFS(tokens);
            node.right = deserializeDFS(tokens);
            return node;
        }
    }

    // =========================================================================
    // Helper: Inorder to verify reconstruction
    // =========================================================================
    public static List<Integer> inorder(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        inorderHelper(root, result);
        return result;
    }

    private static void inorderHelper(TreeNode node, List<Integer> result) {
        if (node == null) return;
        inorderHelper(node.left, result);
        result.add(node.val);
        inorderHelper(node.right, result);
    }

    // =========================================================================
    // Main — Test Cases
    // =========================================================================
    public static void main(String[] args) {
        System.out.println("=== Serialize and Deserialize Binary Tree ===\n");

        TreeNode root = new TreeNode(1);
        root.left = new TreeNode(2);
        root.right = new TreeNode(3);
        root.right.left = new TreeNode(4);
        root.right.right = new TreeNode(5);

        BFSCodec bfs = new BFSCodec();
        String bfsSerialized = bfs.serialize(root);
        System.out.println("BFS Serialized:   " + bfsSerialized);
        TreeNode bfsRoot = bfs.deserialize(bfsSerialized);
        System.out.println("BFS Inorder:      " + inorder(bfsRoot));

        DFSCodec dfs = new DFSCodec();
        String dfsSerialized = dfs.serialize(root);
        System.out.println("\nDFS Serialized:   " + dfsSerialized);
        TreeNode dfsRoot = dfs.deserialize(dfsSerialized);
        System.out.println("DFS Inorder:      " + inorder(dfsRoot));

        System.out.println("\nNull tree:");
        System.out.println("BFS: " + bfs.serialize(null));
        System.out.println("DFS: " + dfs.serialize(null));
    }
}
