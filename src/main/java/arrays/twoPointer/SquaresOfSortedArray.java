import java.util.Arrays;

public class SquaresOfSortedArray {
    public int[] sortedSquares(int[] nums) {
        // Your implementation here
        int left = 0;
        int right = nums.length - 1;
        int k = right;
        int result[] = new int[nums.length];

        while(left <= right) {
            if(Math.abs(nums[left]) < Math.abs(nums[right])) {
                result[k --] = nums[right] * nums[right];
                right --;
            } else {
                result[k --] = nums[left] * nums[left];
                left ++;
            }
        }

        return result;
    }

    public static void main(String[] args) {
        SquaresOfSortedArray solution = new SquaresOfSortedArray();
        
        // Test Case
        int[] nums = {-4, -1, 0, 3, 10};
        int[] result = solution.sortedSquares(nums);
        System.out.println(Arrays.toString(result)); // Expected: [0, 1, 9, 16, 100]
    }
}