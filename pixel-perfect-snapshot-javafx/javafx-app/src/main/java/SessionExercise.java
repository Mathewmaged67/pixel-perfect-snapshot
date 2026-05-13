import java.util.ArrayList;
import java.util.List;

public class SessionExercise {
  private final String id;
  private final String exerciseId;
  private final List<SetEntry> sets;

  public SessionExercise(String id, String exerciseId, List<SetEntry> sets) {
    this.id = id;
    this.exerciseId = exerciseId;
    this.sets = new ArrayList<>(sets);
  }

  public String getId() {
    return id;
  }

  public String getExerciseId() {
    return exerciseId;
  }

  public List<SetEntry> getSets() {
    return sets;
  }

  public int completedReps() {
    return sets.stream().filter(SetEntry::isDone).mapToInt(SetEntry::getReps).sum();
  }

  public int volume() {
    return sets.stream().filter(SetEntry::isDone).mapToInt(s -> s.getWeight() * s.getReps()).sum();
  }
}
