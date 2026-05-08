import { createFileRoute, Link } from "@tanstack/react-router";
import { useWorkoutStore } from "@/stores/workout-store";
import { exerciseById } from "@/data/exercises";
import { computeWorkoutXp } from "@/lib/xp-engine";
import { HeatMap } from "@/components/rpg/heat-map";
import { Button } from "@/components/ui/button";
import { Trophy } from "lucide-react";
import type { MuscleGroup } from "@/lib/types";

export const Route = createFileRoute("/workout/summary/$id")({
  component: Summary,
});

function Summary() {
  const { id } = Route.useParams();
  const session = useWorkoutStore((s) => s.history.find((h) => h.id === id));

  if (!session) {
    return <p className="rounded-md border border-border bg-card p-6 text-sm">Session not found.</p>;
  }

  const xp = computeWorkoutXp(session);
  const sets = session.exercises.reduce((n, e) => n + e.sets.filter((s) => s.done).length, 0);
  const volume = session.exercises.reduce(
    (n, e) => n + e.sets.reduce((m, s) => m + (s.done ? s.weight * s.reps : 0), 0),
    0,
  );
  const duration = session.endedAt
    ? Math.max(1, Math.round((session.endedAt - session.startedAt) / 60000))
    : 0;

  const intensities: Partial<Record<MuscleGroup, number>> = {};
  for (const se of session.exercises) {
    const ex = exerciseById(se.exerciseId);
    if (!ex) continue;
    const v = se.sets.reduce((s, st) => s + (st.done ? st.weight * st.reps : 0), 0) || 1;
    for (const m of ex.muscles) {
      intensities[m] = Math.min(1, (intensities[m] ?? 0) + v / 1500);
    }
  }

  return (
    <div className="space-y-6">
      <header className="text-center">
        <Trophy className="mx-auto h-10 w-10 text-gold" />
        <h1 className="mt-3 font-display text-base text-gold text-glow-gold">Quest complete</h1>
        <p className="text-sm text-muted-foreground">{session.name}</p>
      </header>

      <div className="grid gap-4 sm:grid-cols-4">
        {[
          { label: "Volume", value: `${volume.toLocaleString("de-DE")} kg` },
          { label: "Sets", value: sets },
          { label: "Duration", value: `${duration} min` },
          { label: "XP", value: `+${xp.total}` },
        ].map((s) => (
          <div key={s.label} className="border-rune rounded-md bg-card p-4 text-center">
            <p className="font-display text-[10px] uppercase text-muted-foreground">{s.label}</p>
            <p className="mt-1 tabular text-xl text-foreground">{s.value}</p>
          </div>
        ))}
      </div>

      <div className="grid gap-6 lg:grid-cols-[1fr_320px]">
        <div className="border-rune rounded-md bg-card p-5">
          <h2 className="font-display text-xs uppercase text-gold">XP breakdown</h2>
          <ul className="mt-3 space-y-2 text-sm">
            <li className="flex justify-between"><span className="text-str">Strength</span><span className="tabular">+{xp.str}</span></li>
            <li className="flex justify-between"><span className="text-sta">Stamina</span><span className="tabular">+{xp.sta}</span></li>
            <li className="flex justify-between"><span className="text-agi">Agility</span><span className="tabular">+{xp.agi}</span></li>
          </ul>
        </div>
        <div className="border-rune flex flex-col items-center rounded-md bg-card p-5">
          <h2 className="font-display text-xs uppercase text-gold">Muscle map</h2>
          <HeatMap intensities={intensities} className="mt-2" />
        </div>
      </div>

      <div className="flex justify-end">
        <Button asChild className="bg-gold text-primary-foreground hover:opacity-90">
          <Link to="/nutrition/post-workout">See nutrition plan</Link>
        </Button>
      </div>
    </div>
  );
}
