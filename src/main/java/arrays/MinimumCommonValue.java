import java.util.Arrays;

public class MinimumCommonValue {

    static public  int getCommon(int[] nums1, int[] nums2) {
        int i = 0;
        int j = 0;

        if (nums1.length == 0 || nums2.length == 0 || nums1[i] > nums2[nums2.length - 1] || nums2[j] > nums1[nums1.length - 1]) {
            return -1;
        }

        while (i < nums1.length && j < nums2.length) {
            if (nums1[i] < nums2[j]) {
                i++;
            } else if (nums1[i] > nums2[j]) {
                j++;
            } else {
                return nums1[i];
            }
        }

        return -1;
    }

    public static void main(String[] args) {
        int[] nums1 = {1,2,3,6};
        int[] nums2 = {2,3,4,5};

        System.out.println(Arrays.toString(nums1));
        System.out.println(Arrays.toString(nums2));
        System.out.println("MinimumCommonValue: "+ getCommon(nums1, nums2));
    }
}
