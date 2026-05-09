import javafx.scene.Scene;
import javafx.stage.Stage;

public class Router {
  private final Stage stage;
  private final SceneFactory sceneFactory;
  private Route currentRoute;

  public Router(Stage stage, SceneFactory sceneFactory) {
    this.stage = stage;
    this.sceneFactory = sceneFactory;
  }

  public void goTo(Route route) {
    try {
      Scene scene = sceneFactory.createScene(route);
      stage.setScene(scene);
      stage.setTitle("FitQuest - " + route.getTitle());
      stage.setMinWidth(Math.min(route.getWidth(), 1000));
      stage.setMinHeight(Math.min(route.getHeight(), 700));
      stage.show();
      currentRoute = route;
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public Route getCurrentRoute() {
    return currentRoute;
  }
}
