public class ReversePrefixOfWord {

    static public String reversePrefix(String word, char ch) {
        char[] array = word.toCharArray();
        for (int index = 0; index < array.length; index++) {
            if (array[index] == ch) {
                int left = 0;
                int right = index;
                while (left < right) {
                    char temp = array[left];
                    array[left] = array[right];
                    array[right] = temp;

                    left++;
                    right--;
                }

                return new String(array);
            }
        }

        return new String(array);
    }

    public static void main(String[] args) {
        String s = "Test1ng-Leet=code-Q!";
        System.out.println(s);
        System.out.println(reversePrefix(s, '-'));
    }
}
