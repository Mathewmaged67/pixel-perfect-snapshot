public final class XpEngine {
  private XpEngine() {}

  public static WorkoutXp computeWorkoutXp(WorkoutSession session) {
    int strXp = 0;
    int staXp = 0;
    int agiXp = 0;
    if (session == null) {
      return new WorkoutXp(0, 0, 0);
    }
    for (SessionExercise se : session.getExercises()) {
      Exercise ex = SeedData.exerciseById(se.getExerciseId());
      if (ex == null) {
        continue;
      }
      int completedReps = se.completedReps();
      switch (ex.getCategory()) {
        case STRENGTH -> strXp += Math.round(se.volume() / 10.0f);
        case CARDIO -> staXp += completedReps * 4;
        case MOBILITY -> agiXp += completedReps * 6;
      }
    }
    agiXp += 20;
    return new WorkoutXp(strXp, staXp, agiXp);
  }

  public static WorkoutXp finalizeWorkoutRewards(WorkoutSession session, AppState appState) {
    WorkoutXp xp = computeWorkoutXp(session);
    CharacterState character = appState.getCharacter();
    if (xp.getStr() > 0) {
      character.addStat(StatKey.STR, Math.max(1, Math.round(xp.getStr() / 30.0f)));
    }
    if (xp.getSta() > 0) {
      character.addStat(StatKey.STA, Math.max(1, Math.round(xp.getSta() / 30.0f)));
    }
    if (xp.getAgi() > 0) {
      character.addStat(StatKey.AGI, Math.max(1, Math.round(xp.getAgi() / 30.0f)));
    }
    XpResult result = character.addXp(xp.getTotal());
    character.addGold(Math.round(xp.getTotal() / 5.0f));
    appState.addActivity("Workout complete: +" + xp.getTotal() + " XP");
    if (result.leveledUp()) {
      appState.addActivity("Level up! Now level " + character.getLevel());
    }
    return xp;
  }

  public static NutritionTotals computePostWorkoutTargets(WorkoutSession session, AppState appState) {
    int volume = 0;
    if (session != null) {
      for (SessionExercise se : session.getExercises()) {
        volume += se.volume();
      }
    }
    long durationMs = session != null ? session.durationMs() : 45 * 60 * 1000;
    int durationMin = Math.max(15, (int) Math.round(durationMs / 60000.0));
    int protein = Math.round(30 + volume / 200.0f);
    int carbs = Math.round(40 + durationMin * 1.2f);
    int fat = 18;
    int kcal = protein * 4 + carbs * 4 + fat * 9;
    NutritionTotals targets = new NutritionTotals(kcal, protein, carbs, fat);
    appState.getNutrition().setPostWorkoutTargets(targets);
    return targets;
  }

  public static void logMealRewards(String foodId, int servings, AppState appState) {
    Food food = SeedData.foodById(foodId);
    if (food == null) {
      return;
    }
    int xp = Math.round((food.getProtein() * servings) * 2 + 10);
    CharacterState character = appState.getCharacter();
    character.addStat(StatKey.VIT, 1);
    character.addXp(xp);
    character.addMp(Math.round((food.getCarbs() * servings) / 4.0f));
    character.addHp(Math.round((food.getProtein() * servings) / 5.0f));
    appState.getQuests().bumpProgress("q-d1", 1);
    appState.addActivity("Meal logged: " + food.getName());
  }
}
