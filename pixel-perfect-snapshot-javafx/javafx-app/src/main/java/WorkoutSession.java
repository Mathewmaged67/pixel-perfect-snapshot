import java.util.ArrayList;
import java.util.List;

public class WorkoutSession {
  private final String id;
  private final long startedAt;
  private long endedAt;
  private final String name;
  private final List<SessionExercise> exercises;

  public WorkoutSession(String id, long startedAt, String name, List<SessionExercise> exercises) {
    this.id = id;
    this.startedAt = startedAt;
    this.name = name;
    this.exercises = new ArrayList<>(exercises);
  }

  public String getId() {
    return id;
  }

  public long getStartedAt() {
    return startedAt;
  }

  public long getEndedAt() {
    return endedAt;
  }

  public void setEndedAt(long endedAt) {
    this.endedAt = endedAt;
  }

  public String getName() {
    return name;
  }

  public List<SessionExercise> getExercises() {
    return exercises;
  }

  public long durationMs() {
    long end = endedAt > 0 ? endedAt : System.currentTimeMillis();
    return Math.max(0, end - startedAt);
  }
}
