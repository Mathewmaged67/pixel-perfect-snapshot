public class FoodLogEntry {
  private final String id;
  private final String foodId;
  private final int servings;
  private final long loggedAt;

  public FoodLogEntry(String id, String foodId, int servings, long loggedAt) {
    this.id = id;
    this.foodId = foodId;
    this.servings = servings;
    this.loggedAt = loggedAt;
  }

  public String getId() {
    return id;
  }

  public String getFoodId() {
    return foodId;
  }

  public int getServings() {
    return servings;
  }

  public long getLoggedAt() {
    return loggedAt;
  }
}
