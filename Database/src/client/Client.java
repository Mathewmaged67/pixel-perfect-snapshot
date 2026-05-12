package client;

import ui.LoginPage;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Entry point for the Chat Client application.
 * Launches the JavaFX UI starting at the LoginPage.
 */
public class Client extends Application {

    /** Shared NetworkManager used across all UI pages. */
    private static NetworkManager networkManager;

    /** Currently logged-in userId (set after successful login/register). */
    private static int    currentUserId   = -1;
    private static String currentUsername = "";

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("💬 ChatApp");
        primaryStage.setMinWidth(900);
        primaryStage.setMinHeight(620);

        // Create network manager (UI pages will attach handlers after login)
        networkManager = new NetworkManager(
            msg  -> { /* handled per-page */ },
            err  -> System.err.println("[Client] Network error: " + err)
        );

        // Show login page
        LoginPage loginPage = new LoginPage(primaryStage, networkManager);
        loginPage.show();
    }

    @Override
    public void stop() {
        if (networkManager != null && networkManager.isConnected()) {
            networkManager.disconnect();
        }
    }

    // ─────────────────────── Static helpers ──────────────────────────────────

    public static NetworkManager getNetworkManager() { return networkManager; }

    public static int getCurrentUserId()       { return currentUserId;   }
    public static String getCurrentUsername()  { return currentUsername; }

    public static void setCurrentUser(int id, String username) {
        currentUserId   = id;
        currentUsername = username;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
