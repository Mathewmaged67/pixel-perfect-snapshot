package server;

import database.DatabaseManager;
import models.Message;
import models.Snapshot;
import models.User;

import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.Map;

/**
 * Handles one connected client in its own thread.
 *
 * Protocol commands received from client:
 *   LOGIN|username|password
 *   REGISTER|username|password|email
 *   MSG|receiverId|content          (private message, receiverId=-1 for broadcast)
 *   LIST_USERS                      (online users)
 *   HISTORY|otherUserId
 *   BROADCAST_HISTORY
 *   SNAPSHOT|description
 *   LOGOUT
 *
 * Responses sent to client:
 *   LOGIN_OK|userId|username
 *   LOGIN_FAIL|reason
 *   REGISTER_OK|userId|username
 *   REGISTER_FAIL|reason
 *   MSG_RECV|fromUsername|fromId|content|timestamp|isBroadcast
 *   USERS|id:username:online,id:username:online,...
 *   HISTORY_DATA|count|senderUsername|senderId|receiverId|content|timestamp~...
 *   SNAPSHOT_OK|snapshotId
 *   SNAPSHOT_FAIL
 *   USER_JOINED|userId|username
 *   USER_LEFT|userId|username
 *   ERROR|description
 */
public class ClientHandler implements Runnable {

    private final Socket socket;
    private final Map<Integer, ClientHandler> onlineClients;

    private BufferedReader in;
    private PrintWriter out;

    private User currentUser;

    public ClientHandler(Socket socket, Map<Integer, ClientHandler> onlineClients) {
        this.socket = socket;
        this.onlineClients = onlineClients;
    }

