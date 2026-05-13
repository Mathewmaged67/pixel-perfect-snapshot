import java.util.EnumMap;
import java.util.Map;

public class CharacterState {
  private boolean initialized;
  private String name;
  private CharacterClass characterClass;
  private Gender gender;
  private int level;
  private int xp;
  private int xpToNext;
  private int gold;
  private int hp;
  private int hpMax;
  private int mp;
  private int mpMax;
  private final Map<StatKey, Integer> stats = new EnumMap<>(StatKey.class);
  private int weight;
  private int height;
  private int weeklyTarget;
  private int streak;

  public CharacterState() {
    resetDefaults();
  }

  public void init(String name, CharacterClass characterClass, Gender gender,
                   int weight, int height, int weeklyTarget) {
    this.initialized = true;
    this.name = name;
    this.characterClass = characterClass;
    this.gender = gender;
    this.weight = weight;
    this.height = height;
    this.weeklyTarget = weeklyTarget;
    this.level = 1;
    this.xp = 0;
    this.xpToNext = xpForLevel(1);
    this.gold = 50;
    this.hp = 80;
    this.hpMax = 100;
    this.mp = 60;
    this.mpMax = 100;
    this.stats.put(StatKey.STR, 10);
    this.stats.put(StatKey.STA, 10);
    this.stats.put(StatKey.VIT, 10);
    this.stats.put(StatKey.AGI, 10);
    this.streak = 1;
  }

  public XpResult addXp(int amount) {
    int newXp = xp + Math.max(0, amount);
    int newLevel = level;
    int newXpToNext = xpToNext;
    boolean leveledUp = false;
    while (newXp >= newXpToNext) {
      newXp -= newXpToNext;
      newLevel += 1;
      newXpToNext = xpForLevel(newLevel);
      leveledUp = true;
    }
    xp = newXp;
    level = newLevel;
    xpToNext = newXpToNext;
    return new XpResult(leveledUp);
  }

  public void addStat(StatKey stat, int amount) {
    stats.put(stat, stats.getOrDefault(stat, 0) + amount);
  }

  public void addGold(int amount) {
    gold += amount;
  }

  public void addHp(int amount) {
    hp = clamp(hp + amount, 0, hpMax);
  }

  public void addMp(int amount) {
    mp = clamp(mp + amount, 0, mpMax);
  }

  public void reset() {
    initialized = false;
    resetDefaults();
  }

  private void resetDefaults() {
    name = "";
    characterClass = CharacterClass.WARRIOR;
    gender = Gender.MALE;
    level = 1;
    xp = 0;
    xpToNext = xpForLevel(1);
    gold = 50;
    hp = 80;
    hpMax = 100;
    mp = 60;
    mpMax = 100;
    stats.put(StatKey.STR, 10);
    stats.put(StatKey.STA, 10);
    stats.put(StatKey.VIT, 10);
    stats.put(StatKey.AGI, 10);
    weight = 75;
    height = 175;
    weeklyTarget = 4;
    streak = 0;
  }

  private int xpForLevel(int lvl) {
    return 100 + lvl * 80;
  }

  private int clamp(int value, int min, int max) {
    return Math.max(min, Math.min(max, value));
  }

  public boolean isInitialized() {
    return initialized;
  }

  public String getName() {
    return name;
  }

  public CharacterClass getCharacterClass() {
    return characterClass;
  }

  public Gender getGender() {
    return gender;
  }

  public int getLevel() {
    return level;
  }

  public int getXp() {
    return xp;
  }

  public int getXpToNext() {
    return xpToNext;
  }

  public int getGold() {
    return gold;
  }

  public int getHp() {
    return hp;
  }

  public int getHpMax() {
    return hpMax;
  }

  public int getMp() {
    return mp;
  }

  public int getMpMax() {
    return mpMax;
  }

  public Map<StatKey, Integer> getStats() {
    return stats;
  }

  public int getWeight() {
    return weight;
  }

  public int getHeight() {
    return height;
  }

  public int getWeeklyTarget() {
    return weeklyTarget;
  }

  public int getStreak() {
    return streak;
  }

  public void setStreak(int streak) {
    this.streak = streak;
  }
}
