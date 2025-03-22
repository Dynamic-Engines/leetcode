

public class LongestSubarrayWithSum {
    
    public int longestSubarray(int[] nums, int k) {
        // Implementation goes here
        int left = 0;
        int current = 0; 
        int answer = 0;
        
        for(int right = 0; right < nums.length; right ++) {
            current = current + nums[right];

            while(current > k && left <= right) {
                current = current - nums[left ++];
            }

            if(current <= k) {
                answer = Math.max(answer, right - left + 1);
            }
        }

        return answer;
    }

    public static void main(String[] args) {
        LongestSubarrayWithSum solution = new LongestSubarrayWithSum();
        
        int[] nums = { 3, 2, 1, 3, 1, 1};
        int k = 6;
        
        int result = solution.longestSubarray(nums, k);
        System.out.println("Length of longest subarray: " + result);
    }
}