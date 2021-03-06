package wordchains;

import wordchains.exceptions.DifferentWordLengthsException;

/**
 *
 * @author Adam Parys
 */
public class WordUtil {

    public static boolean areWordsDirectlyConnected(String word1, String word2) throws DifferentWordLengthsException {
        if (word1.length() != word2.length()) {
            throw new DifferentWordLengthsException();
        }
        int result = 0;
        for (int i = 0; i < word1.length(); i++) {
            if (word1.charAt(i) != word2.charAt(i)) {
                result++;
            }
            if (result >= 2) {
                return false;
            }
        }
        return result == 1;
    }
}
