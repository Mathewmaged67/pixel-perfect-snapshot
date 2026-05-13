import { cn } from "@/lib/utils";
import type { StatKey } from "@/lib/types";
import { Dumbbell, Footprints, Apple, Zap } from "lucide-react";

const META: Record<StatKey, { label: string; icon: typeof Dumbbell; cls: string }> = {
  str: { label: "STR", icon: Dumbbell, cls: "text-str" },
  sta: { label: "STA", icon: Footprints, cls: "text-sta" },
  vit: { label: "VIT", icon: Apple, cls: "text-vit" },
  agi: { label: "AGI", icon: Zap, cls: "text-agi" },
};

interface StatBadgeProps {
  stat: StatKey;
  value: number;
  trend?: number;
  className?: string;
}

export function StatBadge({ stat, value, trend, className }: StatBadgeProps) {
  const m = META[stat];
  const Icon = m.icon;
  return (
    <div className={cn("flex flex-col items-center gap-1 rounded-md border border-border bg-elevated/60 p-3", className)}>
      <Icon className={cn("h-5 w-5", m.cls)} />
      <span className="font-display text-[10px] text-muted-foreground">{m.label}</span>
      <span className="tabular text-lg text-foreground">{value}</span>
      {trend !== undefined && trend !== 0 && (
        <span className={cn("text-[10px] tabular", trend > 0 ? "text-hp" : "text-destructive")}>
          {trend > 0 ? "+" : ""}
          {trend}
        </span>
      )}
    </div>
  );
}
