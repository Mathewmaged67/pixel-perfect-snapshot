import javafx.application.Application;
import javafx.scene.text.Font;
public class FontCheck {
  public static void main(String[] args) throws Exception {
    Font f = Font.loadFont(new java.io.File("..\\javafx\\fonts\\PixelSquareBold10.ttf").toURI().toString(), 12);
    System.out.println(f == null ? "null" : f.getName() + " | " + f.getFamily());
  }
}
