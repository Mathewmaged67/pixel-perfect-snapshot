import { useEffect, useState } from "react";
import { cn } from "@/lib/utils";

interface RestTimerProps {
  seconds: number;
  onComplete?: () => void;
  onSkip?: () => void;
}

export function RestTimer({ seconds, onComplete, onSkip }: RestTimerProps) {
  const [remaining, setRemaining] = useState(seconds);
  useEffect(() => {
    if (remaining <= 0) {
      onComplete?.();
      return;
    }
    const t = setTimeout(() => setRemaining((r) => r - 1), 1000);
    return () => clearTimeout(t);
  }, [remaining, onComplete]);

  const size = 220;
  const stroke = 14;
  const radius = (size - stroke) / 2;
  const c = 2 * Math.PI * radius;
  const offset = c * (1 - remaining / seconds);

  return (
    <div className="fixed inset-0 z-50 flex flex-col items-center justify-center gap-6 bg-background/90 backdrop-blur-sm">
      <p className="font-display text-xs uppercase text-muted-foreground">Rest</p>
      <div className="relative" style={{ width: size, height: size }}>
        <svg width={size} height={size} className="-rotate-90">
          <circle cx={size / 2} cy={size / 2} r={radius} stroke="var(--border)" strokeWidth={stroke} fill="none" />
          <circle
            cx={size / 2}
            cy={size / 2}
            r={radius}
            stroke="var(--gold)"
            strokeWidth={stroke}
            strokeLinecap="round"
            fill="none"
            strokeDasharray={c}
            strokeDashoffset={offset}
            style={{ transition: "stroke-dashoffset 1s linear" }}
          />
        </svg>
        <div className={cn("absolute inset-0 flex items-center justify-center font-display text-4xl text-foreground")}>
          {remaining}
        </div>
      </div>
      <button
        type="button"
        onClick={onSkip}
        className="rounded-md border border-gold-dim px-4 py-2 font-display text-[10px] uppercase text-gold hover:bg-elevated"
      >
        Skip
      </button>
    </div>
  );
}
