package strings;

/**
 * Problem: Reverse a String
 * 
 * Approach: Two Pointers
 * Time Complexity: O(N)
 * Space Complexity: O(1) (in-place modification of character array)
 */
public class ReverseString {
    public void reverseString(char[] s) {
        int left = 0;
        int right = s.length - 1;
        while (left < right) {
            char temp = s[left];
            s[left] = s[right];
            s[right] = temp;
            left++;
            right--;
        }
    }

    public static void main(String[] args) {
        ReverseString rs = new ReverseString();
        char[] s = {'h', 'e', 'l', 'l', 'o'};
        System.out.println("Original: " + new String(s));
        rs.reverseString(s);
        System.out.println("Reversed: " + new String(s));
    }
}
