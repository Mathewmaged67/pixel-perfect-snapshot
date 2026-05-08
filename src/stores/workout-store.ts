import { create } from "zustand";
import { persist } from "zustand/middleware";
import type { SessionExercise, SetEntry, WorkoutSession } from "@/lib/types";
import { exerciseById } from "@/data/exercises";

const uid = () => Math.random().toString(36).slice(2, 10);

export interface WorkoutState {
  active?: WorkoutSession;
  history: WorkoutSession[];
  startSession: (name: string, exerciseIds: string[]) => string;
  addExercise: (exerciseId: string) => void;
  addSet: (sessionExerciseId: string) => void;
  updateSet: (sessionExerciseId: string, setId: string, patch: Partial<SetEntry>) => void;
  removeSet: (sessionExerciseId: string, setId: string) => void;
  finishSession: () => WorkoutSession | undefined;
  cancelSession: () => void;
}

const seedSet = (prev?: SetEntry): SetEntry => ({
  id: uid(),
  weight: prev?.weight ?? 0,
  reps: prev?.reps ?? 0,
  done: false,
});

export const useWorkoutStore = create<WorkoutState>()(
  persist(
    (set, get) => ({
      active: undefined,
      history: [],
      startSession: (name, exerciseIds) => {
        const id = uid();
        const session: WorkoutSession = {
          id,
          startedAt: Date.now(),
          name,
          exercises: exerciseIds.map<SessionExercise>((exerciseId) => {
            const ex = exerciseById(exerciseId);
            const sets = Array.from({ length: ex?.defaultSets ?? 3 }, () => seedSet());
            return { id: uid(), exerciseId, sets };
          }),
        };
        set({ active: session });
        return id;
      },
      addExercise: (exerciseId) => {
        const s = get();
        if (!s.active) return;
        const ex = exerciseById(exerciseId);
        const sets = Array.from({ length: ex?.defaultSets ?? 3 }, () => seedSet());
        set({
          active: {
            ...s.active,
            exercises: [...s.active.exercises, { id: uid(), exerciseId, sets }],
          },
        });
      },
      addSet: (sessionExerciseId) => {
        const s = get();
        if (!s.active) return;
        set({
          active: {
            ...s.active,
            exercises: s.active.exercises.map((se) =>
              se.id === sessionExerciseId
                ? { ...se, sets: [...se.sets, seedSet(se.sets[se.sets.length - 1])] }
                : se,
            ),
          },
        });
      },
      updateSet: (sessionExerciseId, setId, patch) => {
        const s = get();
        if (!s.active) return;
        set({
          active: {
            ...s.active,
            exercises: s.active.exercises.map((se) =>
              se.id === sessionExerciseId
                ? {
                    ...se,
                    sets: se.sets.map((st) => (st.id === setId ? { ...st, ...patch } : st)),
                  }
                : se,
            ),
          },
        });
      },
      removeSet: (sessionExerciseId, setId) => {
        const s = get();
        if (!s.active) return;
        set({
          active: {
            ...s.active,
            exercises: s.active.exercises.map((se) =>
              se.id === sessionExerciseId
                ? { ...se, sets: se.sets.filter((st) => st.id !== setId) }
                : se,
            ),
          },
        });
      },
      finishSession: () => {
        const s = get();
        if (!s.active) return undefined;
        const finished: WorkoutSession = { ...s.active, endedAt: Date.now() };
        set({ active: undefined, history: [finished, ...s.history] });
        return finished;
      },
      cancelSession: () => set({ active: undefined }),
    }),
    { name: "fitquest-workout" },
  ),
);
