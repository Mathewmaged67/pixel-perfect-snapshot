import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class WorkoutTemplatesController extends BaseController {
  @FXML private VBox templateBox;

  private record Template(String name, String description, List<String> exerciseIds) {}

  private final List<Template> templates = List.of(
      new Template("Push Day", "Chest, shoulders, triceps",
          List.of("ex-bench", "ex-ohp", "ex-dip", "ex-fly", "ex-tri")),
      new Template("Pull Day", "Back, biceps",
          List.of("ex-row", "ex-pull", "ex-lat", "ex-curl", "ex-rear")),
      new Template("Leg Day", "Legs and glutes",
          List.of("ex-squat", "ex-rdl", "ex-leg", "ex-lunge", "ex-calf")),
      new Template("Cardio Burst", "Endurance focus",
          List.of("ex-run", "ex-bike", "ex-row-c", "ex-jump")),
      new Template("Mobility Flow", "Stretch and mobility",
          List.of("ex-yoga", "ex-stretch", "ex-shoulder-mob", "ex-cat", "ex-pigeon"))
  );

  @Override
  public void onNavigatedTo(Route route) {
    super.onNavigatedTo(route);
    refresh();
  }

  private void refresh() {
    templateBox.getChildren().clear();
    for (Template template : templates) {
      VBox card = new VBox(6);
      card.getStyleClass().add("card");
      Label title = new Label(template.name());
      Label desc = new Label(template.description());
      desc.getStyleClass().add("muted");
      String exerciseNames = template.exerciseIds().stream()
          .map(id -> {
            Exercise ex = SeedData.exerciseById(id);
            return ex != null ? ex.getName() : id;
          })
          .reduce((a, b) -> a + ", " + b)
          .orElse("");
      Label exercises = new Label("Exercises: " + exerciseNames);
      exercises.getStyleClass().add("muted");
      Button start = new Button("Start template");
      start.getStyleClass().add("primary-button");
      start.setOnAction(event -> startTemplate(template));
      card.getChildren().addAll(title, desc, exercises, start);
      templateBox.getChildren().add(card);
    }
  }

  private void startTemplate(Template template) {
    if (appState == null) {
      return;
    }
    appState.getWorkout().startSession(template.name(), template.exerciseIds());
    appState.addActivity("Workout started: " + template.name());
    router.goTo(Route.WORKOUT_ACTIVE);
  }
}
