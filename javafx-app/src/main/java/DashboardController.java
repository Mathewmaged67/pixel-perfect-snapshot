import javafx.fxml.FXML;
import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class DashboardController extends BaseController {
  @FXML private Label headerNameLabel;
  @FXML private Label heroNameLabel;
  @FXML private Label heroClassLabel;
  @FXML private ImageView heroSprite;
  @FXML private ProgressBar xpBar;
  @FXML private Label xpLabel;
  @FXML private Label strLabel;
  @FXML private Label staLabel;
  @FXML private Label vitLabel;
  @FXML private Label agiLabel;
  @FXML private Label questTitleLabel;
  @FXML private Label questDescLabel;
  @FXML private ProgressBar questProgress;
  @FXML private Label questProgressLabel;
  @FXML private Label questRewardLabel;
  @FXML private Button questClaimButton;
  @FXML private ProgressIndicator proteinRing;
  @FXML private ProgressIndicator carbsRing;
  @FXML private ProgressIndicator fatRing;
  @FXML private Label proteinValueLabel;
  @FXML private Label carbsValueLabel;
  @FXML private Label fatValueLabel;
  @FXML private Label caloriesLabel;
  @FXML private Label goldLabel;
  @FXML private Label streakLabel;
  @FXML private VBox recentGainsBox;
  @FXML private VBox partyBox;
  private List<PartyMember> partyMembers = new ArrayList<>();

  @Override
  public void onNavigatedTo(Route route) {
    super.onNavigatedTo(route);
    refresh();
  }

  @FXML
  private void onClaimQuest() {
    if (appState == null) {
      return;
    }
    Quest quest = appState.getQuests().getActiveQuest(QuestScope.DAILY);
    if (quest == null || quest.isCompleted() || quest.getProgress() < quest.getGoal()) {
      return;
    }
    appState.getQuests().complete(quest.getId());
    appState.getCharacter().addXp(quest.getRewardXp());
    appState.getCharacter().addGold(quest.getRewardGold());
    appState.addActivity("Quest claimed: " + quest.getTitle());
    refresh();
  }

  @FXML
  private void onViewAllQuests() {
    if (router != null) {
      router.goTo(Route.QUESTS);
    }
  }

  @FXML
  private void onQuickWorkout() {
    if (router != null) {
      router.goTo(Route.WORKOUT_INDEX);
    }
  }

  @FXML
  private void onQuickNutrition() {
    if (router != null) {
      router.goTo(Route.NUTRITION_TODAY);
    }
  }

  @FXML
  private void onQuickQuests() {
    if (router != null) {
      router.goTo(Route.QUESTS);
    }
  }

  private void onAddPartyMember() {
    int count = partyMembers.size() + 1;
    partyMembers.add(new PartyMember("Ally " + count, 1, 100));
    rebuildParty();
  }

  private void refresh() {
    if (appState == null) {
      return;
    }
    CharacterState character = appState.getCharacter();
    String heroName = character.getName().isEmpty() ? "Hero" : character.getName();
    headerNameLabel.setText(heroName);
    heroNameLabel.setText(heroName);
    heroClassLabel.setText("LV. " + character.getLevel() + " " + character.getCharacterClass().name());
    SpriteUtil.applyCharacterSprite(heroSprite, character, 180);

    double xpProgress = character.getXpToNext() == 0 ? 0 : (double) character.getXp() / character.getXpToNext();
    xpBar.setProgress(xpProgress);
    xpLabel.setText(character.getXp() + " / " + character.getXpToNext() + " XP");

    strLabel.setText(String.valueOf(character.getStats().getOrDefault(StatKey.STR, 0)));
    staLabel.setText(String.valueOf(character.getStats().getOrDefault(StatKey.STA, 0)));
    vitLabel.setText(String.valueOf(character.getStats().getOrDefault(StatKey.VIT, 0)));
    agiLabel.setText(String.valueOf(character.getStats().getOrDefault(StatKey.AGI, 0)));

    Quest quest = appState.getQuests().getActiveQuest(QuestScope.DAILY);
    if (quest != null) {
      questTitleLabel.setText(quest.getTitle());
      questDescLabel.setText(quest.getDescription());
      double questPct = quest.getGoal() == 0 ? 0 : (double) quest.getProgress() / quest.getGoal();
      questProgress.setProgress(questPct);
      questProgressLabel.setText(quest.getProgress() + " / " + quest.getGoal());
      questClaimButton.setDisable(quest.getProgress() < quest.getGoal());
      questRewardLabel.setText(quest.getRewardXp() + " XP | " + quest.getRewardGold() + " GOLD");
    } else {
      questTitleLabel.setText("All quests cleared");
      questDescLabel.setText("Check back tomorrow");
      questProgress.setProgress(1);
      questProgressLabel.setText("Complete");
      questClaimButton.setDisable(true);
      questRewardLabel.setText("0 XP | 0 GOLD");
    }

    NutritionTotals goals = appState.getNutrition().getGoals();
    NutritionTotals totals = appState.getNutrition().todayTotals();
    caloriesLabel.setText(totals.getKcal() + " / " + goals.getKcal());

    proteinRing.setProgress(goals.getProtein() == 0 ? 0 : (double) totals.getProtein() / goals.getProtein());
    carbsRing.setProgress(goals.getCarbs() == 0 ? 0 : (double) totals.getCarbs() / goals.getCarbs());
    fatRing.setProgress(goals.getFat() == 0 ? 0 : (double) totals.getFat() / goals.getFat());
    proteinValueLabel.setText(totals.getProtein() + " / " + goals.getProtein() + " g");
    carbsValueLabel.setText(totals.getCarbs() + " / " + goals.getCarbs() + " g");
    fatValueLabel.setText(totals.getFat() + " / " + goals.getFat() + " g");

    goldLabel.setText(String.valueOf(character.getGold()));
    streakLabel.setText(character.getStreak() + (character.getStreak() == 1 ? " day" : " days"));

    rebuildRecentGains();
    rebuildParty();
  }

  private void rebuildRecentGains() {
    recentGainsBox.getChildren().clear();
    List<WorkoutSession> history = appState.getWorkout().getHistory();
    if (history.isEmpty()) {
      Label empty = new Label("Your XP feed will appear here once you log a session.");
      empty.getStyleClass().add("muted");
      empty.setWrapText(true);
      recentGainsBox.getChildren().add(empty);
      return;
    }
    for (WorkoutSession session : history.stream().limit(4).toList()) {
      int sets = session.getExercises().stream()
          .mapToInt(ex -> (int) ex.getSets().stream().filter(SetEntry::isDone).count())
          .sum();
      HBox row = new HBox(6);
      Label name = new Label(session.getName());
      name.getStyleClass().add("muted");
      Region spacer = new Region();
      HBox.setHgrow(spacer, Priority.ALWAYS);
      Label value = new Label("+" + sets + " sets");
      value.getStyleClass().add("list-strong");
      row.getChildren().addAll(name, spacer, value);
      recentGainsBox.getChildren().add(row);
    }
  }

  private void rebuildParty() {
    partyBox.getChildren().clear();

    // Add header with Add button
    HBox headerBox = new HBox(6);
    Label title = new Label("PARTY");
    title.setStyle("-fx-font-size: 10px; -fx-text-fill: #0a7995; -fx-font-weight: bold;");
    Region spacer = new Region();
    HBox.setHgrow(spacer, Priority.ALWAYS);
    Button addButton = new Button("Add");
    addButton.setStyle("-fx-padding: 4 12 4 12; -fx-font-size: 11px; -fx-text-fill: #0a7995; -fx-border-color: #0a7995; -fx-border-width: 1; -fx-background-color: transparent; -fx-border-radius: 4; -fx-cursor: hand;");
    addButton.setOnAction(e -> onAddPartyMember());
    headerBox.getChildren().addAll(title, spacer, addButton);
    partyBox.getChildren().add(headerBox);

    // Add party members
    if (partyMembers.isEmpty()) {
      Label emptyLabel = new Label("No party members yet. Click Add to recruit an ally.");
      emptyLabel.setStyle("-fx-text-fill: #7a7a7a; -fx-font-size: 11px; -fx-wrap-text: true;");
      emptyLabel.setWrapText(true);
      VBox emptyBox = new VBox();
      emptyBox.setStyle("-fx-padding: 16;");
      emptyBox.getChildren().add(emptyLabel);
      partyBox.getChildren().add(emptyBox);
      return;
    }

    for (PartyMember member : partyMembers) {
      VBox row = new VBox(4);
      HBox header = new HBox(6);
      Label name = new Label(member.name + " Lv. " + member.level);
      name.getStyleClass().add("muted");
      Region memberSpacer = new Region();
      HBox.setHgrow(memberSpacer, Priority.ALWAYS);
      Label hpLabel = new Label(member.hp + "/100");
      hpLabel.getStyleClass().add("muted");
      header.getChildren().addAll(name, memberSpacer, hpLabel);

      ProgressBar hp = new ProgressBar(member.hp / 100.0);
      hp.getStyleClass().add("hp-bar");
      hp.setPrefWidth(240);

      row.getChildren().addAll(header, hp);
      row.setPadding(new Insets(0, 0, 4, 0));
      partyBox.getChildren().add(row);
    }
  }

  private record PartyMember(String name, int level, int hp) {}
}
