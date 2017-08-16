package wordchains;

import wordchains.exceptions.DifferentWordLengthsException;
import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import wordchains.exceptions.WordNotInDictionaryException;

/**
 *
 * @author Adam Parys
 */
public class WordChainsTest {

    final static String TEST_FILENAME = "wordListTest.txt";
    final static String REAL_FILENAME = "wordList.txt";
    private static WordChains wc;
    private static WordChains wcReal;

    public WordChainsTest() {
    }

    @BeforeClass
    public static void setUpClass() throws IOException {
        wc = new WordChains(TEST_FILENAME);
        wcReal = new WordChains(REAL_FILENAME);
    }

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void differentLengthWordsShouldThrowException() throws DifferentWordLengthsException, WordNotInDictionaryException {
        thrown.expect(DifferentWordLengthsException.class);
        wc.getChainBetween("cat", "gold");
    }

    @Test
    public void wordNotInDictionaryShouldThrowException() throws DifferentWordLengthsException, WordNotInDictionaryException {
        thrown.expect(WordNotInDictionaryException.class);
        wc.getChainBetween("neck", "gold");
    }

    @Test
    public void testResultLength() {
        try {
            assertEquals(4, wc.getChainBetween("cat", "dog").size());
            assertEquals(4, wc.getChainBetween("dog", "cat").size());
            assertEquals(4, wc.getChainBetween("lead", "gold").size());
            assertEquals(6, wc.getChainBetween("ruby", "code").size());
        } catch (DifferentWordLengthsException ex) {
        } catch (WordNotInDictionaryException ex) {
        }
    }

    @Test
    public void succesiveWordsShouldDifferByOneLetter() throws DifferentWordLengthsException, WordNotInDictionaryException {
        boolean isOk = true;
        ArrayList<String> testedChain = wc.getChainBetween("cat", "dog");
        if (testedChain.size() == 0) {
            isOk = false;
        }
        for (int i = 0; i < testedChain.size() - 1; i++) {
            if (WordUtil.charDifference(testedChain.get(i), testedChain.get(i + 1)) != 1) {
                isOk = false;
            }
        }
        assertTrue(isOk);
    }

    @Test(timeout = 1000)
    public void testCatDogPerformance() throws DifferentWordLengthsException, WordNotInDictionaryException {
        wcReal.getChainBetween("cat", "dog").size();
    }

    @Test(timeout = 1000)
    public void testDogCatPerformance() throws DifferentWordLengthsException, WordNotInDictionaryException {
        wcReal.getChainBetween("dog", "cat").size();
    }

    @Test(timeout = 1000)
    public void testLeadGoldPerformance() throws DifferentWordLengthsException, WordNotInDictionaryException {
        wcReal.getChainBetween("lead", "gold").size();
    }

    @Test(timeout = 1000)
    public void testRubyCodePerformance() throws DifferentWordLengthsException, WordNotInDictionaryException {
        wcReal.getChainBetween("ruby", "code").size();
    }

}
