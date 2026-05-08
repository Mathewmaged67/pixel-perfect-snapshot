import { createFileRoute, useNavigate } from "@tanstack/react-router";
import { useState } from "react";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { CharacterSprite } from "@/components/rpg/character-sprite";
import { useCharacterStore } from "@/stores/character-store";
import type { CharacterClass, Gender } from "@/lib/types";
import { Swords, Bow, Wand2, Shield } from "lucide-react";
import { cn } from "@/lib/utils";
import heroBg from "@/assets/hero-bg.jpg";

export const Route = createFileRoute("/onboarding")({
  head: () => ({
    meta: [
      { title: "Create your hero — FitQuest" },
      { name: "description", content: "Forge your fitness hero and start your quest." },
    ],
  }),
  component: Onboarding,
});

const CLASSES: { id: CharacterClass; label: string; tagline: string; icon: typeof Swords }[] = [
  { id: "warrior", label: "Warrior", tagline: "Strength & powerlifting", icon: Swords },
  { id: "ranger", label: "Ranger", tagline: "Cardio & endurance", icon: Bow },
  { id: "mage", label: "Mage", tagline: "Mobility & yoga", icon: Wand2 },
  { id: "paladin", label: "Paladin", tagline: "Balanced fitness", icon: Shield },
];

const GENDERS: { id: Gender; label: string }[] = [
  { id: "male", label: "Male" },
  { id: "female", label: "Female" },
  { id: "nonbinary", label: "Non-binary" },
];

function Onboarding() {
  const navigate = useNavigate();
  const init = useCharacterStore((s) => s.init);
  const [step, setStep] = useState(0);
  const [name, setName] = useState("");
  const [gender, setGender] = useState<Gender>("male");
  const [characterClass, setClass] = useState<CharacterClass>("warrior");
  const [weight, setWeight] = useState(75);
  const [height, setHeight] = useState(175);
  const [weeklyTarget, setWeeklyTarget] = useState(4);

  const next = () => setStep((s) => s + 1);
  const back = () => setStep((s) => Math.max(0, s - 1));

  const finish = () => {
    init({ name: name || "Hero", characterClass, gender, weight, height, weeklyTarget });
    navigate({ to: "/dashboard" });
  };

  return (
    <div
      className="relative flex min-h-screen items-center justify-center px-4 py-10"
      style={{ backgroundImage: `url(${heroBg})`, backgroundSize: "cover", backgroundPosition: "center" }}
    >
      <div className="absolute inset-0 bg-background/70" />
      <div className="relative z-10 w-full max-w-3xl rounded-lg border border-gold-dim/40 bg-card/95 p-6 shadow-[0_0_40px_rgba(0,0,0,0.6)] sm:p-10">
        <div className="mb-6 flex items-center justify-between">
          <h1 className="font-display text-base text-gold">Forge your hero</h1>
          <span className="font-display text-[10px] uppercase text-muted-foreground">Step {step + 1} / 4</span>
        </div>

        <div className="grid gap-2">
          <div className="h-1 overflow-hidden rounded-full bg-elevated">
            <div className="h-full bg-gold transition-[width] duration-300" style={{ width: `${((step + 1) / 4) * 100}%` }} />
          </div>
        </div>

        <div className="mt-8 min-h-[320px]">
          {step === 0 && (
            <div className="grid gap-6 sm:grid-cols-[220px_1fr] sm:items-center">
              <div className="flex justify-center">
                <CharacterSprite characterClass={characterClass} gender={gender} size={220} />
              </div>
              <div className="space-y-4">
                <div>
                  <Label htmlFor="hero-name">Hero name</Label>
                  <Input
                    id="hero-name"
                    value={name}
                    onChange={(e) => setName(e.target.value)}
                    placeholder="Aelric the Bold"
                    className="mt-1"
                  />
                </div>
                <div>
                  <Label>Avatar</Label>
                  <div className="mt-2 flex flex-wrap gap-2">
                    {GENDERS.map((g) => (
                      <button
                        key={g.id}
                        type="button"
                        onClick={() => setGender(g.id)}
                        className={cn(
                          "rounded-md border px-3 py-2 text-sm transition",
                          gender === g.id
                            ? "border-gold bg-elevated text-gold"
                            : "border-border text-muted-foreground hover:border-gold-dim",
                        )}
                      >
                        {g.label}
                      </button>
                    ))}
                  </div>
                </div>
              </div>
            </div>
          )}

          {step === 1 && (
            <div className="grid gap-3 sm:grid-cols-2">
              {CLASSES.map((c) => {
                const Icon = c.icon;
                const active = characterClass === c.id;
                return (
                  <button
                    key={c.id}
                    type="button"
                    onClick={() => setClass(c.id)}
                    className={cn(
                      "flex items-center gap-4 rounded-md border p-4 text-left transition",
                      active ? "border-gold bg-elevated" : "border-border hover:border-gold-dim",
                    )}
                  >
                    <Icon className={cn("h-8 w-8", active ? "text-gold" : "text-muted-foreground")} />
                    <div>
                      <p className="font-display text-xs uppercase text-foreground">{c.label}</p>
                      <p className="text-xs text-muted-foreground">{c.tagline}</p>
                    </div>
                  </button>
                );
              })}
            </div>
          )}

          {step === 2 && (
            <div className="grid gap-4 sm:grid-cols-3">
              <div>
                <Label htmlFor="weight">Weight (kg)</Label>
                <Input id="weight" type="number" value={weight} onChange={(e) => setWeight(Number(e.target.value))} className="mt-1" />
              </div>
              <div>
                <Label htmlFor="height">Height (cm)</Label>
                <Input id="height" type="number" value={height} onChange={(e) => setHeight(Number(e.target.value))} className="mt-1" />
              </div>
              <div>
                <Label htmlFor="target">Weekly workouts</Label>
                <Input id="target" type="number" min={1} max={7} value={weeklyTarget} onChange={(e) => setWeeklyTarget(Number(e.target.value))} className="mt-1" />
              </div>
              <p className="sm:col-span-3 text-xs text-muted-foreground">
                These shape your starting goals. You can change them later in your profile.
              </p>
            </div>
          )}

          {step === 3 && (
            <div className="flex flex-col items-center justify-center gap-6 text-center">
              <CharacterSprite characterClass={characterClass} gender={gender} size={200} />
              <div>
                <h2 className="font-display text-lg text-gold text-glow-gold">Your quest begins</h2>
                <p className="mt-2 text-sm text-muted-foreground">
                  {name || "Hero"}, the {CLASSES.find((c) => c.id === characterClass)?.label}, steps onto the path.
                </p>
              </div>
            </div>
          )}
        </div>

        <div className="mt-8 flex justify-between gap-3">
          <Button variant="ghost" onClick={back} disabled={step === 0}>
            Back
          </Button>
          {step < 3 ? (
            <Button onClick={next}>Next</Button>
          ) : (
            <Button onClick={finish} className="bg-gold text-primary-foreground hover:opacity-90">
              Begin quest
            </Button>
          )}
        </div>
      </div>
    </div>
  );
}
