public enum CharacterClass {
  WARRIOR("Warrior", "Strength and powerlifting"),
  RANGER("Ranger", "Cardio and endurance"),
  MAGE("Mage", "Mobility and yoga"),
  PALADIN("Paladin", "Balanced fitness");

  private final String label;
  private final String tagline;

  CharacterClass(String label, String tagline) {
    this.label = label;
    this.tagline = tagline;
  }

  public String getLabel() {
    return label;
  }

  public String getTagline() {
    return tagline;
  }

  public static CharacterClass fromString(String value) {
    if (value == null) {
      return WARRIOR;
    }
    return CharacterClass.valueOf(value.trim().toUpperCase());
  }
}
