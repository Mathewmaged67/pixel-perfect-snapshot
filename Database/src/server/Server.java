package server;

import database.DB;
import database.DatabaseManager;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Multi-threaded TCP chat server.
 * Protocol (text over socket):
 *   Client → Server:  COMMAND|arg1|arg2|...
 *   Server → Client:  RESPONSE|arg1|arg2|...
 */
public class Server {

    public static final int PORT = 12345;

    // Maps userId → its active ClientHandler
    private static final Map<Integer, ClientHandler> onlineClients = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════╗");
        System.out.println("║      Chat Server v1.0        ║");
        System.out.println("╚══════════════════════════════╝");

        // Initialise database
        try {
            DB.initializeDatabase();
        } catch (Exception e) {
            System.err.println("[Server] DB init failed: " + e.getMessage());
            return;
        }

        // Set all users offline at startup (clean slate)
        DatabaseManager.getAllUsers().forEach(u -> DatabaseManager.setUserOnline(u.getId(), false));

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("[Server] Listening on port " + PORT + " ...");

            // Shutdown hook to clean up
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                System.out.println("[Server] Shutting down...");
                onlineClients.values().forEach(ClientHandler::disconnect);
                DB.closeConnection();
            }));

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("[Server] New connection: " + clientSocket.getInetAddress());
                ClientHandler handler = new ClientHandler(clientSocket, onlineClients);
                new Thread(handler).start();
            }

        } catch (IOException e) {
            System.err.println("[Server] Fatal error: " + e.getMessage());
        }
    }

    /** Broadcast a raw message string to every connected client except the sender. */
    public static void broadcastToAll(String message, int senderUserId) {
        onlineClients.forEach((uid, handler) -> {
            if (uid != senderUserId) handler.sendRaw(message);
        });
    }

    /** Send a message to one specific connected client. */
    public static boolean sendToUser(int userId, String message) {
        ClientHandler handler = onlineClients.get(userId);
        if (handler != null) {
            handler.sendRaw(message);
            return true;
        }
        return false;
    }

    /** Return a pipe-separated list of currently online users: "id:username,id:username,..." */
    public static String getOnlineUserList() {
        StringBuilder sb = new StringBuilder();
        onlineClients.forEach((uid, h) -> {
            if (sb.length() > 0) sb.append(",");
            sb.append(uid).append(":").append(h.getUsername());
        });
        return sb.toString();
    }

    public static Map<Integer, ClientHandler> getOnlineClients() {
        return onlineClients;
    }
}
