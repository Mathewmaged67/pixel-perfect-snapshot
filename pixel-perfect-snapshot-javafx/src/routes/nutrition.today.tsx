import { createFileRoute } from "@tanstack/react-router";
import { useMemo } from "react";
import { MacroRing } from "@/components/rpg/macro-ring";
import { calculateTodayTotals, useNutritionStore } from "@/stores/nutrition-store";
import { foodById } from "@/data/foods";

export const Route = createFileRoute("/nutrition/today")({
  component: Today,
});

function Today() {
  const goals = useNutritionStore((s) => s.goals);
  const log = useNutritionStore((s) => s.log);
  const totals = useMemo(() => calculateTodayTotals(log), [log]);
  const recentLog = log.slice(0, 8);
  return (
    <div className="space-y-6">
      <div className="border-rune rounded-md bg-card p-5">
        <h2 className="font-display text-xs uppercase text-gold">Macros today</h2>
        <div className="mt-4 flex flex-wrap items-center justify-around gap-4">
          <MacroRing label="Protein" value={totals.protein} goal={goals.protein} color="protein" />
          <MacroRing label="Carbs" value={totals.carbs} goal={goals.carbs} color="carbs" />
          <MacroRing label="Fat" value={totals.fat} goal={goals.fat} color="fat" />
          <div className="text-center">
            <p className="font-display text-[10px] uppercase text-muted-foreground">Calories</p>
            <p className="tabular text-3xl text-gold">{Math.round(totals.kcal)}</p>
            <p className="text-xs text-muted-foreground">/ {goals.kcal}</p>
          </div>
        </div>
      </div>
      <div className="border-rune rounded-md bg-card p-5">
        <h2 className="font-display text-xs uppercase text-gold">Recent log</h2>
        <ul className="mt-3 space-y-1 text-sm">
          {recentLog.length === 0 && <li className="text-xs text-muted-foreground">Nothing logged today.</li>}
          {recentLog.map((e) => {
            const f = foodById(e.foodId);
            if (!f) return null;
            return (
              <li key={e.id} className="flex justify-between">
                <span>
                  {f.name} <span className="text-muted-foreground">x{e.servings}</span>
                </span>
                <span className="tabular text-muted-foreground">{Math.round(f.kcal * e.servings)} kcal</span>
              </li>
            );
          })}
        </ul>
      </div>
    </div>
  );
}
