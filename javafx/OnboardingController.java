import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.EnumMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

import javafx.animation.Animation;
import javafx.animation.TranslateTransition;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import javafx.util.StringConverter;

public class OnboardingController extends BaseController implements Initializable {
  private static final int MAX_STEP = 3;
  private static final int SPRITE_COLUMNS = 3;

  private enum CharacterClass {
    WARRIOR,
    RANGER,
    MAGE,
    PALADIN
  }

  private enum Gender {
    MALE,
    FEMALE
  }

  private record ClassMeta(String label, String tagline) {}

  public record HeroProfile(
      String name,
      CharacterClass characterClass,
      Gender gender,
      int weight,
      int height,
      int weeklyTarget
  ) {}

  private static final Map<CharacterClass, ClassMeta> CLASS_META = Map.of(
      CharacterClass.WARRIOR, new ClassMeta("Warrior", "Strength & powerlifting"),
      CharacterClass.RANGER, new ClassMeta("Ranger", "Cardio & endurance"),
      CharacterClass.MAGE, new ClassMeta("Mage", "Mobility & yoga"),
      CharacterClass.PALADIN, new ClassMeta("Paladin", "Balanced fitness")
  );

  @FXML private StackPane root;
  @FXML private Region overlay;
  @FXML private Region progressTrack;
  @FXML private Region progressFill;
  @FXML private Label stepLabel;
  @FXML private Label summaryLabel;
  @FXML private TextField nameField;
  @FXML private TextField weightField;
  @FXML private TextField heightField;
  @FXML private TextField targetField;
  @FXML private ImageView spriteViewMain;
  @FXML private ImageView spriteViewFinal;
  @FXML private ImageView sunIcon;
  @FXML private ImageView moonIcon;
  @FXML private ToggleGroup genderGroup;
  @FXML private ToggleGroup classGroup;
  @FXML private ToggleButton maleToggle;
  @FXML private ToggleButton warriorToggle;
  @FXML private Region step0Pane;
  @FXML private Region step1Pane;
  @FXML private Region step2Pane;
  @FXML private Region step3Pane;
  @FXML private Button backButton;
  @FXML private Button nextButton;
  @FXML private Button finishButton;
  @FXML private Button themeToggle;

  private final IntegerProperty step = new SimpleIntegerProperty(0);
  private final StringProperty heroName = new SimpleStringProperty("");
  private final ObjectProperty<Gender> gender = new SimpleObjectProperty<>(Gender.MALE);
  private final ObjectProperty<CharacterClass> characterClass = new SimpleObjectProperty<>(CharacterClass.WARRIOR);
  private final IntegerProperty weight = new SimpleIntegerProperty(75);
  private final IntegerProperty height = new SimpleIntegerProperty(175);
  private final IntegerProperty weeklyTarget = new SimpleIntegerProperty(4);
  private final ObjectProperty<Consumer<HeroProfile>> onComplete = new SimpleObjectProperty<>();
  private final EnumMap<CharacterClass, Image> spriteSheets = new EnumMap<>(CharacterClass.class);
  private Path assetRoot;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    overlay.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

    stepLabel.textProperty().bind(Bindings.createStringBinding(
        () -> String.format("STEP %d / 4", step.get() + 1),
        step
    ));

    summaryLabel.textProperty().bind(Bindings.createStringBinding(() -> {
      String name = heroName.get() == null || heroName.get().trim().isEmpty() ? "Hero" : heroName.get().trim();
      String classLabel = CLASS_META.get(characterClass.get()).label();
      return name + ", the " + classLabel + ", steps onto the path.";
    }, heroName, characterClass));

    bindStep(step0Pane, 0);
    bindStep(step1Pane, 1);
    bindStep(step2Pane, 2);
    bindStep(step3Pane, 3);

    backButton.visibleProperty().bind(step.greaterThan(0));
    backButton.managedProperty().bind(backButton.visibleProperty());
    nextButton.visibleProperty().bind(step.lessThan(MAX_STEP));
    nextButton.managedProperty().bind(nextButton.visibleProperty());
    finishButton.visibleProperty().bind(step.isEqualTo(MAX_STEP));
    finishButton.managedProperty().bind(finishButton.visibleProperty());

    progressFill.prefWidthProperty().bind(
        progressTrack.widthProperty().multiply(step.add(1).divide(4.0))
    );

