import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class WorkoutSummaryController extends BaseController {
  @FXML private Label sessionLabel;
  @FXML private Label volumeLabel;
  @FXML private Label setsLabel;
  @FXML private Label durationLabel;
  @FXML private Label xpLabel;
  @FXML private Label xpBreakdownLabel;

  @Override
  public void onNavigatedTo(Route route) {
    super.onNavigatedTo(route);
    refresh();
  }

  @FXML
  private void onGoNutrition() {
    if (router != null) {
      router.goTo(Route.NUTRITION_POST_WORKOUT);
    }
  }

  private void refresh() {
    if (appState == null) {
      return;
    }
    WorkoutState workout = appState.getWorkout();
    WorkoutSession session = workout.findSession(workout.getSelectedSessionId());
    if (session == null && !workout.getHistory().isEmpty()) {
      session = workout.getHistory().get(0);
    }
    if (session == null) {
      sessionLabel.setText("No session yet");
      return;
    }

    int volume = session.getExercises().stream().mapToInt(SessionExercise::volume).sum();
    int sets = session.getExercises().stream().mapToInt(se -> se.getSets().size()).sum();
    WorkoutXp xp = XpEngine.computeWorkoutXp(session);

    sessionLabel.setText(session.getName());
    volumeLabel.setText(volume + " kg");
    setsLabel.setText(String.valueOf(sets));
    durationLabel.setText(FormatUtil.formatDuration(session.durationMs()));
    xpLabel.setText(String.valueOf(xp.getTotal()));
    xpBreakdownLabel.setText(xp.getStr() + " / " + xp.getSta() + " / " + xp.getAgi());
  }
}
