import { cn } from "@/lib/utils";

interface VitalBarProps {
  label: string;
  value: number;
  max: number;
  color: "hp" | "mp";
  className?: string;
}

export function VitalBar({ label, value, max, color, className }: VitalBarProps) {
  const pct = Math.min(100, Math.max(0, (value / max) * 100));
  const bg = color === "hp" ? "bg-hp" : "bg-mp";
  return (
    <div className={cn("w-full", className)}>
      <div className="mb-0.5 flex items-center justify-between text-[10px] uppercase tracking-wider text-muted-foreground">
        <span className="font-display">{label}</span>
        <span className="tabular text-foreground">
          {Math.round(value)}/{max}
        </span>
      </div>
      <div className="h-2.5 w-full overflow-hidden rounded-full border border-border bg-elevated">
        <div className={cn("h-full transition-[width] duration-500", bg)} style={{ width: `${pct}%` }} />
      </div>
    </div>
  );
}
