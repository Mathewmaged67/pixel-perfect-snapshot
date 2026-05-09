public class GearItem {
  private final String id;
  private final String name;
  private final Rarity rarity;
  private final String slot;
  private final int spriteIndex;
  private final String unlockHint;
  private boolean owned;

  public GearItem(String id, String name, Rarity rarity, String slot,
                  int spriteIndex, String unlockHint, boolean owned) {
    this.id = id;
    this.name = name;
    this.rarity = rarity;
    this.slot = slot;
    this.spriteIndex = spriteIndex;
    this.unlockHint = unlockHint;
    this.owned = owned;
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public Rarity getRarity() {
    return rarity;
  }

  public String getSlot() {
    return slot;
  }

  public int getSpriteIndex() {
    return spriteIndex;
  }

  public String getUnlockHint() {
    return unlockHint;
  }

  public boolean isOwned() {
    return owned;
  }

  public void setOwned(boolean owned) {
    this.owned = owned;
  }
}
