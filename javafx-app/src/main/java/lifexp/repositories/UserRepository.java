package lifexp.repositories;

import lifexp.models.User;

/**
 * Repository for User entity operations.
 * Extends generic CRUD functionality for user-specific operations.
 */
public class UserRepository extends GenericCrudRepository<User, String> {
    
    public UserRepository() {
        super(User::getId);
    }

    /**
     * Find user by username
     */
    public User findByUsername(String username) {
        return findBy(u -> u.getUsername().equalsIgnoreCase(username))
                .stream()
                .findFirst()
                .orElse(null);
    }

    /**
     * Find user by email
     */
    public User findByEmail(String email) {
        return findBy(u -> u.getEmail().equalsIgnoreCase(email))
                .stream()
                .findFirst()
                .orElse(null);
    }

    /**
     * Get all active users
     */
    public java.util.List<User> findAllActive() {
        return findBy(User::isActive);
    }
}
