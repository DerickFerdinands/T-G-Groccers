package Controller;

import Database.DBConnection;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;

public class SplashScreenFormController {

    public AnchorPane MainPane;

    public void initialize(){
        Timeline clock = new Timeline(new KeyFrame(Duration.seconds(2), e -> {
            Stage stage = (Stage) MainPane.getScene().getWindow();


            Scene scene = null;
            try {
                scene = new Scene(FXMLLoader.load(getClass().getResource("/View/LoginForm.fxml")));
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            Stage newStage = new Stage();
            newStage.setScene(scene);
            newStage.centerOnScreen();
            newStage.show();
            try {
                Connection connection = DBConnection.getInstance().getConnection();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException classNotFoundException) {
                classNotFoundException.printStackTrace();
            }
            newStage.getIcons().add(new Image("/Assets/shopping-cart_icon-icons.com_60593.png"));
            newStage.setTitle("T&G Grocers Supermarket System");
            stage.close();
        }),
                new KeyFrame(Duration.seconds(0)));
        clock.setCycleCount(1);
        clock.play();
    }
}
