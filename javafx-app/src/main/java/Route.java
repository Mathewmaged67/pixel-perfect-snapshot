public enum Route {
  LOGIN("Login", "Login.fxml", 1040, 720, NavGroup.NONE),
  SIGNUP("Create Account", "Signup.fxml", 1040, 720, NavGroup.NONE),
  ONBOARDING("Onboarding", "Onboarding.fxml", 1040, 720, NavGroup.NONE),
  DASHBOARD("Dashboard", "Dashboard.fxml", 1280, 800, NavGroup.DASHBOARD),
  QUESTS("Quests", "Quests.fxml", 1280, 800, NavGroup.QUESTS),
  INVENTORY("Inventory", "Inventory.fxml", 1280, 800, NavGroup.INVENTORY),
  NUTRITION_TODAY("Nutrition Today", "NutritionToday.fxml", 1280, 800, NavGroup.NUTRITION),
  NUTRITION_LOG("Nutrition Log", "NutritionLog.fxml", 1280, 800, NavGroup.NUTRITION),
  NUTRITION_GOALS("Nutrition Goals", "NutritionGoals.fxml", 1280, 800, NavGroup.NUTRITION),
  NUTRITION_POST_WORKOUT("Post Workout", "NutritionPostWorkout.fxml", 1280, 800, NavGroup.NUTRITION),
  WORKOUT_INDEX("Workout", "WorkoutIndex.fxml", 1280, 800, NavGroup.WORKOUT),
  WORKOUT_ACTIVE("Workout Session", "WorkoutActive.fxml", 1280, 800, NavGroup.WORKOUT),
  WORKOUT_HISTORY("Workout History", "WorkoutHistory.fxml", 1280, 800, NavGroup.WORKOUT),
  WORKOUT_TEMPLATES("Workout Templates", "WorkoutTemplates.fxml", 1280, 800, NavGroup.WORKOUT),
  WORKOUT_SUMMARY("Workout Summary", "WorkoutSummary.fxml", 1280, 800, NavGroup.WORKOUT),
  PROFILE("Profile", "Profile.fxml", 1280, 800, NavGroup.PROFILE);

  private final String title;
  private final String fxmlFile;
  private final int width;
  private final int height;
  private final NavGroup navGroup;

  Route(String title, String fxmlFile, int width, int height, NavGroup navGroup) {
    this.title = title;
    this.fxmlFile = fxmlFile;
    this.width = width;
    this.height = height;
    this.navGroup = navGroup;
  }

  public String getTitle() {
    return title;
  }

  public String getFxmlFile() {
    return fxmlFile;
  }

  public int getWidth() {
    return width;
  }

  public int getHeight() {
    return height;
  }

  public NavGroup getNavGroup() {
    return navGroup;
  }
}
