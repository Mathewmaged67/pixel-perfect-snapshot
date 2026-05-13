import java.util.ArrayList;
import java.util.List;

public class QuestState {
  private List<Quest> quests = SeedData.copyQuests();

  public List<Quest> getQuests() {
    return quests;
  }

  public void bumpProgress(String id, int amount) {
    for (Quest quest : quests) {
      if (quest.getId().equals(id)) {
        quest.setProgress(Math.min(quest.getGoal(), quest.getProgress() + amount));
        break;
      }
    }
  }

  public void complete(String id) {
    for (Quest quest : quests) {
      if (quest.getId().equals(id)) {
        quest.setCompleted(true);
        quest.setProgress(quest.getGoal());
        break;
      }
    }
  }

  public Quest getActiveQuest(QuestScope scope) {
    for (Quest quest : quests) {
      if (quest.getScope() == scope && !quest.isCompleted()) {
        return quest;
      }
    }
    return null;
  }

  public List<Quest> getByScope(QuestScope scope) {
    List<Quest> result = new ArrayList<>();
    for (Quest quest : quests) {
      if (quest.getScope() == scope) {
        result.add(quest);
      }
    }
    return result;
  }

  public void reset() {
    quests = SeedData.copyQuests();
  }
}
