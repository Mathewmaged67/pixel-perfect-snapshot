public class WorkoutXp {
  private final int str;
  private final int sta;
  private final int agi;
  private final int total;

  public WorkoutXp(int str, int sta, int agi) {
    this.str = str;
    this.sta = sta;
    this.agi = agi;
    this.total = str + sta + agi;
  }

  public int getStr() {
    return str;
  }

  public int getSta() {
    return sta;
  }

  public int getAgi() {
    return agi;
  }

  public int getTotal() {
    return total;
  }
}
