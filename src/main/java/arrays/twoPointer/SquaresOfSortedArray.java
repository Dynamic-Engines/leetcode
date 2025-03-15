import java.util.Arrays;

public class SquaresOfSortedArray {
    public int[] sortedSquares(int[] nums) {
        // Your implementation here
        return new int[]{};
    }

    public static void main(String[] args) {
        SquaresOfSortedArray solution = new SquaresOfSortedArray();
        
        // Test Case
        int[] nums = {-4, -1, 0, 3, 10};
        int[] result = solution.sortedSquares(nums);
        System.out.println(Arrays.toString(result)); // Expected: [0, 1, 9, 16, 100]
    }
}