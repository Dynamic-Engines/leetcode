import java.util.Arrays;

class MoveZeroes {
    static public void moveZeroes(int[] nums) {

        int lastNonZeroIndex = 0;
        int left = 0;

        for (; left < nums.length; left++) {
            if (nums[left] != 0) {
                int temp = nums[lastNonZeroIndex];
                nums[lastNonZeroIndex] = nums[left];
                nums[left] = temp;

                lastNonZeroIndex++;
            }
        }

    }

    public static void main(String[] args) {
        int[] nums = {0,0,1,2,3,6};

        System.out.println(Arrays.toString(nums));
        moveZeroes(nums);
        System.out.println(Arrays.toString(nums));
    }
}