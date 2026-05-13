package lifexp.logic;

/**
 * Engine for level-up mechanics and progression events.
 * Handles level-up triggers and scaling bonuses.
 */
public class LevelUpEngine {
    
    private static final int HP_PER_LEVEL = 10;
    private static final int MP_PER_LEVEL = 5;
    private static final float STAT_GAIN_PER_LEVEL = 1.2f;

    /**
     * Calculate HP increase for level up
     */
    public static int calculateHpIncrease(int level) {
        return HP_PER_LEVEL + (level / 5);
    }

    /**
     * Calculate MP increase for level up
     */
    public static int calculateMpIncrease(int level) {
        return MP_PER_LEVEL + (level / 10);
    }

    /**
     * Calculate stat increase for level up
     */
    public static float calculateStatIncrease(int level) {
        return STAT_GAIN_PER_LEVEL * (1 + (level * 0.05f));
    }

    /**
     * Get level-up rewards
     */
    public static LevelUpReward getLevelUpRewards(int newLevel) {
        int hpBonus = calculateHpIncrease(newLevel);
        int mpBonus = calculateMpIncrease(newLevel);
        int goldBonus = 100 * newLevel;
        long xpBonus = 0;
        
        return new LevelUpReward(hpBonus, mpBonus, goldBonus, xpBonus);
    }

    /**
     * Check if milestone level reached
     */
    public static boolean isMilestoneLevel(int level) {
        return (level > 1) && (level % 5 == 0);
    }

    /**
     * Get milestone reward
     */
    public static int getMilestoneReward(int level) {
        if (!isMilestoneLevel(level)) return 0;
        return (level / 5) * 500;
    }

    /**
     * Class representing level-up rewards
     */
    public static class LevelUpReward {
        private int hpBonus;
        private int mpBonus;
        private int goldBonus;
        private long xpBonus;

        public LevelUpReward(int hpBonus, int mpBonus, int goldBonus, long xpBonus) {
            this.hpBonus = hpBonus;
            this.mpBonus = mpBonus;
            this.goldBonus = goldBonus;
            this.xpBonus = xpBonus;
        }

        public int getHpBonus() { return hpBonus; }
        public int getMpBonus() { return mpBonus; }
        public int getGoldBonus() { return goldBonus; }
        public long getXpBonus() { return xpBonus; }
    }
}
