import { createFileRoute } from "@tanstack/react-router";
import { useInventoryStore } from "@/stores/inventory-store";
import { useCharacterStore } from "@/stores/character-store";
import { LootCard } from "@/components/rpg/loot-card";
import { GoldCounter } from "@/components/rpg/gold-counter";

export const Route = createFileRoute("/inventory")({
  head: () => ({
    meta: [
      { title: "Inventory — FitQuest" },
      { name: "description", content: "Browse and equip your unlocked gear." },
    ],
  }),
  component: Inventory,
});

function Inventory() {
  const items = useInventoryStore((s) => s.items);
  const gold = useCharacterStore((s) => s.gold);
  return (
    <>
      <header className="mb-6 flex items-center justify-between">
        <h1 className="text-xl">Inventory</h1>
        <GoldCounter value={gold} />
      </header>
      <div className="grid gap-3 sm:grid-cols-3 lg:grid-cols-4">
        {items.map((item) => (
          <LootCard
            key={item.id}
            name={item.name}
            rarity={item.rarity}
            spriteIndex={item.spriteIndex}
            hint={item.owned ? item.slot : item.unlockHint}
            locked={!item.owned}
          />
        ))}
      </div>
    </>
  );
}
