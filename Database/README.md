# 💬 ChatApp – Java OOP Project

A multi-user real-time **chat application** built with Java, JavaFX, SQLite, and TCP sockets.

---

## 📁 Project Structure

```
src/
├── server/
│   ├── Server.java          ← TCP server, manages all clients
│   └── ClientHandler.java   ← Per-client thread, handles protocol
├── client/
│   ├── Client.java          ← JavaFX Application entry point
│   └── NetworkManager.java  ← Socket connection & async listener
├── database/
│   ├── DB.java              ← SQLite connection singleton
│   └── DatabaseManager.java ← Full CRUD (users, messages, snapshots)
├── ui/
│   ├── LoginPage.java       ← Login / Register screen
│   ├── Dashboard.java       ← User list + Global broadcast chat
│   └── ChatPage.java        ← Private 1-on-1 chat window
├── models/
│   ├── User.java            ← User entity
│   ├── Message.java         ← Message entity (TEXT/IMAGE/FILE/SYSTEM)
│   └── Snapshot.java        ← Chat snapshot entity
└── utils/
    ├── FileUtils.java       ← File read/write/copy helpers
    └── ImageUtils.java      ← Image load/resize/Base64 helpers
```

---

## ⚙️ Setup

### Prerequisites
| Tool | Version |
|------|---------|
| JDK  | 17 +    |
| JavaFX SDK | 17 + |
| SQLite JDBC | 3.x |

### Step 1 – Place dependencies in `lib/`
```
lib/
├── sqlite-jdbc.jar          ← Download from https://github.com/xerial/sqlite-jdbc/releases
└── javafx/
    └── lib/
        ├── javafx.controls.jar
        ├── javafx.fxml.jar
        ├── javafx.base.jar
        └── javafx.graphics.jar
```
Download JavaFX SDK from: https://gluonhq.com/products/javafx/

### Step 2 – Compile
```bat
build.bat
```

### Step 3 – Run Server (first!)
```bat
run-server.bat
```

### Step 4 – Run Client(s) (one per user)
```bat
run-client.bat
```

---

## 🔌 Protocol Reference

All messages are plain text lines over TCP, pipe-delimited:

| Direction | Command | Description |
|-----------|---------|-------------|
| C → S | `LOGIN\|username\|password` | Authenticate |
| C → S | `REGISTER\|username\|password\|email` | Create account |
| C → S | `MSG\|-1\|text` | Broadcast to all |
| C → S | `MSG\|userId\|text` | Private message |
| C → S | `LIST_USERS` | Get all users + online status |
| C → S | `HISTORY\|userId` | Load private history |
| C → S | `BROADCAST_HISTORY` | Load global history |
| C → S | `SNAPSHOT\|desc` | Save chat snapshot |
| C → S | `LOGOUT` | Disconnect |
| S → C | `LOGIN_OK\|id\|username` | Login success |
| S → C | `MSG_RECV\|from\|fromId\|content\|time\|isBroadcast` | Incoming message |
| S → C | `USER_JOINED\|id\|username` | Notification |
| S → C | `USER_LEFT\|id\|username` | Notification |

---

## 🗄️ Database Schema

- **users** – id, username, password (SHA-256), email, profile_image, online
- **messages** – id, sender_id, sender_username, receiver_id (-1=broadcast), content, type, timestamp, is_read
- **snapshots** – id, user_id, description, message_count, captured_at
- **snapshot_messages** – snapshot_id ↔ message_id (junction table)

---

## ✨ Features

- 🔐 Secure login / registration (SHA-256 hashed passwords)
- 🌐 Global broadcast chat room
- 💬 Private 1-on-1 chat with message history
- 👥 Real-time online/offline user list
- 📷 Chat snapshots (save current messages to DB)
- 🎨 Dark-themed JavaFX UI with message bubbles
- 🗄️ Persistent SQLite storage
- 🧵 Multi-threaded server (one thread per client)
