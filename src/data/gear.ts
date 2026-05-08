import type { GearItem } from "@/lib/types";

// spriteIndex maps to position in src/assets/gear-sheet.png (4 cols x 2 rows)
export const seedGear: GearItem[] = [
  { id: "g-helm", name: "Helm of the Dawn", rarity: "legend", slot: "head", spriteIndex: 0, unlockHint: "Reach level 10", owned: true },
  { id: "g-boots", name: "Stormstride Boots", rarity: "rare", slot: "feet", spriteIndex: 1, unlockHint: "Run 10 km total", owned: true },
  { id: "g-gloves", name: "Iron Grips", rarity: "common", slot: "accessory", spriteIndex: 2, unlockHint: "Default gear", owned: true },
  { id: "g-blade", name: "Emberblade", rarity: "epic", slot: "weapon", spriteIndex: 3, unlockHint: "Hit a PR on bench", owned: false },
  { id: "g-potion", name: "Recovery Potion", rarity: "common", slot: "accessory", spriteIndex: 4, unlockHint: "Log 5 meals", owned: true },
  { id: "g-crystal", name: "Crystal Conduit", rarity: "rare", slot: "weapon", spriteIndex: 5, unlockHint: "Complete 10 mobility sessions", owned: false },
  { id: "g-bow", name: "Hunter's Bow", rarity: "rare", slot: "weapon", spriteIndex: 6, unlockHint: "Cardio quest complete", owned: false },
  { id: "g-dagger", name: "Swift Dagger", rarity: "epic", slot: "weapon", spriteIndex: 7, unlockHint: "30 day streak", owned: false },
];
