package lifexp.app;

import lifexp.managers.*;
import lifexp.repositories.*;
import lifexp.threads.BackgroundTaskPool;
import lifexp.networking.LeaderboardClient;

/**
 * Application context holding all repositories and managers.
 * Provides singleton access to core application services.
 * Acts as the dependency injection container for the application.
 */
public class AppContext {
    
    // Repositories
    private final UserRepository userRepository;
    private final CharacterRepository characterRepository;
    private final HabitRepository habitRepository;
    private final WorkoutRepository workoutRepository;
    private final QuestRepository questRepository;
    private final AchievementRepository achievementRepository;
    private final XpLogRepository xpLogRepository;
    private final LeaderboardRepository leaderboardRepository;

    // Managers
    private final XpManager xpManager;
    private final HabitManager habitManager;
    private final WorkoutManager workoutManager;
    private final QuestManager questManager;
    private final AchievementManager achievementManager;

    // Services
    private final BackgroundTaskPool backgroundTaskPool;
    private final LeaderboardClient leaderboardClient;

    public AppContext() {
        // Initialize repositories
        this.userRepository = new UserRepository();
        this.characterRepository = new CharacterRepository();
        this.habitRepository = new HabitRepository();
        this.workoutRepository = new WorkoutRepository();
        this.questRepository = new QuestRepository();
        this.achievementRepository = new AchievementRepository();
        this.xpLogRepository = new XpLogRepository();
        this.leaderboardRepository = new LeaderboardRepository();

        // Initialize managers with dependencies
        this.xpManager = new XpManager(characterRepository, xpLogRepository);
        this.habitManager = new HabitManager(habitRepository, xpManager);
        this.workoutManager = new WorkoutManager(workoutRepository, xpManager);
        this.questManager = new QuestManager(questRepository, xpManager);
        this.achievementManager = new AchievementManager(
                achievementRepository,
                characterRepository,
                habitRepository,
                workoutRepository,
                questRepository,
                xpManager
        );

        // Initialize services
        this.backgroundTaskPool = new BackgroundTaskPool();
        this.leaderboardClient = new LeaderboardClient("localhost", 9999);
    }

    // Repository getters
    public UserRepository getUserRepository() { return userRepository; }
    public CharacterRepository getCharacterRepository() { return characterRepository; }
    public HabitRepository getHabitRepository() { return habitRepository; }
    public WorkoutRepository getWorkoutRepository() { return workoutRepository; }
    public QuestRepository getQuestRepository() { return questRepository; }
    public AchievementRepository getAchievementRepository() { return achievementRepository; }
    public XpLogRepository getXpLogRepository() { return xpLogRepository; }
    public LeaderboardRepository getLeaderboardRepository() { return leaderboardRepository; }

    // Manager getters
    public XpManager getXpManager() { return xpManager; }
    public HabitManager getHabitManager() { return habitManager; }
    public WorkoutManager getWorkoutManager() { return workoutManager; }
    public QuestManager getQuestManager() { return questManager; }
    public AchievementManager getAchievementManager() { return achievementManager; }

    // Service getters
    public BackgroundTaskPool getBackgroundTaskPool() { return backgroundTaskPool; }
    public LeaderboardClient getLeaderboardClient() { return leaderboardClient; }

    /**
     * Shutdown the application context
     */
    public void shutdown() {
        backgroundTaskPool.shutdown();
        leaderboardClient.disconnect();
    }
}
