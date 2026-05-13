import java.util.ArrayList;
import java.util.List;

public class AppState {
  private final CharacterState character = new CharacterState();
  private final QuestState quests = new QuestState();
  private final InventoryState inventory = new InventoryState();
  private final NutritionState nutrition = new NutritionState();
  private final WorkoutState workout = new WorkoutState();
  private final UiState ui = new UiState();
  private final List<String> activity = new ArrayList<>();

  public CharacterState getCharacter() {
    return character;
  }

  public QuestState getQuests() {
    return quests;
  }

  public InventoryState getInventory() {
    return inventory;
  }

  public NutritionState getNutrition() {
    return nutrition;
  }

  public WorkoutState getWorkout() {
    return workout;
  }

  public UiState getUi() {
    return ui;
  }

  public List<String> getActivity() {
    return activity;
  }

  public void addActivity(String message) {
    activity.add(0, message);
  }

  public void resetAll() {
    character.reset();
    quests.reset();
    inventory.reset();
    nutrition.reset();
    workout.reset();
    ui.reset();
    activity.clear();
  }
}
