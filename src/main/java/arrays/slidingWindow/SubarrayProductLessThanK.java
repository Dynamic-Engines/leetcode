

public class SubarrayProductLessThanK {
    
    public int numSubarrayProductLessThanK(int[] nums, int k) {
        // Implementation goes here
        if(k <=1) {
            return 0;
        }
        
        int left = 0;
        int current = 1;
        int answer = 0;

        for(int right = 0; right < nums.length; right ++) {
            current = current * nums[right];

            while(current >= k) {
                current = current / nums[left ++];
            }

            answer = answer + (right - left + 1);
        }

        return answer;
    }

    public static void main(String[] args) {
        SubarrayProductLessThanK solution = new SubarrayProductLessThanK();
        
        int[] nums = { 10,5,2,6 };
        int k = 100;
        
        int result = solution.numSubarrayProductLessThanK(nums, k);
        System.out.println("Number of subarrays with product less than k: " + result);
    }
}