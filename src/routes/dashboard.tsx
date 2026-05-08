import { createFileRoute, Link } from "@tanstack/react-router";
import { useMemo } from "react";
import { CharacterSprite } from "@/components/rpg/character-sprite";
import { XpBar } from "@/components/rpg/xp-bar";
import { VitalBar } from "@/components/rpg/vital-bar";
import { StatBadge } from "@/components/rpg/stat-badge";
import { MacroRing } from "@/components/rpg/macro-ring";
import { QuestCard } from "@/components/rpg/quest-card";
import { GoldCounter } from "@/components/rpg/gold-counter";
import { StreakBadge } from "@/components/rpg/streak-badge";
import { useCharacterStore } from "@/stores/character-store";
import { useNutritionStore } from "@/stores/nutrition-store";
import { useQuestStore } from "@/stores/quest-store";
import { useWorkoutStore } from "@/stores/workout-store";
import { Sword, Apple, Dumbbell, Trophy } from "lucide-react";
import { calculateTodayTotals } from "@/stores/nutrition-store";
import { toast } from "sonner";

export const Route = createFileRoute("/dashboard")({
  head: () => ({
    meta: [
      { title: "Dashboard — FitQuest" },
      { name: "description", content: "Your hero's HUD: stats, today's quests, and progress." },
    ],
  }),
  component: Dashboard,
});

