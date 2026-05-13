import { cn } from "@/lib/utils";
import type { CharacterClass, Gender } from "@/lib/types";
import warriorSheet from "@/assets/sprites/warrior.png";
import rangerSheet from "@/assets/sprites/ranger.png";
import mageSheet from "@/assets/sprites/mage.png";
import paladinSheet from "@/assets/sprites/paladin.png";

const SHEETS: Record<CharacterClass, string> = {
  warrior: warriorSheet,
  ranger: rangerSheet,
  mage: mageSheet,
  paladin: paladinSheet,
};

const GENDER_INDEX: Record<Gender, number> = { male: 0, female: 1, nonbinary: 2 };

interface CharacterSpriteProps {
  characterClass: CharacterClass;
  gender: Gender;
  size?: number;
  bob?: boolean;
  className?: string;
}

export function CharacterSprite({ characterClass, gender, size = 220, bob = true, className }: CharacterSpriteProps) {
  const idx = GENDER_INDEX[gender];
  // Each sheet has 3 panels horizontally — show one panel
  const sheet = SHEETS[characterClass];
  return (
    <div
      className={cn("relative overflow-hidden", bob && "idle-bob", className)}
      style={{ width: size, height: size }}
    >
      <div
        className="absolute inset-0 bg-no-repeat"
        style={{
          backgroundImage: `url(${sheet})`,
          backgroundSize: "300% 100%",
          backgroundPosition: `${(idx / 2) * 100}% center`,
        }}
        aria-hidden
      />
    </div>
  );
}
