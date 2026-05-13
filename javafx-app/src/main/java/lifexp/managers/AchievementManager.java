package lifexp.managers;

import lifexp.enums.AchievementType;
import lifexp.logic.AchievementEngine;
import lifexp.models.Achievement;
import lifexp.models.Character;
import lifexp.repositories.AchievementRepository;
import lifexp.repositories.CharacterRepository;
import lifexp.repositories.HabitRepository;
import lifexp.repositories.QuestRepository;
import lifexp.repositories.WorkoutRepository;

/**
 * Manager for achievement operations.
 * Handles achievement tracking, unlocking, and milestone detection.
 */
public class AchievementManager {
    private AchievementRepository achievementRepository;
    private CharacterRepository characterRepository;
    private HabitRepository habitRepository;
    private WorkoutRepository workoutRepository;
    private QuestRepository questRepository;
    private XpManager xpManager;

    public AchievementManager(
            AchievementRepository achievementRepository,
            CharacterRepository characterRepository,
            HabitRepository habitRepository,
            WorkoutRepository workoutRepository,
            QuestRepository questRepository,
            XpManager xpManager) {
        this.achievementRepository = achievementRepository;
        this.characterRepository = characterRepository;
        this.habitRepository = habitRepository;
        this.workoutRepository = workoutRepository;
        this.questRepository = questRepository;
        this.xpManager = xpManager;
    }

    /**
     * Check and unlock achievements for a character
     */
    public void checkAndUnlockAchievements(String characterId) {
        Character character = characterRepository.findById(characterId).orElse(null);
        if (character == null) return;

        // Check level achievements
        if (AchievementEngine.shouldUnlockLevelAchievement(character.getLevel())) {
            unlockLevelAchievement(characterId, character.getLevel());
        }

        // Check XP achievements
        if (AchievementEngine.shouldUnlockXpAchievement(character.getTotalXp())) {
            unlockXpAchievement(characterId, character.getTotalXp());
        }

        // Check habit achievements
        long habitCount = habitRepository.countByUserId(character.getUserId());
        if (AchievementEngine.shouldUnlockHabitAchievement((int) habitCount)) {
            unlockHabitAchievement(characterId, (int) habitCount);
        }

        // Check workout achievements
        long workoutCount = workoutRepository.countCompletedByUserId(character.getUserId());
        if (AchievementEngine.shouldUnlockWorkoutAchievement((int) workoutCount)) {
            unlockWorkoutAchievement(characterId, (int) workoutCount);
        }
    }

    /**
     * Unlock a level achievement
     */
    private void unlockLevelAchievement(String characterId, int level) {
        String achievementId = "ach_level_" + level;
        Achievement achievement = achievementRepository.findById(achievementId).orElse(null);
        if (achievement != null && !achievement.isUnlocked()) {
            achievement.unlock();
            achievementRepository.update(achievement);
        }
    }

    /**
     * Unlock an XP achievement
     */
    private void unlockXpAchievement(String characterId, long totalXp) {
        String achievementId = "ach_xp_" + totalXp;
        Achievement achievement = achievementRepository.findById(achievementId).orElse(null);
        if (achievement != null && !achievement.isUnlocked()) {
            achievement.unlock();
            achievementRepository.update(achievement);
        }
    }

    /**
     * Unlock a habit achievement
     */
    private void unlockHabitAchievement(String characterId, int habitCount) {
        String achievementId = "ach_habit_" + habitCount;
        Achievement achievement = achievementRepository.findById(achievementId).orElse(null);
        if (achievement != null && !achievement.isUnlocked()) {
            achievement.unlock();
            achievementRepository.update(achievement);
        }
    }

    /**
     * Unlock a workout achievement
     */
    private void unlockWorkoutAchievement(String characterId, int workoutCount) {
        String achievementId = "ach_workout_" + workoutCount;
        Achievement achievement = achievementRepository.findById(achievementId).orElse(null);
        if (achievement != null && !achievement.isUnlocked()) {
            achievement.unlock();
            achievementRepository.update(achievement);
        }
    }

    /**
     * Get all achievements for a character
     */
    public java.util.List<Achievement> getCharacterAchievements(String characterId) {
        return achievementRepository.findByCharacterId(characterId);
    }

    /**
     * Get unlocked achievements for a character
     */
    public java.util.List<Achievement> getUnlockedAchievements(String characterId) {
        return achievementRepository.findUnlockedByCharacterId(characterId);
    }

    /**
     * Get locked achievements for a character
     */
    public java.util.List<Achievement> getLockedAchievements(String characterId) {
        return achievementRepository.findLockedByCharacterId(characterId);
    }

    /**
     * Get total achievement XP for a character
     */
    public long getTotalAchievementXp(String characterId) {
        return achievementRepository.getTotalXpByCharacterId(characterId);
    }

    /**
     * Get achievement completion percentage
     */
    public float getCompletionPercentage(String characterId) {
        long total = achievementRepository.countByCharacterId(characterId);
        long unlocked = achievementRepository.countUnlockedByCharacterId(characterId);
        if (total == 0) return 0;
        return (float) unlocked / total * 100;
    }

    /**
     * Get achievement statistics
     */
    public java.util.Map<String, Object> getAchievementStats(String characterId) {
        java.util.Map<String, Object> stats = new java.util.HashMap<>();
        java.util.List<Achievement> all = getCharacterAchievements(characterId);
        java.util.List<Achievement> unlocked = getUnlockedAchievements(characterId);

        stats.put("totalAchievements", all.size());
        stats.put("unlockedAchievements", unlocked.size());
        stats.put("completionPercentage", getCompletionPercentage(characterId));
        stats.put("totalXp", getTotalAchievementXp(characterId));

        return stats;
    }
}
