import java.util.UUID;

public final class IdUtil {
  private IdUtil() {}

  public static String randomId() {
    return UUID.randomUUID().toString().replace("-", "").substring(0, 8);
  }
}
