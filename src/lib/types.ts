export type CharacterClass = "warrior" | "ranger" | "mage" | "paladin";
export type Gender = "male" | "female" | "nonbinary";
export type Rarity = "common" | "rare" | "epic" | "legend";
export type StatKey = "str" | "sta" | "vit" | "agi";
export type MuscleGroup =
  | "chest" | "back" | "shoulders" | "arms" | "legs" | "core" | "glutes" | "cardio" | "fullbody";

export interface Exercise {
  id: string;
  name: string;
  muscles: MuscleGroup[];
  equipment: "barbell" | "dumbbell" | "machine" | "bodyweight" | "cardio";
  defaultSets: number;
  defaultReps: number;
  category: "strength" | "cardio" | "mobility";
}

export interface SetEntry {
  id: string;
  weight: number;
  reps: number;
  done: boolean;
  pr?: boolean;
}

export interface SessionExercise {
  id: string;
  exerciseId: string;
  sets: SetEntry[];
}

export interface WorkoutSession {
  id: string;
  startedAt: number;
  endedAt?: number;
  exercises: SessionExercise[];
  name: string;
}

export interface Food {
  id: string;
  name: string;
  kcal: number;
  protein: number;
  carbs: number;
  fat: number;
  serving: string;
  tag?: "wholefood" | "highprotein" | "vegan";
}

export interface FoodLogEntry {
  id: string;
  foodId: string;
  servings: number;
  loggedAt: number;
}

export interface Quest {
  id: string;
  title: string;
  description: string;
  scope: "daily" | "weekly" | "epic";
  rewardXp: number;
  rewardGold: number;
  goal: number;
  progress: number;
  completed?: boolean;
}

export interface GearItem {
  id: string;
  name: string;
  rarity: Rarity;
  slot: "head" | "body" | "feet" | "weapon" | "accessory";
  spriteIndex: number;
  unlockHint: string;
  owned: boolean;
}
