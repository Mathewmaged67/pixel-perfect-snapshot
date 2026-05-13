package lifexp.models;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents a user in the LifeXP system.
 * Encapsulates user profile information and account details.
 */
public class User {
    private String id;
    private String username;
    private String email;
    private LocalDateTime createdAt;
    private LocalDateTime lastLoginAt;
    private boolean active;

    public User(String id, String username, String email) {
        this.id = Objects.requireNonNull(id, "User ID cannot be null");
        this.username = Objects.requireNonNull(username, "Username cannot be null");
        this.email = Objects.requireNonNull(email, "Email cannot be null");
        this.createdAt = LocalDateTime.now();
        this.active = true;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getLastLoginAt() {
        return lastLoginAt;
    }

    public boolean isActive() {
        return active;
    }

    // Setters
    public void setUsername(String username) {
        this.username = Objects.requireNonNull(username, "Username cannot be null");
    }

    public void setEmail(String email) {
        this.email = Objects.requireNonNull(email, "Email cannot be null");
    }

    public void setLastLoginAt(LocalDateTime lastLoginAt) {
        this.lastLoginAt = lastLoginAt;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("User{id='%s', username='%s', email='%s', active=%s}", 
            id, username, email, active);
    }
}
