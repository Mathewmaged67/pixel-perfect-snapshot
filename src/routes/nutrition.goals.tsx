import { createFileRoute } from "@tanstack/react-router";
import { useNutritionStore } from "@/stores/nutrition-store";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";

export const Route = createFileRoute("/nutrition/goals")({
  component: Goals,
});

function Goals() {
  const goals = useNutritionStore((s) => s.goals);
  const setGoals = useNutritionStore((s) => s.setGoals);
  return (
    <div className="border-rune max-w-md rounded-md bg-card p-5">
      <h2 className="font-display text-xs uppercase text-gold">Daily macro goals</h2>
      <div className="mt-4 grid grid-cols-2 gap-3">
        {(["kcal", "protein", "carbs", "fat"] as const).map((k) => (
          <div key={k}>
            <Label className="capitalize">{k}</Label>
            <Input
              type="number"
              value={goals[k]}
              onChange={(e) => setGoals({ ...goals, [k]: Number(e.target.value) })}
              className="mt-1"
            />
          </div>
        ))}
      </div>
    </div>
  );
}
