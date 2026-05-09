import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public final class SpriteUtil {
  private static final int SPRITE_COLUMNS = 3;

  private SpriteUtil() {}

  public static void applyCharacterSprite(ImageView view, CharacterState character, double size) {
    if (view == null || character == null) {
      return;
    }
    Image sheet = AssetManager.getSpriteSheet(character.getCharacterClass());
    if (sheet == null || sheet.isError()) {
      return;
    }
    int genderIndex = character.getGender() == Gender.MALE ? 0 : 1;
    double panelWidth = sheet.getWidth() / SPRITE_COLUMNS;
    double panelHeight = sheet.getHeight();
    view.setImage(sheet);
    view.setViewport(new Rectangle2D(panelWidth * genderIndex, 0, panelWidth, panelHeight));
    view.setFitWidth(size);
    view.setFitHeight(size);
  }
}
