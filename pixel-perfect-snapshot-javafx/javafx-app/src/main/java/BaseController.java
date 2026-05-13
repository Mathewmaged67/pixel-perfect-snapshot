import javafx.fxml.FXML;

public abstract class BaseController implements AppController {
  protected Router router;
  protected AppState appState;

  @FXML private NavController navController;

  @Override
  public void setRouter(Router router) {
    this.router = router;
    if (navController != null) {
      navController.setRouter(router);
    }
  }

  @Override
  public void setAppState(AppState appState) {
    this.appState = appState;
  }

  @Override
  public void onNavigatedTo(Route route) {
    if (navController != null) {
      navController.setActive(route);
    }
  }
}
