import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class WorkoutIndexController extends BaseController {
  @FXML private TextField searchField;
  @FXML private ListView<Exercise> exerciseList;
  @FXML private ListView<Exercise> selectedList;
  @FXML private TextField sessionNameField;
  @FXML private Label statusLabel;

  private final ObservableList<Exercise> filteredExercises = FXCollections.observableArrayList();
  private final ObservableList<Exercise> selectedExercises = FXCollections.observableArrayList();

  @FXML
  private void initialize() {
    exerciseList.setItems(filteredExercises);
    selectedList.setItems(selectedExercises);

    exerciseList.setCellFactory(list -> new ListCell<>() {
      @Override
      protected void updateItem(Exercise item, boolean empty) {
        super.updateItem(item, empty);
        setText(empty || item == null ? null : item.getName());
      }
    });

    selectedList.setCellFactory(list -> new ListCell<>() {
      @Override
      protected void updateItem(Exercise item, boolean empty) {
        super.updateItem(item, empty);
        setText(empty || item == null ? null : item.getName());
      }
    });

    searchField.textProperty().addListener((obs, oldValue, newValue) -> applyFilter(newValue));
    applyFilter("");
  }

  @Override
  public void onNavigatedTo(Route route) {
    super.onNavigatedTo(route);
    statusLabel.setText("");
    sessionNameField.setText("");
    selectedExercises.clear();
  }

  @FXML
  private void onAddExercise() {
    Exercise selected = exerciseList.getSelectionModel().getSelectedItem();
    if (selected != null && !selectedExercises.contains(selected)) {
      selectedExercises.add(selected);
    }
  }

  @FXML
  private void onRemoveExercise() {
    Exercise selected = selectedList.getSelectionModel().getSelectedItem();
    if (selected != null) {
      selectedExercises.remove(selected);
    }
  }

  @FXML
  private void onBegin() {
    if (appState == null) {
      return;
    }
    if (selectedExercises.isEmpty()) {
      statusLabel.setText("Pick at least one exercise.");
      return;
    }
    String name = sessionNameField.getText().trim();
    if (name.isEmpty()) {
      name = "Training Session";
    }
    List<String> ids = new ArrayList<>();
    for (Exercise exercise : selectedExercises) {
      ids.add(exercise.getId());
    }
    appState.getWorkout().startSession(name, ids);
    appState.addActivity("Workout started: " + name);
    router.goTo(Route.WORKOUT_ACTIVE);
  }

  @FXML
  private void onHistory() {
    if (router != null) {
      router.goTo(Route.WORKOUT_HISTORY);
    }
  }

  @FXML
  private void onTemplates() {
    if (router != null) {
      router.goTo(Route.WORKOUT_TEMPLATES);
    }
  }

  private void applyFilter(String query) {
    String normalized = query == null ? "" : query.trim().toLowerCase();
    filteredExercises.setAll(SeedData.exercises().stream()
        .filter(ex -> ex.getName().toLowerCase().contains(normalized))
        .toList());
  }
}
