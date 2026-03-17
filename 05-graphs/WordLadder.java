/**
 * Problem: Word Ladder (LeetCode #127)
 * Difficulty: Hard
 * Topics: BFS, Hash Table, String
 * Frequently asked at: Microsoft
 *
 * Description:
 *   Given beginWord, endWord, and a dictionary wordList, return the number
 *   of words in the shortest transformation sequence, or 0 if no such sequence.
 *   Each transformed word must exist in the word list.
 *   Only one letter can be changed at a time.
 *
 * Approach 1 – BFS (Standard)
 *   BFS from beginWord; try all single-char changes.
 *   TC: O(M² * N) where M=word length, N=list size | SC: O(M*N)
 *
 * Approach 2 – BFS with Wildcard Preprocessing (Optimal)
 *   Build adjacency via wildcard patterns (e.g., h*t → hot, hat).
 *   TC: O(M² * N) | SC: O(M² * N)
 *
 * Approach 3 – Bidirectional BFS (Best)
 *   BFS from both ends; meet in the middle.
 *   TC: O(M² * N) but practically much faster | SC: O(M*N)
 */

import java.util.*;

public class WordLadder {

    // =========================================================================
    // Approach 1: BFS (Standard) — O(M² * N)
    // =========================================================================
    public static int ladderBrute(String beginWord, String endWord, List<String> wordList) {
        Set<String> wordSet = new HashSet<>(wordList);
        if (!wordSet.contains(endWord)) return 0;

        Queue<String> queue = new LinkedList<>();
        queue.offer(beginWord);
        int level = 1;

        while (!queue.isEmpty()) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                char[] word = queue.poll().toCharArray();
                for (int j = 0; j < word.length; j++) {
                    char orig = word[j];
                    for (char c = 'a'; c <= 'z'; c++) {
                        if (c == orig) continue;
                        word[j] = c;
                        String next = new String(word);
                        if (next.equals(endWord)) return level + 1;
                        if (wordSet.contains(next)) {
                            wordSet.remove(next);
                            queue.offer(next);
                        }
                    }
                    word[j] = orig;
                }
            }
            level++;
        }
        return 0;
    }

    // =========================================================================
    // Approach 2: BFS with Wildcard Patterns — O(M² * N)
    // =========================================================================
    public static int ladderOptimal(String beginWord, String endWord, List<String> wordList) {
        Set<String> wordSet = new HashSet<>(wordList);
        if (!wordSet.contains(endWord)) return 0;

        // Build wildcard adjacency: h*t → [hot, hat, ...]
        Map<String, List<String>> patterns = new HashMap<>();
        wordSet.add(beginWord);
        for (String word : wordSet) {
            for (int i = 0; i < word.length(); i++) {
                String pattern = word.substring(0, i) + "*" + word.substring(i + 1);
                patterns.computeIfAbsent(pattern, k -> new ArrayList<>()).add(word);
            }
        }

        Queue<String> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();
        queue.offer(beginWord);
        visited.add(beginWord);
        int level = 1;

        while (!queue.isEmpty()) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                String word = queue.poll();
                for (int j = 0; j < word.length(); j++) {
                    String pattern = word.substring(0, j) + "*" + word.substring(j + 1);
                    for (String neighbor : patterns.getOrDefault(pattern, Collections.emptyList())) {
                        if (neighbor.equals(endWord)) return level + 1;
                        if (!visited.contains(neighbor)) {
                            visited.add(neighbor);
                            queue.offer(neighbor);
                        }
                    }
                }
            }
            level++;
        }
        return 0;
    }

    // =========================================================================
    // Approach 3: Bidirectional BFS (Best) — O(M² * N) but faster in practice
    // =========================================================================
    public static int ladderBest(String beginWord, String endWord, List<String> wordList) {
        Set<String> wordSet = new HashSet<>(wordList);
        if (!wordSet.contains(endWord)) return 0;

        Set<String> beginSet = new HashSet<>(), endSet = new HashSet<>();
        beginSet.add(beginWord);
        endSet.add(endWord);
        Set<String> visited = new HashSet<>();
        visited.add(beginWord);
        visited.add(endWord);
        int level = 1;

        while (!beginSet.isEmpty() && !endSet.isEmpty()) {
            // Always expand the smaller set
            if (beginSet.size() > endSet.size()) {
                Set<String> temp = beginSet; beginSet = endSet; endSet = temp;
            }

            Set<String> nextSet = new HashSet<>();
            for (String word : beginSet) {
                char[] chars = word.toCharArray();
                for (int i = 0; i < chars.length; i++) {
                    char orig = chars[i];
                    for (char c = 'a'; c <= 'z'; c++) {
                        chars[i] = c;
                        String next = new String(chars);
                        if (endSet.contains(next)) return level + 1;
                        if (wordSet.contains(next) && !visited.contains(next)) {
                            nextSet.add(next);
                            visited.add(next);
                        }
                    }
                    chars[i] = orig;
                }
            }
            beginSet = nextSet;
            level++;
        }
        return 0;
    }

    // =========================================================================
    // Main
    // =========================================================================
    public static void main(String[] args) {
        System.out.println("=== Word Ladder ===");

        List<String> dict = Arrays.asList("hot","dot","dog","lot","log","cog");
        System.out.println("BFS:          " + ladderBrute("hit", "cog", dict));   // 5
        System.out.println("Wildcard BFS: " + ladderOptimal("hit", "cog", dict)); // 5
        System.out.println("Bi-BFS:       " + ladderBest("hit", "cog", dict));     // 5

        List<String> dict2 = Arrays.asList("hot","dot","dog","lot","log");
        System.out.println("\nNo path: " + ladderBrute("hit", "cog", dict2));      // 0
    }
}
