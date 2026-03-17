/**
 * Problem: Serialize and Deserialize Binary Tree (LeetCode #297)
 * Difficulty: Hard
 * Topics: Tree, DFS, BFS, Design
 * Frequently asked at: Microsoft
 *
 * Description:
 *   Design an algorithm to serialize a binary tree to a string and
 *   deserialize it back to the original tree structure.
 *
 * Approach 1 – BFS (Level Order)
 *   Serialize using BFS; null nodes as "null". TC: O(n) | SC: O(n)
 *
 * Approach 2 – Preorder DFS (Optimal/Best)
 *   Preorder with "null" markers. TC: O(n) | SC: O(n)
 *
 * Approach 3 – Postorder DFS
 *   Postorder with "null" markers, deserialize in reverse.
 *   TC: O(n) | SC: O(n)
 */

import java.util.*;

public class SerializeDeserializeBT {

    static class TreeNode {
        int val;
        TreeNode left, right;
        TreeNode(int val) { this.val = val; }
    }

    // =========================================================================
    // Approach 1: BFS (Level Order) — O(n) time, O(n) space
    // =========================================================================
    public static String serializeBFS(TreeNode root) {
        if (root == null) return "null";
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

    public static TreeNode deserializeBFS(String data) {
        if (data.equals("null")) return null;
        String[] vals = data.split(",");
        TreeNode root = new TreeNode(Integer.parseInt(vals[0]));
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        int i = 1;
        while (!queue.isEmpty() && i < vals.length) {
            TreeNode node = queue.poll();
            if (!vals[i].equals("null")) {
                node.left = new TreeNode(Integer.parseInt(vals[i]));
                queue.offer(node.left);
            }
            i++;
            if (i < vals.length && !vals[i].equals("null")) {
                node.right = new TreeNode(Integer.parseInt(vals[i]));
                queue.offer(node.right);
            }
            i++;
        }
        return root;
    }

    // =========================================================================
    // Approach 2: Preorder DFS (Optimal/Best) — O(n) time, O(n) space
    // =========================================================================
    public static String serializePreorder(TreeNode root) {
        StringBuilder sb = new StringBuilder();
        serializePre(root, sb);
        return sb.toString();
    }

    private static void serializePre(TreeNode node, StringBuilder sb) {
        if (node == null) { sb.append("null,"); return; }
        sb.append(node.val).append(",");
        serializePre(node.left, sb);
        serializePre(node.right, sb);
    }

    public static TreeNode deserializePreorder(String data) {
        Queue<String> queue = new LinkedList<>(Arrays.asList(data.split(",")));
        return deserializePre(queue);
    }

    private static TreeNode deserializePre(Queue<String> queue) {
        String val = queue.poll();
        if (val.equals("null")) return null;
        TreeNode node = new TreeNode(Integer.parseInt(val));
        node.left = deserializePre(queue);
        node.right = deserializePre(queue);
        return node;
    }

    // =========================================================================
    // Approach 3: Postorder DFS — O(n) time, O(n) space
    // =========================================================================
    public static String serializePostorder(TreeNode root) {
        StringBuilder sb = new StringBuilder();
        serializePost(root, sb);
        return sb.toString();
    }

    private static void serializePost(TreeNode node, StringBuilder sb) {
        if (node == null) { sb.append("null,"); return; }
        serializePost(node.left, sb);
        serializePost(node.right, sb);
        sb.append(node.val).append(",");
    }

    public static TreeNode deserializePostorder(String data) {
        LinkedList<String> list = new LinkedList<>(Arrays.asList(data.split(",")));
        return deserializePost(list);
    }

    private static TreeNode deserializePost(LinkedList<String> list) {
        String val = list.removeLast();
        if (val.equals("null")) return null;
        TreeNode node = new TreeNode(Integer.parseInt(val));
        node.right = deserializePost(list);
        node.left = deserializePost(list);
        return node;
    }

    // =========================================================================
    // Main — Test Cases
    // =========================================================================
    public static void main(String[] args) {
        System.out.println("=== Serialize and Deserialize Binary Tree ===");

        //      1
        //     / \
        //    2   3
        //       / \
        //      4   5
        TreeNode root = new TreeNode(1);
        root.left = new TreeNode(2);
        root.right = new TreeNode(3);
        root.right.left = new TreeNode(4);
        root.right.right = new TreeNode(5);

        String bfs = serializeBFS(root);
        System.out.println("BFS serialized:      " + bfs);
        TreeNode r1 = deserializeBFS(bfs);
        System.out.println("BFS deserialized OK: " + (serializeBFS(r1).equals(bfs)));

        String pre = serializePreorder(root);
        System.out.println("Preorder serialized: " + pre);
        TreeNode r2 = deserializePreorder(pre);
        System.out.println("Preorder deser OK:   " + (serializePreorder(r2).equals(pre)));

        String post = serializePostorder(root);
        System.out.println("Postorder serialized:" + post);
        TreeNode r3 = deserializePostorder(post);
        System.out.println("Postorder deser OK:  " + (serializePostorder(r3).equals(post)));
    }
}
