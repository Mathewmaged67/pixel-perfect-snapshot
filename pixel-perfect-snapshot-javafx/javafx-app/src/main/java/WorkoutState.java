import java.util.ArrayList;
import java.util.List;

public class WorkoutState {
  private WorkoutSession active;
  private final List<WorkoutSession> history = new ArrayList<>();
  private String selectedSessionId;

  public WorkoutSession getActive() {
    return active;
  }

  public List<WorkoutSession> getHistory() {
    return history;
  }

  public String startSession(String name, List<String> exerciseIds) {
    String id = IdUtil.randomId();
    List<SessionExercise> exercises = new ArrayList<>();
    for (String exerciseId : exerciseIds) {
      Exercise ex = SeedData.exerciseById(exerciseId);
      int setCount = ex != null ? ex.getDefaultSets() : 3;
      List<SetEntry> sets = new ArrayList<>();
      for (int i = 0; i < setCount; i++) {
        sets.add(seedSet(null));
      }
      exercises.add(new SessionExercise(IdUtil.randomId(), exerciseId, sets));
    }
    active = new WorkoutSession(id, System.currentTimeMillis(), name, exercises);
    return id;
  }

  public void addExercise(String exerciseId) {
    if (active == null) {
      return;
    }
    Exercise ex = SeedData.exerciseById(exerciseId);
    int setCount = ex != null ? ex.getDefaultSets() : 3;
    List<SetEntry> sets = new ArrayList<>();
    for (int i = 0; i < setCount; i++) {
      sets.add(seedSet(null));
    }
    active.getExercises().add(new SessionExercise(IdUtil.randomId(), exerciseId, sets));
  }

  public void addSet(String sessionExerciseId) {
    if (active == null) {
      return;
    }
    for (SessionExercise se : active.getExercises()) {
      if (se.getId().equals(sessionExerciseId)) {
        SetEntry prev = se.getSets().isEmpty() ? null : se.getSets().get(se.getSets().size() - 1);
        se.getSets().add(seedSet(prev));
        break;
      }
    }
  }

  public void updateSet(String sessionExerciseId, String setId, int weight, int reps, boolean done) {
    if (active == null) {
      return;
    }
    for (SessionExercise se : active.getExercises()) {
      if (!se.getId().equals(sessionExerciseId)) {
        continue;
      }
      for (SetEntry set : se.getSets()) {
        if (set.getId().equals(setId)) {
          set.setWeight(weight);
          set.setReps(reps);
          set.setDone(done);
          return;
        }
      }
    }
  }

  public void removeSet(String sessionExerciseId, String setId) {
    if (active == null) {
      return;
    }
    for (SessionExercise se : active.getExercises()) {
      if (se.getId().equals(sessionExerciseId)) {
        se.getSets().removeIf(set -> set.getId().equals(setId));
        break;
      }
    }
  }

  public WorkoutSession finishSession() {
    if (active == null) {
      return null;
    }
    active.setEndedAt(System.currentTimeMillis());
    history.add(0, active);
    WorkoutSession finished = active;
    active = null;
    return finished;
  }

  public void cancelSession() {
    active = null;
  }

  public WorkoutSession findSession(String id) {
    if (id == null) {
      return null;
    }
    for (WorkoutSession session : history) {
      if (session.getId().equals(id)) {
        return session;
      }
    }
    return null;
  }

  public void setSelectedSessionId(String id) {
    selectedSessionId = id;
  }

  public String getSelectedSessionId() {
    return selectedSessionId;
  }

  public void reset() {
    active = null;
    history.clear();
    selectedSessionId = null;
  }

  private SetEntry seedSet(SetEntry prev) {
    int weight = prev != null ? prev.getWeight() : 0;
    int reps = prev != null ? prev.getReps() : 0;
    return new SetEntry(IdUtil.randomId(), weight, reps, false);
  }
}
