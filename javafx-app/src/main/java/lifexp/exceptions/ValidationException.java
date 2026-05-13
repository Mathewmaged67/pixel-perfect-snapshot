package lifexp.exceptions;

/**
 * Exception thrown when data validation fails.
 */
public class ValidationException extends LifeXPException {
    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String field, String message) {
        super(String.format("Validation error in '%s': %s", field, message));
    }
}
