import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javafx.application.Application;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class MainApp extends Application {
  @Override
  public void start(Stage stage) throws Exception {
    Path fontPath = Paths.get("..", "javafx", "fonts", "PressStart2P-Regular.ttf")
        .toAbsolutePath()
        .normalize();
    if (Files.exists(fontPath)) {
      Font.loadFont(fontPath.toUri().toString(), 12);
    }

    AppState appState = new AppState();
    SceneFactory sceneFactory = new SceneFactory(appState);
    Router router = new Router(stage, sceneFactory);
    sceneFactory.setRouter(router);

    router.goTo(Route.ONBOARDING);
  }

  public static void main(String[] args) {
    launch(args);
  }
}
