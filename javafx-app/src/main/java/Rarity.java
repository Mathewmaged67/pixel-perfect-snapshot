public enum Rarity {
  COMMON,
  RARE,
  EPIC,
  LEGEND;

  public static Rarity fromString(String value) {
    if (value == null) {
      return COMMON;
    }
    return Rarity.valueOf(value.trim().toUpperCase());
  }
}
