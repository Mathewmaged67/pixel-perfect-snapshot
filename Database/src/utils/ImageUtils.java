package utils;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.*;
import java.util.Base64;

/**
 * Utility methods for image loading, resizing, converting,
 * and managing profile pictures within the chat application.
 */
public class ImageUtils {

    /** Default avatar shown when no profile picture is set. */
    public static final String DEFAULT_AVATAR = "assets/default_avatar.png";

    private ImageUtils() {}

    // ─────────────────────── JavaFX Image Loading ────────────────────────────

    /**
     * Load a JavaFX Image from a file path.
     * Falls back to the default avatar if the file is missing.
     */
    public static Image loadImage(String filePath) {
        try {
            File f = new File(filePath);
            if (f.exists()) {
                return new Image(f.toURI().toString());
            }
        } catch (Exception e) {
            System.err.println("[ImageUtils] loadImage error: " + e.getMessage());
        }
        return loadDefaultAvatar();
    }

    /** Load the default avatar image. */
    public static Image loadDefaultAvatar() {
        try {
            InputStream is = ImageUtils.class.getResourceAsStream("/" + DEFAULT_AVATAR);
            if (is != null) return new Image(is);
        } catch (Exception ignored) {}
        // Programmatically generate a grey placeholder
        return generatePlaceholderAvatar(40);
    }

    /**
     * Create an ImageView pre-configured for a circular profile picture.
     * @param filePath path to the image (or null for default)
     * @param size     diameter in pixels
     */
    public static ImageView createAvatarView(String filePath, double size) {
        Image img = (filePath != null && !filePath.isBlank())
                ? loadImage(filePath) : loadDefaultAvatar();
        ImageView iv = new ImageView(img);
        iv.setFitWidth(size);
        iv.setFitHeight(size);
        iv.setPreserveRatio(false);
        iv.setSmooth(true);
        return iv;
    }

    // ─────────────────────── AWT Image Ops ───────────────────────────────────

    /**
     * Resize an image file and save it to the destination path.
     * @return true on success
     */
    public static boolean resizeImage(String srcPath, String dstPath, int width, int height) {
        try {
            BufferedImage src = ImageIO.read(new File(srcPath));
            if (src == null) return false;

            BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = resized.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.drawImage(src, 0, 0, width, height, null);
            g.dispose();

            String ext = FileUtils.getExtension(dstPath);
            if (ext.isEmpty()) ext = "png";
            Path dstP = Path.of(dstPath);
            Files.createDirectories(dstP.getParent());
            ImageIO.write(resized, ext, dstP.toFile());
            return true;
        } catch (IOException e) {
            System.err.println("[ImageUtils] resizeImage error: " + e.getMessage());
            return false;
        }
    }

    /**
     * Copy an image file into the profiles/ directory with a new name.
     * Returns the destination path, or null on failure.
     */
    public static String saveProfileImage(String srcPath, String username) {
        String ext = FileUtils.getExtension(srcPath);
        if (ext.isEmpty()) ext = "png";
        FileUtils.ensureDirectory("profiles");
        String dst = "profiles/" + username + "_avatar." + ext;
        boolean ok = resizeImage(srcPath, dst, 128, 128);
        return ok ? dst : null;
    }

    // ─────────────────────── Base64 Helpers ──────────────────────────────────

    /** Encode an image file to a Base64 string (for transmission over the protocol). */
    public static String encodeImageToBase64(String filePath) {
        byte[] bytes = FileUtils.readBytes(filePath);
        if (bytes == null) return null;
        return Base64.getEncoder().encodeToString(bytes);
    }

    /** Decode a Base64 string and save it as an image file. */
    public static boolean decodeBase64ToFile(String base64, String dstPath) {
        try {
            byte[] bytes = Base64.getDecoder().decode(base64);
            return FileUtils.writeBytes(dstPath, bytes);
        } catch (Exception e) {
            System.err.println("[ImageUtils] decodeBase64ToFile error: " + e.getMessage());
            return false;
        }
    }

    // ─────────────────────── Placeholder ─────────────────────────────────────

    /**
     * Programmatically generate a simple grey circle avatar as a JavaFX Image.
     * Used as a last-resort fallback when no image file is available.
     */
    private static Image generatePlaceholderAvatar(int size) {
        BufferedImage buf = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = buf.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(new Color(100, 100, 120));
        g.fillOval(0, 0, size, size);
        g.setColor(Color.WHITE);
        g.setFont(new Font("SansSerif", Font.BOLD, size / 2));
        FontMetrics fm = g.getFontMetrics();
        String text = "?";
        g.drawString(text, (size - fm.stringWidth(text)) / 2, (size + fm.getAscent()) / 2 - 2);
        g.dispose();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(buf, "png", baos);
            return new Image(new ByteArrayInputStream(baos.toByteArray()));
        } catch (IOException e) {
            return null;
        }
    }

    // ─────────────────────── Validation ──────────────────────────────────────

    /** Returns true if the file has a supported image extension. */
    public static boolean isSupportedImage(String filePath) {
        String ext = FileUtils.getExtension(filePath);
        return ext.equals("png") || ext.equals("jpg") || ext.equals("jpeg")
            || ext.equals("gif") || ext.equals("bmp") || ext.equals("webp");
    }
}
