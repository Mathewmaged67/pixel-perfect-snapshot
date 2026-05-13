import { cn } from "@/lib/utils";

interface XpBarProps {
  value: number;
  max: number;
  className?: string;
  showText?: boolean;
}

export function XpBar({ value, max, className, showText = true }: XpBarProps) {
  const pct = Math.min(100, Math.max(0, (value / max) * 100));
  return (
    <div className={cn("w-full", className)}>
      <div className="relative h-3 w-full overflow-hidden rounded-full border border-gold-dim/60 bg-elevated">
        <div className="xp-shimmer h-full transition-[width] duration-700 ease-out" style={{ width: `${pct}%` }} />
      </div>
      {showText && (
        <div className="mt-1 flex justify-between text-[10px] tabular text-muted-foreground">
          <span>{Math.round(value)} XP</span>
          <span>{max} XP</span>
        </div>
      )}
    </div>
  );
}