    @Override
    public void run() {
        try {
            in  = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);

            String line;
            while ((line = in.readLine()) != null) {
                handleCommand(line.trim());
            }
        } catch (IOException e) {
            // Client disconnected abruptly
        } finally {
            disconnect();
        }
    }

    // ─────────────────────────── Command Dispatcher ───────────────────────────

    private void handleCommand(String raw) {
        if (raw.isEmpty()) return;
        String[] parts = raw.split("\\|", -1);
        String cmd = parts[0].toUpperCase();

        switch (cmd) {
            case "LOGIN"            -> handleLogin(parts);
            case "REGISTER"         -> handleRegister(parts);
            case "MSG"              -> handleMessage(parts);
            case "LIST_USERS"       -> handleListUsers();
            case "HISTORY"          -> handleHistory(parts);
            case "BROADCAST_HISTORY"-> handleBroadcastHistory();
            case "SNAPSHOT"         -> handleSnapshot(parts);
            case "LOGOUT"           -> disconnect();
            default                 -> sendRaw("ERROR|Unknown command: " + cmd);
        }
    }

    // ─────────────────────────── Handlers ─────────────────────────────────────

    private void handleLogin(String[] parts) {
        if (parts.length < 3) { sendRaw("LOGIN_FAIL|Missing username or password"); return; }
        String username = parts[1];
        String password = parts[2];

        User user = DatabaseManager.authenticateUser(username, password);
        if (user == null) {
            sendRaw("LOGIN_FAIL|Invalid username or password");
            return;
        }

        currentUser = user;
        onlineClients.put(user.getId(), this);
        DatabaseManager.setUserOnline(user.getId(), true);

        sendRaw("LOGIN_OK|" + user.getId() + "|" + user.getUsername());
        System.out.println("[Server] User logged in: " + username);

        // Notify everyone else
        Server.broadcastToAll("USER_JOINED|" + user.getId() + "|" + user.getUsername(), user.getId());
    }

    private void handleRegister(String[] parts) {
        if (parts.length < 4) { sendRaw("REGISTER_FAIL|Missing fields"); return; }
        String username = parts[1];
        String password = parts[2];
        String email    = parts[3];

        boolean ok = DatabaseManager.registerUser(username, password, email);
        if (!ok) { sendRaw("REGISTER_FAIL|Username already exists"); return; }

        User user = DatabaseManager.authenticateUser(username, password);
        if (user == null) { sendRaw("REGISTER_FAIL|Internal error"); return; }

        currentUser = user;
        onlineClients.put(user.getId(), this);
        DatabaseManager.setUserOnline(user.getId(), true);

        sendRaw("REGISTER_OK|" + user.getId() + "|" + user.getUsername());
        System.out.println("[Server] New user registered: " + username);
        Server.broadcastToAll("USER_JOINED|" + user.getId() + "|" + user.getUsername(), user.getId());
    }

    private void handleMessage(String[] parts) {
        if (currentUser == null) { sendRaw("ERROR|Not logged in"); return; }
        if (parts.length < 3)   { sendRaw("ERROR|Invalid MSG format"); return; }

        int receiverId;
        try { receiverId = Integer.parseInt(parts[1]); }
        catch (NumberFormatException e) { sendRaw("ERROR|Invalid receiverId"); return; }

        String content = parts[2];
        Message saved = DatabaseManager.saveMessage(
                currentUser.getId(), currentUser.getUsername(), receiverId, content, "TEXT");

        if (saved == null) { sendRaw("ERROR|Could not save message"); return; }

        String packet = "MSG_RECV|" + currentUser.getUsername() + "|" + currentUser.getId()
                + "|" + content + "|" + saved.getFormattedTimestamp()
                + "|" + (receiverId == -1 ? "1" : "0");

        if (receiverId == -1) {
            // Broadcast to all
            Server.broadcastToAll(packet, currentUser.getId());
            sendRaw(packet); // echo back to sender too
        } else {
            // Private: send to receiver + echo to sender
            Server.sendToUser(receiverId, packet);
            sendRaw(packet);
        }
    }

    private void handleListUsers() {
        List<User> allUsers = DatabaseManager.getAllUsers();
        StringBuilder sb = new StringBuilder("USERS|");
        for (int i = 0; i < allUsers.size(); i++) {
            User u = allUsers.get(i);
            boolean isOnline = onlineClients.containsKey(u.getId());
            if (i > 0) sb.append(",");
            sb.append(u.getId()).append(":").append(u.getUsername()).append(":").append(isOnline ? "1" : "0");
        }
        sendRaw(sb.toString());
    }

    private void handleHistory(String[] parts) {
        if (currentUser == null) { sendRaw("ERROR|Not logged in"); return; }
        if (parts.length < 2)   { sendRaw("ERROR|Missing otherUserId"); return; }

        int otherId;
        try { otherId = Integer.parseInt(parts[1]); }
        catch (NumberFormatException e) { sendRaw("ERROR|Invalid userId"); return; }

        List<Message> msgs = DatabaseManager.getMessagesBetweenUsers(currentUser.getId(), otherId);
        sendRaw("HISTORY_DATA|" + buildHistoryPayload(msgs));
    }

    private void handleBroadcastHistory() {
        List<Message> msgs = DatabaseManager.getBroadcastMessages();
        sendRaw("HISTORY_DATA|" + buildHistoryPayload(msgs));
    }

    private void handleSnapshot(String[] parts) {
        if (currentUser == null) { sendRaw("SNAPSHOT_FAIL"); return; }
        String description = (parts.length >= 2) ? parts[1] : "Snapshot";

        List<Message> recent = DatabaseManager.getRecentMessages(50);
        Snapshot snap = DatabaseManager.saveSnapshot(
                currentUser.getId(), currentUser.getUsername(), recent, description);

        if (snap != null) sendRaw("SNAPSHOT_OK|" + snap.getId());
        else              sendRaw("SNAPSHOT_FAIL");
    }

    // ─────────────────────────── Helpers ──────────────────────────────────────

    private String buildHistoryPayload(List<Message> msgs) {
        StringBuilder sb = new StringBuilder(msgs.size() + "");
        for (Message m : msgs) {
            sb.append("|")
              .append(m.getSenderUsername()).append("|")
              .append(m.getSenderId()).append("|")
              .append(m.getReceiverId()).append("|")
              .append(m.getContent()).append("|")
              .append(m.getFormattedTimestamp());
        }
        return sb.toString();
    }

    public void sendRaw(String message) {
        if (out != null) out.println(message);
    }

    public void disconnect() {
        if (currentUser != null) {
            onlineClients.remove(currentUser.getId());
            DatabaseManager.setUserOnline(currentUser.getId(), false);
            Server.broadcastToAll("USER_LEFT|" + currentUser.getId() + "|" + currentUser.getUsername(),
                    currentUser.getId());
            System.out.println("[Server] User disconnected: " + currentUser.getUsername());
            currentUser = null;
        }
        try { if (!socket.isClosed()) socket.close(); } catch (IOException ignored) {}
    }

    public String getUsername() {
        return currentUser != null ? currentUser.getUsername() : "unknown";
    }
}
