public class ReverseString {
    public void reverseString(char[] s) {
        // Your implementation here
        int left = 0;
        int right = s.length - 1;

        while(left < right) {
            char temp = s[left];
            s[left ++] = s[right];
            s[right --] = temp;
        }
    }

    public static void main(String[] args) {
        ReverseString solution = new ReverseString();
        
        // Test Case
        char[] s = {'h', 'e', 'l', 'l', 'o'};
        solution.reverseString(s);
        System.out.println(s); // Expected: ['o', 'l', 'l', 'e', 'h']
    }
}
