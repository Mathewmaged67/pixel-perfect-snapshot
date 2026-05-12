package database;

import java.sql.*;

public class DB {
    private static final String DB_URL = "jdbc:sqlite:chat.db";
    private static Connection connection;

    private DB() {}

    public static synchronized Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                Class.forName("org.sqlite.JDBC");
            } catch (ClassNotFoundException e) {
                throw new SQLException("SQLite JDBC driver not found: " + e.getMessage());
            }
            connection = DriverManager.getConnection(DB_URL);
            connection.createStatement().execute("PRAGMA foreign_keys = ON");
        }
        return connection;
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                    System.out.println("[DB] Connection closed.");
                }
            } catch (SQLException e) {
                System.err.println("[DB] Error closing connection: " + e.getMessage());
            }
        }
    }

    public static void initializeDatabase() throws SQLException {
        Connection conn = getConnection();
        Statement stmt = conn.createStatement();

        stmt.execute("""
            CREATE TABLE IF NOT EXISTS users (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                username TEXT UNIQUE NOT NULL,
                password TEXT NOT NULL,
                email TEXT,
                profile_image TEXT,
                created_at TEXT DEFAULT CURRENT_TIMESTAMP,
                online INTEGER DEFAULT 0
            )
        """);

        stmt.execute("""
            CREATE TABLE IF NOT EXISTS messages (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                sender_id INTEGER NOT NULL,
                sender_username TEXT NOT NULL,
                receiver_id INTEGER DEFAULT -1,
                content TEXT NOT NULL,
                type TEXT DEFAULT 'TEXT',
                timestamp TEXT DEFAULT CURRENT_TIMESTAMP,
                is_read INTEGER DEFAULT 0,
                FOREIGN KEY (sender_id) REFERENCES users(id)
            )
        """);

        stmt.execute("""
            CREATE TABLE IF NOT EXISTS snapshots (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                user_id INTEGER NOT NULL,
                username TEXT NOT NULL,
                description TEXT,
                message_count INTEGER DEFAULT 0,
                captured_at TEXT DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY (user_id) REFERENCES users(id)
            )
        """);

        stmt.execute("""
            CREATE TABLE IF NOT EXISTS snapshot_messages (
                snapshot_id INTEGER NOT NULL,
                message_id INTEGER NOT NULL,
                PRIMARY KEY (snapshot_id, message_id),
                FOREIGN KEY (snapshot_id) REFERENCES snapshots(id),
                FOREIGN KEY (message_id) REFERENCES messages(id)
            )
        """);

        stmt.close();
        System.out.println("[DB] Database initialized successfully.");
    }
}
