package wordchains.exceptions;

/**
 *
 * @author Adam Parys
 */
public class DifferentWordLengthsException extends Exception {

    /**
     * Creates a new instance of <code>WrongWordLengthsException</code> without
     * detail message.
     */
    public DifferentWordLengthsException() {
        super("Starting and ending words must be of the same length");
    }

    /**
     * Constructs an instance of <code>WrongWordLengthsException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public DifferentWordLengthsException(String msg) {
        super(msg);
    }
}
