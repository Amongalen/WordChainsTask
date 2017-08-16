package wordchains;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

/**
 *
 * @author Adam Parys
 */
public class DictionaryTest {

    final static String TEST_FILENAME = "wordListTest.txt";

    public DictionaryTest() {
    }

    @Rule
    public ExpectedException thrown = ExpectedException.none();
    /**
     * Test of readDictionary method, of class Dictionary.
     */
    @Test
    public void testDictionaryShouldHave17Elements() {
        try {
            Dictionary instance = new Dictionary(TEST_FILENAME);
            assertEquals(17, instance.getWords().size());
        } catch (IOException ex) {
            Logger.getLogger(DictionaryTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Test
    public void wrongFilenameShouldThrowException() throws IOException{
            thrown.expect(NoSuchFileException.class);
            new Dictionary("wrongFilename.exe");
    }

}
