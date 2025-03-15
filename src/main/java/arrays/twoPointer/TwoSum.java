import java.util.Arrays;

class TwoSumII {
    /**
     * Problem: Two Sum II - Input Array Is Sorted
     * 
     * Given a 1-indexed array of integers 'numbers' that is already sorted in non-decreasing order,
     * find two numbers such that they add up to a specific 'target' number.
     * 
     * Return the indices of the two numbers, index1 and index2, added by one as an integer array [index1, index2].
     * 
     * Note:
     * - Your solution must use only constant extra space.
     * - The tests are generated such that there is exactly one solution.
     * - You may not use the same element twice.
     * 
     * Example 1:
     * Input: numbers = [2,7,11,15], target = 9
     * Output: [1,2]
     * Explanation: The sum of 2 and 7 is 9. Therefore, index1 = 1, index2 = 2.
     * 
     * Example 2:
     * Input: numbers = [2,3,4], target = 6
     * Output: [1,3]
     * 
     * Example 3:
     * Input: numbers = [-1,0], target = -1
     * Output: [1,2]
     * 
     * Constraints:
     * - 2 <= numbers.length <= 3 * 10^4
     * - -1000 <= numbers[i] <= 1000
     * - numbers is sorted in non-decreasing order
     * - -1000 <= target <= 1000
     */
    public int[] twoSum(int[] numbers, int target) {
        // Write your solution here
        int left = 0;
        int right = numbers.length -1;

        while(left < right) {
            int sum = numbers[left] + numbers[right];
            if(sum == target) {return new int[]{left+1, right+1};}
            if(sum > target) {-- right;}
            if(sum < target) {++ left;}   
        }

        return new int[]{};
    }

    // Test cases
    public static void main(String[] args) {
        TwoSumII solution = new TwoSumII();
        
        // Test Case 1
        int[] numbers1 = {2,7,11,15};
        int target1 = 9;
        int[] result1 = solution.twoSum(numbers1, target1);
        System.out.println("Test Case 1: " + Arrays.toString(result1)); // Expected: [1,2]
        
        // Test Case 2
        int[] numbers2 = {2,3,4};
        int target2 = 6;
        int[] result2 = solution.twoSum(numbers2, target2);
        System.out.println("Test Case 2: " + Arrays.toString(result2)); // Expected: [1,3]
        
        // Test Case 3
        int[] numbers3 = {-1,0};
        int target3 = -1;
        int[] result3 = solution.twoSum(numbers3, target3);
        System.out.println("Test Case 3: " + Arrays.toString(result3)); // Expected: [1,2]
    }
}