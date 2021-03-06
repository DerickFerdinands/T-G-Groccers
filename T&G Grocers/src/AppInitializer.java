import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class AppInitializer extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        primaryStage.setScene(new Scene(FXMLLoader.load(getClass().getResource("View/SplashScreenForm.fxml"))));
        primaryStage.setResizable(false);
        primaryStage.getIcons().add(new Image("/Assets/shopping-cart_icon-icons.com_60593.png"));
        primaryStage.setTitle("T&G Grocers Supermarket System");
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }
}
