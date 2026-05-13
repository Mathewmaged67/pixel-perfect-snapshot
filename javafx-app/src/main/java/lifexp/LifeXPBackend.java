package lifexp;

import lifexp.app.LifeXPApplication;
import lifexp.enums.CharacterClass;
import lifexp.enums.HabitCategory;
import lifexp.enums.QuestDifficulty;
import lifexp.enums.WorkoutType;
import lifexp.models.Character;
import lifexp.models.Habit;
import lifexp.models.Quest;
import lifexp.models.Workout;
import lifexp.utils.IdGenerator;

/**
 * LifeXP Backend Demo and Entry Point.
 * Demonstrates core application functionality and systems.
 */
public class LifeXPBackend {
    
    public static void main(String[] args) {
        // Initialize application
        LifeXPApplication app = LifeXPApplication.getInstance();
        LifeXPApplication.printApplicationInfo();

        try {
            // Demo: Create a user and character
            System.out.println("Creating new user...");
            Character character = app.createNewUser(
                    "DragonSlayer",
                    "player@lifexp.com",
                    "Aragorn",
                    CharacterClass.WARRIOR
            );
            System.out.println("✓ Created character: " + character.getName());
            System.out.println("✓ Class: " + character.getCharacterClass().getDisplayName());
            System.out.println();

            // Demo: Initialize achievements
            System.out.println("Initializing achievements...");
            app.initializeAchievements(character.getId());
            System.out.println("✓ Achievements initialized");
            System.out.println();

            // Demo: Create habits
            System.out.println("Creating habits...");
            var habitManager = app.getAppContext().getHabitManager();
            Habit habit1 = habitManager.createHabit(character.getUserId(), "Morning Jog", HabitCategory.HEALTH, 5);
            Habit habit2 = habitManager.createHabit(character.getUserId(), "Read Book", HabitCategory.LEARNING, 7);
            System.out.println("✓ Created " + 2 + " habits");
            System.out.println();

            // Demo: Create and complete workouts
            System.out.println("Creating workouts...");
            var workoutManager = app.getAppContext().getWorkoutManager();
            Workout workout1 = workoutManager.createWorkout(character.getUserId(), "Upper Body", WorkoutType.STRENGTH);
            Workout workout2 = workoutManager.createWorkout(character.getUserId(), "Cardio Run", WorkoutType.CARDIO);
            System.out.println("✓ Created " + 2 + " workouts");
            System.out.println();

            // Demo: Create quests
            System.out.println("Creating quests...");
            var questManager = app.getAppContext().getQuestManager();
            Quest quest1 = questManager.createDailyQuest(
                    character.getId(),
                    "Morning Champion",
                    "Complete 3 morning habits",
                    QuestDifficulty.NORMAL
            );
            System.out.println("✓ Created " + 1 + " quest");
            System.out.println();

            // Demo: Show system statistics
            System.out.println("System Statistics:");
            java.util.Map<String, Object> stats = app.getSystemStats();
            stats.forEach((key, value) -> System.out.println("  " + key + ": " + value));
            System.out.println();

            // Demo: Show character stats
            System.out.println("Character Statistics:");
            System.out.println("  Level: " + character.getLevel());
            System.out.println("  Total XP: " + character.getTotalXp());
            System.out.println("  Gold: " + character.getGold());
            System.out.println("  Strength: " + String.format("%.1f", character.getStrength()));
            System.out.println("  Intelligence: " + String.format("%.1f", character.getIntelligence()));
            System.out.println("  Agility: " + String.format("%.1f", character.getAgility()));
            System.out.println();

            System.out.println("✓ LifeXP Backend is ready for integration with JavaFX UI!");
            System.out.println();

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Shutdown application
            app.shutdown();
        }
    }
}
