import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.EnumMap;
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
import javafx.scene.layout.Region;
import javafx.util.Duration;
import javafx.util.StringConverter;

public class OnboardingController implements Initializable, AppController {
  private static final int MAX_STEP = 3;
  private static final int SPRITE_COLUMNS = 3;

  public record HeroProfile(
      String name,
      CharacterClass characterClass,
      Gender gender,
      int weight,
      int height,
      int weeklyTarget
  ) {}

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
  private Router router;
  private AppState appState;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    overlay.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

    stepLabel.textProperty().bind(Bindings.createStringBinding(
        () -> String.format("STEP %d / 4", step.get() + 1),
        step
    ));

    summaryLabel.textProperty().bind(Bindings.createStringBinding(() -> {
      String name = heroName.get() == null || heroName.get().trim().isEmpty() ? "Hero" : heroName.get().trim();
      String classLabel = characterClass.get().getLabel();
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

  @Override
  public void setRouter(Router router) {
    this.router = router;
  }

  @Override
  public void setAppState(AppState appState) {
    this.appState = appState;
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

    if (appState != null) {
      appState.getCharacter().init(
          profile.name(),
          profile.characterClass(),
          profile.gender(),
          profile.weight(),
          profile.height(),
          profile.weeklyTarget()
      );
      appState.addActivity("Hero created: " + profile.name());
    }

    Consumer<HeroProfile> handler = onComplete.get();
    if (handler != null) {
      handler.accept(profile);
    }

    if (router != null) {
      router.goTo(Route.DASHBOARD);
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
