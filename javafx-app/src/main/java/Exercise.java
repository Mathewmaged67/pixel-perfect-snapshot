import java.util.List;

public class Exercise {
  private final String id;
  private final String name;
  private final List<MuscleGroup> muscles;
  private final String equipment;
  private final int defaultSets;
  private final int defaultReps;
  private final ExerciseCategory category;

  public Exercise(String id, String name, List<MuscleGroup> muscles, String equipment,
                  int defaultSets, int defaultReps, ExerciseCategory category) {
    this.id = id;
    this.name = name;
    this.muscles = List.copyOf(muscles);
    this.equipment = equipment;
    this.defaultSets = defaultSets;
    this.defaultReps = defaultReps;
    this.category = category;
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public List<MuscleGroup> getMuscles() {
    return muscles;
  }

  public String getEquipment() {
    return equipment;
  }

  public int getDefaultSets() {
    return defaultSets;
  }

  public int getDefaultReps() {
    return defaultReps;
  }

  public ExerciseCategory getCategory() {
    return category;
  }
}
