import { createFileRoute, Link } from "@tanstack/react-router";
import { useWorkoutStore } from "@/stores/workout-store";
import { exerciseById } from "@/data/exercises";

export const Route = createFileRoute("/workout/history")({
  component: History,
});

function History() {
  const history = useWorkoutStore((s) => s.history);
  return (
    <div className="space-y-4">
      <header>
        <h1 className="text-xl">Session history</h1>
        <p className="text-sm text-muted-foreground">Past quests on your record.</p>
      </header>
      {history.length === 0 ? (
        <p className="rounded-md border border-border bg-card p-6 text-sm text-muted-foreground">
          No sessions yet. <Link to="/workout" className="text-gold underline">Start one</Link>.
        </p>
      ) : (
        <ul className="space-y-3">
          {history.map((h) => {
            const sets = h.exercises.reduce((n, e) => n + e.sets.filter((s) => s.done).length, 0);
            const volume = h.exercises.reduce(
              (n, e) => n + e.sets.reduce((m, s) => m + (s.done ? s.weight * s.reps : 0), 0),
              0,
            );
            return (
              <li key={h.id} className="border-rune rounded-md bg-card p-4">
                <div className="flex items-center justify-between">
                  <div>
                    <p className="text-sm">{h.name}</p>
                    <p className="text-[10px] uppercase text-muted-foreground">
                      {new Date(h.startedAt).toLocaleDateString("en-GB")} · {h.exercises.length} exercises · {sets} sets
                    </p>
                  </div>
                  <span className="tabular text-sm text-gold">{volume.toLocaleString("de-DE")} kg</span>
                </div>
                <ul className="mt-2 text-xs text-muted-foreground">
                  {h.exercises.slice(0, 3).map((e) => (
                    <li key={e.id}>· {exerciseById(e.exerciseId)?.name}</li>
                  ))}
                </ul>
              </li>
            );
          })}
        </ul>
      )}
    </div>
  );
}
