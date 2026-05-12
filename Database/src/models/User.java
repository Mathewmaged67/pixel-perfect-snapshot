package models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String username;
    private String password;
    private String email;
    private String profileImagePath;
    private LocalDateTime createdAt;
    private boolean online;

    public User() {}

    public User(int id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.online = false;
    }

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.createdAt = LocalDateTime.now();
        this.online = false;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getProfileImagePath() { return profileImagePath; }
    public void setProfileImagePath(String path) { this.profileImagePath = path; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public boolean isOnline() { return online; }
    public void setOnline(boolean online) { this.online = online; }

    public String getFormattedCreatedAt() {
        if (createdAt == null) return "";
        return createdAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

    @Override
    public String toString() {
        return "User{id=" + id + ", username='" + username + "', online=" + online + "}";
    }
}
