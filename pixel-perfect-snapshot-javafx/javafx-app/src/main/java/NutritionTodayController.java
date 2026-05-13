import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;

public class NutritionTodayController extends BaseController {
  @FXML private Label caloriesLabel;
  @FXML private ProgressBar proteinBar;
  @FXML private ProgressBar carbsBar;
  @FXML private ProgressBar fatBar;
  @FXML private Label proteinLabel;
  @FXML private Label carbsLabel;
  @FXML private Label fatLabel;
  @FXML private ListView<String> logList;

  @Override
  public void onNavigatedTo(Route route) {
    super.onNavigatedTo(route);
    refresh();
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

  private void refresh() {
    if (appState == null) {
      return;
    }
    NutritionTotals goals = appState.getNutrition().getGoals();
    NutritionTotals totals = appState.getNutrition().todayTotals();

    caloriesLabel.setText("Calories: " + totals.getKcal() + " / " + goals.getKcal());
    proteinBar.setProgress(goals.getProtein() == 0 ? 0 : (double) totals.getProtein() / goals.getProtein());
    carbsBar.setProgress(goals.getCarbs() == 0 ? 0 : (double) totals.getCarbs() / goals.getCarbs());
    fatBar.setProgress(goals.getFat() == 0 ? 0 : (double) totals.getFat() / goals.getFat());
    proteinLabel.setText(totals.getProtein() + "g");
    carbsLabel.setText(totals.getCarbs() + "g");
    fatLabel.setText(totals.getFat() + "g");

    logList.getItems().setAll(appState.getNutrition().getLog().stream().limit(8).map(entry -> {
      Food food = SeedData.foodById(entry.getFoodId());
      String name = food != null ? food.getName() : "Unknown";
      return name + " x" + entry.getServings();
    }).toList());
  }
}
