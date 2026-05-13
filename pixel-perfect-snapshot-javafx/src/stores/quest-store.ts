import { create } from "zustand";
import { persist } from "zustand/middleware";
import type { Quest } from "@/lib/types";
import { seedQuests } from "@/data/quests";

export interface QuestState {
  quests: Quest[];
  bumpProgress: (id: string, amount?: number) => void;
  complete: (id: string) => void;
}

export const useQuestStore = create<QuestState>()(
  persist(
    (set) => ({
      quests: seedQuests,
      bumpProgress: (id, amount = 1) =>
        set((s) => ({
          quests: s.quests.map((q) =>
            q.id === id
              ? { ...q, progress: Math.min(q.goal, q.progress + amount) }
              : q,
          ),
        })),
      complete: (id) =>
        set((s) => ({
          quests: s.quests.map((q) => (q.id === id ? { ...q, completed: true, progress: q.goal } : q)),
        })),
    }),
    { name: "fitquest-quests" },
  ),
);
