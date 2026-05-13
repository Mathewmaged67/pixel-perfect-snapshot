package lifexp.models;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents an XP log entry tracking character progression.
 * Encapsulates XP transactions and history.
 */
public class XpLog {
    private String id;
    private String characterId;
    private long xpAmount;
    private String source;
    private LocalDateTime timestamp;
    private long totalXpAfter;

    public XpLog(String id, String characterId, long xpAmount, String source) {
        this.id = Objects.requireNonNull(id, "XP Log ID cannot be null");
        this.characterId = Objects.requireNonNull(characterId, "Character ID cannot be null");
        this.xpAmount = xpAmount;
        this.source = Objects.requireNonNull(source, "Source cannot be null");
        this.timestamp = LocalDateTime.now();
    }

    // Getters
    public String getId() { return id; }
    public String getCharacterId() { return characterId; }
    public long getXpAmount() { return xpAmount; }
    public String getSource() { return source; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public long getTotalXpAfter() { return totalXpAfter; }

    // Setters
    public void setTotalXpAfter(long totalXpAfter) {
        this.totalXpAfter = totalXpAfter;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        XpLog xpLog = (XpLog) o;
        return Objects.equals(id, xpLog.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("XpLog{id='%s', xp=%d, source='%s', time=%s}", 
            id, xpAmount, source, timestamp);
    }
}
