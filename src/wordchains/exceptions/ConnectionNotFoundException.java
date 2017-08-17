package wordchains.exceptions;

/**
 *
 * @author Adam Parys
 */
public class ConnectionNotFoundException extends Exception {

    /**
     * Creates a new instance of <code>ConnectionNotFoundException</code>
     * without detail message.
     */
    public ConnectionNotFoundException() {
    }

    /**
     * Constructs an instance of <code>ConnectionNotFoundException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public ConnectionNotFoundException(String msg) {
        super(msg);
    }
}
