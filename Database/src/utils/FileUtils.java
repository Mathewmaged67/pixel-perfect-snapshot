package utils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility methods for reading, writing, and managing files.
 */
public class FileUtils {

    private FileUtils() {}

    // ─────────────────────── Read / Write ────────────────────────────────────

    /** Read all lines from a text file. Returns empty list on error. */
    public static List<String> readLines(String filePath) {
        List<String> lines = new ArrayList<>();
        try {
            lines = Files.readAllLines(Path.of(filePath), StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.err.println("[FileUtils] readLines error: " + e.getMessage());
        }
        return lines;
    }

    /** Read entire file as a single String. Returns empty string on error. */
    public static String readAll(String filePath) {
        try {
            return Files.readString(Path.of(filePath), StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.err.println("[FileUtils] readAll error: " + e.getMessage());
            return "";
        }
    }

    /** Write text to a file, overwriting any existing content. */
    public static boolean writeText(String filePath, String content) {
        try {
            Path p = Path.of(filePath);
            Files.createDirectories(p.getParent());
            Files.writeString(p, content, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            return true;
        } catch (IOException e) {
            System.err.println("[FileUtils] writeText error: " + e.getMessage());
            return false;
        }
    }

    /** Append text to a file (creates it if it doesn't exist). */
    public static boolean appendText(String filePath, String content) {
        try {
            Path p = Path.of(filePath);
            Files.createDirectories(p.getParent());
            Files.writeString(p, content, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            return true;
        } catch (IOException e) {
            System.err.println("[FileUtils] appendText error: " + e.getMessage());
            return false;
        }
    }

    /** Write bytes to a file (used for image / binary data). */
    public static boolean writeBytes(String filePath, byte[] data) {
        try {
            Path p = Path.of(filePath);
            Files.createDirectories(p.getParent());
            Files.write(p, data, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            return true;
        } catch (IOException e) {
            System.err.println("[FileUtils] writeBytes error: " + e.getMessage());
            return false;
        }
    }

    /** Read all bytes from a file. Returns null on error. */
    public static byte[] readBytes(String filePath) {
        try {
            return Files.readAllBytes(Path.of(filePath));
        } catch (IOException e) {
            System.err.println("[FileUtils] readBytes error: " + e.getMessage());
            return null;
        }
    }

    // ─────────────────────── File Helpers ────────────────────────────────────

    /** Ensure a directory exists; create it (and parents) if needed. */
    public static boolean ensureDirectory(String dirPath) {
        try {
            Files.createDirectories(Path.of(dirPath));
            return true;
        } catch (IOException e) {
            System.err.println("[FileUtils] ensureDirectory error: " + e.getMessage());
            return false;
        }
    }

    /** Delete a file if it exists. */
    public static boolean deleteFile(String filePath) {
        try {
            return Files.deleteIfExists(Path.of(filePath));
        } catch (IOException e) {
            System.err.println("[FileUtils] deleteFile error: " + e.getMessage());
            return false;
        }
    }

    /** Check whether a file exists and is a regular file. */
    public static boolean fileExists(String filePath) {
        return Files.isRegularFile(Path.of(filePath));
    }

    /** Get the file extension (lowercase), e.g. "png", "txt". Empty if none. */
    public static String getExtension(String filePath) {
        int dot = filePath.lastIndexOf('.');
        if (dot < 0 || dot == filePath.length() - 1) return "";
        return filePath.substring(dot + 1).toLowerCase();
    }

    /** Return a timestamped filename, e.g. "prefix_20240511_153045.ext" */
    public static String timestampedFilename(String prefix, String extension) {
        String ts = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        return prefix + "_" + ts + (extension.startsWith(".") ? extension : "." + extension);
    }

    /** Save a chat log to the logs/ directory. */
    public static boolean saveChatLog(String username, String chatContent) {
        ensureDirectory("logs");
        String filename = "logs/" + timestampedFilename("chat_" + username, "txt");
        return writeText(filename, chatContent);
    }

    /** Copy a file from src to dst. */
    public static boolean copyFile(String src, String dst) {
        try {
            Path dstPath = Path.of(dst);
            Files.createDirectories(dstPath.getParent());
            Files.copy(Path.of(src), dstPath, StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (IOException e) {
            System.err.println("[FileUtils] copyFile error: " + e.getMessage());
            return false;
        }
    }
}
