package strings;

/**
 * Problem: Valid Palindrome
 * 
 * Approach: Two Pointers
 * Time Complexity: O(N)
 * Space Complexity: O(1)
 */
public class ValidPalindrome {
    public boolean isPalindrome(String s) {
        int left = 0;
        int right = s.length() - 1;
        
        while (left < right) {
            char l = s.charAt(left);
            char r = s.charAt(right);
            
            if (!Character.isLetterOrDigit(l)) {
                left++;
            } else if (!Character.isLetterOrDigit(r)) {
                right--;
            } else {
                if (Character.toLowerCase(l) != Character.toLowerCase(r)) {
                    return false;
                }
                left++;
                right--;
            }
        }
        
        return true;
    }

    public static void main(String[] args) {
        ValidPalindrome vp = new ValidPalindrome();
        String s1 = "A man, a plan, a canal: Panama";
        String s2 = "race a car";
        
        System.out.println("\"" + s1 + "\" is palindrome? " + vp.isPalindrome(s1));
        System.out.println("\"" + s2 + "\" is palindrome? " + vp.isPalindrome(s2));
    }
}
