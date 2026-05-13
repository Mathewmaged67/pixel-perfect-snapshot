public class Food {
  private final String id;
  private final String name;
  private final int kcal;
  private final int protein;
  private final int carbs;
  private final int fat;
  private final String serving;
  private final String tag;

  public Food(String id, String name, int kcal, int protein, int carbs, int fat,
              String serving, String tag) {
    this.id = id;
    this.name = name;
    this.kcal = kcal;
    this.protein = protein;
    this.carbs = carbs;
    this.fat = fat;
    this.serving = serving;
    this.tag = tag;
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public int getKcal() {
    return kcal;
  }

  public int getProtein() {
    return protein;
  }

  public int getCarbs() {
    return carbs;
  }

  public int getFat() {
    return fat;
  }

  public String getServing() {
    return serving;
  }

  public String getTag() {
    return tag;
  }
}
