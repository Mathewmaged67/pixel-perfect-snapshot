import { cn } from "@/lib/utils";
import type { Quest } from "@/lib/types";
import { Coins, Sparkles } from "lucide-react";

interface QuestCardProps {
  quest: Quest;
  onComplete?: (id: string) => void;
}

export function QuestCard({ quest, onComplete }: QuestCardProps) {
  const pct = Math.min(100, (quest.progress / quest.goal) * 100);
  const ready = quest.progress >= quest.goal && !quest.completed;
  return (
    <div
      className={cn(
        "rounded-md border border-border bg-card p-4 transition",
        ready && "border-gold legend-pulse",
        quest.completed && "opacity-60",
      )}
    >
      <div className="flex items-start justify-between gap-2">
        <div>
          <p className="font-display text-[10px] uppercase text-muted-foreground">{quest.scope}</p>
          <h4 className="mt-1 text-base text-foreground">{quest.title}</h4>
          <p className="mt-1 text-xs text-muted-foreground">{quest.description}</p>
        </div>
        <div className="flex flex-col items-end gap-1">
          <span className="flex items-center gap-1 text-xs tabular text-gold">
            <Sparkles className="h-3 w-3" /> {quest.rewardXp} XP
          </span>
          <span className="flex items-center gap-1 text-xs tabular text-gold">
            <Coins className="h-3 w-3" /> {quest.rewardGold}
          </span>
        </div>
      </div>
      <div className="mt-3">
        <div className="h-2 w-full overflow-hidden rounded-full border border-border bg-elevated">
          <div className="h-full bg-gold transition-[width] duration-500" style={{ width: `${pct}%` }} />
        </div>
        <div className="mt-1 flex justify-between text-[10px] tabular text-muted-foreground">
          <span>
            {quest.progress} / {quest.goal}
          </span>
          {ready && onComplete && (
            <button
              type="button"
              onClick={() => onComplete(quest.id)}
              className="font-display text-[10px] uppercase text-gold hover:text-foreground"
            >
              Done
            </button>
          )}
        </div>
      </div>
    </div>
  );
}
