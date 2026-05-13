import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

public class QuestsController extends BaseController {
  @FXML private FlowPane dailyPane;
  @FXML private FlowPane weeklyPane;
  @FXML private FlowPane epicPane;

  @Override
  public void onNavigatedTo(Route route) {
    super.onNavigatedTo(route);
    refresh();
  }

  private void refresh() {
    if (appState == null) {
      return;
    }
    dailyPane.getChildren().setAll(appState.getQuests().getByScope(QuestScope.DAILY).stream()
        .map(this::createQuestCard).toList());
    weeklyPane.getChildren().setAll(appState.getQuests().getByScope(QuestScope.WEEKLY).stream()
        .map(this::createQuestCard).toList());
    epicPane.getChildren().setAll(appState.getQuests().getByScope(QuestScope.EPIC).stream()
        .map(this::createQuestCard).toList());
  }

  private VBox createQuestCard(Quest quest) {
    VBox card = new VBox(6);
    card.getStyleClass().add("card");
    card.setPrefWidth(260);

    Label title = new Label(quest.getTitle());
    Label desc = new Label(quest.getDescription());
    desc.getStyleClass().add("muted");
    desc.setWrapText(true);

    ProgressBar progress = new ProgressBar();
    double pct = quest.getGoal() == 0 ? 0 : (double) quest.getProgress() / quest.getGoal();
    progress.setProgress(pct);

    Label progressLabel = new Label(quest.getProgress() + " / " + quest.getGoal());
    progressLabel.getStyleClass().add("muted");

    Label reward = new Label("Rewards: " + quest.getRewardXp() + " XP, " + quest.getRewardGold() + " gold");
    reward.getStyleClass().add("muted");

    Button claim = new Button("Claim");
    claim.getStyleClass().add("primary-button");
    boolean ready = quest.getProgress() >= quest.getGoal() && !quest.isCompleted();
    claim.setDisable(!ready);
    claim.setOnAction(event -> onClaimQuest(quest));

    card.getChildren().addAll(title, desc, progress, progressLabel, reward, claim);
    return card;
  }

  private void onClaimQuest(Quest quest) {
    if (quest.getProgress() < quest.getGoal() || quest.isCompleted()) {
      return;
    }
    appState.getQuests().complete(quest.getId());
    appState.getCharacter().addXp(quest.getRewardXp());
    appState.getCharacter().addGold(quest.getRewardGold());
    appState.addActivity("Quest claimed: " + quest.getTitle());
    refresh();
  }
}
