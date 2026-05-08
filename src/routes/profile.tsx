import { createFileRoute, useNavigate } from "@tanstack/react-router";
import { AppShell } from "@/components/layout/app-shell";
import { useCharacterStore } from "@/stores/character-store";
import { useUiStore } from "@/stores/ui-store";
import { useWorkoutStore } from "@/stores/workout-store";
import { Button } from "@/components/ui/button";
import { CharacterSprite } from "@/components/rpg/character-sprite";
import { StatBadge } from "@/components/rpg/stat-badge";

export const Route = createFileRoute("/profile")({
  head: () => ({
    meta: [
      { title: "Profile — FitQuest" },
      { name: "description", content: "Stats, settings, and your hero's history." },
    ],
  }),
  component: Profile,
});

function Profile() {
  const navigate = useNavigate();
  const c = useCharacterStore();
  const ui = useUiStore();
  const history = useWorkoutStore((s) => s.history);

  return (
    <AppShell>
      <header className="mb-6">
        <h1 className="text-xl">{c.name || "Hero"}</h1>
        <p className="text-sm text-muted-foreground">Lv. {c.level} {c.characterClass}</p>
      </header>

      <div className="grid gap-6 lg:grid-cols-[280px_1fr]">
        <div className="border-rune flex flex-col items-center rounded-md bg-card p-5">
          <CharacterSprite characterClass={c.characterClass} gender={c.gender} size={200} />
          <div className="mt-4 grid w-full grid-cols-4 gap-2">
            <StatBadge stat="str" value={c.stats.str} />
            <StatBadge stat="sta" value={c.stats.sta} />
            <StatBadge stat="vit" value={c.stats.vit} />
            <StatBadge stat="agi" value={c.stats.agi} />
          </div>
        </div>

        <div className="space-y-6">
          <div className="border-rune rounded-md bg-card p-5">
            <h2 className="font-display text-xs uppercase text-gold">Activity</h2>
            <p className="mt-2 text-sm text-muted-foreground">
              {history.length} sessions logged · {c.streak} day streak
            </p>
            <div className="mt-3 grid grid-cols-7 gap-1">
              {Array.from({ length: 35 }).map((_, i) => {
                const intensity = Math.random();
                const on = i < 35 - 7 || Math.random() > 0.4;
                return (
                  <div
                    key={i}
                    className="aspect-square rounded-sm border border-border"
                    style={{ background: on ? `oklch(0.55 ${0.15 * intensity} 145)` : "var(--elevated)" }}
                  />
                );
              })}
            </div>
          </div>

          <div className="border-rune rounded-md bg-card p-5">
            <h2 className="font-display text-xs uppercase text-gold">Settings</h2>
            <div className="mt-3 flex flex-wrap items-center gap-4">
              <div>
                <p className="text-[10px] uppercase text-muted-foreground">Theme</p>
                <div className="mt-1 flex gap-2">
                  {(["dark", "light"] as const).map((t) => (
                    <Button
                      key={t}
                      size="sm"
                      variant={ui.theme === t ? "default" : "outline"}
                      onClick={() => ui.setTheme(t)}
                    >
                      {t === "dark" ? "Dark fantasy" : "Light parchment"}
                    </Button>
                  ))}
                </div>
              </div>
              <div>
                <p className="text-[10px] uppercase text-muted-foreground">Units</p>
                <div className="mt-1 flex gap-2">
                  {(["kg", "lbs"] as const).map((u) => (
                    <Button
                      key={u}
                      size="sm"
                      variant={ui.units === u ? "default" : "outline"}
                      onClick={() => ui.setUnits(u)}
                    >
                      {u}
                    </Button>
                  ))}
                </div>
              </div>
              <div>
                <p className="text-[10px] uppercase text-muted-foreground">Reset</p>
                <Button
                  size="sm"
                  variant="destructive"
                  className="mt-1"
                  onClick={() => {
                    c.reset();
                    navigate({ to: "/onboarding" });
                  }}
                >
                  Restart character
                </Button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </AppShell>
  );
}
