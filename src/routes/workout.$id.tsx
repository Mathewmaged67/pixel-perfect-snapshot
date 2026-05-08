import { createFileRoute, useNavigate } from "@tanstack/react-router";
import { useEffect, useMemo, useState } from "react";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { useWorkoutStore } from "@/stores/workout-store";
import { exerciseById } from "@/data/exercises";
import { RestTimer } from "@/components/rpg/rest-timer";
import { cn } from "@/lib/utils";
import { computeWorkoutXp, finalizeWorkoutRewards, computePostWorkoutTargets } from "@/lib/xp-engine";
import { Check, Plus, Square, Trash2, Timer } from "lucide-react";

export const Route = createFileRoute("/workout/$id")({
  component: ActiveSession,
});

function ActiveSession() {
  const { id } = Route.useParams();
  const navigate = useNavigate();
  const { active, updateSet, addSet, removeSet, addExercise, finishSession } = useWorkoutStore();
  const [resting, setResting] = useState(false);
  const [elapsed, setElapsed] = useState(0);

  useEffect(() => {
    if (!active) return;
    const t = setInterval(() => setElapsed(Math.floor((Date.now() - active.startedAt) / 1000)), 1000);
    return () => clearInterval(t);
  }, [active]);

  const liveXp = useMemo(() => (active ? computeWorkoutXp(active) : null), [active]);

  if (!active || active.id !== id) {
    return (
      <div className="rounded-md border border-border bg-card p-6 text-center">
        <p className="text-sm text-muted-foreground">No active session.</p>
        <Button asChild className="mt-3">
          <a href="/workout">Start one</a>
        </Button>
      </div>
    );
  }

  const finish = () => {
    const session = finishSession();
    if (!session) return;
    finalizeWorkoutRewards(session);
    computePostWorkoutTargets(session);
    navigate({ to: "/workout/summary/$id", params: { id: session.id } });
  };

  const fmt = (s: number) =>
    `${Math.floor(s / 60).toString().padStart(2, "0")}:${(s % 60).toString().padStart(2, "0")}`;

  return (
    <div className="space-y-6">
      <header className="border-rune flex items-center justify-between rounded-md bg-card p-4">
        <div>
          <p className="font-display text-[10px] uppercase text-muted-foreground">In progress</p>
          <h1 className="text-lg">{active.name}</h1>
        </div>
        <div className="text-right">
          <p className="font-display text-[10px] uppercase text-muted-foreground">Time</p>
          <p className="tabular text-2xl text-gold">{fmt(elapsed)}</p>
        </div>
      </header>

      {liveXp && (
        <div className="border-rune flex items-center justify-around rounded-md bg-card p-3 text-xs">
          <span className="text-str">STR +{liveXp.str}</span>
          <span className="text-sta">STA +{liveXp.sta}</span>
          <span className="text-agi">AGI +{liveXp.agi}</span>
          <span className="text-gold">Total {liveXp.total} XP</span>
        </div>
      )}

      <ul className="space-y-4">
        {active.exercises.map((se) => {
          const ex = exerciseById(se.exerciseId);
          if (!ex) return null;
          return (
            <li key={se.id} className="border-rune rounded-md bg-card p-4">
              <header className="mb-3 flex items-center justify-between">
                <div>
                  <h3 className="text-base">{ex.name}</h3>
                  <p className="text-[10px] uppercase text-muted-foreground">{ex.muscles.join(" · ")}</p>
                </div>
                <button
                  type="button"
                  onClick={() => setResting(true)}
                  className="rounded-md border border-border p-2 text-muted-foreground hover:text-gold"
                  aria-label="Start rest"
                >
                  <Timer className="h-4 w-4" />
                </button>
              </header>
              <div className="grid grid-cols-[40px_1fr_1fr_60px_40px] gap-2 text-[10px] uppercase text-muted-foreground">
                <span>Set</span>
                <span>Weight</span>
                <span>Reps</span>
                <span>Done</span>
                <span />
              </div>
              <ul className="mt-2 space-y-2">
                {se.sets.map((s, i) => (
                  <li
                    key={s.id}
                    className={cn(
                      "grid grid-cols-[40px_1fr_1fr_60px_40px] items-center gap-2 rounded-md border border-transparent px-1 py-1 transition",
                      s.done && "border-hp/40 bg-hp/5",
                    )}
                  >
                    <span className="font-display text-xs text-muted-foreground">{i + 1}</span>
                    <Input
                      type="number"
                      value={s.weight}
                      onChange={(e) => updateSet(se.id, s.id, { weight: Number(e.target.value) })}
                      className="h-9"
                    />
                    <Input
                      type="number"
                      value={s.reps}
                      onChange={(e) => updateSet(se.id, s.id, { reps: Number(e.target.value) })}
                      className="h-9"
                    />
                    <button
                      type="button"
                      onClick={() => {
                        updateSet(se.id, s.id, { done: !s.done });
                        if (!s.done) setResting(true);
                      }}
                      className={cn(
                        "flex h-9 items-center justify-center rounded-md border transition",
                        s.done ? "border-hp text-hp" : "border-border text-muted-foreground hover:border-gold-dim",
                      )}
                      aria-label="Toggle done"
                    >
                      {s.done ? <Check className="h-4 w-4" /> : <Square className="h-4 w-4" />}
                    </button>
                    <button
                      type="button"
                      onClick={() => removeSet(se.id, s.id)}
                      className="text-muted-foreground hover:text-destructive"
                      aria-label="Remove set"
                    >
                      <Trash2 className="h-4 w-4" />
                    </button>
                  </li>
                ))}
              </ul>
              <Button variant="ghost" size="sm" onClick={() => addSet(se.id)} className="mt-2">
                <Plus className="mr-1 h-4 w-4" /> Add set
              </Button>
            </li>
          );
        })}
      </ul>

      <AddExerciseRow onAdd={addExercise} />

      <div className="sticky bottom-20 z-10 lg:bottom-4">
        <Button onClick={finish} className="w-full bg-gold text-primary-foreground hover:opacity-90">
          Finish session
        </Button>
      </div>

      {resting && <RestTimer seconds={90} onComplete={() => setResting(false)} onSkip={() => setResting(false)} />}
    </div>
  );
}

function AddExerciseRow({ onAdd }: { onAdd: (id: string) => void }) {
  const [open, setOpen] = useState(false);
  const [query, setQuery] = useState("");
  const filtered = useMemo(
    () => exerciseById(query) ? [exerciseById(query)!] : [],
    [query],
  );
  return (
    <div className="border-rune rounded-md bg-card p-4">
      {!open ? (
        <Button variant="ghost" onClick={() => setOpen(true)} className="w-full">
          <Plus className="mr-1 h-4 w-4" /> Add exercise
        </Button>
      ) : (
        <div className="space-y-2">
          <Input placeholder="Type exercise id (e.g. ex-bench)" value={query} onChange={(e) => setQuery(e.target.value)} />
          <div className="flex flex-wrap gap-2">
            {filtered.map((ex) => (
              <button
                key={ex.id}
                type="button"
                onClick={() => {
                  onAdd(ex.id);
                  setOpen(false);
                  setQuery("");
                }}
                className="rounded-md border border-gold-dim px-3 py-1 text-xs hover:bg-elevated"
              >
                + {ex.name}
              </button>
            ))}
          </div>
          <p className="text-[10px] text-muted-foreground">Tip: open the launcher for full search.</p>
        </div>
      )}
    </div>
  );
}
