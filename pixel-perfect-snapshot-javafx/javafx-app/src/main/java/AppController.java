public interface AppController {
  void setRouter(Router router);
  void setAppState(AppState appState);

  default void onNavigatedTo(Route route) {
    // optional
  }
}
