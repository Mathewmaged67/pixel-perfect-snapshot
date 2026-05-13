package lifexp.managers;

import lifexp.models.Character;
import lifexp.models.XpLog;
import lifexp.repositories.CharacterRepository;
import lifexp.repositories.XpLogRepository;
import lifexp.logic.StreakEngine;
import lifexp.logic.XpCalculationEngine;
import lifexp.logic.LevelUpEngine;

/**
 * Manager for XP and experience point operations.
 * Handles XP distribution, level progression, and XP tracking.
 */
public class XpManager {
    private CharacterRepository characterRepository;
    private XpLogRepository xpLogRepository;

    public XpManager(CharacterRepository characterRepository, XpLogRepository xpLogRepository) {
        this.characterRepository = characterRepository;
        this.xpLogRepository = xpLogRepository;
    }

    /**
     * Add XP to a character and handle level-ups
     */
    public void addXp(String characterId, long xpAmount, String source) {
        Character character = characterRepository.findById(characterId).orElse(null);
        if (character == null) return;

        int levelBefore = character.getLevel();
        character.addXp(xpAmount);
        int levelAfter = character.getLevel();

        // Log the XP transaction
        String logId = "xp_" + System.currentTimeMillis();
        XpLog log = new XpLog(logId, characterId, xpAmount, source);
        log.setTotalXpAfter(character.getTotalXp());
        xpLogRepository.save(log);

        characterRepository.update(character);

        // Handle level-up if applicable
        if (levelAfter > levelBefore) {
            onLevelUp(character, levelBefore, levelAfter);
        }
    }

    /**
     * Handle level-up event
     */
    private void onLevelUp(Character character, int oldLevel, int newLevel) {
        LevelUpEngine.LevelUpReward reward = LevelUpEngine.getLevelUpRewards(newLevel);
        character.addGold(reward.getGoldBonus());
        // Additional level-up effects can be added here
    }

    /**
     * Apply streak multiplier to XP reward
     */
    public long applyStreakMultiplier(long baseXp, int streak) {
        float multiplier = StreakEngine.calculateStreakMultiplier(streak);
        return Math.round(baseXp * multiplier);
    }

    /**
     * Get total XP for a character
     */
    public long getTotalXp(String characterId) {
        Character character = characterRepository.findById(characterId).orElse(null);
        if (character == null) return 0;
        return character.getTotalXp();
    }

    /**
     * Get current level for a character
     */
    public int getLevel(String characterId) {
        Character character = characterRepository.findById(characterId).orElse(null);
        if (character == null) return 1;
        return character.getLevel();
    }

    /**
     * Get XP progress to next level
     */
    public long getXpToNextLevel(String characterId) {
        Character character = characterRepository.findById(characterId).orElse(null);
        if (character == null) return 0;
        return XpCalculationEngine.getXpForNextLevel(character.getLevel()) - character.getTotalXp();
    }

    /**
     * Get XP log history for a character
     */
    public java.util.List<XpLog> getXpHistory(String characterId) {
        return xpLogRepository.findByCharacterId(characterId);
    }
}
