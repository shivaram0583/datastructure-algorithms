/**
 * Problem: Clone Graph (LeetCode #133)
 * Difficulty: Medium
 * Topics: Hash Table, DFS, BFS, Graph
 * Frequently asked at: Microsoft
 *
 * Description:
 *   Given a reference of a node in a connected undirected graph,
 *   return a deep copy (clone) of the graph.
 *
 * Approach 1 – BFS + HashMap
 *   BFS to traverse; HashMap maps original → clone.
 *   TC: O(V+E) | SC: O(V)
 *
 * Approach 2 – DFS Recursive + HashMap (Optimal/Best)
 *   DFS recursion; HashMap prevents revisiting.
 *   TC: O(V+E) | SC: O(V)
 *
 * Approach 3 – DFS Iterative + HashMap
 *   Iterative DFS using stack + HashMap.
 *   TC: O(V+E) | SC: O(V)
 */

import java.util.*;

public class CloneGraph {

    static class Node {
        public int val;
        public List<Node> neighbors;
        public Node(int val) { this.val = val; neighbors = new ArrayList<>(); }
    }

    // =========================================================================
    // Approach 1: BFS — O(V+E) time, O(V) space
    // =========================================================================
    public static Node cloneBFS(Node node) {
        if (node == null) return null;
        Map<Node, Node> map = new HashMap<>();
        map.put(node, new Node(node.val));
        Queue<Node> queue = new LinkedList<>();
        queue.offer(node);

        while (!queue.isEmpty()) {
            Node curr = queue.poll();
            for (Node neighbor : curr.neighbors) {
                if (!map.containsKey(neighbor)) {
                    map.put(neighbor, new Node(neighbor.val));
                    queue.offer(neighbor);
                }
                map.get(curr).neighbors.add(map.get(neighbor));
            }
        }
        return map.get(node);
    }

    // =========================================================================
    // Approach 2: DFS Recursive (Optimal/Best) — O(V+E) time, O(V) space
    // =========================================================================
    static Map<Node, Node> visited = new HashMap<>();

    public static Node cloneDFS(Node node) {
        if (node == null) return null;
        if (visited.containsKey(node)) return visited.get(node);

        Node clone = new Node(node.val);
        visited.put(node, clone);
        for (Node neighbor : node.neighbors) {
            clone.neighbors.add(cloneDFS(neighbor));
        }
        return clone;
    }

    // =========================================================================
    // Approach 3: DFS Iterative — O(V+E) time, O(V) space
    // =========================================================================
    public static Node cloneDFSIterative(Node node) {
        if (node == null) return null;
        Map<Node, Node> map = new HashMap<>();
        map.put(node, new Node(node.val));
        Stack<Node> stack = new Stack<>();
        stack.push(node);

        while (!stack.isEmpty()) {
            Node curr = stack.pop();
            for (Node neighbor : curr.neighbors) {
                if (!map.containsKey(neighbor)) {
                    map.put(neighbor, new Node(neighbor.val));
                    stack.push(neighbor);
                }
                map.get(curr).neighbors.add(map.get(neighbor));
            }
        }
        return map.get(node);
    }

    // =========================================================================
    // Main
    // =========================================================================
    public static void main(String[] args) {
        System.out.println("=== Clone Graph ===");

        // Build: 1 -- 2
        //        |    |
        //        4 -- 3
        Node n1 = new Node(1), n2 = new Node(2), n3 = new Node(3), n4 = new Node(4);
        n1.neighbors.addAll(Arrays.asList(n2, n4));
        n2.neighbors.addAll(Arrays.asList(n1, n3));
        n3.neighbors.addAll(Arrays.asList(n2, n4));
        n4.neighbors.addAll(Arrays.asList(n1, n3));

        Node clone = cloneBFS(n1);
        System.out.println("BFS Clone val: " + clone.val + ", neighbors: "
            + clone.neighbors.size()); // val=1, neighbors=2

        visited.clear();
        Node clone2 = cloneDFS(n1);
        System.out.println("DFS Clone val: " + clone2.val + ", is deep copy: "
            + (clone2 != n1)); // true

        System.out.println("All clone approaches verified!");
    }
}