function Dashboard() {
  const c = useCharacterStore();
  const goals = useNutritionStore((s) => s.goals);
  const log = useNutritionStore((s) => s.log);
  const totals = useMemo(() => calculateTodayTotals(log), [log]);
  const quests = useQuestStore((s) => s.quests);
  const complete = useQuestStore((s) => s.complete);
  const history = useWorkoutStore((s) => s.history);
  const activeDailyQuest = quests.find((q) => q.scope === "daily" && q.progress < q.goal);

  const claimQuest = (id: string) => {
    const quest = quests.find((q) => q.id === id);
    if (!quest) return;
    complete(id);
    c.addXp(quest.rewardXp);
    c.addGold(quest.rewardGold);
    toast.success(`+${quest.rewardXp} XP, +${quest.rewardGold} gold`, { description: quest.title });
  };

  return (
    <>
      <header className="mb-6 flex flex-wrap items-center justify-between gap-3">
        <div>
          <p className="font-display text-[10px] uppercase text-muted-foreground">Welcome back</p>
          <h1 className="text-xl text-foreground">{c.name || "Hero"}</h1>
        </div>
        <div className="flex items-center gap-2">
          <StreakBadge days={c.streak} />
          <GoldCounter value={c.gold} />
        </div>
      </header>

      <div className="grid gap-6 lg:grid-cols-[320px_1fr_320px]">
        {/* Character card */}
        <section className="border-rune rounded-md bg-card p-5">
          <div className="flex flex-col items-center text-center">
            <CharacterSprite characterClass={c.characterClass} gender={c.gender} size={200} />
            <p className="mt-2 font-display text-sm text-foreground">{c.name || "Hero"}</p>
            <span className="mt-1 inline-flex items-center gap-2 rounded-full border border-gold-dim/60 px-3 py-1 font-display text-[10px] uppercase text-gold">
              Lv. {c.level} {c.characterClass}
            </span>
            <div className="mt-4 w-full">
              <XpBar value={c.xp} max={c.xpToNext} />
            </div>
          </div>
          <div className="mt-5 grid grid-cols-4 gap-2">
            <StatBadge stat="str" value={c.stats.str} />
            <StatBadge stat="sta" value={c.stats.sta} />
            <StatBadge stat="vit" value={c.stats.vit} />
            <StatBadge stat="agi" value={c.stats.agi} />
          </div>
          <div className="mt-5 space-y-3">
            <VitalBar label="HP" value={c.hp} max={c.hpMax} color="hp" />
            <VitalBar label="MP" value={c.mp} max={c.mpMax} color="mp" />
          </div>
        </section>

        {/* Center */}
        <section className="space-y-6">
          <div className="border-rune rounded-md bg-card p-5">
            <div className="flex items-center justify-between">
              <h2 className="font-display text-xs uppercase text-gold">Active quest</h2>
              <Link to="/quests" className="text-xs text-muted-foreground hover:text-gold">
                View all
              </Link>
            </div>
            {activeDailyQuest ? (
              <div className="mt-4">
                <QuestCard quest={activeDailyQuest} onComplete={claimQuest} />
              </div>
            ) : (
              <p className="mt-4 text-sm text-muted-foreground">No active quests.</p>
            )}
          </div>

          <div className="grid gap-4 sm:grid-cols-3">
            <Link to="/workout" className="border-rune rounded-md bg-card p-4 transition hover:bg-elevated">
              <Dumbbell className="h-6 w-6 text-str" />
              <p className="mt-3 font-display text-[10px] uppercase text-muted-foreground">Today</p>
              <p className="mt-1 text-sm">Start a workout</p>
            </Link>
            <Link to="/nutrition" className="border-rune rounded-md bg-card p-4 transition hover:bg-elevated">
              <Apple className="h-6 w-6 text-vit" />
              <p className="mt-3 font-display text-[10px] uppercase text-muted-foreground">Fuel</p>
              <p className="mt-1 text-sm">Log a meal</p>
            </Link>
            <Link to="/quests" className="border-rune rounded-md bg-card p-4 transition hover:bg-elevated">
              <Trophy className="h-6 w-6 text-gold" />
              <p className="mt-3 font-display text-[10px] uppercase text-muted-foreground">Reward</p>
              <p className="mt-1 text-sm">Claim quests</p>
            </Link>
          </div>

          <div className="border-rune rounded-md bg-card p-5">
            <h2 className="font-display text-xs uppercase text-gold">Today's nutrition</h2>
            <div className="mt-4 flex flex-wrap items-center justify-around gap-4">
              <MacroRing label="Protein" value={totals.protein} goal={goals.protein} color="protein" />
              <MacroRing label="Carbs" value={totals.carbs} goal={goals.carbs} color="carbs" />
              <MacroRing label="Fat" value={totals.fat} goal={goals.fat} color="fat" />
              <div className="text-center">
                <p className="font-display text-[10px] uppercase text-muted-foreground">Calories</p>
                <p className="tabular text-2xl text-foreground">
                  {Math.round(totals.kcal)}
                  <span className="text-sm text-muted-foreground"> / {goals.kcal}</span>
                </p>
              </div>
            </div>
          </div>
        </section>

        {/* Right panel */}
        <aside className="space-y-4">
          <div className="border-rune rounded-md bg-card p-5">
            <h2 className="font-display text-xs uppercase text-gold">Recent gains</h2>
            <ul className="mt-3 space-y-2 text-sm">
              {history.slice(0, 4).map((h) => (
                <li key={h.id} className="flex items-center gap-2 text-muted-foreground">
                  <Sword className="h-3.5 w-3.5 text-gold" />
                  <span>
                    {h.name} —{" "}
                    <span className="tabular text-foreground">
                      +{h.exercises.reduce((n, e) => n + e.sets.filter((s) => s.done).length, 0)} sets
                    </span>
                  </span>
                </li>
              ))}
              {history.length === 0 && (
                <li className="text-xs text-muted-foreground">Your XP feed will appear here once you log a session.</li>
              )}
            </ul>
          </div>

          <div className="border-rune rounded-md bg-card p-5">
            <h2 className="font-display text-xs uppercase text-gold">Party</h2>
            <ul className="mt-3 space-y-3 text-sm">
              {[{ n: "Mira", lv: 9, hp: 80 }, { n: "Eldon", lv: 14, hp: 65 }, { n: "Sora", lv: 7, hp: 92 }].map((p) => (
                <li key={p.n}>
                  <div className="mb-1 flex justify-between text-xs">
                    <span>
                      {p.n} <span className="text-muted-foreground">Lv. {p.lv}</span>
                    </span>
                    <span className="tabular text-muted-foreground">{p.hp}/100</span>
                  </div>
                  <div className="h-2 overflow-hidden rounded-full bg-elevated">
                    <div className="h-full bg-hp" style={{ width: `${p.hp}%` }} />
                  </div>
                </li>
              ))}
            </ul>
          </div>
        </aside>
      </div>
    </>
  );
}
