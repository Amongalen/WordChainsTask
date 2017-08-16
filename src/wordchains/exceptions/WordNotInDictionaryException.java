package wordchains.exceptions;

/**
 *
 * @author Adam Parys
 */
public class WordNotInDictionaryException extends Exception {

    /**
     * Creates a new instance of <code>WordNotInDictionaryException</code>
     * without detail message.
     */
    public WordNotInDictionaryException() {
        super("Starting and ending words must be in dictionary");
    }

    /**
     * Constructs an instance of <code>WordNotInDictionaryException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public WordNotInDictionaryException(String msg) {
        super(msg);
    }
}
