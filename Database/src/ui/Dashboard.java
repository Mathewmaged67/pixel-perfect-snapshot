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

import java.util.HashMap;
import java.util.Map;

/**
 * Main dashboard showing the user list (online/offline) on the left
 * and the global broadcast chat on the right.
 */
public class Dashboard {

    private final Stage          stage;
    private final NetworkManager network;

    private VBox     userListBox;
    private VBox     broadcastBox;
    private TextField broadcastInput;
    private Label    statusBar;

    // userId → username (all known users)
    private final Map<Integer, String> knownUsers = new HashMap<>();

    public Dashboard(Stage stage, NetworkManager network) {
        this.stage   = stage;
        this.network = network;
    }

    public void show() {
        // ── Root layout
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #0f0f1a;");

        // ── LEFT SIDEBAR: User list
        VBox sidebar = buildSidebar();
        root.setLeft(sidebar);

        // ── CENTER: Broadcast chat
        VBox center = buildBroadcastPanel();
        root.setCenter(center);

        // ── TOP BAR
        HBox topBar = buildTopBar();
        root.setTop(topBar);

        // ── BOTTOM STATUS
        statusBar = new Label("● Connected  |  Logged in as: " + Client.getCurrentUsername());
        statusBar.setStyle("-fx-background-color: #12122a; -fx-text-fill: #34d399; -fx-font-size: 11px; -fx-padding: 6 16 6 16;");
        statusBar.setMaxWidth(Double.MAX_VALUE);
        root.setBottom(statusBar);

        // Install network handler
        network.setOnMessage(msg -> Platform.runLater(() -> handleServerMessage(msg)));

        // Request user list + broadcast history
        network.sendCommand("LIST_USERS");
        network.sendCommand("BROADCAST_HISTORY");

        Scene scene = new Scene(root, 980, 660);
        stage.setScene(scene);
        stage.setTitle("💬 ChatApp – Dashboard");
    }

    // ─────────────────────── Top bar ─────────────────────────────────────────

    private HBox buildTopBar() {
        HBox bar = new HBox(12);
        bar.setAlignment(Pos.CENTER_LEFT);
        bar.setPadding(new Insets(14, 20, 14, 20));
        bar.setStyle("-fx-background-color: #1a1a2e; -fx-border-color: #2d2d4a; -fx-border-width: 0 0 1 0;");

        Label logo  = new Label("💬 ChatApp");
        logo.setFont(Font.font("Segoe UI", FontWeight.BOLD, 20));
        logo.setTextFill(Color.WHITE);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button snapshotBtn = actionButton("📷 Snapshot", "#6366f1");
        snapshotBtn.setOnAction(e -> {
            TextInputDialog dlg = new TextInputDialog("My snapshot");
            dlg.setTitle("Save Snapshot");
            dlg.setHeaderText("Enter a description for this snapshot:");
            dlg.setContentText("Description:");
            dlg.showAndWait().ifPresent(desc -> network.sendCommand("SNAPSHOT", desc));
        });

        Button logoutBtn = actionButton("⏻ Logout", "#ef4444");
        logoutBtn.setOnAction(e -> {
            network.sendCommand("LOGOUT");
            network.disconnect();
            new LoginPage(stage, new NetworkManager(null, null)).show();
        });

        bar.getChildren().addAll(logo, spacer, snapshotBtn, logoutBtn);
        return bar;
    }

    // ─────────────────────── Sidebar ─────────────────────────────────────────

    private VBox buildSidebar() {
        VBox sidebar = new VBox(0);
        sidebar.setPrefWidth(230);
        sidebar.setStyle("-fx-background-color: #141428; -fx-border-color: #2d2d4a; -fx-border-width: 0 1 0 0;");

        Label header = new Label("  👥 Users");
        header.setFont(Font.font("Segoe UI", FontWeight.BOLD, 13));
        header.setTextFill(Color.web("#8b8baa"));
        header.setPadding(new Insets(16, 0, 10, 0));
        header.setMaxWidth(Double.MAX_VALUE);
        header.setStyle("-fx-background-color: #141428; -fx-border-color: #2d2d4a; -fx-border-width: 0 0 1 0;");

        userListBox = new VBox(2);
        userListBox.setPadding(new Insets(8, 6, 8, 6));

        ScrollPane scroll = new ScrollPane(userListBox);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background: #141428; -fx-background-color: #141428; -fx-border-color: transparent;");
        VBox.setVgrow(scroll, Priority.ALWAYS);

        sidebar.getChildren().addAll(header, scroll);
        return sidebar;
    }

