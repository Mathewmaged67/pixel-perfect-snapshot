public class NutritionTotals {
  private int kcal;
  private int protein;
  private int carbs;
  private int fat;

  public NutritionTotals(int kcal, int protein, int carbs, int fat) {
    this.kcal = kcal;
    this.protein = protein;
    this.carbs = carbs;
    this.fat = fat;
  }

  public int getKcal() {
    return kcal;
  }

  public void setKcal(int kcal) {
    this.kcal = kcal;
  }

  public int getProtein() {
    return protein;
  }

  public void setProtein(int protein) {
    this.protein = protein;
  }

  public int getCarbs() {
    return carbs;
  }

  public void setCarbs(int carbs) {
    this.carbs = carbs;
  }

  public int getFat() {
    return fat;
  }

  public void setFat(int fat) {
    this.fat = fat;
  }

  public NutritionTotals copy() {
    return new NutritionTotals(kcal, protein, carbs, fat);
  }
}
