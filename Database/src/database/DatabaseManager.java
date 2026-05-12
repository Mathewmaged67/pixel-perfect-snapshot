package database;

import models.Message;
import models.Snapshot;
import models.User;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // ─────────────────────────────── USER OPS ────────────────────────────────

    public static boolean registerUser(String username, String password, String email) {
        String sql = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, hashPassword(password));
            ps.setString(3, email);
            ps.executeUpdate();
            System.out.println("[DB] Registered user: " + username);
            return true;
        } catch (SQLException e) {
            System.err.println("[DB] registerUser error: " + e.getMessage());
            return false;
        }
    }

    public static User authenticateUser(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, hashPassword(password));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapUser(rs);
        } catch (SQLException e) {
            System.err.println("[DB] authenticateUser error: " + e.getMessage());
        }
        return null;
    }

    public static User getUserById(int id) {
        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM users WHERE id = ?")) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapUser(rs);
        } catch (SQLException e) {
            System.err.println("[DB] getUserById error: " + e.getMessage());
        }
        return null;
    }

    public static List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try (Connection conn = DB.getConnection();
             ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM users ORDER BY username")) {
            while (rs.next()) users.add(mapUser(rs));
        } catch (SQLException e) {
            System.err.println("[DB] getAllUsers error: " + e.getMessage());
        }
        return users;
    }

    public static boolean setUserOnline(int userId, boolean online) {
        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement("UPDATE users SET online = ? WHERE id = ?")) {
            ps.setInt(1, online ? 1 : 0);
            ps.setInt(2, userId);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("[DB] setUserOnline error: " + e.getMessage());
            return false;
        }
    }

    public static boolean updateProfileImage(int userId, String imagePath) {
        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement("UPDATE users SET profile_image = ? WHERE id = ?")) {
            ps.setString(1, imagePath);
            ps.setInt(2, userId);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("[DB] updateProfileImage error: " + e.getMessage());
            return false;
        }
    }

    // ─────────────────────────────── MESSAGE OPS ─────────────────────────────

    public static Message saveMessage(int senderId, String senderUsername, int receiverId,
                                      String content, String type) {
        String sql = "INSERT INTO messages (sender_id, sender_username, receiver_id, content, type, timestamp) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            String ts = LocalDateTime.now().format(FMT);
            ps.setInt(1, senderId);
            ps.setString(2, senderUsername);
            ps.setInt(3, receiverId);
            ps.setString(4, content);
            ps.setString(5, type);
            ps.setString(6, ts);
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                Message msg = new Message(senderId, senderUsername, receiverId, content,
                        Message.MessageType.valueOf(type));
                msg.setId(keys.getInt(1));
                msg.setTimestamp(LocalDateTime.parse(ts, FMT));
                return msg;
            }
        } catch (SQLException e) {
            System.err.println("[DB] saveMessage error: " + e.getMessage());
        }
        return null;
    }

    public static List<Message> getMessagesBetweenUsers(int u1, int u2) {
        List<Message> msgs = new ArrayList<>();
        String sql = "SELECT * FROM messages WHERE " +
                     "(sender_id=? AND receiver_id=?) OR (sender_id=? AND receiver_id=?) " +
                     "ORDER BY timestamp ASC";
        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, u1); ps.setInt(2, u2);
            ps.setInt(3, u2); ps.setInt(4, u1);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) msgs.add(mapMessage(rs));
        } catch (SQLException e) {
            System.err.println("[DB] getMessagesBetweenUsers error: " + e.getMessage());
        }
        return msgs;
    }

    public static List<Message> getBroadcastMessages() {
        List<Message> msgs = new ArrayList<>();
        try (Connection conn = DB.getConnection();
             ResultSet rs = conn.createStatement().executeQuery(
                     "SELECT * FROM messages WHERE receiver_id = -1 ORDER BY timestamp ASC")) {
            while (rs.next()) msgs.add(mapMessage(rs));
        } catch (SQLException e) {
            System.err.println("[DB] getBroadcastMessages error: " + e.getMessage());
        }
        return msgs;
    }

    public static List<Message> getRecentMessages(int limit) {
        List<Message> msgs = new ArrayList<>();
        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT * FROM messages ORDER BY timestamp DESC LIMIT ?")) {
            ps.setInt(1, limit);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) msgs.add(mapMessage(rs));
        } catch (SQLException e) {
            System.err.println("[DB] getRecentMessages error: " + e.getMessage());
        }
        return msgs;
    }

    public static boolean markMessageAsRead(int messageId) {
        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement("UPDATE messages SET is_read=1 WHERE id=?")) {
            ps.setInt(1, messageId);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("[DB] markMessageAsRead error: " + e.getMessage());
            return false;
        }
    }

    // ─────────────────────────────── SNAPSHOT OPS ────────────────────────────

    public static Snapshot saveSnapshot(int userId, String username,
                                        List<Message> messages, String description) {
        String sql = "INSERT INTO snapshots (user_id, username, description, message_count, captured_at) " +
                     "VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            String ts = LocalDateTime.now().format(FMT);
            ps.setInt(1, userId);
            ps.setString(2, username);
            ps.setString(3, description);
            ps.setInt(4, messages != null ? messages.size() : 0);
            ps.setString(5, ts);
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                int snapId = keys.getInt(1);
                if (messages != null) {
                    for (Message m : messages) {
                        try (PreparedStatement link = conn.prepareStatement(
                                "INSERT OR IGNORE INTO snapshot_messages VALUES (?,?)")) {
                            link.setInt(1, snapId);
                            link.setInt(2, m.getId());
                            link.executeUpdate();
                        } catch (SQLException ignored) {}
                    }
                }
                Snapshot snap = new Snapshot(userId, username, messages, description);
                snap.setId(snapId);
                snap.setCapturedAt(LocalDateTime.parse(ts, FMT));
                System.out.println("[DB] Snapshot saved: " + snapId);
                return snap;
            }
        } catch (SQLException e) {
            System.err.println("[DB] saveSnapshot error: " + e.getMessage());
        }
        return null;
    }

    public static List<Snapshot> getSnapshotsByUser(int userId) {
        List<Snapshot> snaps = new ArrayList<>();
        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT * FROM snapshots WHERE user_id=? ORDER BY captured_at DESC")) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Snapshot s = new Snapshot();
                s.setId(rs.getInt("id"));
                s.setUserId(rs.getInt("user_id"));
                s.setUsername(rs.getString("username"));
                s.setDescription(rs.getString("description"));
                s.setMessageCount(rs.getInt("message_count"));
                String ts = rs.getString("captured_at");
                if (ts != null) s.setCapturedAt(LocalDateTime.parse(ts, FMT));
                snaps.add(s);
            }
        } catch (SQLException e) {
            System.err.println("[DB] getSnapshotsByUser error: " + e.getMessage());
        }
        return snaps;
    }

    // ─────────────────────────────── HELPERS ─────────────────────────────────

    private static User mapUser(ResultSet rs) throws SQLException {
        User u = new User();
        u.setId(rs.getInt("id"));
        u.setUsername(rs.getString("username"));
        u.setPassword(rs.getString("password"));
        u.setEmail(rs.getString("email"));
        u.setProfileImagePath(rs.getString("profile_image"));
        u.setOnline(rs.getInt("online") == 1);
        String ts = rs.getString("created_at");
        if (ts != null) {
            try { u.setCreatedAt(LocalDateTime.parse(ts, FMT)); } catch (Exception ignored) {}
        }
        return u;
    }

    private static Message mapMessage(ResultSet rs) throws SQLException {
        Message m = new Message();
        m.setId(rs.getInt("id"));
        m.setSenderId(rs.getInt("sender_id"));
        m.setSenderUsername(rs.getString("sender_username"));
        m.setReceiverId(rs.getInt("receiver_id"));
        m.setContent(rs.getString("content"));
        try { m.setType(Message.MessageType.valueOf(rs.getString("type"))); }
        catch (Exception e) { m.setType(Message.MessageType.TEXT); }
        m.setRead(rs.getInt("is_read") == 1);
        String ts = rs.getString("timestamp");
        if (ts != null) {
            try { m.setTimestamp(LocalDateTime.parse(ts, FMT)); } catch (Exception ignored) {}
        }
        return m;
    }

    /** Simple password hashing using SHA-256 */
    public static String hashPassword(String password) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) {
            return password; // fallback
        }
    }
}
