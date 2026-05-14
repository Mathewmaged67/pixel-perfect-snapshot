import java.util.EnumMap;
import java.util.Map;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class NavController {
  private static NavController instance;

  @FXML private Button dashboardButton;
  @FXML private Button workoutButton;
  @FXML private Button nutritionButton;
  @FXML private Button questsButton;
  @FXML private Button inventoryButton;
  @FXML private Button financeButton;
  @FXML private Button profileButton;

  private final Map<NavGroup, Button> navButtons = new EnumMap<>(NavGroup.class);
  private Router router;

  @FXML
  private void initialize() {
    instance = this;
    navButtons.put(NavGroup.DASHBOARD, dashboardButton);
    navButtons.put(NavGroup.WORKOUT, workoutButton);
    navButtons.put(NavGroup.NUTRITION, nutritionButton);
    navButtons.put(NavGroup.QUESTS, questsButton);
    navButtons.put(NavGroup.INVENTORY, inventoryButton);
    navButtons.put(NavGroup.FINANCE, financeButton);
    navButtons.put(NavGroup.PROFILE, profileButton);
  }

  public static NavController getInstance() {
    return instance;
  }

  public void setRouter(Router router) {
    this.router = router;
  }

  public void setActive(Route route) {
    navButtons.values().forEach(button -> button.getStyleClass().remove("sidebar-button-active"));
    if (route == null) {
      return;
    }
    Button active = navButtons.get(route.getNavGroup());
    if (active != null && !active.getStyleClass().contains("sidebar-button-active")) {
      active.getStyleClass().add("sidebar-button-active");
    }
  }

  @FXML
  private void onDashboard() {
    if (router != null) {
      router.goTo(Route.DASHBOARD);
    }
  }

  @FXML
  private void onWorkout() {
    if (router != null) {
      router.goTo(Route.WORKOUT_INDEX);
    }
  }

  @FXML
  private void onNutrition() {
    if (router != null) {
      router.goTo(Route.NUTRITION_TODAY);
    }
  }

  @FXML
  private void onQuests() {
    if (router != null) {
      router.goTo(Route.QUESTS);
    }
  }

  @FXML
  private void onInventory() {
    if (router != null) {
      router.goTo(Route.INVENTORY);
    }
  }

  @FXML
  private void onFinance() {
    if (router != null) {
      router.goTo(Route.FINANCE);
    }
  }

  @FXML
  private void onProfile() {
    if (router != null) {
      router.goTo(Route.PROFILE);
    }
  }
}
