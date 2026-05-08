import { create } from "zustand";
import { persist } from "zustand/middleware";
import type { GearItem } from "@/lib/types";
import { seedGear } from "@/data/gear";

export interface InventoryState {
  items: GearItem[];
  equipped: Record<string, string | undefined>;
  unlock: (id: string) => void;
  equip: (slot: string, id: string) => void;
}

export const useInventoryStore = create<InventoryState>()(
  persist(
    (set) => ({
      items: seedGear,
      equipped: { weapon: undefined, head: "g-helm", feet: "g-boots", accessory: "g-gloves" },
      unlock: (id) =>
        set((s) => ({ items: s.items.map((i) => (i.id === id ? { ...i, owned: true } : i)) })),
      equip: (slot, id) => set((s) => ({ equipped: { ...s.equipped, [slot]: id } })),
    }),
    { name: "fitquest-inventory" },
  ),
);
