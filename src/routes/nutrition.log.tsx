import { createFileRoute } from "@tanstack/react-router";
import { useState } from "react";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import { foods } from "@/data/foods";
import { useNutritionStore } from "@/stores/nutrition-store";
import { logMealRewards } from "@/lib/xp-engine";
import { cn } from "@/lib/utils";

export const Route = createFileRoute("/nutrition/log")({
  component: Log,
});

function Log() {
  const [q, setQ] = useState("");
  const [servings, setServings] = useState<Record<string, number>>({});
  const addEntry = useNutritionStore((s) => s.addEntry);
  const filtered = foods.filter((f) => f.name.toLowerCase().includes(q.toLowerCase())).slice(0, 30);
  return (
    <div className="space-y-4">
      <Input value={q} onChange={(e) => setQ(e.target.value)} placeholder="Search food" />
      <ul className="grid gap-2 sm:grid-cols-2">
        {filtered.map((f) => {
          const s = servings[f.id] ?? 1;
          return (
            <li key={f.id} className={cn("border-rune rounded-md bg-card p-3")}>
              <div className="flex items-center justify-between">
                <div>
                  <p className="text-sm">{f.name}</p>
                  <p className="text-[10px] uppercase text-muted-foreground">
                    {f.kcal} kcal · P{f.protein} C{f.carbs} F{f.fat} · {f.serving}
                  </p>
                </div>
              </div>
              <div className="mt-2 flex items-center gap-2">
                <Input
                  type="number"
                  min={0}
                  step={0.5}
                  value={s}
                  onChange={(e) => setServings((m) => ({ ...m, [f.id]: Number(e.target.value) }))}
                  className="h-8 w-20"
                />
                <Button
                  size="sm"
                  onClick={() => {
                    addEntry(f.id, s);
                    logMealRewards(f.id, s);
                  }}
                >
                  Log
                </Button>
              </div>
            </li>
          );
        })}
      </ul>
    </div>
  );
}
