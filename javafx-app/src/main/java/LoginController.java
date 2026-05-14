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

public class LoginController extends BaseController implements Initializable {

  @FXML private StackPane root;
  @FXML private Region overlay;
  @FXML private TextField emailField;
  @FXML private PasswordField passwordField;
  @FXML private Label errorLabel;
  @FXML private Button loginButton;
  @FXML private Button signupButton;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    // Enable login on Enter key in password field
    passwordField.setOnAction(e -> onLogin());
    
    // Clear error message when user starts typing
    emailField.textProperty().addListener((obs, old, newVal) -> clearError());
    passwordField.textProperty().addListener((obs, old, newVal) -> clearError());
  }

  @FXML
  private void onLogin() {
    String email = emailField.getText().trim();
    String password = passwordField.getText();

    // Basic validation
    if (email.isEmpty()) {
      showError("Please enter your email or username");
      return;
    }

    if (password.isEmpty()) {
      showError("Please enter your password");
      return;
    }

    // TODO: Replace with actual authentication logic
    // For now, accept any non-empty credentials as a placeholder
    if (isValidCredentials(email, password)) {
      // Start background music after successful login
      AudioManager.getInstance().playLoopingMusic("/music/background-music.mp3");
      
      // Navigate to onboarding after successful login
      router.goTo(Route.ONBOARDING);
    } else {
      showError("Invalid email or password");
    }
  }

  @FXML
  private void onSignup() {
    router.goTo(Route.SIGNUP);
  }

  private boolean isValidCredentials(String email, String password) {
    // TODO: Implement actual authentication with backend/database
    // This is a placeholder that accepts any credentials
    // Replace with proper validation
    return !email.isEmpty() && !password.isEmpty() && password.length() >= 4;
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
