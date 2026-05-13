import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

public final class RestTimerDialog {
  private RestTimerDialog() {}

  public static void show(int seconds) {
    Stage stage = new Stage();
    stage.initModality(Modality.APPLICATION_MODAL);
    stage.setTitle("Rest Timer");

    Label label = new Label();
    label.getStyleClass().add("page-title");
    Label sub = new Label("Recover and get ready for the next set.");
    sub.getStyleClass().add("muted");

    Button skip = new Button("Skip");
    skip.getStyleClass().add("ghost-button");
    skip.setOnAction(event -> stage.close());

    VBox root = new VBox(10, label, sub, skip);
    root.setAlignment(Pos.CENTER);
    root.getStyleClass().add("card");
    Scene scene = new Scene(root, 320, 200);
    Path cssPath = Paths.get("..", "javafx", "app.css").toAbsolutePath().normalize();
    if (Files.exists(cssPath)) {
      scene.getStylesheets().add(cssPath.toUri().toString());
    }
    stage.setScene(scene);

    final int[] remaining = { seconds };
    label.setText("Rest " + remaining[0] + "s");

    Timeline timer = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
      remaining[0] -= 1;
      label.setText("Rest " + remaining[0] + "s");
      if (remaining[0] <= 0) {
        stage.close();
      }
    }));
    timer.setCycleCount(seconds);
    timer.play();

    stage.show();
  }
}
