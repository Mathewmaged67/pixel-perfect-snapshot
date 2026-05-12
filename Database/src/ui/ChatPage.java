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
 * Private chat window between the current user and one other user.
 * Reuses the shared NetworkManager; injects its own message handler
 * and restores the Dashboard handler on close/back.
 */
public class ChatPage {

    private final Stage          stage;
    private final NetworkManager network;
    private final int            peerId;
    private final String         peerUsername;

    private VBox      messagesBox;
    private TextField inputField;
    private ScrollPane scrollPane;

    public ChatPage(Stage stage, NetworkManager network, int peerId, String peerUsername) {
        this.stage        = stage;
        this.network      = network;
        this.peerId       = peerId;
        this.peerUsername = peerUsername;
    }

    public void show() {
        // ── Root
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #0f0f1a;");

        // ── TOP BAR
        root.setTop(buildTopBar());

        // ── MESSAGE AREA
        messagesBox = new VBox(8);
        messagesBox.setPadding(new Insets(16));

        scrollPane = new ScrollPane(messagesBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: #0f0f1a; -fx-background-color: #0f0f1a; -fx-border-color: transparent;");
        scrollPane.vvalueProperty().bind(messagesBox.heightProperty());
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        // ── INPUT BAR
        HBox inputBar = buildInputBar();

        VBox center = new VBox(scrollPane, inputBar);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);
        root.setCenter(center);

        // ── Register network handler for this page
        network.setOnMessage(msg -> Platform.runLater(() -> handleServerMessage(msg)));

        // ── Load history
        network.sendCommand("HISTORY", String.valueOf(peerId));

        Scene scene = new Scene(root, 980, 660);
        stage.setScene(scene);
        stage.setTitle("💬 Chat with " + peerUsername);
    }

    // ─────────────────────── Top bar ─────────────────────────────────────────

    private HBox buildTopBar() {
        HBox bar = new HBox(14);
        bar.setAlignment(Pos.CENTER_LEFT);
        bar.setPadding(new Insets(14, 20, 14, 16));
        bar.setStyle("-fx-background-color: #1a1a2e; -fx-border-color: #2d2d4a; -fx-border-width: 0 0 1 0;");

        Button backBtn = new Button("← Back");
        backBtn.setStyle("""
            -fx-background-color: transparent;
            -fx-text-fill: #8b8baa;
            -fx-font-size: 13px;
            -fx-cursor: hand;
            -fx-border-color: #2d2d4a;
            -fx-border-radius: 8;
            -fx-background-radius: 8;
            -fx-padding: 6 14 6 14;
        """);
        backBtn.setOnAction(e -> {
            Dashboard dashboard = new Dashboard(stage, network);
            dashboard.show();
        });

        Label dot = new Label("●");
        dot.setTextFill(Color.web("#34d399"));
        dot.setFont(Font.font(10));

        Label name = new Label(peerUsername);
        name.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        name.setTextFill(Color.WHITE);

        Label sub = new Label("Private conversation");
        sub.setFont(Font.font("Segoe UI", FontPosture.ITALIC, 11));
        sub.setTextFill(Color.web("#555577"));

        VBox nameBox = new VBox(2, name, sub);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label meLabel = new Label("You: " + Client.getCurrentUsername());
        meLabel.setFont(Font.font("Segoe UI", 12));
        meLabel.setTextFill(Color.web("#6366f1"));

        bar.getChildren().addAll(backBtn, dot, nameBox, spacer, meLabel);
        return bar;
    }

    // ─────────────────────── Input bar ───────────────────────────────────────

    private HBox buildInputBar() {
        HBox bar = new HBox(10);
        bar.setPadding(new Insets(12, 16, 12, 16));
        bar.setAlignment(Pos.CENTER);
        bar.setStyle("-fx-background-color: #1a1a2e; -fx-border-color: #2d2d4a; -fx-border-width: 1 0 0 0;");

        inputField = new TextField();
        inputField.setPromptText("Type a message to " + peerUsername + "…");
        inputField.setStyle("""
            -fx-background-color: #0f0f1a;
            -fx-text-fill: white;
            -fx-prompt-text-fill: #555577;
            -fx-background-radius: 22;
            -fx-border-color: #2d2d4a;
            -fx-border-radius: 22;
            -fx-padding: 11 18 11 18;
            -fx-font-size: 13px;
        """);
        HBox.setHgrow(inputField, Priority.ALWAYS);
        inputField.setOnAction(e -> sendMessage());

        Button sendBtn = new Button("Send ➤");
        sendBtn.setStyle("""
            -fx-background-color: #6366f1;
            -fx-text-fill: white;
            -fx-font-size: 13px;
            -fx-font-weight: bold;
            -fx-background-radius: 22;
            -fx-cursor: hand;
            -fx-padding: 11 22 11 22;
        """);
        sendBtn.setOnMouseEntered(e -> sendBtn.setStyle("""
            -fx-background-color: #818cf8;
            -fx-text-fill: white;
            -fx-font-size: 13px;
            -fx-font-weight: bold;
            -fx-background-radius: 22;
            -fx-cursor: hand;
            -fx-padding: 11 22 11 22;
        """));
        sendBtn.setOnMouseExited(e -> sendBtn.setStyle("""
            -fx-background-color: #6366f1;
            -fx-text-fill: white;
            -fx-font-size: 13px;
            -fx-font-weight: bold;
            -fx-background-radius: 22;
            -fx-cursor: hand;
            -fx-padding: 11 22 11 22;
        """));
        sendBtn.setOnAction(e -> sendMessage());

        bar.getChildren().addAll(inputField, sendBtn);
        return bar;
    }

