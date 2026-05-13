package lifexp.models;

import lifexp.enums.WorkoutType;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents a workout session completed by a user.
 * Encapsulates workout data, duration, intensity, and XP rewards.
 */
public class Workout {
    private String id;
    private String userId;
    private String name;
    private WorkoutType type;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int durationMinutes;
    private int intensity;
    private int caloriesBurned;
    private int xpReward;
    private String notes;
    private boolean completed;

    public Workout(String id, String userId, String name, WorkoutType type) {
        this.id = Objects.requireNonNull(id, "Workout ID cannot be null");
        this.userId = Objects.requireNonNull(userId, "User ID cannot be null");
        this.name = Objects.requireNonNull(name, "Workout name cannot be null");
        this.type = Objects.requireNonNull(type, "Workout type cannot be null");
        this.startTime = LocalDateTime.now();
        this.intensity = 5;
        this.completed = false;
    }

    // Workout Management
    public void complete() {
        this.endTime = LocalDateTime.now();
        this.completed = true;
        calculateDuration();
        calculateRewards();
    }

    private void calculateDuration() {
        if (startTime != null && endTime != null) {
            this.durationMinutes = (int) java.time.temporal.ChronoUnit.MINUTES.between(startTime, endTime);
        }
    }

    private void calculateRewards() {
        if (durationMinutes <= 0) return;

        int baseXp = durationMinutes * 2;
        this.xpReward = (int) (baseXp * intensity / 5.0f * type.getXpMultiplier());
        this.caloriesBurned = (int) ((durationMinutes * 5) * (intensity / 5.0f));
    }

    public void setIntensity(int intensity) {
        if (intensity < 1 || intensity > 10) {
            throw new IllegalArgumentException("Intensity must be between 1 and 10");
        }
        this.intensity = intensity;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public long getXpPerMinute() {
        if (durationMinutes == 0) return 0;
        return xpReward / durationMinutes;
    }

    public boolean isInProgress() {
        return !completed;
    }

    // Getters
    public String getId() { return id; }
    public String getUserId() { return userId; }
    public String getName() { return name; }
    public WorkoutType getType() { return type; }
    public LocalDateTime getStartTime() { return startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    public int getDurationMinutes() { return durationMinutes; }
    public int getIntensity() { return intensity; }
    public int getCaloriesBurned() { return caloriesBurned; }
    public int getXpReward() { return xpReward; }
    public String getNotes() { return notes; }
    public boolean isCompleted() { return completed; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Workout workout = (Workout) o;
        return Objects.equals(id, workout.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("Workout{id='%s', name='%s', type=%s, duration=%d min, xp=%d}", 
            id, name, type.getDisplayName(), durationMinutes, xpReward);
    }
}
