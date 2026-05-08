import { create } from "zustand";
import { persist } from "zustand/middleware";
import type { CharacterClass, Gender, StatKey } from "@/lib/types";

export interface CharacterState {
  initialized: boolean;
  name: string;
  characterClass: CharacterClass;
  gender: Gender;
  level: number;
  xp: number;
  xpToNext: number;
  gold: number;
  hp: number;
  hpMax: number;
  mp: number;
  mpMax: number;
  stats: Record<StatKey, number>;
  weight: number;
  height: number;
  weeklyTarget: number;
  streak: number;
  init: (input: {
    name: string;
    characterClass: CharacterClass;
    gender: Gender;
    weight: number;
    height: number;
    weeklyTarget: number;
  }) => void;
  addXp: (amount: number) => { leveledUp: boolean };
  addStat: (stat: StatKey, amount: number) => void;
  addGold: (amount: number) => void;
  addHp: (amount: number) => void;
  addMp: (amount: number) => void;
  reset: () => void;
}

const xpForLevel = (lvl: number) => 100 + lvl * 80;

export const useCharacterStore = create<CharacterState>()(
  persist(
    (set, get) => ({
      initialized: false,
      name: "",
      characterClass: "warrior",
      gender: "male",
      level: 1,
      xp: 0,
      xpToNext: xpForLevel(1),
      gold: 50,
      hp: 80,
      hpMax: 100,
      mp: 60,
      mpMax: 100,
      stats: { str: 10, sta: 10, vit: 10, agi: 10 },
      weight: 75,
      height: 175,
      weeklyTarget: 4,
      streak: 0,
      init: (input) =>
        set({
          initialized: true,
          name: input.name,
          characterClass: input.characterClass,
          gender: input.gender,
          weight: input.weight,
          height: input.height,
          weeklyTarget: input.weeklyTarget,
          level: 1,
          xp: 0,
          xpToNext: xpForLevel(1),
          gold: 50,
          hp: 80,
          hpMax: 100,
          mp: 60,
          mpMax: 100,
          stats: { str: 10, sta: 10, vit: 10, agi: 10 },
          streak: 1,
        }),
      addXp: (amount) => {
        const s = get();
        let xp = s.xp + amount;
        let level = s.level;
        let xpToNext = s.xpToNext;
        let leveledUp = false;
        while (xp >= xpToNext) {
          xp -= xpToNext;
          level += 1;
          xpToNext = xpForLevel(level);
          leveledUp = true;
        }
        set({ xp, level, xpToNext });
        return { leveledUp };
      },
      addStat: (stat, amount) =>
        set((s) => ({ stats: { ...s.stats, [stat]: s.stats[stat] + amount } })),
      addGold: (amount) => set((s) => ({ gold: s.gold + amount })),
      addHp: (amount) => set((s) => ({ hp: Math.max(0, Math.min(s.hpMax, s.hp + amount)) })),
      addMp: (amount) => set((s) => ({ mp: Math.max(0, Math.min(s.mpMax, s.mp + amount)) })),
      reset: () => set({ initialized: false }),
    }),
    { name: "fitquest-character" },
  ),
);
