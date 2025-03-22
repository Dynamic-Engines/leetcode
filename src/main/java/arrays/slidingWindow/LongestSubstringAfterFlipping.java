

public class LongestSubstringAfterFlipping {
    
    public int longestSubstring(String s) {
        // Implementation goes here
        int left = 0;
        int zeroCount = 0;
        int answer = 0;

        for(int right = 0; right < s.length(); right ++) {
            if(s.charAt(right) == '0') {
                zeroCount ++;
            }

            while(zeroCount > 1) {
                if(s.charAt(left) == '0') {
                    zeroCount --;
                }

                left ++;
            }

            answer = Math.max(answer, right - left + 1);
        }
        
        return answer;
    }

    public static void main(String[] args) {
        LongestSubstringAfterFlipping solution = new LongestSubstringAfterFlipping();
        
        String s = "1101100111"; // Example input
        int result = solution.longestSubstring(s);
        System.out.println("Length of longest substring of 1's after flipping: " + result);
    }
}