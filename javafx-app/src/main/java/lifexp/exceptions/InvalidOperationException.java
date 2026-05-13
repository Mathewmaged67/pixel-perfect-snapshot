package lifexp.exceptions;

/**
 * Exception thrown when an invalid operation is attempted.
 */
public class InvalidOperationException extends LifeXPException {
    public InvalidOperationException(String message) {
        super(message);
    }

    public InvalidOperationException(String operation, String reason) {
        super(String.format("Invalid operation '%s': %s", operation, reason));
    }
}
