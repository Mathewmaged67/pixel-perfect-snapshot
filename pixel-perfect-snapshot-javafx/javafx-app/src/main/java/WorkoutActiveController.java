import java.util.HashSet;
import java.util.Set;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Duration;
import javafx.util.converter.NumberStringConverter;

public class WorkoutActiveController extends BaseController {
  @FXML private Label sessionNameLabel;
  @FXML private Label timerLabel;
  @FXML private Label xpPreviewLabel;
  @FXML private ListView<SessionExercise> exerciseList;
  @FXML private TableView<SetEntry> setTable;
  @FXML private TableColumn<SetEntry, Number> setNumberCol;
  @FXML private TableColumn<SetEntry, Number> weightCol;
  @FXML private TableColumn<SetEntry, Number> repsCol;
  @FXML private TableColumn<SetEntry, Boolean> doneCol;
  @FXML private ComboBox<Exercise> addExerciseCombo;
  @FXML private Label statusLabel;

  private final ObservableList<SessionExercise> sessionExercises = FXCollections.observableArrayList();
  private final ObservableList<SetEntry> setEntries = FXCollections.observableArrayList();
  private final Set<String> restShownFor = new HashSet<>();
  private Timeline timer;

  @FXML
  private void initialize() {
    exerciseList.setItems(sessionExercises);
    exerciseList.setCellFactory(list -> new ListCell<>() {
      @Override
      protected void updateItem(SessionExercise item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
          setText(null);
        } else {
          Exercise ex = SeedData.exerciseById(item.getExerciseId());
          String name = ex != null ? ex.getName() : item.getExerciseId();
          setText(name + " (" + item.getSets().size() + " sets)");
        }
      }
    });

    setTable.setItems(setEntries);
    setTable.setEditable(true);

    setNumberCol.setCellValueFactory(cell -> {
      int index = setTable.getItems().indexOf(cell.getValue()) + 1;
      return new javafx.beans.property.SimpleIntegerProperty(index);
    });

    weightCol.setCellValueFactory(cell -> cell.getValue().weightProperty());
    weightCol.setCellFactory(TextFieldTableCell.forTableColumn(new NumberStringConverter()));
    weightCol.setOnEditCommit(event -> {
      SetEntry entry = event.getRowValue();
      entry.setWeight(Math.max(0, event.getNewValue().intValue()));
      updateXpPreview();
    });

    repsCol.setCellValueFactory(cell -> cell.getValue().repsProperty());
    repsCol.setCellFactory(TextFieldTableCell.forTableColumn(new NumberStringConverter()));
    repsCol.setOnEditCommit(event -> {
      SetEntry entry = event.getRowValue();
      entry.setReps(Math.max(0, event.getNewValue().intValue()));
      updateXpPreview();
    });

    doneCol.setCellValueFactory(cell -> cell.getValue().doneProperty());
    doneCol.setCellFactory(col -> new TableCell<>() {
      private final CheckBox checkBox = new CheckBox();
      {
        checkBox.setOnAction(event -> {
          SetEntry entry = getTableView().getItems().get(getIndex());
          entry.setDone(checkBox.isSelected());
          handleRestTimer(entry);
          updateXpPreview();
        });
      }

      @Override
      protected void updateItem(Boolean item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
          setGraphic(null);
        } else {
          checkBox.setSelected(item != null && item);
          setGraphic(checkBox);
        }
      }
    });

    exerciseList.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
      if (newValue == null) {
        setEntries.clear();
      } else {
        setEntries.setAll(newValue.getSets());
      }
    });

    addExerciseCombo.setItems(FXCollections.observableArrayList(SeedData.exercises()));
    addExerciseCombo.setCellFactory(list -> new ListCell<>() {
      @Override
      protected void updateItem(Exercise item, boolean empty) {
        super.updateItem(item, empty);
        setText(empty || item == null ? null : item.getName());
      }
    });
    addExerciseCombo.setButtonCell(new ListCell<>() {
      @Override
      protected void updateItem(Exercise item, boolean empty) {
        super.updateItem(item, empty);
        setText(empty || item == null ? null : item.getName());
      }
    });
  }

  @Override
  public void onNavigatedTo(Route route) {
    super.onNavigatedTo(route);
    refresh();
  }

  @FXML
  private void onAddSet() {
    SessionExercise selected = exerciseList.getSelectionModel().getSelectedItem();
    if (selected == null || appState == null) {
      return;
    }
    appState.getWorkout().addSet(selected.getId());
    setEntries.setAll(selected.getSets());
    updateXpPreview();
  }

  @FXML
  private void onRemoveSet() {
    SessionExercise selected = exerciseList.getSelectionModel().getSelectedItem();
    SetEntry set = setTable.getSelectionModel().getSelectedItem();
    if (selected == null || set == null || appState == null) {
      return;
    }
    appState.getWorkout().removeSet(selected.getId(), set.getId());
    setEntries.setAll(selected.getSets());
    updateXpPreview();
  }

  @FXML
  private void onAddExercise() {
    if (appState == null) {
      return;
    }
    Exercise selected = addExerciseCombo.getSelectionModel().getSelectedItem();
    if (selected == null) {
      statusLabel.setText("Pick an exercise to add.");
      return;
    }
    appState.getWorkout().addExercise(selected.getId());
    refresh();
  }

  @FXML
  private void onFinish() {
    if (appState == null) {
      return;
    }
    WorkoutSession session = appState.getWorkout().finishSession();
    if (session == null) {
      return;
    }
    stopTimer();
    XpEngine.finalizeWorkoutRewards(session, appState);
    XpEngine.computePostWorkoutTargets(session, appState);
    appState.addActivity("Workout finished: " + session.getName());
    router.goTo(Route.WORKOUT_SUMMARY);
  }

  private void refresh() {
    if (appState == null) {
      return;
    }
    WorkoutSession active = appState.getWorkout().getActive();
    if (active == null) {
      stopTimer();
      router.goTo(Route.WORKOUT_INDEX);
      return;
    }
    sessionNameLabel.setText(active.getName());
    sessionExercises.setAll(active.getExercises());
    if (!sessionExercises.isEmpty()) {
      exerciseList.getSelectionModel().select(0);
    }
    statusLabel.setText("");
    updateXpPreview();
    startTimer(active);
  }

  private void updateXpPreview() {
    if (appState == null) {
      return;
    }
    WorkoutSession active = appState.getWorkout().getActive();
    if (active == null) {
      return;
    }
    WorkoutXp xp = XpEngine.computeWorkoutXp(active);
    xpPreviewLabel.setText("XP Preview: " + xp.getTotal());
  }

  private void startTimer(WorkoutSession session) {
    if (timer != null) {
      timer.stop();
    }
    timer = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
      timerLabel.setText("Time: " + FormatUtil.formatDuration(session.durationMs()));
    }));
    timer.setCycleCount(Timeline.INDEFINITE);
    timer.play();
  }

  private void stopTimer() {
    if (timer != null) {
      timer.stop();
      timer = null;
    }
  }

  private void handleRestTimer(SetEntry entry) {
    if (entry.isDone() && restShownFor.add(entry.getId())) {
      RestTimerDialog.show(90);
    }
  }
}
