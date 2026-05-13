package lifexp.exceptions;

/**
 * Exception thrown when an entity is not found in the repository.
 */
public class EntityNotFoundException extends LifeXPException {
    public EntityNotFoundException(String entityType, String id) {
        super(String.format("%s with id '%s' not found", entityType, id));
    }

    public EntityNotFoundException(String message) {
        super(message);
    }
}
