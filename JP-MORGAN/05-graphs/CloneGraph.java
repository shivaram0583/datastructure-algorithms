/**
 * Problem: Clone Graph (LeetCode #133)
 * Difficulty: Medium
 * Topics: Graph, DFS, BFS, HashMap
 * Frequently asked at: JP Morgan Chase (Round 1)
 *
 * Description:
 *   Given a reference to a node in a connected undirected graph,
 *   return a deep copy (clone) of the graph.
 *   Each node contains a val (int) and a list of neighbors.
 *
 * Example:
 *   Input: adjList = [[2,4],[1,3],[2,4],[1,3]]
 *   Nodes: 1-2-3-4 with edges 1-2, 2-3, 3-4, 4-1 (cycle)
 *   Output: deep copy of the same graph
 *
 * JP Morgan Context:
 *   Models deep-copying a transaction dependency graph or a financial
 *   network for isolated simulation/testing purposes.
 *
 * ============================================================
 * Approach 1 — DFS + HashMap
 * ============================================================
 *   HashMap<original_node, cloned_node> to track already cloned nodes.
 *   For each node: if already cloned, return clone. Else create clone,
 *   register in map, recursively clone all neighbors.
 *
 *   Step-by-step (1-2-3-4 cycle):
 *     clone(1): map={1:c1}, for neighbor 2:
 *       clone(2): map={1:c1,2:c2}, for neighbor 1: already in map, return c1
 *                 for neighbor 3:
 *         clone(3): map={...,3:c3}, for neighbor 2: return c2
 *                   for neighbor 4:
 *           clone(4): map={...,4:c4}, for neighbor 3: return c3
 *                     for neighbor 1: return c1
 *                   c4.neighbors=[c3,c1], return c4
 *                 c3.neighbors=[c2,c4], return c3
 *               c2.neighbors=[c1,c3], return c2
 *     c1.neighbors=[c2,c4], return c1
 *
 *   TC: O(V + E)  SC: O(V)
 *
 * ============================================================
 * Approach 2 — BFS + HashMap
 * ============================================================
 *   Same map, but use BFS queue to traverse nodes layer by layer.
 *   TC: O(V + E)  SC: O(V)
 */

import java.util.*;

public class CloneGraph {

    static class Node {
        int val;
        List<Node> neighbors;
        Node(int val) { this.val = val; this.neighbors = new ArrayList<>(); }
    }

    // =========================================================================
    // Approach 1: DFS + HashMap — O(V+E) time, O(V) space
    // =========================================================================
    private Map<Node, Node> mapDFS = new HashMap<>();

    public Node cloneGraphDFS(Node node) {
        if (node == null) return null;
        if (mapDFS.containsKey(node)) return mapDFS.get(node);

        Node clone = new Node(node.val);
        mapDFS.put(node, clone);
        for (Node neighbor : node.neighbors) {
            clone.neighbors.add(cloneGraphDFS(neighbor));
        }
        return clone;
    }

    // =========================================================================
    // Approach 2: BFS + HashMap — O(V+E) time, O(V) space
    // =========================================================================
    public static Node cloneGraphBFS(Node node) {
        if (node == null) return null;
        Map<Node, Node> map = new HashMap<>();
        Queue<Node> queue = new LinkedList<>();

        Node clone = new Node(node.val);
        map.put(node, clone);
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
        return clone;
    }

    // =========================================================================
    // Helper: Verify clone
    // =========================================================================
    public static void printGraph(Node node, Set<Integer> visited) {
        if (node == null || visited.contains(node.val)) return;
        visited.add(node.val);
        System.out.print("Node " + node.val + " neighbors: ");
        for (Node n : node.neighbors) System.out.print(n.val + " ");
        System.out.println();
        for (Node n : node.neighbors) printGraph(n, visited);
    }

    // =========================================================================
    // Main — Test Cases
    // =========================================================================
    public static void main(String[] args) {
        System.out.println("=== Clone Graph ===\n");

        Node n1 = new Node(1); Node n2 = new Node(2);
        Node n3 = new Node(3); Node n4 = new Node(4);
        n1.neighbors.add(n2); n1.neighbors.add(n4);
        n2.neighbors.add(n1); n2.neighbors.add(n3);
        n3.neighbors.add(n2); n3.neighbors.add(n4);
        n4.neighbors.add(n1); n4.neighbors.add(n3);

        System.out.println("Original Graph:");
        printGraph(n1, new HashSet<>());

        Node cloned = cloneGraphBFS(n1);
        System.out.println("\nCloned Graph (BFS):");
        printGraph(cloned, new HashSet<>());

        System.out.println("\nAre original and clone different objects? " + (n1 != cloned));
    }
}
