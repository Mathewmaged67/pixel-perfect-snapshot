import { createFileRoute } from "@tanstack/react-router";

export const Route = createFileRoute("/workout/templates")({
  component: Templates,
});

const TEMPLATES = [
  { id: "tpl-push", name: "Push Day", exercises: ["Bench", "Overhead Press", "Dips"], badge: "STR" },
  { id: "tpl-pull", name: "Pull Day", exercises: ["Deadlift", "Pull Up", "Row"], badge: "STR" },
  { id: "tpl-legs", name: "Leg Day", exercises: ["Squat", "RDL", "Calf Raise"], badge: "STR" },
  { id: "tpl-cardio", name: "Cardio Burn", exercises: ["Run", "Bike", "Burpees"], badge: "STA" },
  { id: "tpl-mobility", name: "Mobility Flow", exercises: ["Sun Salutation", "Hip Mobility", "Cat-Cow"], badge: "AGI" },
];

function Templates() {
  return (
    <div className="space-y-4">
      <header>
        <h1 className="text-xl">Templates</h1>
        <p className="text-sm text-muted-foreground">Saved routines for quick start.</p>
      </header>
      <ul className="grid gap-3 sm:grid-cols-2">
        {TEMPLATES.map((t) => (
          <li key={t.id} className="border-rune rounded-md bg-card p-4">
            <div className="flex items-center justify-between">
              <h3 className="text-sm">{t.name}</h3>
              <span className="font-display text-[10px] uppercase text-gold">{t.badge}</span>
            </div>
            <ul className="mt-2 text-xs text-muted-foreground">
              {t.exercises.map((e) => (
                <li key={e}>· {e}</li>
              ))}
            </ul>
          </li>
        ))}
      </ul>
    </div>
  );
}
