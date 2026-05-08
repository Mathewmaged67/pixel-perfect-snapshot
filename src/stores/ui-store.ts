import { create } from "zustand";
import { persist } from "zustand/middleware";

type Theme = "dark" | "light";
type Units = "kg" | "lbs";

export interface UiState {
  theme: Theme;
  units: Units;
  setTheme: (t: Theme) => void;
  setUnits: (u: Units) => void;
}

export const useUiStore = create<UiState>()(
  persist(
    (set) => ({
      theme: "dark",
      units: "kg",
      setTheme: (theme) => set({ theme }),
      setUnits: (units) => set({ units }),
    }),
    { name: "fitquest-ui" },
  ),
);
