import { create } from "zustand";
import { persist } from "zustand/middleware";
import type { FoodLogEntry } from "@/lib/types";
import { foodById } from "@/data/foods";

const uid = () => Math.random().toString(36).slice(2, 10);
const todayKey = () => new Date().toISOString().slice(0, 10);

export interface NutritionTotals {
  kcal: number;
  protein: number;
  carbs: number;
  fat: number;
}

export interface NutritionState {
  goals: NutritionTotals;
  log: FoodLogEntry[];
  postWorkoutTargets?: NutritionTotals;
  addEntry: (foodId: string, servings: number) => void;
  removeEntry: (id: string) => void;
  setGoals: (goals: NutritionTotals) => void;
  setPostWorkoutTargets: (t: NutritionTotals) => void;
  todayTotals: () => NutritionTotals;
}

export const useNutritionStore = create<NutritionState>()(
  persist(
    (set, get) => ({
      goals: { kcal: 2400, protein: 160, carbs: 280, fat: 80 },
      log: [],
      postWorkoutTargets: undefined,
      addEntry: (foodId, servings) =>
        set((s) => ({
          log: [{ id: uid(), foodId, servings, loggedAt: Date.now() }, ...s.log],
        })),
      removeEntry: (id) => set((s) => ({ log: s.log.filter((e) => e.id !== id) })),
      setGoals: (goals) => set({ goals }),
      setPostWorkoutTargets: (postWorkoutTargets) => set({ postWorkoutTargets }),
      todayTotals: () => {
        const key = todayKey();
        return get().log.reduce<NutritionTotals>(
          (acc, entry) => {
            if (new Date(entry.loggedAt).toISOString().slice(0, 10) !== key) return acc;
            const food = foodById(entry.foodId);
            if (!food) return acc;
            return {
              kcal: acc.kcal + food.kcal * entry.servings,
              protein: acc.protein + food.protein * entry.servings,
              carbs: acc.carbs + food.carbs * entry.servings,
              fat: acc.fat + food.fat * entry.servings,
            };
          },
          { kcal: 0, protein: 0, carbs: 0, fat: 0 },
        );
      },
    }),
    { name: "fitquest-nutrition" },
  ),
);
