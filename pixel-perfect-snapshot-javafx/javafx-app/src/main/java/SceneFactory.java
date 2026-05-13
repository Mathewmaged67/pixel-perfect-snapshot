import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class SceneFactory {
  private final AppState appState;
  private Router router;
  private final Path fxmlRoot = Paths.get("..", "javafx").toAbsolutePath().normalize();
  private final Path appCss = fxmlRoot.resolve("app.css");

  public SceneFactory(AppState appState) {
    this.appState = appState;
  }

  public void setRouter(Router router) {
    this.router = router;
  }

  public Scene createScene(Route route) throws Exception {
    Path fxmlPath = fxmlRoot.resolve(route.getFxmlFile());
    FXMLLoader loader = new FXMLLoader(fxmlPath.toUri().toURL());
    Parent root = loader.load();
    Object controller = loader.getController();
    if (controller instanceof AppController appController) {
      appController.setRouter(router);
      appController.setAppState(appState);
      appController.onNavigatedTo(route);
    }

    Scene scene = new Scene(root, route.getWidth(), route.getHeight());
    if (Files.exists(appCss)) {
      scene.getStylesheets().add(appCss.toUri().toString());
    }
    if ("light".equalsIgnoreCase(appState.getUi().getTheme())) {
      root.getStyleClass().add("theme-light");
    } else {
      root.getStyleClass().add("theme-dark");
    }
    return scene;
  }
}
