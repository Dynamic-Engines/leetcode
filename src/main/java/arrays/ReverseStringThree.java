class ReverseStringThree {

    public static String reverseWords(String s) {

        char[] array = s.toCharArray();
        int lastSpace = -1;

        for (int index = 0; index <= array.length; index++) {

            if (index == array.length || array[index] == ' ') {
                int left = lastSpace + 1;
                int right = index - 1;

                while (left < right) {
                    char temp = array[left];
                    array[left] = array[right];
                    array[right] = temp;

                    right--;
                    left++;
                }

                lastSpace = index;
            }
        }

        return new String(array);
    }

    public static void main(String[] args) {

        String s = "Let's take LeetCode contest";
        System.out.println(s);
        System.out.println(reverseWords(s));
    }
}