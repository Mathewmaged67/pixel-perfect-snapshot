import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

public class SignupController extends BaseController implements Initializable {

  @FXML private StackPane root;
  @FXML private Region overlay;
  @FXML private TextField emailField;
  @FXML private TextField usernameField;
  @FXML private PasswordField passwordField;
  @FXML private PasswordField confirmPasswordField;
  @FXML private Label errorLabel;
  @FXML private Button signupButton;
  @FXML private Button loginButton;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    // Enable signup on Enter key in confirm password field
    confirmPasswordField.setOnAction(e -> onSignup());
    
    // Clear error message when user starts typing
    emailField.textProperty().addListener((obs, old, newVal) -> clearError());
    usernameField.textProperty().addListener((obs, old, newVal) -> clearError());
    passwordField.textProperty().addListener((obs, old, newVal) -> clearError());
    confirmPasswordField.textProperty().addListener((obs, old, newVal) -> clearError());

    updateMusicButton();
  }

  @FXML
  private void onSignup() {
    String email = emailField.getText().trim();
    String username = usernameField.getText().trim();
    String password = passwordField.getText();
    String confirmPassword = confirmPasswordField.getText();

    // Validate all fields are filled
    if (email.isEmpty()) {
      showError("Please enter your email");
      return;
    }

    if (username.isEmpty()) {
      showError("Please enter a username");
      return;
    }

    if (password.isEmpty()) {
      showError("Please enter a password");
      return;
    }

    if (confirmPassword.isEmpty()) {
      showError("Please confirm your password");
      return;
    }

    // Validate email format (basic check)
    if (!email.contains("@")) {
      showError("Please enter a valid email address");
      return;
    }

    // Validate username length
    if (username.length() < 3) {
      showError("Username must be at least 3 characters");
      return;
    }

    // Validate password length
    if (password.length() < 4) {
      showError("Password must be at least 4 characters");
      return;
    }

    // Validate passwords match
    if (!password.equals(confirmPassword)) {
      showError("Passwords do not match");
      return;
    }

    // TODO: Replace with actual account creation logic
    // For now, create account and navigate to login
    if (createAccount(email, username, password)) {
      router.goTo(Route.LOGIN);
    } else {
      showError("Failed to create account. Email or username may already exist.");
    }
  }

  @FXML
  private void onLogin() {
    router.goTo(Route.LOGIN);
  }

  private boolean createAccount(String email, String username, String password) {
    // TODO: Implement actual account creation with backend/database
    // This is a placeholder that accepts any valid input
    // Replace with proper validation and storage
    System.out.println("Creating account: " + email + " / " + username);
    return true;
  }

  @FXML
  private void onToggleMusic() {
    AudioManager audio = AudioManager.getInstance();
    if (audio.isPlaying()) {
      audio.pauseMusic();
      musicButton.setText("♫ Music Off");
    } else {
      if (audio.getCurrentTrack() != null) {
        audio.resumeMusic();
      } else {
        audio.playLoopingMusic("/music/background-music.mp3");
      }
      musicButton.setText("♫ Music On");
    }
  }

  private void updateMusicButton() {
    if (musicButton == null) return;
    if (AudioManager.getInstance().isPlaying()) {
      musicButton.setText("♫ Music On");
    } else {
      musicButton.setText("♫ Music Off");
    }
  }

  private void showError(String message) {
    errorLabel.setText(message);
    errorLabel.setVisible(true);
  }

  private void clearError() {
    errorLabel.setText("");
    errorLabel.setVisible(false);
  }
}