    // ─────────────────────── Network handler ─────────────────────────────────

    private void handleServerMessage(String msg) {
        String[] parts = msg.split("\\|", -1);
        switch (parts[0]) {
            case "MSG_RECV"     -> handleIncoming(parts);
            case "HISTORY_DATA" -> loadHistory(parts);
            default             -> {}
        }
    }

    private void handleIncoming(String[] parts) {
        if (parts.length < 5) return;
        String fromUser  = parts[1];
        int    fromId    = Integer.parseInt(parts[2]);
        int    toId      = parts.length > 3 ? safeInt(parts[2]) : -1;
        String content   = parts[3];
        String time      = parts[4];
        boolean isBroadcast = parts.length > 5 && parts[5].equals("1");

        // Show if it is part of this private conversation
        if (!isBroadcast && (fromId == peerId || fromId == Client.getCurrentUserId())) {
            boolean mine = (fromId == Client.getCurrentUserId());
            appendMessage(fromUser, content, time, mine);
        }
    }

    private void loadHistory(String[] parts) {
        if (parts.length < 2) return;
        int count;
        try { count = Integer.parseInt(parts[1]); } catch (Exception e) { return; }
        int base = 2;
        for (int i = 0; i < count; i++) {
            int idx = base + i * 5;
            if (idx + 4 >= parts.length) break;
            String uname   = parts[idx];
            int    uid     = safeInt(parts[idx + 1]);
            // receiverId at idx+2
            String content = parts[idx + 3];
            String time    = parts[idx + 4];
            boolean mine   = uid == Client.getCurrentUserId();
            appendMessage(uname, content, time, mine);
        }
    }

    // ─────────────────────── Sending ─────────────────────────────────────────

    private void sendMessage() {
        String text = inputField.getText().trim();
        if (text.isEmpty()) return;
        network.sendCommand("MSG", String.valueOf(peerId), text);
        inputField.clear();
    }

    // ─────────────────────── Message bubble ──────────────────────────────────

    private void appendMessage(String from, String content, String time, boolean isMine) {
        HBox row = new HBox();
        row.setPadding(new Insets(2, 0, 2, 0));

        VBox bubble = new VBox(4);
        bubble.setPadding(new Insets(10, 16, 10, 16));
        bubble.setMaxWidth(480);

        if (!isMine) {
            Label senderLbl = new Label(from);
            senderLbl.setFont(Font.font("Segoe UI", FontWeight.BOLD, 11));
            senderLbl.setTextFill(Color.web("#818cf8"));
            bubble.getChildren().add(senderLbl);
        }

        Label contentLbl = new Label(content);
        contentLbl.setWrapText(true);
        contentLbl.setFont(Font.font("Segoe UI", 13));
        contentLbl.setTextFill(isMine ? Color.WHITE : Color.web("#e2e2f0"));

        Label timeLbl = new Label(time);
        timeLbl.setFont(Font.font("Segoe UI", 10));
        timeLbl.setTextFill(isMine ? Color.web("#c7d2fe") : Color.web("#555577"));

        bubble.getChildren().addAll(contentLbl, timeLbl);

        if (isMine) {
            bubble.setStyle("""
                -fx-background-color: #4f46e5;
                -fx-background-radius: 18 18 4 18;
            """);
            row.setAlignment(Pos.CENTER_RIGHT);
        } else {
            bubble.setStyle("""
                -fx-background-color: #1e1e38;
                -fx-background-radius: 18 18 18 4;
            """);
            row.setAlignment(Pos.CENTER_LEFT);
        }

        row.getChildren().add(bubble);
        messagesBox.getChildren().add(row);
    }

    private int safeInt(String s) {
        try { return Integer.parseInt(s); } catch (Exception e) { return -1; }
    }
}
