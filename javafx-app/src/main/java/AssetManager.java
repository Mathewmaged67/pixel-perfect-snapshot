import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javafx.scene.image.Image;

public final class AssetManager {
  private static final Path ASSET_ROOT = Paths.get("..", "src", "assets").toAbsolutePath().normalize();
  private static final Map<CharacterClass, Image> SPRITE_SHEETS = new EnumMap<>(CharacterClass.class);
  private static final Map<String, Image> OVERRIDE_SPRITES = new HashMap<>();
  private static final Set<String> OVERRIDE_MISSING = new HashSet<>();
  private static Image gearSheet;

  private AssetManager() {}

  public static Image getSpriteSheet(CharacterClass characterClass) {
    return SPRITE_SHEETS.computeIfAbsent(characterClass, AssetManager::loadSpriteSheet);
  }

  public static Image getGearSheet() {
    if (gearSheet == null) {
      gearSheet = new Image(ASSET_ROOT.resolve("gear-sheet.png").toUri().toString());
    }
    return gearSheet;
  }

  public static Image getSpriteOverride(CharacterClass characterClass, Gender gender) {
    if (characterClass == null || gender == null) {
      return null;
    }
    String key = characterClass.name().toLowerCase() + "-" + gender.name().toLowerCase();
    if (OVERRIDE_SPRITES.containsKey(key)) {
      return OVERRIDE_SPRITES.get(key);
    }
    if (OVERRIDE_MISSING.contains(key)) {
      return null;
    }

    Path path = ASSET_ROOT.resolve("sprites").resolve(key + ".png");
    if (!Files.exists(path)) {
      OVERRIDE_MISSING.add(key);
      return null;
    }

    Image image = new Image(path.toUri().toString());
    OVERRIDE_SPRITES.put(key, image);
    return image;
  }

  private static Image loadSpriteSheet(CharacterClass characterClass) {
    String fileName = switch (characterClass) {
      case WARRIOR -> "warrior.png";
      case RANGER -> "ranger.png";
      case MAGE -> "mage.png";
      case PALADIN -> "paladin.png";
    };
    return new Image(ASSET_ROOT.resolve("sprites").resolve(fileName).toUri().toString());
  }
}
