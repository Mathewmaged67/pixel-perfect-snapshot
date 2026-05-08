import { toast } from "sonner";
import { useCharacterStore } from "@/stores/character-store";
import { useNutritionStore, type NutritionTotals } from "@/stores/nutrition-store";
import { useQuestStore } from "@/stores/quest-store";
import type { SessionExercise, StatKey, WorkoutSession } from "@/lib/types";
import { exerciseById } from "@/data/exercises";
import { foodById } from "@/data/foods";

const sessionVolume = (se: SessionExercise) =>
  se.sets.reduce((sum, s) => (s.done ? sum + s.weight * s.reps : sum), 0);

export const computeWorkoutXp = (session: WorkoutSession) => {
  let strXp = 0;
  let staXp = 0;
  let agiXp = 0;
  for (const se of session.exercises) {
    const ex = exerciseById(se.exerciseId);
    if (!ex) continue;
    const completedReps = se.sets.reduce((n, s) => (s.done ? n + s.reps : n), 0);
    if (ex.category === "strength") strXp += Math.round(sessionVolume(se) / 10);
    if (ex.category === "cardio") staXp += completedReps * 4;
    if (ex.category === "mobility") agiXp += completedReps * 6;
  }
  agiXp += 20; // showing up
  return { str: strXp, sta: staXp, agi: agiXp, total: strXp + staXp + agiXp };
};

export const finalizeWorkoutRewards = (session: WorkoutSession) => {
  const xp = computeWorkoutXp(session);
  const character = useCharacterStore.getState();
  if (xp.str > 0) character.addStat("str", Math.max(1, Math.round(xp.str / 30)));
  if (xp.sta > 0) character.addStat("sta", Math.max(1, Math.round(xp.sta / 30)));
  if (xp.agi > 0) character.addStat("agi", Math.max(1, Math.round(xp.agi / 30)));
  const result = character.addXp(xp.total);
  character.addGold(Math.round(xp.total / 5));
  toast.success(`+${xp.total} XP earned`, {
    description: "Quest progress saved",
  });
  if (result.leveledUp) {
    toast(`LEVEL UP! Now level ${useCharacterStore.getState().level}`, {
      className: "font-display",
    });
  }
  return xp;
};

export const computePostWorkoutTargets = (session: WorkoutSession): NutritionTotals => {
  const volume = session.exercises.reduce((sum, se) => sum + sessionVolume(se), 0);
  const durationMin = session.endedAt
    ? Math.max(15, Math.round((session.endedAt - session.startedAt) / 60000))
    : 45;
  const protein = Math.round(30 + volume / 200);
  const carbs = Math.round(40 + durationMin * 1.2);
  const fat = 18;
  const kcal = protein * 4 + carbs * 4 + fat * 9;
  const targets = { protein, carbs, fat, kcal };
  useNutritionStore.getState().setPostWorkoutTargets(targets);
  return targets;
};

export const logMealRewards = (foodId: string, servings: number) => {
  const food = foodById(foodId);
  if (!food) return;
  const xp = Math.round((food.protein * servings) * 2 + 10);
  const character = useCharacterStore.getState();
  character.addStat("vit", 1);
  character.addXp(xp);
  character.addMp(Math.round((food.carbs * servings) / 4));
  character.addHp(Math.round((food.protein * servings) / 5));
  useQuestStore.getState().bumpProgress("q-d1", 1);
  toast.success(`+${xp} VIT XP`, { description: `${food.name} logged` });
};

export const statColor = (stat: StatKey) =>
  ({ str: "var(--str)", sta: "var(--sta)", vit: "var(--vit)", agi: "var(--agi)" })[stat];
