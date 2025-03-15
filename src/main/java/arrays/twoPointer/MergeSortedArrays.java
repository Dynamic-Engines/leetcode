import java.util.Arrays;

public class MergeSortedArrays {
    /**
     * Problem: Merge Two Sorted Arrays
     * 
     * Given two sorted integer arrays arr1 and arr2, return a new array 
     * that combines both of them and is also sorted.
     * 
     * Example 1:
     * Input: arr1 = [1,3,5], arr2 = [2,4,6]
     * Output: [1,2,3,4,5,6]
     * 
     * Example 2:
     * Input: arr1 = [1,2,3], arr2 = [4,5,6]
     * Output: [1,2,3,4,5,6]
     * 
     * Example 3:
     * Input: arr1 = [], arr2 = [1,2,3]
     * Output: [1,2,3]
     * 
     * Constraints:
     * - arr1 and arr2 are sorted in ascending order
     * - -1000 <= arr1[i], arr2[i] <= 1000
     */
    public int[] mergeSortedArrays(int[] arr1, int[] arr2) {
        // Write your solution here
        int first = 0;
        int second = 0;
        int index = 0;
        int[] mergedSortedArray = new int[arr1.length+arr2.length];

        while(first < arr1.length && second < arr2.length) {
            if(arr1[first] <= arr2[second]) {
                mergedSortedArray[index ++] = arr1[first ++];
            } else {
                mergedSortedArray[index ++] = arr2[second ++];
            }
        }

        while(first < arr1.length) {
            mergedSortedArray[index ++] = arr1[first ++];
        }

        while(second < arr2.length) {
            mergedSortedArray[index ++] = arr2[second ++];
        }

        return mergedSortedArray;
    }

    // Test cases
    public static void main(String[] args) {
        MergeSortedArrays solution = new MergeSortedArrays();
        
        // Test Case 1
        int[] arr1 = {1,3,5};
        int[] arr2 = {2,4,6};
        System.out.println("Test Case 1: " + 
            Arrays.toString(solution.mergeSortedArrays(arr1, arr2))); // Expected: [1,2,3,4,5,6]
        
        // Test Case 2
        int[] arr3 = {1,2,3};
        int[] arr4 = {4,5,6};
        System.out.println("Test Case 2: " + 
            Arrays.toString(solution.mergeSortedArrays(arr3, arr4))); // Expected: [1,2,3,4,5,6]
        
        // Test Case 3
        int[] arr5 = {};
        int[] arr6 = {1,2,3};
        System.out.println("Test Case 3: " + 
            Arrays.toString(solution.mergeSortedArrays(arr5, arr6))); // Expected: [1,2,3]
    }
}