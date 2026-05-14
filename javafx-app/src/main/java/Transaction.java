import java.time.LocalDate;

public class Transaction {
  private final String name;
  private final double amount;
  private final String type;
  private final LocalDate date;

  public Transaction(String name, double amount, String type, LocalDate date) {
    this.name = name;
    this.amount = amount;
    this.type = type;
    this.date = date;
  }

  public String getName() { return name; }
  public double getAmount() { return amount; }
  public String getType() { return type; }
  public LocalDate getDate() { return date; }
}
