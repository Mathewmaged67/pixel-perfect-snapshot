import type { MuscleGroup } from "@/lib/types";
import { cn } from "@/lib/utils";

interface HeatMapProps {
  intensities: Partial<Record<MuscleGroup, number>>;
  className?: string;
}

const intensityFill = (v?: number) => {
  if (!v || v <= 0) return "var(--elevated)";
  if (v < 0.34) return "oklch(0.65 0.15 30)";
  if (v < 0.67) return "oklch(0.65 0.20 25)";
  return "oklch(0.62 0.24 20)";
};

export function HeatMap({ intensities, className }: HeatMapProps) {
  const f = (m: MuscleGroup) => intensityFill(intensities[m]);
  return (
    <svg viewBox="0 0 160 240" className={cn("h-56 w-auto", className)} aria-label="Muscle heat map">
      <circle cx="80" cy="24" r="16" fill="var(--elevated)" stroke="var(--border)" />
      {/* shoulders */}
      <ellipse cx="50" cy="56" rx="16" ry="10" fill={f("shoulders")} stroke="var(--border)" />
      <ellipse cx="110" cy="56" rx="16" ry="10" fill={f("shoulders")} stroke="var(--border)" />
      {/* chest */}
      <rect x="52" y="60" width="56" height="36" rx="8" fill={f("chest")} stroke="var(--border)" />
      {/* arms */}
      <rect x="28" y="64" width="14" height="56" rx="6" fill={f("arms")} stroke="var(--border)" />
      <rect x="118" y="64" width="14" height="56" rx="6" fill={f("arms")} stroke="var(--border)" />
      {/* core */}
      <rect x="58" y="98" width="44" height="44" rx="8" fill={f("core")} stroke="var(--border)" />
      {/* glutes */}
      <rect x="56" y="142" width="48" height="22" rx="10" fill={f("glutes")} stroke="var(--border)" />
      {/* legs */}
      <rect x="54" y="166" width="22" height="64" rx="8" fill={f("legs")} stroke="var(--border)" />
      <rect x="84" y="166" width="22" height="64" rx="8" fill={f("legs")} stroke="var(--border)" />
      {/* back hint */}
      <text x="80" y="14" textAnchor="middle" className="fill-muted-foreground" style={{ fontSize: 8 }}>
        FRONT
      </text>
    </svg>
  );
}
