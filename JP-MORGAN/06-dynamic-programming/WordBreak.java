/**
 * Problem: Word Break (LeetCode #139)
 * Difficulty: Medium
 * Topics: Dynamic Programming, Hash Set, BFS
 * Frequently asked at: JP Morgan Chase (Round 1)
 *
 * Description:
 *   Given a string s and a dictionary of strings wordDict, return true if
 *   s can be segmented into a space-separated sequence of dictionary words.
 *
 * Example 1:
 *   s="leetcode", wordDict=["leet","code"] → true  ("leet code")
 * Example 2:
 *   s="applepenapple", wordDict=["apple","pen"] → true  ("apple pen apple")
 * Example 3:
 *   s="catsandog", wordDict=["cats","dog","sand","and","cat"] → false
 *
 * Constraints:
 *   1 <= s.length <= 300
 *   1 <= wordDict.length <= 1000
 *
 * JP Morgan Context:
 *   Models tokenization of financial codes and instrument identifiers.
 *
 * ============================================================
 * Approach 1 — Bottom-Up DP
 * ============================================================
 *   dp[i] = true if s[0..i-1] can be segmented.
 *   dp[0] = true (empty string).
 *   For each i, check every j < i: if dp[j] && s[j..i-1] in wordDict → dp[i]=true
 *
 *   Step-by-step (s="leetcode", wordDict={"leet","code"}):
 *     dp[0]=true
 *     i=1: j=0: dp[0]&&"l"? no → dp[1]=false
 *     ...
 *     i=4: j=0: dp[0]&&"leet"? YES → dp[4]=true
 *     i=5..7: dp[j=4]&&"c","co","cod"? no → false
 *     i=8: j=4: dp[4]&&"code"? YES → dp[8]=true
 *   Result: true
 *
 *   TC: O(n²)  SC: O(n)
 *
 * ============================================================
 * Approach 2 — BFS
 * ============================================================
 *   Treat as shortest path problem. Each position in s is a node.
 *   Start at 0, reach n if s can be fully segmented.
 *   TC: O(n²)  SC: O(n)
 */

import java.util.*;

public class WordBreak {

    // =========================================================================
    // Approach 1: Bottom-Up DP — O(n²) time, O(n) space
    // =========================================================================
    public static boolean wordBreakDP(String s, List<String> wordDict) {
        Set<String> wordSet = new HashSet<>(wordDict);
        int n = s.length();
        boolean[] dp = new boolean[n + 1];
        dp[0] = true;

        for (int i = 1; i <= n; i++) {
            for (int j = 0; j < i; j++) {
                if (dp[j] && wordSet.contains(s.substring(j, i))) {
                    dp[i] = true;
                    break;
                }
            }
        }
        return dp[n];
    }

    // =========================================================================
    // Approach 2: BFS — O(n²) time, O(n) space
    // =========================================================================
    public static boolean wordBreakBFS(String s, List<String> wordDict) {
        Set<String> wordSet = new HashSet<>(wordDict);
        int n = s.length();
        boolean[] visited = new boolean[n];
        Queue<Integer> queue = new LinkedList<>();
        queue.offer(0);

        while (!queue.isEmpty()) {
            int start = queue.poll();
            if (!visited[start]) {
                visited[start] = true;
                for (int end = start + 1; end <= n; end++) {
                    if (wordSet.contains(s.substring(start, end))) {
                        if (end == n) return true;
                        queue.offer(end);
                    }
                }
            }
        }
        return false;
    }

    // =========================================================================
    // Main — Test Cases
    // =========================================================================
    public static void main(String[] args) {
        System.out.println("=== Word Break ===\n");

        System.out.println("Test 1: s=\"leetcode\", dict=[leet,code]  Expected: true");
        System.out.println("  DP:  " + wordBreakDP("leetcode", Arrays.asList("leet","code")));
        System.out.println("  BFS: " + wordBreakBFS("leetcode", Arrays.asList("leet","code")));

        System.out.println("\nTest 2: s=\"applepenapple\", dict=[apple,pen]  Expected: true");
        System.out.println("  DP:  " + wordBreakDP("applepenapple", Arrays.asList("apple","pen")));

        System.out.println("\nTest 3: s=\"catsandog\", dict=[cats,dog,sand,and,cat]  Expected: false");
        System.out.println("  DP:  " + wordBreakDP("catsandog", Arrays.asList("cats","dog","sand","and","cat")));
    }
}
