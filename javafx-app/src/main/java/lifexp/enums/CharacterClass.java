package lifexp.enums;

/**
 * Represents different character classes with unique stat bonuses.
 */
public enum CharacterClass {
    WARRIOR("Warrior", 1.2f, 0.9f, 0.8f),
    MAGE("Mage", 0.8f, 1.2f, 0.9f),
    ROGUE("Rogue", 0.9f, 0.8f, 1.2f),
    PALADIN("Paladin", 1.1f, 1.0f, 0.9f),
    RANGER("Ranger", 1.0f, 0.9f, 1.1f);

    private final String displayName;
    private final float strengthBonus;
    private final float intelligenceBonus;
    private final float agilityBonus;

    CharacterClass(String displayName, float strengthBonus, float intelligenceBonus, float agilityBonus) {
        this.displayName = displayName;
        this.strengthBonus = strengthBonus;
        this.intelligenceBonus = intelligenceBonus;
        this.agilityBonus = agilityBonus;
    }

    public String getDisplayName() {
        return displayName;
    }

    public float getStrengthBonus() {
        return strengthBonus;
    }

    public float getIntelligenceBonus() {
        return intelligenceBonus;
    }

    public float getAgilityBonus() {
        return agilityBonus;
    }
}
