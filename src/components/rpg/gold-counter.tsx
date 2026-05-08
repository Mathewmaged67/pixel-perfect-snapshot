import { Coins } from "lucide-react";
import { cn } from "@/lib/utils";

export function GoldCounter({ value, className }: { value: number; className?: string }) {
  return (
    <span className={cn("inline-flex items-center gap-1 rounded-full border border-gold-dim/50 bg-elevated px-3 py-1 text-sm tabular text-gold", className)}>
      <Coins className="h-3.5 w-3.5" />
      {value}
    </span>
  );
}
