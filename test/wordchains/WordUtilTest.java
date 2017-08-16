package wordchains;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import wordchains.exceptions.DifferentWordLengthsException;

/**
 *
 * @author Adam Parys
 */
public class WordUtilTest {

    public WordUtilTest() {
    }

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    /**
     * Test of charDifference method, of class WordUtil.
     */
    @Test
    public void testCharDifference() throws DifferentWordLengthsException {
        assertEquals(0, WordUtil.areWordsDirectlyConnected("aaa", "aaa"));
        assertEquals(1, WordUtil.areWordsDirectlyConnected("aba", "aaa"));
        assertEquals(3, WordUtil.areWordsDirectlyConnected("cvr", "aaa"));
        assertEquals(3, WordUtil.areWordsDirectlyConnected("fer", "sdf"));
    }

    @Test
    public void differentLengthWordsShouldThrowException() throws DifferentWordLengthsException {
        thrown.expect(DifferentWordLengthsException.class);
        WordUtil.areWordsDirectlyConnected("a", "aa");
    }
    
    
}
