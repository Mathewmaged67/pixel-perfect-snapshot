import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;

public class ProfileController extends BaseController {
  @FXML private BorderPane root;
  @FXML private Label nameLabel;
  @FXML private Label classLabel;
  @FXML private Label strLabel;
  @FXML private Label staLabel;
  @FXML private Label vitLabel;
  @FXML private Label agiLabel;
  @FXML private javafx.scene.image.ImageView spriteView;
  @FXML private GridPane heatmapGrid;
  @FXML private Label statusLabel;
  @FXML private Button musicButton;

  @Override
  public void onNavigatedTo(Route route) {
    super.onNavigatedTo(route);
    refresh();
  }

  @FXML
  private void onThemeDark() {
    if (appState == null) {
      return;
    }
    appState.getUi().setTheme("dark");
    statusLabel.setText("Theme set to dark.");
    applyTheme();
  }

  @FXML
  private void onThemeLight() {
    if (appState == null) {
      return;
    }
    appState.getUi().setTheme("light");
    statusLabel.setText("Theme set to light.");
    applyTheme();
  }

  @FXML
  private void onUnitsKg() {
    if (appState == null) {
      return;
    }
    appState.getUi().setUnits("kg");
    statusLabel.setText("Units set to kg.");
  }

  @FXML
  private void onUnitsLbs() {
    if (appState == null) {
      return;
    }
    appState.getUi().setUnits("lbs");
    statusLabel.setText("Units set to lbs.");
  }

  @FXML
  private void onReset() {
    if (appState == null) {
      return;
    }
    appState.resetAll();
    statusLabel.setText("Character reset.");
    router.goTo(Route.ONBOARDING);
  }

  @FXML
  private void onLogout() {
    router.goTo(Route.LOGIN);
  }

  @FXML
  private void onToggleMusic() {
    AudioManager audio = AudioManager.getInstance();
    if (audio.isPlaying()) {
      audio.pauseMusic();
      musicButton.setText("♫ Music Off");
    } else {
      if (audio.getCurrentTrack() != null) {
        audio.resumeMusic();
      } else {
        audio.playLoopingMusic("/music/background-music.mp3");
      }
      musicButton.setText("♫ Music On");
    }
  }

  private void refresh() {
    if (appState == null) {
      return;
    }
    CharacterState character = appState.getCharacter();
    nameLabel.setText(character.getName().isEmpty() ? "Hero" : character.getName());
    classLabel.setText(character.getCharacterClass().getLabel());
    SpriteUtil.applyCharacterSprite(spriteView, character, 180);

    strLabel.setText(String.valueOf(character.getStats().getOrDefault(StatKey.STR, 0)));
    staLabel.setText(String.valueOf(character.getStats().getOrDefault(StatKey.STA, 0)));
    vitLabel.setText(String.valueOf(character.getStats().getOrDefault(StatKey.VIT, 0)));
    agiLabel.setText(String.valueOf(character.getStats().getOrDefault(StatKey.AGI, 0)));

    buildHeatmap();
    applyTheme();
    updateMusicButton();
  }

  private void applyTheme() {
    if (root == null || appState == null) {
      return;
    }
    root.getStyleClass().removeAll("theme-dark", "theme-light");
    if ("light".equalsIgnoreCase(appState.getUi().getTheme())) {
      root.getStyleClass().add("theme-light");
    } else {
      root.getStyleClass().add("theme-dark");
    }
  }

  private void updateMusicButton() {
    if (musicButton == null) return;
    if (AudioManager.getInstance().isPlaying()) {
      musicButton.setText("♫ Music On");
    } else {
      musicButton.setText("♫ Music Off");
    }
  }

  private void buildHeatmap() {
    heatmapGrid.getChildren().clear();
    Map<LocalDate, Integer> sessionsByDay = new HashMap<>();
    for (WorkoutSession session : appState.getWorkout().getHistory()) {
      LocalDate date = Instant.ofEpochMilli(session.getStartedAt())
          .atZone(ZoneId.systemDefault())
          .toLocalDate();
      sessionsByDay.put(date, sessionsByDay.getOrDefault(date, 0) + 1);
    }

    LocalDate today = LocalDate.now();
    for (int i = 0; i < 35; i++) {
      LocalDate day = today.minusDays(34 - i);
      int count = sessionsByDay.getOrDefault(day, 0);
      int intensity = Math.min(4, count);
      Region cell = new Region();
      cell.getStyleClass().add("heat-cell");
      cell.getStyleClass().add("heat-" + intensity);
      heatmapGrid.add(cell, i % 7, i / 7);
    }
  }
}
