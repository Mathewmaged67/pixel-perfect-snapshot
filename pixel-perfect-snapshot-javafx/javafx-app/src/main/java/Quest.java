public class Quest {
  private final String id;
  private final String title;
  private final String description;
  private final QuestScope scope;
  private final int rewardXp;
  private final int rewardGold;
  private final int goal;
  private int progress;
  private boolean completed;

  public Quest(String id, String title, String description, QuestScope scope, int rewardXp,
               int rewardGold, int goal, int progress, boolean completed) {
    this.id = id;
    this.title = title;
    this.description = description;
    this.scope = scope;
    this.rewardXp = rewardXp;
    this.rewardGold = rewardGold;
    this.goal = goal;
    this.progress = progress;
    this.completed = completed;
  }

  public String getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  public String getDescription() {
    return description;
  }

  public QuestScope getScope() {
    return scope;
  }

  public int getRewardXp() {
    return rewardXp;
  }

  public int getRewardGold() {
    return rewardGold;
  }

  public int getGoal() {
    return goal;
  }

  public int getProgress() {
    return progress;
  }

  public void setProgress(int progress) {
    this.progress = progress;
  }

  public boolean isCompleted() {
    return completed;
  }

  public void setCompleted(boolean completed) {
    this.completed = completed;
  }
}
