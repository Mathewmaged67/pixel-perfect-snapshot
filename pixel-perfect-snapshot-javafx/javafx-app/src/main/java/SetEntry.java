import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class SetEntry {
  private final String id;
  private final IntegerProperty weight;
  private final IntegerProperty reps;
  private final BooleanProperty done;
  private boolean pr;

  public SetEntry(String id, int weight, int reps, boolean done) {
    this.id = id;
    this.weight = new SimpleIntegerProperty(weight);
    this.reps = new SimpleIntegerProperty(reps);
    this.done = new SimpleBooleanProperty(done);
  }

  public String getId() {
    return id;
  }

  public int getWeight() {
    return weight.get();
  }

  public void setWeight(int weight) {
    this.weight.set(weight);
  }

  public IntegerProperty weightProperty() {
    return weight;
  }

  public int getReps() {
    return reps.get();
  }

  public void setReps(int reps) {
    this.reps.set(reps);
  }

  public IntegerProperty repsProperty() {
    return reps;
  }

  public boolean isDone() {
    return done.get();
  }

  public void setDone(boolean done) {
    this.done.set(done);
  }

  public BooleanProperty doneProperty() {
    return done;
  }

  public boolean isPr() {
    return pr;
  }

  public void setPr(boolean pr) {
    this.pr = pr;
  }
}