    // ─────────────────────── Broadcast panel ─────────────────────────────────

    private VBox buildBroadcastPanel() {
        VBox panel = new VBox(0);
        panel.setStyle("-fx-background-color: #0f0f1a;");

        // Header
        Label header = new Label("  🌐 Global Chat");
        header.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        header.setTextFill(Color.WHITE);
        header.setPadding(new Insets(14, 0, 14, 16));
        header.setMaxWidth(Double.MAX_VALUE);
        header.setStyle("-fx-background-color: #1a1a2e; -fx-border-color: #2d2d4a; -fx-border-width: 0 0 1 0;");

        // Messages scroll area
        broadcastBox = new VBox(6);
        broadcastBox.setPadding(new Insets(12));

        ScrollPane scroll = new ScrollPane(broadcastBox);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background: #0f0f1a; -fx-background-color: #0f0f1a; -fx-border-color: transparent;");
        VBox.setVgrow(scroll, Priority.ALWAYS);

        // Input bar
        HBox inputBar = new HBox(10);
        inputBar.setPadding(new Insets(12, 14, 12, 14));
        inputBar.setAlignment(Pos.CENTER);
        inputBar.setStyle("-fx-background-color: #1a1a2e; -fx-border-color: #2d2d4a; -fx-border-width: 1 0 0 0;");

        broadcastInput = new TextField();
        broadcastInput.setPromptText("Message everyone...");
        broadcastInput.setStyle("""
            -fx-background-color: #0f0f1a;
            -fx-text-fill: white;
            -fx-prompt-text-fill: #555577;
            -fx-background-radius: 20;
            -fx-border-color: #2d2d4a;
            -fx-border-radius: 20;
            -fx-padding: 10 16 10 16;
            -fx-font-size: 13px;
        """);
        HBox.setHgrow(broadcastInput, Priority.ALWAYS);

        Button sendBtn = actionButton("Send ➤", "#6366f1");
        sendBtn.setOnAction(e -> sendBroadcast());
        broadcastInput.setOnAction(e -> sendBroadcast());

        inputBar.getChildren().addAll(broadcastInput, sendBtn);
        panel.getChildren().addAll(header, scroll, inputBar);
        return panel;
    }

    // ─────────────────────── Network handler ─────────────────────────────────

    private void handleServerMessage(String msg) {
        String[] parts = msg.split("\\|", -1);
        switch (parts[0]) {
            case "USERS"         -> refreshUserList(parts);
            case "MSG_RECV"      -> handleIncoming(parts);
            case "USER_JOINED"   -> onUserJoined(parts);
            case "USER_LEFT"     -> onUserLeft(parts);
            case "HISTORY_DATA"  -> loadBroadcastHistory(parts);
            case "SNAPSHOT_OK"   -> showSnapshotAlert("Snapshot saved! ID: " + (parts.length > 1 ? parts[1] : "?"));
            case "SNAPSHOT_FAIL" -> showSnapshotAlert("Snapshot failed.");
            default -> {}
        }
    }

    private void refreshUserList(String[] parts) {
        userListBox.getChildren().clear();
        if (parts.length < 2 || parts[1].isBlank()) return;
        for (String entry : parts[1].split(",")) {
            String[] info = entry.split(":");
            if (info.length < 3) continue;
            int     uid      = Integer.parseInt(info[0]);
            String  uname    = info[1];
            boolean isOnline = info[2].equals("1");
            knownUsers.put(uid, uname);
            userListBox.getChildren().add(buildUserRow(uid, uname, isOnline));
        }
    }

    private void handleIncoming(String[] parts) {
        if (parts.length < 5) return;
        String fromUser   = parts[1];
        int    fromId     = Integer.parseInt(parts[2]);
        String content    = parts[3];
        String time       = parts[4];
        boolean broadcast = parts.length > 5 && parts[5].equals("1");

        if (broadcast) {
            appendBroadcastMessage(fromUser, content, time, fromId == Client.getCurrentUserId());
        }
        // Private messages are handled in ChatPage
    }

