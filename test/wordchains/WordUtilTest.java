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


    @Test
    public void testAreWordsDirectlyConnected() throws DifferentWordLengthsException {
        assertEquals(false, WordUtil.areWordsDirectlyConnected("aaa", "aaa"));
        assertEquals(true, WordUtil.areWordsDirectlyConnected("aba", "aaa"));
        assertEquals(false, WordUtil.areWordsDirectlyConnected("cvr", "aaa"));
        assertEquals(false, WordUtil.areWordsDirectlyConnected("fer", "sdf"));
    }

    @Test
    public void differentLengthWordsShouldThrowException() throws DifferentWordLengthsException {
        thrown.expect(DifferentWordLengthsException.class);
        WordUtil.areWordsDirectlyConnected("a", "aa");
    }
    
    
}
