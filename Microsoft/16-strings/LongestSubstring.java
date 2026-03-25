package strings;

import java.util.HashSet;
import java.util.Set;

/**
 * Problem: Longest Substring Without Repeating Characters
 * 
 * Approach: Sliding Window
 * Time Complexity: O(N)
 * Space Complexity: O(min(N, M)) where M is the size of the charset
 */
public class LongestSubstring {
    public int lengthOfLongestSubstring(String s) {
        Set<Character> set = new HashSet<>();
        int max = 0;
        int left = 0;
        
        for (int right = 0; right < s.length(); right++) {
            while (set.contains(s.charAt(right))) {
                set.remove(s.charAt(left));
                left++;
            }
            set.add(s.charAt(right));
            max = Math.max(max, right - left + 1);
        }
        
        return max;
    }

    public static void main(String[] args) {
        LongestSubstring ls = new LongestSubstring();
        String s = "abcabcbb";
        System.out.println("Longest substring length for '" + s + "': " + ls.lengthOfLongestSubstring(s));
    }
}
