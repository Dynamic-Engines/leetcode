import java.util.*;

public class MaxSumSubarrayOfLengthK {
    
    public int maxSum(int[] nums, int k) {
        // Implementation goes here
        int current = 0;

        for(int i = 0; i < k; i++) {
            current = current + nums[i];
        }

        int answer = current;
        for(int right = k; right < nums.length; right ++) {
            current = current + nums[right] - nums[right - k];
            answer = Math.max(answer, current);
        }

        return answer;
    }

    public static void main(String[] args) {
        MaxSumSubarrayOfLengthK solution = new MaxSumSubarrayOfLengthK();
        
        int[] nums = {2, 1, 5, 1, 3, 2};
        int k = 3;
        
        int result = solution.maxSum(nums, k);
        System.out.println("Maximum sum of subarray of length k: " + result);
    }
}