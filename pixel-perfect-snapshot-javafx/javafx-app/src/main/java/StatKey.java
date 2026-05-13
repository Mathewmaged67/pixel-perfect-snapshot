public enum StatKey {
  STR,
  STA,
  VIT,
  AGI;

  public String label() {
    return switch (this) {
      case STR -> "STR";
      case STA -> "STA";
      case VIT -> "VIT";
      case AGI -> "AGI";
    };
  }
}