    nameField.textProperty().bindBidirectional(heroName);
    weightField.setTextFormatter(createIntegerFormatter(weight, 20, 400));
    heightField.setTextFormatter(createIntegerFormatter(height, 80, 260));
    targetField.setTextFormatter(createIntegerFormatter(weeklyTarget, 1, 7));

    maleToggle.setSelected(true);
    warriorToggle.setSelected(true);

    genderGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
      Gender value = readGender(newToggle);
      if (value != null) {
        gender.set(value);
      }
    });

    classGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
      CharacterClass value = readClass(newToggle);
      if (value != null) {
        characterClass.set(value);
      }
    });

    assetRoot = Paths.get("..", "src", "assets").toAbsolutePath().normalize();
    loadSpriteSheets();
    updateSprites();
    gender.addListener((obs, oldValue, newValue) -> updateSprites());
    characterClass.addListener((obs, oldValue, newValue) -> updateSprites());

    applyIdleBob(spriteViewMain);
    applyIdleBob(spriteViewFinal);

    sunIcon.setImage(createSunIcon());
    sunIcon.setSmooth(false);
    moonIcon.setImage(createMoonIcon());
    moonIcon.setSmooth(false);
    updateThemeToggle();
  }

  @Override
  public void setAppState(AppState appState) {
    super.setAppState(appState);
    applyTheme();
    updateThemeToggle();
  }

  @FXML
  private void onToggleTheme() {
    if (appState == null) {
      return;
    }
    String nextTheme = "light".equalsIgnoreCase(appState.getUi().getTheme()) ? "dark" : "light";
    appState.getUi().setTheme(nextTheme);
    applyTheme();
    updateThemeToggle();
  }

  @FXML
  private void onNext() {
    if (step.get() < MAX_STEP) {
      step.set(step.get() + 1);
    }
  }

  @FXML
  private void onBack() {
    if (step.get() > 0) {
      step.set(step.get() - 1);
    }
  }

  @FXML
  private void onFinish() {
    finish();
  }

  public void setOnComplete(Consumer<HeroProfile> handler) {
    onComplete.set(handler);
  }

  private void finish() {
    String name = heroName.get() == null || heroName.get().trim().isEmpty() ? "Hero" : heroName.get().trim();
    HeroProfile profile = new HeroProfile(
        name,
        characterClass.get(),
        gender.get(),
        weight.get(),
        height.get(),
        weeklyTarget.get()
    );

    Consumer<HeroProfile> handler = onComplete.get();
    if (handler != null) {
      handler.accept(profile);
    } else {
      System.out.println("Onboarding complete: " + profile);
    }
  }

  private void applyTheme() {
    if (root == null || appState == null) {
      return;
    }
    root.getStyleClass().removeAll("theme-dark", "theme-light");
    if ("light".equalsIgnoreCase(appState.getUi().getTheme())) {
      root.getStyleClass().add("theme-light");
    } else {
      root.getStyleClass().add("theme-dark");
    }
  }

  private void updateThemeToggle() {
    boolean isLight = appState != null && "light".equalsIgnoreCase(appState.getUi().getTheme());
    if (sunIcon != null) {
      sunIcon.setVisible(isLight);
      sunIcon.setManaged(isLight);
    }
    if (moonIcon != null) {
      moonIcon.setVisible(!isLight);
      moonIcon.setManaged(!isLight);
    }
  }

  private void bindStep(Node node, int index) {
    node.visibleProperty().bind(step.isEqualTo(index));
    node.managedProperty().bind(node.visibleProperty());
  }

  private Gender readGender(Toggle toggle) {
    if (toggle == null || toggle.getUserData() == null) {
      return null;
    }
    return Gender.valueOf(toggle.getUserData().toString().toUpperCase());
  }

  private CharacterClass readClass(Toggle toggle) {
    if (toggle == null || toggle.getUserData() == null) {
      return null;
    }
    return CharacterClass.valueOf(toggle.getUserData().toString().toUpperCase());
  }

  private void loadSpriteSheets() {
    spriteSheets.put(CharacterClass.WARRIOR, loadImage(assetRoot.resolve("sprites/warrior.png")));
    spriteSheets.put(CharacterClass.RANGER, loadImage(assetRoot.resolve("sprites/ranger.png")));
    spriteSheets.put(CharacterClass.MAGE, loadImage(assetRoot.resolve("sprites/mage.png")));
    spriteSheets.put(CharacterClass.PALADIN, loadImage(assetRoot.resolve("sprites/paladin.png")));
  }

  private Image loadImage(Path path) {
    return new Image(path.toUri().toString());
  }

  private void updateSprites() {
    updateSprite(spriteViewMain, 220);
    updateSprite(spriteViewFinal, 200);
  }

  private void updateSprite(ImageView view, double size) {
    Image sheet = spriteSheets.get(characterClass.get());
    if (sheet == null || sheet.isError()) {
      return;
    }

    int genderIndex = gender.get() == Gender.MALE ? 0 : 1;
    double panelWidth = sheet.getWidth() / SPRITE_COLUMNS;
    double panelHeight = sheet.getHeight();

    view.setImage(sheet);
    view.setViewport(new Rectangle2D(panelWidth * genderIndex, 0, panelWidth, panelHeight));
    view.setFitWidth(size);
    view.setFitHeight(size);
  }

  private Image createSunIcon() {
    int size = 12;
    WritableImage image = new WritableImage(size, size);
    PixelWriter writer = image.getPixelWriter();
    Color base = Color.web("#0a7995");
    Color highlight = Color.web("#3aaec9");

    fillRect(writer, 4, 4, 4, 4, base);
    fillRect(writer, 5, 5, 2, 2, highlight);

    fillRect(writer, 5, 0, 2, 2, base);
    fillRect(writer, 5, 10, 2, 2, base);
    fillRect(writer, 0, 5, 2, 2, base);
    fillRect(writer, 10, 5, 2, 2, base);
    fillRect(writer, 1, 1, 2, 2, base);
    fillRect(writer, 9, 1, 2, 2, base);
    fillRect(writer, 1, 9, 2, 2, base);
    fillRect(writer, 9, 9, 2, 2, base);

    return image;
  }

  private Image createMoonIcon() {
    if (assetRoot != null) {
      Path moonPath = assetRoot.resolve("theme-moon.png");
      if (Files.exists(moonPath)) {
        return loadImage(moonPath);
      }
    }

    int size = 12;
    WritableImage image = new WritableImage(size, size);
    PixelWriter writer = image.getPixelWriter();
    Color base = Color.web("#0a3a47");

    int cx = 5;
    int cy = 6;
    int radius = 5;
    int cutCx = 7;
    int cutCy = 6;
    int cutRadius = 4;

    for (int y = 0; y < size; y++) {
      for (int x = 0; x < size; x++) {
        double dist = Math.hypot(x - cx, y - cy);
        if (dist <= radius) {
          writer.setColor(x, y, base);
        }
      }
    }

    for (int y = 0; y < size; y++) {
      for (int x = 0; x < size; x++) {
        double dist = Math.hypot(x - cutCx, y - cutCy);
        if (dist <= cutRadius) {
          writer.setColor(x, y, Color.TRANSPARENT);
        }
      }
    }

    return image;
  }

  private void fillRect(PixelWriter writer, int startX, int startY, int width, int height, Color color) {
    for (int y = startY; y < startY + height; y++) {
      for (int x = startX; x < startX + width; x++) {
        writer.setColor(x, y, color);
      }
    }
  }

  private TextFormatter<Integer> createIntegerFormatter(IntegerProperty target, int min, int max) {
    StringConverter<Integer> converter = new StringConverter<>() {
      @Override
      public String toString(Integer value) {
        return value == null ? "" : value.toString();
      }

      @Override
      public Integer fromString(String text) {
        if (text == null || text.isBlank()) {
          return target.get();
        }
        int value = Integer.parseInt(text);
        if (value < min) {
          return min;
        }
        if (value > max) {
          return max;
        }
        return value;
      }
    };

    UnaryOperator<TextFormatter.Change> filter = change ->
        change.getControlNewText().matches("\\d{0,3}") ? change : null;

    TextFormatter<Integer> formatter = new TextFormatter<>(converter, target.get(), filter);
    target.bind(formatter.valueProperty());
    return formatter;
  }

  private void applyIdleBob(Node node) {
    TranslateTransition bob = new TranslateTransition(Duration.seconds(3.2), node);
    bob.setByY(-4);
    bob.setAutoReverse(true);
    bob.setCycleCount(Animation.INDEFINITE);
    bob.play();
  }
}
