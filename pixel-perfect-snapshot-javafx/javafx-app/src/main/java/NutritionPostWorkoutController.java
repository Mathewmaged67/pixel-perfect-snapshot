import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class NutritionPostWorkoutController extends BaseController {
  @FXML private Label kcalLabel;
  @FXML private Label proteinLabel;
  @FXML private Label carbsLabel;
  @FXML private Label fatLabel;
  @FXML private Label statusLabel;

  @Override
  public void onNavigatedTo(Route route) {
    super.onNavigatedTo(route);
    refreshTargets();
    statusLabel.setText("");
  }

  @FXML
  private void onTabToday() {
    if (router != null) {
      router.goTo(Route.NUTRITION_TODAY);
    }
  }

  @FXML
  private void onTabLog() {
    if (router != null) {
      router.goTo(Route.NUTRITION_LOG);
    }
  }

  @FXML
  private void onTabGoals() {
    if (router != null) {
      router.goTo(Route.NUTRITION_GOALS);
    }
  }

  @FXML
  private void onTabPost() {
    if (router != null) {
      router.goTo(Route.NUTRITION_POST_WORKOUT);
    }
  }

  @FXML
  private void onChicken() {
    logMeal("f-chicken", 1, "Chicken + Rice logged");
  }

  @FXML
  private void onWhey() {
    logMeal("f-whey", 1, "Whey shake logged");
  }

  @FXML
  private void onTempeh() {
    logMeal("f-tempeh", 1, "Tempeh bowl logged");
  }

  private void refreshTargets() {
    if (appState == null) {
      return;
    }
    NutritionTotals targets = appState.getNutrition().getPostWorkoutTargets();
    if (targets == null) {
      WorkoutSession last = appState.getWorkout().getHistory().isEmpty()
          ? null
          : appState.getWorkout().getHistory().get(0);
      targets = XpEngine.computePostWorkoutTargets(last, appState);
    }
    kcalLabel.setText(String.valueOf(targets.getKcal()));
    proteinLabel.setText(targets.getProtein() + " g");
    carbsLabel.setText(targets.getCarbs() + " g");
    fatLabel.setText(targets.getFat() + " g");
  }

  private void logMeal(String foodId, int servings, String message) {
    if (appState == null) {
      return;
    }
    appState.getNutrition().addEntry(foodId, servings);
    XpEngine.logMealRewards(foodId, servings, appState);
    statusLabel.setText(message);
  }
}
