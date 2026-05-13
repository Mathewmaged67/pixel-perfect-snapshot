import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;

public class InventoryController extends BaseController {
  @FXML private TilePane gearPane;
  @FXML private Label goldLabel;

  @Override
  public void onNavigatedTo(Route route) {
    super.onNavigatedTo(route);
    refresh();
  }

  private void refresh() {
    if (appState == null) {
      return;
    }
    goldLabel.setText("Gold: " + appState.getCharacter().getGold());
    gearPane.getChildren().clear();
    Image gearSheet = AssetManager.getGearSheet();
    int columns = 4;
    double cellWidth = gearSheet.getWidth() / columns;
    double cellHeight = gearSheet.getHeight() / 2.0;

    for (GearItem item : appState.getInventory().getItems()) {
      VBox card = new VBox(6);
      card.getStyleClass().add("card");
      card.getStyleClass().add("loot-" + item.getRarity().name().toLowerCase());
      card.setPrefWidth(200);
      if (!item.isOwned()) {
        card.getStyleClass().add("locked");
      }

      ImageView icon = new ImageView(gearSheet);
      int col = item.getSpriteIndex() % columns;
      int row = item.getSpriteIndex() / columns;
      icon.setViewport(new Rectangle2D(col * cellWidth, row * cellHeight, cellWidth, cellHeight));
      icon.setFitWidth(64);
      icon.setFitHeight(64);

      Label name = new Label(item.getName());
      Label rarity = new Label(item.getRarity().name());
      rarity.getStyleClass().add("muted");
      Label hint = new Label(item.isOwned() ? "Owned" : item.getUnlockHint());
      hint.getStyleClass().add("muted");
      hint.setWrapText(true);

      card.getChildren().addAll(icon, name, rarity, hint);
      gearPane.getChildren().add(card);
    }
  }
}
