import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.EnumMap;
import java.util.Map;

import javafx.scene.image.Image;

public final class AssetManager {
  private static final Path ASSET_ROOT = Paths.get("..", "src", "assets").toAbsolutePath().normalize();
  private static final Map<CharacterClass, Image> SPRITE_SHEETS = new EnumMap<>(CharacterClass.class);
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
