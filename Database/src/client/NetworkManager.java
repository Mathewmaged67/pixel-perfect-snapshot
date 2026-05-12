package client;

import java.io.*;
import java.net.Socket;
import java.util.function.Consumer;

/**
 * Manages the raw TCP connection to the server.
 * Runs a background listener thread that forwards every received
 * line to a registered message handler (Consumer<String>).
 */
public class NetworkManager {

    private static final String HOST = "localhost";
    private static final int    PORT = 12345;

    private Socket       socket;
    private PrintWriter  out;
    private BufferedReader in;

    private Thread       listenerThread;
    private boolean      connected = false;

    private Consumer<String> onMessage;   // called for every server line
    private Consumer<String> onError;     // called on connection errors

    public NetworkManager(Consumer<String> onMessage, Consumer<String> onError) {
        this.onMessage = onMessage;
        this.onError   = onError;
    }

    // ─────────────────────── Connection ──────────────────────────────────────

    public boolean connect() {
        return connect(HOST, PORT);
    }

    public boolean connect(String host, int port) {
        try {
            socket = new Socket(host, port);
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            in  = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            connected = true;
            startListener();
            System.out.println("[Network] Connected to " + host + ":" + port);
            return true;
        } catch (IOException e) {
            System.err.println("[Network] Connection failed: " + e.getMessage());
            if (onError != null) onError.accept("Cannot connect to server: " + e.getMessage());
            return false;
        }
    }

    public void disconnect() {
        connected = false;
        sendCommand("LOGOUT");
        try {
            if (listenerThread != null) listenerThread.interrupt();
            if (socket != null && !socket.isClosed()) socket.close();
        } catch (IOException ignored) {}
        System.out.println("[Network] Disconnected.");
    }

    // ─────────────────────── Sending ─────────────────────────────────────────

    /** Send a raw protocol string to the server. */
    public void sendCommand(String command) {
        if (out != null && connected) {
            out.println(command);
        }
    }

    /** Convenience: build pipe-delimited command and send. */
    public void sendCommand(String... parts) {
        sendCommand(String.join("|", parts));
    }

    // ─────────────────────── Listener Thread ─────────────────────────────────

    private void startListener() {
        listenerThread = new Thread(() -> {
            try {
                String line;
                while (connected && (line = in.readLine()) != null) {
                    final String msg = line;
                    if (onMessage != null) onMessage.accept(msg);
                }
            } catch (IOException e) {
                if (connected) {
                    System.err.println("[Network] Connection lost: " + e.getMessage());
                    if (onError != null) onError.accept("Connection lost: " + e.getMessage());
                }
            }
        }, "NetworkListener");
        listenerThread.setDaemon(true);
        listenerThread.start();
    }

    // ─────────────────────── Getters ─────────────────────────────────────────

    public boolean isConnected() { return connected; }

    public void setOnMessage(Consumer<String> onMessage) { this.onMessage = onMessage; }
    public void setOnError(Consumer<String> onError)     { this.onError   = onError;   }
}
