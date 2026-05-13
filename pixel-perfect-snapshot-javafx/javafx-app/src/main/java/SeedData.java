import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class SeedData {
  private static final List<Exercise> EXERCISES = List.of(
      new Exercise("ex-bench", "Barbell Bench Press", List.of(MuscleGroup.CHEST, MuscleGroup.ARMS),
          "barbell", 4, 8, ExerciseCategory.STRENGTH),
      new Exercise("ex-squat", "Back Squat", List.of(MuscleGroup.LEGS, MuscleGroup.GLUTES),
          "barbell", 5, 5, ExerciseCategory.STRENGTH),
      new Exercise("ex-dead", "Deadlift", List.of(MuscleGroup.BACK, MuscleGroup.LEGS, MuscleGroup.GLUTES),
          "barbell", 3, 5, ExerciseCategory.STRENGTH),
      new Exercise("ex-ohp", "Overhead Press", List.of(MuscleGroup.SHOULDERS, MuscleGroup.ARMS),
          "barbell", 4, 6, ExerciseCategory.STRENGTH),
      new Exercise("ex-row", "Barbell Row", List.of(MuscleGroup.BACK, MuscleGroup.ARMS),
          "barbell", 4, 8, ExerciseCategory.STRENGTH),
      new Exercise("ex-pull", "Pull Up", List.of(MuscleGroup.BACK, MuscleGroup.ARMS),
          "bodyweight", 4, 8, ExerciseCategory.STRENGTH),
      new Exercise("ex-dip", "Dip", List.of(MuscleGroup.CHEST, MuscleGroup.ARMS),
          "bodyweight", 3, 10, ExerciseCategory.STRENGTH),
      new Exercise("ex-curl", "Dumbbell Curl", List.of(MuscleGroup.ARMS),
          "dumbbell", 3, 12, ExerciseCategory.STRENGTH),
      new Exercise("ex-tri", "Tricep Pushdown", List.of(MuscleGroup.ARMS),
          "machine", 3, 12, ExerciseCategory.STRENGTH),
      new Exercise("ex-lat", "Lat Pulldown", List.of(MuscleGroup.BACK),
          "machine", 3, 10, ExerciseCategory.STRENGTH),
      new Exercise("ex-leg", "Leg Press", List.of(MuscleGroup.LEGS, MuscleGroup.GLUTES),
          "machine", 4, 10, ExerciseCategory.STRENGTH),
      new Exercise("ex-rdl", "Romanian Deadlift", List.of(MuscleGroup.BACK, MuscleGroup.GLUTES, MuscleGroup.LEGS),
          "barbell", 3, 8, ExerciseCategory.STRENGTH),
      new Exercise("ex-lunge", "Walking Lunge", List.of(MuscleGroup.LEGS, MuscleGroup.GLUTES),
          "dumbbell", 3, 12, ExerciseCategory.STRENGTH),
      new Exercise("ex-calf", "Calf Raise", List.of(MuscleGroup.LEGS),
          "machine", 4, 15, ExerciseCategory.STRENGTH),
      new Exercise("ex-fly", "Cable Fly", List.of(MuscleGroup.CHEST),
          "machine", 3, 12, ExerciseCategory.STRENGTH),
      new Exercise("ex-lateral", "Lateral Raise", List.of(MuscleGroup.SHOULDERS),
          "dumbbell", 3, 15, ExerciseCategory.STRENGTH),
      new Exercise("ex-rear", "Rear Delt Fly", List.of(MuscleGroup.SHOULDERS, MuscleGroup.BACK),
          "dumbbell", 3, 12, ExerciseCategory.STRENGTH),
      new Exercise("ex-plank", "Plank", List.of(MuscleGroup.CORE),
          "bodyweight", 3, 60, ExerciseCategory.STRENGTH),
      new Exercise("ex-crunch", "Cable Crunch", List.of(MuscleGroup.CORE),
          "machine", 3, 15, ExerciseCategory.STRENGTH),
      new Exercise("ex-hang", "Hanging Leg Raise", List.of(MuscleGroup.CORE),
          "bodyweight", 3, 10, ExerciseCategory.STRENGTH),
      new Exercise("ex-run", "Treadmill Run", List.of(MuscleGroup.CARDIO),
          "cardio", 1, 30, ExerciseCategory.CARDIO),
      new Exercise("ex-bike", "Stationary Bike", List.of(MuscleGroup.CARDIO),
          "cardio", 1, 30, ExerciseCategory.CARDIO),
      new Exercise("ex-row-c", "Rowing Machine", List.of(MuscleGroup.CARDIO, MuscleGroup.BACK),
          "cardio", 1, 20, ExerciseCategory.CARDIO),
      new Exercise("ex-jump", "Jump Rope", List.of(MuscleGroup.CARDIO),
          "cardio", 3, 60, ExerciseCategory.CARDIO),
      new Exercise("ex-burp", "Burpees", List.of(MuscleGroup.FULLBODY, MuscleGroup.CARDIO),
          "bodyweight", 3, 12, ExerciseCategory.CARDIO),
      new Exercise("ex-yoga", "Sun Salutation Flow", List.of(MuscleGroup.FULLBODY),
          "bodyweight", 3, 5, ExerciseCategory.MOBILITY),
      new Exercise("ex-stretch", "Hip Mobility Flow", List.of(MuscleGroup.LEGS, MuscleGroup.GLUTES),
          "bodyweight", 2, 8, ExerciseCategory.MOBILITY),
      new Exercise("ex-shoulder-mob", "Shoulder Dislocates", List.of(MuscleGroup.SHOULDERS),
          "bodyweight", 3, 10, ExerciseCategory.MOBILITY),
      new Exercise("ex-cat", "Cat-Cow", List.of(MuscleGroup.BACK, MuscleGroup.CORE),
          "bodyweight", 2, 12, ExerciseCategory.MOBILITY),
      new Exercise("ex-pigeon", "Pigeon Pose", List.of(MuscleGroup.GLUTES, MuscleGroup.LEGS),
          "bodyweight", 2, 60, ExerciseCategory.MOBILITY)
  );

  private static final List<Food> FOODS = List.of(
      new Food("f-chicken", "Grilled Chicken Breast", 165, 31, 0, 4, "100 g", "highprotein"),
      new Food("f-rice", "White Rice", 130, 3, 28, 0, "100 g", "wholefood"),
      new Food("f-rice-b", "Brown Rice", 112, 3, 24, 1, "100 g", "wholefood"),
      new Food("f-egg", "Whole Egg", 78, 6, 1, 5, "1 large", "highprotein"),
      new Food("f-eggwhite", "Egg Whites", 17, 4, 0, 0, "1 white", "highprotein"),
      new Food("f-oats", "Rolled Oats", 389, 17, 66, 7, "100 g", "wholefood"),
      new Food("f-banana", "Banana", 89, 1, 23, 0, "1 medium", "vegan"),
      new Food("f-apple", "Apple", 52, 0, 14, 0, "1 medium", "vegan"),
      new Food("f-yogurt", "Greek Yogurt 0%", 59, 10, 4, 0, "100 g", "highprotein"),
      new Food("f-cottage", "Cottage Cheese", 98, 11, 3, 4, "100 g", "highprotein"),
      new Food("f-salmon", "Salmon Fillet", 208, 20, 0, 13, "100 g", "wholefood"),
      new Food("f-tuna", "Tuna in Water", 116, 26, 0, 1, "100 g", "highprotein"),
      new Food("f-beef", "Lean Ground Beef", 250, 26, 0, 17, "100 g", "wholefood"),
      new Food("f-turkey", "Turkey Breast", 135, 30, 0, 1, "100 g", "highprotein"),
      new Food("f-pasta", "Pasta Cooked", 131, 5, 25, 1, "100 g", "wholefood"),
      new Food("f-bread", "Whole Wheat Bread", 247, 13, 41, 3, "100 g", "wholefood"),
      new Food("f-potato", "Sweet Potato", 86, 2, 20, 0, "100 g", "wholefood"),
      new Food("f-broccoli", "Broccoli", 34, 3, 7, 0, "100 g", "vegan"),
      new Food("f-spinach", "Spinach", 23, 3, 4, 0, "100 g", "vegan"),
      new Food("f-avocado", "Avocado", 160, 2, 9, 15, "100 g", "vegan"),
      new Food("f-almond", "Almonds", 579, 21, 22, 50, "100 g", "vegan"),
      new Food("f-pb", "Peanut Butter", 588, 25, 20, 50, "100 g", "vegan"),
      new Food("f-olive", "Olive Oil", 884, 0, 0, 100, "100 g", "vegan"),
      new Food("f-milk", "Whole Milk", 61, 3, 5, 3, "100 ml", "wholefood"),
      new Food("f-soy", "Soy Milk", 33, 3, 2, 2, "100 ml", "vegan"),
      new Food("f-whey", "Whey Protein Shake", 120, 24, 3, 1, "1 scoop", "highprotein"),
      new Food("f-tofu", "Tofu Firm", 144, 17, 3, 9, "100 g", "vegan"),
      new Food("f-tempeh", "Tempeh", 192, 20, 8, 11, "100 g", "vegan"),
      new Food("f-lentil", "Lentils Cooked", 116, 9, 20, 0, "100 g", "vegan"),
      new Food("f-bean", "Black Beans", 132, 9, 24, 1, "100 g", "vegan"),
      new Food("f-chickpea", "Chickpeas", 164, 9, 27, 3, "100 g", "vegan"),
      new Food("f-quinoa", "Quinoa Cooked", 120, 4, 21, 2, "100 g", "vegan"),
      new Food("f-cheddar", "Cheddar Cheese", 402, 25, 1, 33, "100 g", "wholefood"),
      new Food("f-mozz", "Mozzarella", 280, 28, 3, 17, "100 g", "wholefood"),
      new Food("f-blueberry", "Blueberries", 57, 1, 14, 0, "100 g", "vegan"),
      new Food("f-orange", "Orange", 47, 1, 12, 0, "1 medium", "vegan"),
      new Food("f-strawberry", "Strawberries", 32, 1, 8, 0, "100 g", "vegan"),
      new Food("f-honey", "Honey", 304, 0, 82, 0, "100 g", "wholefood"),
      new Food("f-darkchoc", "Dark Chocolate 85%", 599, 8, 46, 43, "100 g", "wholefood"),
      new Food("f-protein-bar", "Protein Bar", 220, 20, 22, 7, "1 bar", "highprotein"),
      new Food("f-coffee", "Black Coffee", 2, 0, 0, 0, "240 ml", "vegan"),
      new Food("f-water", "Water", 0, 0, 0, 0, "500 ml", "vegan"),
      new Food("f-bagel", "Bagel Plain", 257, 10, 51, 2, "1 bagel", "wholefood"),
      new Food("f-cereal", "Cereal Mixed", 379, 8, 84, 3, "100 g", "wholefood"),
      new Food("f-pizza", "Pizza Slice", 285, 12, 36, 10, "1 slice", "wholefood"),
      new Food("f-burger", "Burger", 540, 25, 40, 30, "1 burger", "wholefood"),
      new Food("f-salad", "Mixed Salad", 35, 2, 6, 0, "100 g", "vegan"),
      new Food("f-shrimp", "Shrimp", 99, 24, 0, 1, "100 g", "highprotein"),
      new Food("f-edamame", "Edamame", 122, 11, 10, 5, "100 g", "vegan"),
      new Food("f-walnut", "Walnuts", 654, 15, 14, 65, "100 g", "vegan")
  );

  private static final List<Quest> QUESTS = List.of(
      new Quest("q-d1", "Log a meal", "Track at least one meal today", QuestScope.DAILY, 30, 10, 1, 0, false),
      new Quest("q-d2", "Hit your water goal", "Drink 2 L of water", QuestScope.DAILY, 25, 5, 2, 0, false),
      new Quest("q-d3", "Complete a workout", "Log a session of any kind", QuestScope.DAILY, 80, 20, 1, 0, false),
      new Quest("q-w1", "Train four times", "Four workouts this week", QuestScope.WEEKLY, 220, 60, 4, 1, false),
      new Quest("q-w2", "Protein streak", "Hit protein goal five days", QuestScope.WEEKLY, 180, 40, 5, 2, false),
      new Quest("q-e1", "Bench 100 kg", "Lift 100 kg on bench press", QuestScope.EPIC, 1500, 400, 100, 72, false),
      new Quest("q-e2", "30 day streak", "Log activity for 30 days straight", QuestScope.EPIC, 2000, 500, 30, 7, false),
      new Quest("q-e3", "Run a 5K", "Complete a 5 km cardio session", QuestScope.EPIC, 1200, 300, 5, 2, false)
  );

  private static final List<GearItem> GEAR = List.of(
      new GearItem("g-helm", "Helm of the Dawn", Rarity.LEGEND, "head", 0, "Reach level 10", true),
      new GearItem("g-boots", "Stormstride Boots", Rarity.RARE, "feet", 1, "Run 10 km total", true),
      new GearItem("g-gloves", "Iron Grips", Rarity.COMMON, "accessory", 2, "Default gear", true),
      new GearItem("g-blade", "Emberblade", Rarity.EPIC, "weapon", 3, "Hit a PR on bench", false),
      new GearItem("g-potion", "Recovery Potion", Rarity.COMMON, "accessory", 4, "Log 5 meals", true),
      new GearItem("g-crystal", "Crystal Conduit", Rarity.RARE, "weapon", 5, "Complete 10 mobility sessions", false),
      new GearItem("g-bow", "Hunter's Bow", Rarity.RARE, "weapon", 6, "Cardio quest complete", false),
      new GearItem("g-dagger", "Swift Dagger", Rarity.EPIC, "weapon", 7, "30 day streak", false)
  );

  private static final Map<String, Exercise> EXERCISE_BY_ID = new HashMap<>();
  private static final Map<String, Food> FOOD_BY_ID = new HashMap<>();

  static {
    for (Exercise exercise : EXERCISES) {
      EXERCISE_BY_ID.put(exercise.getId(), exercise);
    }
    for (Food food : FOODS) {
      FOOD_BY_ID.put(food.getId(), food);
    }
  }

  private SeedData() {}

  public static List<Exercise> exercises() {
    return EXERCISES;
  }

  public static List<Food> foods() {
    return FOODS;
  }

  public static List<Quest> copyQuests() {
    List<Quest> copy = new ArrayList<>();
    for (Quest quest : QUESTS) {
      copy.add(new Quest(
          quest.getId(),
          quest.getTitle(),
          quest.getDescription(),
          quest.getScope(),
          quest.getRewardXp(),
          quest.getRewardGold(),
          quest.getGoal(),
          quest.getProgress(),
          quest.isCompleted()
      ));
    }
    return copy;
  }

  public static List<GearItem> copyGear() {
    List<GearItem> copy = new ArrayList<>();
    for (GearItem gear : GEAR) {
      copy.add(new GearItem(
          gear.getId(),
          gear.getName(),
          gear.getRarity(),
          gear.getSlot(),
          gear.getSpriteIndex(),
          gear.getUnlockHint(),
          gear.isOwned()
      ));
    }
    return copy;
  }

  public static Exercise exerciseById(String id) {
    return EXERCISE_BY_ID.get(id);
  }

  public static Food foodById(String id) {
    return FOOD_BY_ID.get(id);
  }
}
