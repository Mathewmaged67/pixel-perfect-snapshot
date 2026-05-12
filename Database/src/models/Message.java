package models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Message implements Serializable {
    private static final long serialVersionUID = 1L;

    public enum MessageType { TEXT, IMAGE, FILE, SYSTEM }

    private int id;
    private int senderId;
    private String senderUsername;
    private int receiverId; // -1 = broadcast
    private String content;
    private MessageType type;
    private LocalDateTime timestamp;
    private boolean read;

    public Message() {}

    public Message(int senderId, String senderUsername, int receiverId, String content) {
        this.senderId = senderId;
        this.senderUsername = senderUsername;
        this.receiverId = receiverId;
        this.content = content;
        this.type = MessageType.TEXT;
        this.timestamp = LocalDateTime.now();
        this.read = false;
    }

    public Message(int senderId, String senderUsername, int receiverId, String content, MessageType type) {
        this(senderId, senderUsername, receiverId, content);
        this.type = type;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getSenderId() { return senderId; }
    public void setSenderId(int senderId) { this.senderId = senderId; }

    public String getSenderUsername() { return senderUsername; }
    public void setSenderUsername(String senderUsername) { this.senderUsername = senderUsername; }

    public int getReceiverId() { return receiverId; }
    public void setReceiverId(int receiverId) { this.receiverId = receiverId; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public MessageType getType() { return type; }
    public void setType(MessageType type) { this.type = type; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public boolean isRead() { return read; }
    public void setRead(boolean read) { this.read = read; }

    public boolean isBroadcast() { return receiverId == -1; }

    public String getFormattedTimestamp() {
        if (timestamp == null) return "";
        return timestamp.format(DateTimeFormatter.ofPattern("HH:mm"));
    }

    @Override
    public String toString() {
        return "Message{id=" + id + ", from=" + senderUsername +
               ", to=" + receiverId + ", content='" + content + "'}";
    }
}
