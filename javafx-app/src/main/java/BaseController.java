import javafx.fxml.FXML;

public abstract class BaseController implements AppController {
  protected Router router;
  protected AppState appState;

  @Override
  public void setRouter(Router router) {
    this.router = router;
  }

  @Override
  public void setAppState(AppState appState) {
    this.appState = appState;
  }

  @Override
  public void onNavigatedTo(Route route) {
    // Navigation styling is handled by individual page controllers if needed
  }
}
