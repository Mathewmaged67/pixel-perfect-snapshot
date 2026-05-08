import { createFileRoute } from "@tanstack/react-router";
import { useQuestStore } from "@/stores/quest-store";
import { useCharacterStore } from "@/stores/character-store";
import { QuestCard } from "@/components/rpg/quest-card";
import { toast } from "sonner";

export const Route = createFileRoute("/quests")({
  head: () => ({
    meta: [
      { title: "Quests — FitQuest" },
      { name: "description", content: "Daily, weekly, and epic quests to power your hero." },
    ],
  }),
  component: Quests,
});

function Quests() {
  const quests = useQuestStore((s) => s.quests);
  const complete = useQuestStore((s) => s.complete);
  const character = useCharacterStore();

  const claim = (id: string) => {
    const q = quests.find((x) => x.id === id);
    if (!q) return;
    complete(id);
    character.addXp(q.rewardXp);
    character.addGold(q.rewardGold);
    toast.success(`+${q.rewardXp} XP, +${q.rewardGold} gold`, { description: q.title });
  };

  const groups = [
    { label: "Daily", items: quests.filter((q) => q.scope === "daily") },
    { label: "Weekly", items: quests.filter((q) => q.scope === "weekly") },
    { label: "Epic", items: quests.filter((q) => q.scope === "epic") },
  ];

  return (
    <>
      <header className="mb-6">
        <h1 className="text-xl">Quest board</h1>
        <p className="text-sm text-muted-foreground">Complete quests to earn XP, gold, and gear.</p>
      </header>
      <div className="space-y-8">
        {groups.map((g) => (
          <section key={g.label}>
            <h2 className="font-display mb-3 text-xs uppercase text-gold">{g.label}</h2>
            <div className="grid gap-3 sm:grid-cols-2 lg:grid-cols-3">
              {g.items.map((q) => (
                <QuestCard key={q.id} quest={q} onComplete={claim} />
              ))}
            </div>
          </section>
        ))}
      </div>
    </>
  );
}
