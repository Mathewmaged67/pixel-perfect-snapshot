import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class FinanceController extends BaseController {
  @FXML private TextField transactionNameField;
  @FXML private TextField transactionAmountField;
  @FXML private ComboBox<String> transactionTypeCombo;
  @FXML private DatePicker transactionDatePicker;
  @FXML private TableView<Transaction> transactionTable;
  @FXML private TableColumn<Transaction, String> nameColumn;
  @FXML private TableColumn<Transaction, Double> amountColumn;
  @FXML private TableColumn<Transaction, String> typeColumn;
  @FXML private TableColumn<Transaction, LocalDate> dateColumn;
  @FXML private Label totalIncomeLabel;
  @FXML private Label totalExpensesLabel;
  @FXML private Label netBalanceLabel;
  @FXML private VBox resourceCardsBox;
  @FXML private VBox chatMessageBox;
  @FXML private TextField chatInputField;

  private final ObservableList<Transaction> transactions = FXCollections.observableArrayList();

  private record Resource(String name, double quantity, double max, String status, String statusClass) {}

  @Override
  public void onNavigatedTo(Route route) {
    super.onNavigatedTo(route);
    initCoachMessages();
  }

  @FXML
  private void initialize() {
    transactionTypeCombo.getItems().addAll("Income", "Expense");
    transactionTypeCombo.getSelectionModel().selectFirst();
    transactionDatePicker.setValue(LocalDate.now());

    nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
    amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
    typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
    dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
    dateColumn.setCellFactory(column -> new javafx.scene.control.TableCell<>() {
      private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
      @Override
      protected void updateItem(LocalDate item, boolean empty) {
        super.updateItem(item, empty);
        setText(empty || item == null ? null : item.format(fmt));
      }
    });
    amountColumn.setCellFactory(column -> new javafx.scene.control.TableCell<>() {
      @Override
      protected void updateItem(Double item, boolean empty) {
        super.updateItem(item, empty);
        setText(empty || item == null ? null : String.format("$%.2f", item));
      }
    });

    transactionTable.setItems(transactions);
    transactionTable.getSelectionModel().selectedItemProperty().addListener((obs, old, sel) -> {
      if (sel != null) {
        transactionNameField.setText(sel.getName());
        transactionAmountField.setText(String.valueOf(sel.getAmount()));
        transactionTypeCombo.setValue(sel.getType());
        transactionDatePicker.setValue(sel.getDate());
      }
    });

    buildResourceCards();
  }

  @FXML
  private void onAddTransaction() {
    String name = transactionNameField.getText().trim();
    String amountText = transactionAmountField.getText().trim();
    String type = transactionTypeCombo.getValue();
    LocalDate date = transactionDatePicker.getValue();

    if (name.isEmpty() || amountText.isEmpty() || date == null) {
      return;
    }

    try {
      double amount = Double.parseDouble(amountText);
      transactions.add(new Transaction(name, amount, type, date));
      clearTransactionForm();
      updateFinancialSummary();
    } catch (NumberFormatException e) {
      // ignore invalid input
    }
  }

  @FXML
  private void onEditTransaction() {
    Transaction selected = transactionTable.getSelectionModel().getSelectedItem();
    if (selected == null) {
      return;
    }

    String name = transactionNameField.getText().trim();
    String amountText = transactionAmountField.getText().trim();
    String type = transactionTypeCombo.getValue();
    LocalDate date = transactionDatePicker.getValue();

    if (name.isEmpty() || amountText.isEmpty() || date == null) {
      return;
    }

    try {
      double amount = Double.parseDouble(amountText);
      int idx = transactions.indexOf(selected);
      if (idx >= 0) {
        transactions.set(idx, new Transaction(name, amount, type, date));
      }
      clearTransactionForm();
      updateFinancialSummary();
    } catch (NumberFormatException e) {
      // ignore invalid input
    }
  }

  @FXML
  private void onDeleteTransaction() {
    Transaction selected = transactionTable.getSelectionModel().getSelectedItem();
    if (selected != null) {
      transactions.remove(selected);
      clearTransactionForm();
      updateFinancialSummary();
    }
  }

  @FXML
  private void onViewTransactionList() {
    if (!transactionTable.getItems().isEmpty()) {
      transactionTable.getSelectionModel().clearSelection();
    }
  }

  private void clearTransactionForm() {
    transactionNameField.clear();
    transactionAmountField.clear();
    transactionTypeCombo.getSelectionModel().selectFirst();
    transactionDatePicker.setValue(LocalDate.now());
  }

  private void updateFinancialSummary() {
    double totalIncome = 0;
    double totalExpenses = 0;
    for (Transaction t : transactions) {
      if ("Income".equals(t.getType())) {
        totalIncome += t.getAmount();
      } else {
        totalExpenses += t.getAmount();
      }
    }
    double netBalance = totalIncome - totalExpenses;

    totalIncomeLabel.setText(String.format("$%.2f", totalIncome));
    totalExpensesLabel.setText(String.format("$%.2f", totalExpenses));
    netBalanceLabel.setText(String.format("$%.2f", netBalance));

    if (netBalance >= 0) {
      netBalanceLabel.setStyle("-fx-text-fill: #4bd26a;");
    } else {
      netBalanceLabel.setStyle("-fx-text-fill: #e85b57;");
    }
  }

  private void buildResourceCards() {
    Resource[] resources = {
      new Resource("Gold Reserve", 1250, 5000, "Healthy", "status-healthy"),
      new Resource("Food Supply", 340, 1000, "Moderate", "status-moderate"),
      new Resource("Equipment Stock", 18, 30, "Good", "status-good"),
      new Resource("Energy Potions", 45, 100, "Low", "status-low"),
      new Resource("Training Capacity", 780, 1000, "High", "status-high")
    };

    for (Resource res : resources) {
      VBox card = new VBox(8);
      card.getStyleClass().add("card");

      HBox header = new HBox(6);
      Label nameLabel = new Label(res.name());
      nameLabel.getStyleClass().add("section-title");
      Region spacer = new Region();
      HBox.setHgrow(spacer, Priority.ALWAYS);
      Label statusLabel = new Label(res.status());
      statusLabel.getStyleClass().addAll("resource-status", res.statusClass());
      header.getChildren().addAll(nameLabel, spacer, statusLabel);

      HBox quantityRow = new HBox(6);
      Label qtyLabel = new Label(String.format("%.0f / %.0f", res.quantity(), res.max()));
      qtyLabel.getStyleClass().add("stat-value");
      quantityRow.getChildren().add(qtyLabel);

      ProgressBar bar = new ProgressBar(res.quantity() / res.max());
      bar.setPrefWidth(Double.MAX_VALUE);
      bar.getStyleClass().add("resource-bar");

      card.getChildren().addAll(header, quantityRow, bar);
      resourceCardsBox.getChildren().add(card);
    }
  }

  private void initCoachMessages() {
    chatMessageBox.getChildren().clear();
    addCoachMessage("Hello! I'm your personal finance coach. I can help you with:");
    addCoachMessage("• Managing transactions (add, edit, delete)\n• Tracking your resources and usage\n• Understanding your financial summaries\n• Providing tips to improve your savings");
    addCoachMessage("Try adding a transaction or ask me a question below!");
  }

  @FXML
  private void onSendChat() {
    String message = chatInputField.getText().trim();
    if (message.isEmpty()) {
      return;
    }
    addUserMessage(message);
    chatInputField.clear();
    handleCoachResponse(message);
  }

  private void addUserMessage(String text) {
    VBox bubble = new VBox(4);
    bubble.getStyleClass().add("coach-message user-message");
    bubble.setMaxWidth(400);
    bubble.setPadding(new Insets(8, 12, 8, 12));
    Label msg = new Label(text);
    msg.setWrapText(true);
    bubble.getChildren().add(msg);
    chatMessageBox.getChildren().add(bubble);
  }

  private void addCoachMessage(String text) {
    VBox bubble = new VBox(4);
    bubble.getStyleClass().add("coach-message coach-bubble");
    bubble.setMaxWidth(400);
    bubble.setPadding(new Insets(8, 12, 8, 12));
    Label msg = new Label(text);
    msg.setWrapText(true);
    bubble.getChildren().add(msg);
    chatMessageBox.getChildren().add(bubble);
  }

  private void handleCoachResponse(String message) {
    String lower = message.toLowerCase();
    String response;

    if (lower.contains("income") || lower.contains("earn") || lower.contains("revenue")) {
      response = "To increase your income, consider diversifying your revenue streams. Look for freelance work, invest in skills, or explore passive income opportunities. I see you can track your income in the Transactions tab!";
    } else if (lower.contains("expense") || lower.contains("spend") || lower.contains("cost")) {
      response = "Tracking expenses is key to financial health! Use the Transactions tab to log all expenses. Try to follow the 50/30/20 rule: 50% needs, 30% wants, 20% savings.";
    } else if (lower.contains("sav") || lower.contains("balance") || lower.contains("net")) {
      response = "Your net balance is the difference between income and expenses. A positive balance means you're on track! Aim to save at least 20% of your income each month.";
    } else if (lower.contains("resource") || lower.contains("supply") || lower.contains("stock")) {
      response = "Keep an eye on your resource levels in the Resources tab. Try to maintain healthy levels across all categories. Low resources may impact your progress!";
    } else if (lower.contains("transaction") || lower.contains("add") || lower.contains("edit") || lower.contains("delete")) {
      response = "You can manage all your transactions in the Transactions tab. Use the form to add new entries, select a row to edit or delete, and click 'View List' to see everything.";
    } else if (lower.contains("hello") || lower.contains("hi") || lower.contains("hey")) {
      response = "Hello there! How can I help you with your finances today? Try asking about transactions, resources, or your financial summary.";
    } else if (lower.contains("help") || lower.contains("guide") || lower.contains("what")) {
      response = "I can help you with:\n• Managing transactions (add, edit, delete)\n• Tracking your resources\n• Understanding your financial summaries\n• Providing money management tips\nJust ask me anything!";
    } else if (lower.contains("thank")) {
      response = "You're welcome! Feel free to ask me anytime you need financial guidance.";
    } else {
      response = "Great question! For now, try exploring the Transactions tab to log your income and expenses, check the Resources tab for your current stock levels, or view the Financial Summary for an overview of your finances.";
    }

    addCoachMessage(response);
  }
}
