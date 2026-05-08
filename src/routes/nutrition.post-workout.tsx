import { createFileRoute } from "@tanstack/react-router";
import { useNutritionStore } from "@/stores/nutrition-store";
import { foods } from "@/data/foods";
import { LootCard } from "@/components/rpg/loot-card";
import { logMealRewards } from "@/lib/xp-engine";
import { Apple } from "lucide-react";

export const Route = createFileRoute("/nutrition/post-workout")({
  component: PostWorkout,
});

function PostWorkout() {
  const targets = useNutritionStore((s) => s.postWorkoutTargets);
  const addEntry = useNutritionStore((s) => s.addEntry);

  const suggestions = [
    { id: "wholefood", title: "Whole Food Bowl", food: foods.find((f) => f.id === "f-chicken")!, rarity: "rare" as const, sprite: 0 },
    { id: "highprotein", title: "High Protein Recovery", food: foods.find((f) => f.id === "f-whey")!, rarity: "epic" as const, sprite: 4 },
    { id: "vegan", title: "Vegan Power Plate", food: foods.find((f) => f.id === "f-tempeh")!, rarity: "legend" as const, sprite: 5 },
  ];

  return (
    <div className="space-y-6">
      <div className="border-rune rounded-md bg-card p-5">
        <h2 className="font-display text-xs uppercase text-gold">Recovery targets</h2>
        {targets ? (
          <div className="mt-3 grid grid-cols-2 gap-3 sm:grid-cols-4">
            {[
              { l: "Calories", v: targets.kcal },
              { l: "Protein g", v: targets.protein },
              { l: "Carbs g", v: targets.carbs },
              { l: "Fat g", v: targets.fat },
            ].map((s) => (
              <div key={s.l} className="rounded-md border border-border bg-elevated p-3 text-center">
                <p className="font-display text-[10px] uppercase text-muted-foreground">{s.l}</p>
                <p className="tabular text-lg text-gold">{Math.round(s.v)}</p>
              </div>
            ))}
          </div>
        ) : (
          <p className="mt-2 flex items-center gap-2 text-sm text-muted-foreground">
            <Apple className="h-4 w-4" /> Finish a workout to generate recovery targets.
          </p>
        )}
      </div>

      <div>
        <h2 className="font-display text-xs uppercase text-gold">Recovery loot</h2>
        <div className="mt-3 grid gap-3 sm:grid-cols-3">
          {suggestions.map((s) => (
            <LootCard
              key={s.id}
              name={s.title}
              rarity={s.rarity}
              spriteIndex={s.sprite}
              hint={`${s.food.kcal} kcal · ${s.food.protein}g P`}
              onClick={() => {
                addEntry(s.food.id, 1);
                logMealRewards(s.food.id, 1);
              }}
            />
          ))}
        </div>
      </div>
    </div>
  );
}
