import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public final class FormatUtil {
  private static final DateTimeFormatter DATE_FORMAT =
      DateTimeFormatter.ofPattern("MMM dd").withZone(ZoneId.systemDefault());

  private FormatUtil() {}

  public static String formatDuration(long ms) {
    long totalSeconds = Math.max(0, ms / 1000);
    long minutes = totalSeconds / 60;
    long seconds = totalSeconds % 60;
    return String.format("%dm %02ds", minutes, seconds);
  }

  public static String formatDate(long timestamp) {
    return DATE_FORMAT.format(Instant.ofEpochMilli(timestamp));
  }
}
