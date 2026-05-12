package ui;

import client.Client;
import client.NetworkManager;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Stage;

/**
 * Login / Register page.
 * Connects to the server, sends LOGIN or REGISTER, then navigates to Dashboard.
 */
public class LoginPage {

    private final Stage          stage;
    private final NetworkManager network;

    private TextField    usernameField;
    private PasswordField passwordField;
    private TextField    emailField;
    private Label        statusLabel;
    private boolean      isRegisterMode = false;

    public LoginPage(Stage stage, NetworkManager network) {
        this.stage   = stage;
        this.network = network;
    }

    public void show() {
        // ── Root
        VBox root = new VBox(0);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #0f0f1a;");

        // ── Card
        VBox card = new VBox(18);
        card.setAlignment(Pos.CENTER);
        card.setMaxWidth(420);
        card.setPadding(new Insets(40, 44, 40, 44));
        card.setStyle("""
            -fx-background-color: #1a1a2e;
            -fx-background-radius: 18;
            -fx-effect: dropshadow(gaussian, rgba(99,102,241,0.35), 30, 0, 0, 4);
        """);

        // ── Logo / title
        Label logo = new Label("💬");
        logo.setFont(Font.font(52));

        Label title = new Label("ChatApp");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 30));
        title.setTextFill(Color.WHITE);

        Label subtitle = new Label("Connect. Chat. Share.");
        subtitle.setFont(Font.font("Segoe UI", 13));
        subtitle.setTextFill(Color.web("#8b8baa"));

        // ── Fields
        usernameField = styledTextField("Username");
        passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        applyFieldStyle(passwordField);

        emailField = styledTextField("Email (for registration)");
        emailField.setVisible(false);
        emailField.setManaged(false);

        // ── Status label
        statusLabel = new Label();
        statusLabel.setFont(Font.font("Segoe UI", 12));
        statusLabel.setTextFill(Color.web("#f87171"));
        statusLabel.setWrapText(true);
        statusLabel.setMaxWidth(340);

        // ── Buttons
        Button actionBtn = new Button("Login");
        actionBtn.setMaxWidth(Double.MAX_VALUE);
        actionBtn.setStyle("""
            -fx-background-color: #6366f1;
            -fx-text-fill: white;
            -fx-font-size: 14px;
            -fx-font-weight: bold;
            -fx-background-radius: 10;
            -fx-cursor: hand;
            -fx-padding: 12 0 12 0;
        """);
        actionBtn.setOnMouseEntered(e -> actionBtn.setStyle("""
            -fx-background-color: #818cf8;
            -fx-text-fill: white;
            -fx-font-size: 14px;
            -fx-font-weight: bold;
            -fx-background-radius: 10;
            -fx-cursor: hand;
            -fx-padding: 12 0 12 0;
        """));
        actionBtn.setOnMouseExited(e -> actionBtn.setStyle("""
            -fx-background-color: #6366f1;
            -fx-text-fill: white;
            -fx-font-size: 14px;
            -fx-font-weight: bold;
            -fx-background-radius: 10;
            -fx-cursor: hand;
            -fx-padding: 12 0 12 0;
        """));

        Hyperlink toggleLink = new Hyperlink("Don't have an account? Register");
        toggleLink.setTextFill(Color.web("#6366f1"));
        toggleLink.setFont(Font.font("Segoe UI", 12));
        toggleLink.setStyle("-fx-border-color: transparent;");

        // ── Toggle login / register
        toggleLink.setOnAction(e -> {
            isRegisterMode = !isRegisterMode;
            emailField.setVisible(isRegisterMode);
            emailField.setManaged(isRegisterMode);
            actionBtn.setText(isRegisterMode ? "Register" : "Login");
            toggleLink.setText(isRegisterMode
                    ? "Already have an account? Login"
                    : "Don't have an account? Register");
            statusLabel.setText("");
        });

        // ── Action
        actionBtn.setOnAction(e -> handleAction());
        passwordField.setOnAction(e -> handleAction());

        card.getChildren().addAll(logo, title, subtitle,
                new Separator(), usernameField, passwordField, emailField,
                statusLabel, actionBtn, toggleLink);

        root.getChildren().add(card);

        Scene scene = new Scene(root, 900, 620);
        stage.setScene(scene);
        stage.show();
    }

    // ─────────────────────────────────────────────────────────────────────────

    private void handleAction() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();
        setStatus("Connecting...", false);

        if (username.isEmpty() || password.isEmpty()) {
            setStatus("Username and password are required.", true);
            return;
        }

        // Connect if not already connected
        if (!network.isConnected()) {
            boolean ok = network.connect();
            if (!ok) { setStatus("Cannot reach server. Is it running?", true); return; }
        }

        // Install response handler
        network.setOnMessage(msg -> Platform.runLater(() -> handleServerResponse(msg)));

        if (isRegisterMode) {
            String email = emailField.getText().trim();
            network.sendCommand("REGISTER", username, password, email);
        } else {
            network.sendCommand("LOGIN", username, password);
        }
    }

    private void handleServerResponse(String msg) {
        String[] parts = msg.split("\\|", -1);
        switch (parts[0]) {
            case "LOGIN_OK", "REGISTER_OK" -> {
                int uid       = Integer.parseInt(parts[1]);
                String uname  = parts[2];
                Client.setCurrentUser(uid, uname);
                setStatus("Welcome, " + uname + "!", false);
                Dashboard dashboard = new Dashboard(stage, network);
                dashboard.show();
            }
            case "LOGIN_FAIL"    -> setStatus("Login failed: " + (parts.length > 1 ? parts[1] : ""), true);
            case "REGISTER_FAIL" -> setStatus("Register failed: " + (parts.length > 1 ? parts[1] : ""), true);
            default -> {} // ignore other messages before login
        }
    }

    private void setStatus(String msg, boolean isError) {
        statusLabel.setText(msg);
        statusLabel.setTextFill(isError ? Color.web("#f87171") : Color.web("#34d399"));
    }

    // ─────────────────────── Styling helpers ─────────────────────────────────

    private TextField styledTextField(String prompt) {
        TextField tf = new TextField();
        tf.setPromptText(prompt);
        applyFieldStyle(tf);
        return tf;
    }

    private void applyFieldStyle(Control ctrl) {
        ctrl.setStyle("""
            -fx-background-color: #0f0f1a;
            -fx-text-fill: white;
            -fx-prompt-text-fill: #555577;
            -fx-background-radius: 10;
            -fx-border-color: #2d2d4a;
            -fx-border-radius: 10;
            -fx-padding: 10 14 10 14;
            -fx-font-size: 13px;
        """);
        ctrl.setMaxWidth(Double.MAX_VALUE);
        ctrl.setPrefHeight(42);
    }
}
