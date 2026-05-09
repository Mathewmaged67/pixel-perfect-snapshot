import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class NutritionLogController extends BaseController {
  @FXML private TextField searchField;
  @FXML private ListView<Food> foodList;
  @FXML private TextField servingsField;
  @FXML private Label statusLabel;

  private final ObservableList<Food> filteredFoods = FXCollections.observableArrayList();

  @FXML
  private void initialize() {
    foodList.setItems(filteredFoods);
    foodList.setCellFactory(list -> new ListCell<>() {
      @Override
      protected void updateItem(Food item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
          setText(null);
        } else {
          setText(item.getName() + " - " + item.getKcal() + " kcal");
        }
      }
    });
    searchField.textProperty().addListener((obs, oldValue, newValue) -> applyFilter(newValue));
    applyFilter("");
  }

  @Override
  public void onNavigatedTo(Route route) {
    super.onNavigatedTo(route);
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
  private void onLogMeal() {
    if (appState == null) {
      return;
    }
    Food selected = foodList.getSelectionModel().getSelectedItem();
    if (selected == null) {
      statusLabel.setText("Select a food first.");
      return;
    }
    int servings = parseServings();
    appState.getNutrition().addEntry(selected.getId(), servings);
    XpEngine.logMealRewards(selected.getId(), servings, appState);
    statusLabel.setText("Logged: " + selected.getName());
  }

  private void applyFilter(String query) {
    String normalized = query == null ? "" : query.trim().toLowerCase();
    filteredFoods.setAll(SeedData.foods().stream()
        .filter(food -> food.getName().toLowerCase().contains(normalized))
        .limit(30)
        .toList());
  }

  private int parseServings() {
    try {
      int value = Integer.parseInt(servingsField.getText().trim());
      return Math.max(1, Math.min(value, 20));
    } catch (Exception ex) {
      return 1;
    }
  }
}
