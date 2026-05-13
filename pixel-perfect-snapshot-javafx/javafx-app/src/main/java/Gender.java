public enum Gender {
  MALE,
  FEMALE;

  public static Gender fromString(String value) {
    if (value == null) {
      return MALE;
    }
    return Gender.valueOf(value.trim().toUpperCase());
  }
}
