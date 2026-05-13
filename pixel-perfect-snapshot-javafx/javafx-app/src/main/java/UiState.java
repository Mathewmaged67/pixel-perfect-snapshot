public class UiState {
  private String theme = "dark";
  private String units = "kg";

  public String getTheme() {
    return theme;
  }

  public void setTheme(String theme) {
    this.theme = theme;
  }

  public String getUnits() {
    return units;
  }

  public void setUnits(String units) {
    this.units = units;
  }

  public void reset() {
    theme = "dark";
    units = "kg";
  }
}
