public class ReverseOnlyLetter {

    public static String reverseOnlyLetters(String s) {
        char[] array = s.toCharArray();

        int left = 0;
        int right = array.length - 1;

        while (left < right) {
            int rightAscii = (int) array[right];
            int leftAscii = (int) array[left];

            if (!isAlpha(rightAscii)) {
                right--;
            } else if (!isAlpha(leftAscii)) {
                left++;
            } else {
                char temp = array[left];
                array[left] = array[right];
                array[right] = temp;

                right--;
                left++;
            }
        }

        return new String(array);
    }

    private static boolean isAlpha(int index) {
        return (index > 64 && index < 91) || (index > 96 && index < 123);
    }

    public static void main(String[] args) {

        String s = "Test1ng-Leet=code-Q!";
        System.out.println(s);
        System.out.println(reverseOnlyLetters(s));
    }
}
