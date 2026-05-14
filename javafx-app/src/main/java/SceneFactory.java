import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;

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

    // Initialize the nav controller if it was loaded with this scene
    NavController navController = NavController.getInstance();
    if (navController != null) {
      navController.setRouter(router);
      navController.setActive(route);
    }

    Scene scene = new Scene(root, route.getWidth(), route.getHeight());
    String bgColor = "light".equalsIgnoreCase(appState.getUi().getTheme()) ? "#f2f6f7" : "#10002b";
    scene.setFill(Color.web(bgColor));
    if (Files.exists(appCss)) {
      scene.getStylesheets().add(appCss.toUri().toString());
    }
    root.setStyle("-fx-font-family: 'Jersey 10'; -fx-font-size: 18px;");
    if ("light".equalsIgnoreCase(appState.getUi().getTheme())) {
      root.getStyleClass().add("theme-light");
    } else {
      root.getStyleClass().add("theme-dark");
    }
    return scene;
  }
}