    private void onUserJoined(String[] parts) {
        if (parts.length < 3) return;
        int    uid   = Integer.parseInt(parts[1]);
        String uname = parts[2];
        knownUsers.put(uid, uname);
        network.sendCommand("LIST_USERS");
        appendSystemMessage("● " + uname + " joined");
    }

    private void onUserLeft(String[] parts) {
        if (parts.length < 3) return;
        String uname = parts[2];
        network.sendCommand("LIST_USERS");
        appendSystemMessage("○ " + uname + " left");
    }

    private void loadBroadcastHistory(String[] parts) {
        if (parts.length < 2) return;
        int count;
        try { count = Integer.parseInt(parts[1]); } catch (Exception e) { return; }
        int base = 2;
        for (int i = 0; i < count; i++) {
            int idx = base + i * 5;
            if (idx + 4 >= parts.length) break;
            String uname = parts[idx];
            int    uid   = Integer.parseInt(parts[idx + 1]);
            // receiverId at idx+2
            String content = parts[idx + 3];
            String time    = parts[idx + 4];
            appendBroadcastMessage(uname, content, time, uid == Client.getCurrentUserId());
        }
    }

    // ─────────────────────── UI helpers ──────────────────────────────────────

    private HBox buildUserRow(int uid, String username, boolean online) {
        HBox row = new HBox(10);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(8, 10, 8, 10));
        row.setStyle("-fx-background-radius: 10; -fx-cursor: hand;");

        Label dot = new Label(online ? "●" : "○");
        dot.setTextFill(online ? Color.web("#34d399") : Color.web("#555577"));

        Label name = new Label(username);
        name.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 13));
        name.setTextFill(Color.WHITE);

        row.getChildren().addAll(dot, name);

        // Hover effects
        row.setOnMouseEntered(e -> row.setStyle("-fx-background-color: #1e1e38; -fx-background-radius: 10; -fx-cursor: hand;"));
        row.setOnMouseExited(e  -> row.setStyle("-fx-background-radius: 10; -fx-cursor: hand;"));

        // Click → open private chat
        row.setOnMouseClicked(e -> {
            if (uid != Client.getCurrentUserId()) {
                ChatPage chat = new ChatPage(stage, network, uid, username);
                chat.show();
            }
        });

        return row;
    }

    private void appendBroadcastMessage(String from, String content, String time, boolean isMine) {
        HBox row = new HBox();
        row.setPadding(new Insets(2, 0, 2, 0));

        Label bubble = new Label("[" + time + "] " + from + ": " + content);
        bubble.setWrapText(true);
        bubble.setMaxWidth(500);
        bubble.setPadding(new Insets(8, 14, 8, 14));
        bubble.setFont(Font.font("Segoe UI", 13));

        if (isMine) {
            bubble.setStyle("-fx-background-color: #4f46e5; -fx-text-fill: white; -fx-background-radius: 14 14 4 14;");
            row.setAlignment(Pos.CENTER_RIGHT);
        } else {
            bubble.setStyle("-fx-background-color: #1e1e38; -fx-text-fill: #e2e2f0; -fx-background-radius: 14 14 14 4;");
            row.setAlignment(Pos.CENTER_LEFT);
        }

        row.getChildren().add(bubble);
        broadcastBox.getChildren().add(row);
    }

    private void appendSystemMessage(String msg) {
        Label lbl = new Label(msg);
        lbl.setFont(Font.font("Segoe UI", FontPosture.ITALIC, 11));
        lbl.setTextFill(Color.web("#555577"));
        lbl.setMaxWidth(Double.MAX_VALUE);
        lbl.setAlignment(Pos.CENTER);
        broadcastBox.getChildren().add(lbl);
    }

    private void sendBroadcast() {
        String text = broadcastInput.getText().trim();
        if (!text.isEmpty()) {
            network.sendCommand("MSG", "-1", text);
            broadcastInput.clear();
        }
    }

    private void showSnapshotAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
        alert.setTitle("Snapshot");
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    private Button actionButton(String label, String color) {
        Button btn = new Button(label);
        String base = "-fx-background-color: " + color + "; -fx-text-fill: white; " +
                      "-fx-font-size: 12px; -fx-background-radius: 8; -fx-cursor: hand; -fx-padding: 8 16 8 16;";
        btn.setStyle(base);
        return btn;
    }
}
