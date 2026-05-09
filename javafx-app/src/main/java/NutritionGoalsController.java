import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class NutritionGoalsController extends BaseController {
  @FXML private TextField kcalField;
  @FXML private TextField proteinField;
  @FXML private TextField carbsField;
  @FXML private TextField fatField;
  @FXML private Label statusLabel;

  @Override
  public void onNavigatedTo(Route route) {
    super.onNavigatedTo(route);
    if (appState == null) {
      return;
    }
    NutritionTotals goals = appState.getNutrition().getGoals();
    kcalField.setText(String.valueOf(goals.getKcal()));
    proteinField.setText(String.valueOf(goals.getProtein()));
    carbsField.setText(String.valueOf(goals.getCarbs()));
    fatField.setText(String.valueOf(goals.getFat()));
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
  private void onSave() {
    if (appState == null) {
      return;
    }
    int kcal = parseInt(kcalField.getText(), 2400);
    int protein = parseInt(proteinField.getText(), 160);
    int carbs = parseInt(carbsField.getText(), 280);
    int fat = parseInt(fatField.getText(), 80);
    appState.getNutrition().setGoals(new NutritionTotals(kcal, protein, carbs, fat));
    statusLabel.setText("Goals saved.");
  }

  private int parseInt(String text, int fallback) {
    try {
      return Integer.parseInt(text.trim());
    } catch (Exception ex) {
      return fallback;
    }
  }
}
