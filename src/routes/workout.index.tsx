import { createFileRoute, Link, useNavigate } from "@tanstack/react-router";
import { useMemo, useState } from "react";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { exercises } from "@/data/exercises";
import { useWorkoutStore } from "@/stores/workout-store";
import { cn } from "@/lib/utils";
import { Search, Plus } from "lucide-react";

export const Route = createFileRoute("/workout/")({
  component: WorkoutStart,
});

function WorkoutStart() {
  const navigate = useNavigate();
  const startSession = useWorkoutStore((s) => s.startSession);
  const active = useWorkoutStore((s) => s.active);
  const history = useWorkoutStore((s) => s.history);

  const [name, setName] = useState("Daily Quest Session");
  const [picked, setPicked] = useState<string[]>([]);
  const [query, setQuery] = useState("");

  const filtered = useMemo(
    () => exercises.filter((e) => e.name.toLowerCase().includes(query.toLowerCase())),
    [query],
  );

  const toggle = (id: string) => setPicked((p) => (p.includes(id) ? p.filter((x) => x !== id) : [...p, id]));

  const start = () => {
    if (picked.length === 0) return;
    const id = startSession(name, picked);
    navigate({ to: "/workout/$id", params: { id } });
  };

  return (
    <div className="space-y-6">
      <header className="flex flex-wrap items-center justify-between gap-3">
        <div>
          <h1 className="text-xl text-foreground">Start a session</h1>
          <p className="text-sm text-muted-foreground">Pick exercises to begin your quest.</p>
        </div>
        <nav className="flex gap-2 text-xs">
          <Link to="/workout/history" className="rounded-md border border-border px-3 py-2 hover:border-gold-dim">
            History
          </Link>
          <Link to="/workout/templates" className="rounded-md border border-border px-3 py-2 hover:border-gold-dim">
            Templates
          </Link>
        </nav>
      </header>

      {active && (
        <div className="border-rune flex items-center justify-between rounded-md bg-card p-4">
          <p className="text-sm">
            You have an active session: <span className="text-gold">{active.name}</span>
          </p>
          <Button asChild>
            <Link to="/workout/$id" params={{ id: active.id }}>Resume</Link>
          </Button>
        </div>
      )}

      <div className="grid gap-6 lg:grid-cols-[1fr_320px]">
        <section className="border-rune rounded-md bg-card p-5">
          <div className="mb-3 flex items-center gap-2">
            <Search className="h-4 w-4 text-muted-foreground" />
            <Input value={query} onChange={(e) => setQuery(e.target.value)} placeholder="Search exercise" />
          </div>
          <ul className="grid max-h-[60vh] grid-cols-1 gap-2 overflow-y-auto sm:grid-cols-2">
            {filtered.map((ex) => {
              const active = picked.includes(ex.id);
              return (
                <li key={ex.id}>
                  <button
                    type="button"
                    onClick={() => toggle(ex.id)}
                    className={cn(
                      "flex w-full items-center justify-between rounded-md border px-3 py-2 text-left transition",
                      active ? "border-gold bg-elevated" : "border-border hover:border-gold-dim",
                    )}
                  >
                    <div>
                      <p className="text-sm">{ex.name}</p>
                      <p className="text-[10px] uppercase text-muted-foreground">
                        {ex.muscles.join(" · ")}
                      </p>
                    </div>
                    <Plus className={cn("h-4 w-4", active && "rotate-45 text-gold")} />
                  </button>
                </li>
              );
            })}
          </ul>
        </section>

        <aside className="border-rune sticky top-4 self-start rounded-md bg-card p-5">
          <label className="font-display text-[10px] uppercase text-muted-foreground">Session name</label>
          <Input value={name} onChange={(e) => setName(e.target.value)} className="mt-1" />
          <p className="mt-4 font-display text-[10px] uppercase text-muted-foreground">Selected ({picked.length})</p>
          <ul className="mt-2 space-y-1 text-sm">
            {picked.map((id) => {
              const ex = exercises.find((e) => e.id === id);
              return (
                <li key={id} className="text-muted-foreground">
                  · {ex?.name}
                </li>
              );
            })}
            {picked.length === 0 && <li className="text-xs text-muted-foreground">Pick exercises to add them here.</li>}
          </ul>
          <Button onClick={start} disabled={picked.length === 0} className="mt-5 w-full">
            Begin quest
          </Button>
          {history.length > 0 && (
            <p className="mt-3 text-center text-[10px] text-muted-foreground">
              Previous sessions: {history.length}
            </p>
          )}
        </aside>
      </div>
    </div>
  );
}
