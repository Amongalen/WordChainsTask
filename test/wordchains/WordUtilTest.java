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
        assertEquals(0, WordUtil.charDifference("aaa", "aaa"));
        assertEquals(1, WordUtil.charDifference("aba", "aaa"));
        assertEquals(3, WordUtil.charDifference("cvr", "aaa"));
        assertEquals(3, WordUtil.charDifference("fer", "sdf"));
    }

    @Test
    public void differentLengthWordsShouldThrowException() throws DifferentWordLengthsException {
        thrown.expect(DifferentWordLengthsException.class);
        WordUtil.charDifference("a", "aa");
    }
    
    
}
