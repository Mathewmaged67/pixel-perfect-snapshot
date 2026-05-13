import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

public class WorkoutHistoryController extends BaseController {
  @FXML private ListView<WorkoutSession> historyList;

  @FXML
  private void initialize() {
    historyList.setCellFactory(list -> new ListCell<>() {
      @Override
      protected void updateItem(WorkoutSession item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
          setText(null);
        } else {
          int volume = item.getExercises().stream().mapToInt(SessionExercise::volume).sum();
          setText(FormatUtil.formatDate(item.getStartedAt()) + " - " + item.getName()
              + " (" + item.getExercises().size() + " exercises, " + volume + " kg)");
        }
      }
    });
  }

  @Override
  public void onNavigatedTo(Route route) {
    super.onNavigatedTo(route);
    if (appState == null) {
      return;
    }
    historyList.getItems().setAll(appState.getWorkout().getHistory());
  }

  @FXML
  private void onViewSummary() {
    if (appState == null) {
      return;
    }
    WorkoutSession selected = historyList.getSelectionModel().getSelectedItem();
    if (selected != null) {
      appState.getWorkout().setSelectedSessionId(selected.getId());
      router.goTo(Route.WORKOUT_SUMMARY);
    }
  }
}
