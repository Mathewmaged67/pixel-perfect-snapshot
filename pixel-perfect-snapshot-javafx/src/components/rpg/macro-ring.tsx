import { cn } from "@/lib/utils";

interface MacroRingProps {
  label: string;
  value: number;
  goal: number;
  color: "protein" | "carbs" | "fat";
  size?: number;
  className?: string;
}

const COLOR_MAP = {
  protein: "var(--protein)",
  carbs: "var(--carbs)",
  fat: "var(--fat)",
} as const;

export function MacroRing({ label, value, goal, color, size = 96, className }: MacroRingProps) {
  const stroke = 8;
  const radius = (size - stroke) / 2;
  const circumference = 2 * Math.PI * radius;
  const pct = Math.min(1, value / Math.max(1, goal));
  const offset = circumference * (1 - pct);
  return (
    <div className={cn("flex flex-col items-center gap-1", className)}>
      <div className="relative" style={{ width: size, height: size }}>
        <svg width={size} height={size} className="-rotate-90">
          <circle
            cx={size / 2}
            cy={size / 2}
            r={radius}
            stroke="var(--border)"
            strokeWidth={stroke}
            fill="none"
          />
          <circle
            cx={size / 2}
            cy={size / 2}
            r={radius}
            stroke={COLOR_MAP[color]}
            strokeWidth={stroke}
            strokeLinecap="round"
            fill="none"
            strokeDasharray={circumference}
            strokeDashoffset={offset}
            style={{ transition: "stroke-dashoffset 600ms ease" }}
          />
        </svg>
        <div className="absolute inset-0 flex flex-col items-center justify-center">
          <span className="tabular text-base text-foreground">{Math.round(value)}</span>
          <span className="text-[9px] tabular text-muted-foreground">/ {Math.round(goal)} g</span>
        </div>
      </div>
      <span className="font-display text-[10px] uppercase text-muted-foreground">{label}</span>
    </div>
  );
}
