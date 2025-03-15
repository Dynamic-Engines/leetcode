public class IsSubsequence {
    /**
     * Problem 392. Is Subsequence
     * 
     * Given two strings s and t, return true if s is a subsequence of t, or false otherwise.
     * 
     * A subsequence of a string is a new string that is formed from the original string by
     * deleting some (can be none) of the characters without disturbing the relative positions
     * of the remaining characters. (i.e., "ace" is a subsequence of "abcde" while "aec" is not).
     * 
     * Example 1:
     * Input: s = "abc", t = "ahbgdc"
     * Output: true
     * 
     * Example 2:
     * Input: s = "axc", t = "ahbgdc"
     * Output: false
     * 
     * Constraints:
     * - 0 <= s.length <= 100
     * - 0 <= t.length <= 10^4
     * - s and t consist only of lowercase English letters.
     * 
     * Follow up: If there are lots of incoming s, say s1, s2, ..., sk where k >= 10^9, 
     * and you want to check one by one to see if t has its subsequence. 
     * In this scenario, how would you change your code?
     */
    public boolean isSubsequence(String s, String t) {
        // Write your solution here
        int sIndex = 0;
        int tIndex = 0;

        while(sIndex < s.length() && tIndex < t.length()) {
            if(s.charAt(sIndex) == t.charAt(tIndex)) { 
                sIndex ++;
            } 
            
            tIndex ++;
        }

        return sIndex == s.length();
    }

    public static void main(String[] args) {
        IsSubsequence solution = new IsSubsequence();
        
        // Test Case 1
        System.out.println("Test Case 1: " + 
            solution.isSubsequence("abc", "ahbgdc")); // Expected: true
        
        // Test Case 2
        System.out.println("Test Case 2: " + 
            solution.isSubsequence("axc", "ahbgdc")); // Expected: false
        
        // Test Case 3
        System.out.println("Test Case 3: " + 
            solution.isSubsequence("", "ahbgdc")); // Expected: true (empty string is a subsequence of any string)
        
        // Edge Case 1: Both strings are empty
        System.out.println("Edge Case 1: " + 
            solution.isSubsequence("", "")); // Expected: true (empty string is a subsequence of empty string)
        
        // Edge Case 2: s is longer than t
        System.out.println("Edge Case 2: " + 
            solution.isSubsequence("abc", "ab")); // Expected: false (s cannot be a subsequence of a shorter t)
        
        // Edge Case 3: s is the same as t
        System.out.println("Edge Case 3: " + 
            solution.isSubsequence("hello", "hello")); // Expected: true (same strings)
        
        // Edge Case 4: s has characters not in t
        System.out.println("Edge Case 4: " + 
            solution.isSubsequence("abc", "def")); // Expected: false (no common characters)
        
        // Edge Case 5: s is a single character present in t
        System.out.println("Edge Case 5: " + 
            solution.isSubsequence("a", "ahbgdc")); // Expected: true (single character subsequence)
        
        // Edge Case 6: s is a single character not present in t
        System.out.println("Edge Case 6: " + 
            solution.isSubsequence("z", "ahbgdc")); // Expected: false (character not found)
    }
}
