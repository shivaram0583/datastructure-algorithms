/**
 * Problem: Reverse Bits (LeetCode #190)
 * Difficulty: Medium
 * Topics: Divide and Conquer, Bit Manipulation
 * Frequently asked at: Microsoft
 *
 * Description:
 *   Reverse bits of a given 32 bits unsigned integer.
 *
 * Approach 1 – Brute (string reverse): TC: O(32) | SC: O(32)
 * Approach 2 – Bit by bit (Optimal): TC: O(32) | SC: O(1)
 * Approach 3 – Divide and conquer with masks (Best for repeated calls):
 *   Swap halves progressively. TC: O(1) (5 operations) | SC: O(1)
 */

public class ReverseBits {

    public static int reverseBrute(int n) {
        String binary = String.format("%32s", Integer.toBinaryString(n)).replace(' ', '0');
        String reversed = new StringBuilder(binary).reverse().toString();
        return (int) Long.parseLong(reversed, 2); // use long to handle unsigned
    }

    public static int reverseOptimal(int n) {
        int result = 0;
        for (int i = 0; i < 32; i++) {
            result <<= 1;
            result |= (n & 1);
            n >>= 1;
        }
        return result;
    }

    public static int reverseBest(int n) {
        n = ((n & 0xffff0000) >>> 16) | ((n & 0x0000ffff) << 16);
        n = ((n & 0xff00ff00) >>> 8)  | ((n & 0x00ff00ff) << 8);
        n = ((n & 0xf0f0f0f0) >>> 4)  | ((n & 0x0f0f0f0f) << 4);
        n = ((n & 0xcccccccc) >>> 2)  | ((n & 0x33333333) << 2);
        n = ((n & 0xaaaaaaaa) >>> 1)  | ((n & 0x55555555) << 1);
        return n;
    }

    public static void main(String[] args) {
        System.out.println("=== Reverse Bits ===");
        int n = 0b00000010100101000001111010011100;
        System.out.println("Input:    " + Integer.toBinaryString(n));
        System.out.println("Brute:    " + Integer.toBinaryString(reverseBrute(n)));
        System.out.println("BitByBit: " + Integer.toBinaryString(reverseOptimal(n)));
        System.out.println("D&C:      " + Integer.toBinaryString(reverseBest(n)));
        // 964176192 = 00111001011110000010100101000000
    }
}
