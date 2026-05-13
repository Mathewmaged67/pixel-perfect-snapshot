import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class NutritionState {
  private NutritionTotals goals = new NutritionTotals(2400, 160, 280, 80);
  private final List<FoodLogEntry> log = new ArrayList<>();
  private NutritionTotals postWorkoutTargets;

  public NutritionTotals getGoals() {
    return goals;
  }

  public List<FoodLogEntry> getLog() {
    return log;
  }

  public NutritionTotals getPostWorkoutTargets() {
    return postWorkoutTargets;
  }

  public void addEntry(String foodId, int servings) {
    log.add(0, new FoodLogEntry(IdUtil.randomId(), foodId, servings, System.currentTimeMillis()));
  }

  public void removeEntry(String id) {
    log.removeIf(entry -> entry.getId().equals(id));
  }

  public void setGoals(NutritionTotals goals) {
    this.goals = goals;
  }

  public void setPostWorkoutTargets(NutritionTotals targets) {
    this.postWorkoutTargets = targets;
  }

  public NutritionTotals todayTotals() {
    LocalDate today = LocalDate.now();
    int kcal = 0;
    int protein = 0;
    int carbs = 0;
    int fat = 0;
    for (FoodLogEntry entry : log) {
        LocalDate entryDate = Instant.ofEpochMilli(entry.getLoggedAt())
          .atZone(ZoneId.systemDefault())
          .toLocalDate();
      if (!entryDate.equals(today)) {
        continue;
      }
      Food food = SeedData.foodById(entry.getFoodId());
      if (food == null) {
        continue;
      }
      kcal += food.getKcal() * entry.getServings();
      protein += food.getProtein() * entry.getServings();
      carbs += food.getCarbs() * entry.getServings();
      fat += food.getFat() * entry.getServings();
    }
    return new NutritionTotals(kcal, protein, carbs, fat);
  }

  public void reset() {
    goals = new NutritionTotals(2400, 160, 280, 80);
    log.clear();
    postWorkoutTargets = null;
  }
}
