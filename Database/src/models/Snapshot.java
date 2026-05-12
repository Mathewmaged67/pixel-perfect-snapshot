package models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Snapshot implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private int userId;
    private String username;
    private List<Message> messages;
    private LocalDateTime capturedAt;
    private String description;
    private int messageCount;

    public Snapshot() {
        this.messages = new ArrayList<>();
        this.capturedAt = LocalDateTime.now();
    }

    public Snapshot(int userId, String username, List<Message> messages, String description) {
        this();
        this.userId = userId;
        this.username = username;
        this.messages = (messages != null) ? messages : new ArrayList<>();
        this.description = description;
        this.messageCount = this.messages.size();
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public List<Message> getMessages() { return messages; }
    public void setMessages(List<Message> messages) {
        this.messages = messages;
        this.messageCount = (messages != null) ? messages.size() : 0;
    }

    public LocalDateTime getCapturedAt() { return capturedAt; }
    public void setCapturedAt(LocalDateTime capturedAt) { this.capturedAt = capturedAt; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getMessageCount() { return messageCount; }
    public void setMessageCount(int messageCount) { this.messageCount = messageCount; }

    public void addMessage(Message message) {
        if (this.messages == null) this.messages = new ArrayList<>();
        this.messages.add(message);
        this.messageCount = this.messages.size();
    }

    public String getFormattedCapturedAt() {
        if (capturedAt == null) return "";
        return capturedAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    @Override
    public String toString() {
        return "Snapshot[" + id + "] by " + username + " at " + getFormattedCapturedAt()
               + " (" + messageCount + " msgs): " + description;
    }
}
