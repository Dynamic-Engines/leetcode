import java.util.Arrays;

public class MinimumSizeSubArraySum {

    public static int minSubArrayLen(int target, int[] nums) {
        int left = 0;
        int sumOfCurrentArray = 0;
        int result = Integer.MAX_VALUE;

        for (int right = 0; right < nums.length; right++) {
            sumOfCurrentArray = sumOfCurrentArray + nums[right];

            while (sumOfCurrentArray >= target) {
                result = Math.min(result, right - left + 1);
                sumOfCurrentArray = sumOfCurrentArray - nums[left];

                left++;
            }
        }

        return result == Integer.MAX_VALUE ? 0 : result;
    }

    public static void main(String[] args) {
        int[] nums = { 0, 0, 1, 2, 3, 6 };
        System.out.println(Arrays.toString(nums));
        System.out.println(minSubArrayLen(5, nums));
    }
}