package strings;

import java.util.HashMap;
import java.util.Map;

/**
 * Problem: First Non-Repeating Character in a String
 * 
 * Approach: Frequency Map / Array
 * Time Complexity: O(N) where N is the length of the string
 * Space Complexity: O(1) or O(K) where K is the number of distinct characters
 */
public class FirstNonRepeatingCharacter {
    public int firstUniqChar(String s) {
        // Since the string only contains lowercase letters, an array of size 26 is enough
        int[] count = new int[26];
        
        // Build frequency array
        for (char c : s.toCharArray()) {
            count[c - 'a']++;
        }
        
        // Find the index
        for (int i = 0; i < s.length(); i++) {
            if (count[s.charAt(i) - 'a'] == 1) {
                return i;
            }
        }
        
        return -1;
    }

    public static void main(String[] args) {
        FirstNonRepeatingCharacter fnrc = new FirstNonRepeatingCharacter();
        String s = "leetcode";
        System.out.println("String: " + s);
        System.out.println("First unique character index: " + fnrc.firstUniqChar(s));
        
        String s2 = "loveleetcode";
        System.out.println("String: " + s2);
        System.out.println("First unique character index: " + fnrc.firstUniqChar(s2));
    }
}
