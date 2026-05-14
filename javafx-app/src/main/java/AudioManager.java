import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class AudioManager {
  private static AudioManager instance;
  private MediaPlayer mediaPlayer;
  private String currentTrack;

  private AudioManager() {}

  public static AudioManager getInstance() {
    if (instance == null) {
      instance = new AudioManager();
    }
    return instance;
  }

  /**
   * Play a background music track that loops indefinitely
   * @param resourcePath path to the music file relative to resources (e.g., "/music/background-music.mp3")
   */
  public void playLoopingMusic(String resourcePath) {
    try {
      // Stop current track if playing
      if (mediaPlayer != null) {
        mediaPlayer.stop();
      }

      // Load the music file from resources
      String resource = AudioManager.class.getResource(resourcePath).toExternalForm();
      Media media = new Media(resource);
      mediaPlayer = new MediaPlayer(media);

      // Set to loop indefinitely
      mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
      mediaPlayer.setVolume(0.5); // Set volume to 50%

      mediaPlayer.play();
      currentTrack = resourcePath;
      System.out.println("Playing music: " + resourcePath);
    } catch (Exception e) {
      System.err.println("Error loading music: " + e.getMessage());
      e.printStackTrace();
    }
  }

  /**
   * Stop the current music
   */
  public void stopMusic() {
    if (mediaPlayer != null) {
      mediaPlayer.stop();
      System.out.println("Music stopped");
    }
  }

  /**
   * Pause the current music
   */
  public void pauseMusic() {
    if (mediaPlayer != null) {
      mediaPlayer.pause();
      System.out.println("Music paused");
    }
  }

  /**
   * Resume the current music
   */
  public void resumeMusic() {
    if (mediaPlayer != null) {
      mediaPlayer.play();
      System.out.println("Music resumed");
    }
  }

  /**
   * Set volume (0.0 to 1.0)
   */
  public void setVolume(double volume) {
    if (mediaPlayer != null) {
      mediaPlayer.setVolume(Math.max(0, Math.min(1, volume)));
    }
  }

  /**
   * Check if music is currently playing
   */
  public boolean isPlaying() {
    return mediaPlayer != null && mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING;
  }

  /**
   * Get the current music track
   */
  public String getCurrentTrack() {
    return currentTrack;
  }
}
