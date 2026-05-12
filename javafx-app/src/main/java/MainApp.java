import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javafx.application.Application;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class MainApp extends Application {
  @Override
  public void start(Stage stage) throws Exception {
    // Try loading font from the module resources (classpath) first
    try (var is = MainApp.class.getResourceAsStream("/javafx/fonts/Minecraft.ttf")) {
      if (is != null) {
        Font f = Font.loadFont(is, 12);
        System.out.println("Loaded font from classpath: " + (f == null ? "null" : f.getName() + " / " + f.getFamily()));
      } else {
        // Fallback: load from the workspace javafx/fonts directory (relative path)
        Path fontPath = Paths.get("..", "javafx", "fonts", "Minecraft.ttf").toAbsolutePath().normalize();
        if (Files.exists(fontPath)) {
          Font f = Font.loadFont(fontPath.toUri().toString(), 12);
          System.out.println("Loaded font from file: " + (f == null ? "null" : f.getName() + " / " + f.getFamily()));
        } else {
          System.out.println("Font not found at classpath or path: " + fontPath);
        }
      }
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
