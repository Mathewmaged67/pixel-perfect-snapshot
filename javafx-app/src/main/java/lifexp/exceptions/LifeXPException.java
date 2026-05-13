package lifexp.exceptions;

/**
 * Base exception for all LifeXP application errors.
 */
public class LifeXPException extends Exception {
    public LifeXPException(String message) {
        super(message);
    }

    public LifeXPException(String message, Throwable cause) {
        super(message, cause);
    }
}
