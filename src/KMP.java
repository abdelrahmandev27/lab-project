/**
 * Knuth-Morris-Pratt string matching algorithm. 
 * Required for the grep command. 
 */
public class KMP {

    /**
     * Searches for pattern in text using KMP algorithm.
     * Returns true if pattern is found.
     */
    public static boolean search(String text, String pattern) {
        if (pattern == null || pattern.isEmpty()) {
            return true;
        }
        if (text == null || text.isEmpty()) {
            return false;
        }

        int[] lps = computeLPS(pattern);
        int i = 0; // index for text
        int j = 0; // index for pattern

        while (i < text.length()) {
            if (text.charAt(i) == pattern.charAt(j)) {
                i++;
                j++;
                if (j == pattern. length()) {
                    return true; // Pattern found
                }
            } else {
                if (j != 0) {
                    j = lps[j - 1];
                } else {
                    i++;
                }
            }
        }
        return false;
    }

    /**
     * Computes the Longest Proper Prefix which is also Suffix (LPS) array.
     */
    private static int[] computeLPS(String pattern) {
        int[] lps = new int[pattern.length()];
        int len = 0;
        int i = 1;
        lps[0] = 0;

        while (i < pattern.length()) {
            if (pattern. charAt(i) == pattern.charAt(len)) {
                len++;
                lps[i] = len;
                i++;
            } else {
                if (len != 0) {
                    len = lps[len - 1];
                } else {
                    lps[i] = 0;
                    i++;
                }
            }
        }
        return lps;
    }
}