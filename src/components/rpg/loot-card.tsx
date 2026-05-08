import { cn } from "@/lib/utils";
import type { Rarity } from "@/lib/types";
import gearSheet from "@/assets/gear-sheet.png";

interface LootCardProps {
  name: string;
  rarity: Rarity;
  spriteIndex?: number;
  hint?: string;
  locked?: boolean;
  className?: string;
  children?: React.ReactNode;
  onClick?: () => void;
}

const RARITY_BORDER: Record<Rarity, string> = {
  common: "border-rarity-common/60",
  rare: "border-rarity-rare",
  epic: "border-rarity-epic",
  legend: "border-rarity-legend legend-pulse",
};

const RARITY_TEXT: Record<Rarity, string> = {
  common: "text-rarity-common",
  rare: "text-rarity-rare",
  epic: "text-rarity-epic",
  legend: "text-rarity-legend",
};

// gear sheet is 4 cols x 2 rows of 256x256 cells
const COLS = 4;
const ROWS = 2;

export function LootCard({ name, rarity, spriteIndex, hint, locked, className, children, onClick }: LootCardProps) {
  const col = spriteIndex !== undefined ? spriteIndex % COLS : 0;
  const row = spriteIndex !== undefined ? Math.floor(spriteIndex / COLS) : 0;
  return (
    <button
      type="button"
      onClick={onClick}
      className={cn(
        "group flex flex-col items-center gap-2 rounded-md border-2 bg-card p-4 text-center transition hover:scale-[1.02]",
        RARITY_BORDER[rarity],
        locked && "opacity-50 grayscale",
        className,
      )}
    >
      {spriteIndex !== undefined ? (
        <div
          className="h-20 w-20 bg-no-repeat"
          style={{
            backgroundImage: `url(${gearSheet})`,
            backgroundSize: `${COLS * 100}% ${ROWS * 100}%`,
            backgroundPosition: `${(col / (COLS - 1)) * 100}% ${(row / (ROWS - 1)) * 100}%`,
          }}
          aria-hidden
        />
      ) : (
        <div className="h-20 w-20 rounded-md bg-elevated" />
      )}
      <p className="font-display text-[10px] uppercase">{name}</p>
      <p className={cn("text-[10px]", RARITY_TEXT[rarity])}>{rarity}</p>
      {hint && <p className="text-[10px] text-muted-foreground">{hint}</p>}
      {children}
    </button>
  );
}
