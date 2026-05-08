import { Flame } from "lucide-react";
import { cn } from "@/lib/utils";

export function StreakBadge({ days, className }: { days: number; className?: string }) {
  return (
    <span
      className={cn(
        "inline-flex items-center gap-1 rounded-full border border-border bg-elevated px-3 py-1 text-sm tabular",
        days >= 7 && "border-gold text-gold",
        className,
      )}
    >
      <Flame className="h-3.5 w-3.5" />
      {days} {days === 1 ? "day" : "days"}
    </span>
  );
}
