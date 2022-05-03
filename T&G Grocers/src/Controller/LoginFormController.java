package Controller;

import Controller.CRUD.AdminCRUDController;
import Controller.CRUD.CustomerCRUDController;
import Controller.CRUD.EmployeeCRUDController;
import Controller.CRUD.UserCRUDController;
import Model.Admin;
import View.TM.UserTM;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.controlsfx.control.Notifications;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class LoginFormController {
    public AnchorPane MainPane;
    public TextField txtUserName;
    public TextField txtPassword;

    public void loginOnAction(ActionEvent actionEvent) throws IOException {
        boolean matched=false;
        try {
            ArrayList<UserTM> userList = UserCRUDController.getAllUserDetails();
            for (UserTM tm : userList) {
                if (tm.getUserName().equals(txtUserName.getText()) && tm.getPassword().equals(txtPassword.getText())) {
                    matched=true;
                    MainPane.getChildren().clear();
                    Stage stage = (Stage) MainPane.getScene().getWindow();
                    Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/View/UserDashBoardForm.fxml")));
                    scene.getStylesheets().add(getClass().getResource("/View/CSS/Text.css").toExternalForm());
                    stage.setScene(scene);
                    stage.centerOnScreen();
                    stage.setFullScreen(true);
                    stage.show();
                    Notifications notify = Notifications.create().title("Welcome "+tm.getName());
                    notify.show();

                }
            }
            if(matched==false){
                Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid UserName Or Password!", ButtonType.OK);
                alert.initOwner(MainPane.getScene().getWindow());
                alert.show();}
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.initOwner(MainPane.getScene().getWindow());
            alert.show();
        }
    }

    public void LogInAsAdminOnAction(ActionEvent actionEvent) throws IOException {
        try {

            ArrayList<Admin> adminList = AdminCRUDController.getALlAdmins();
            boolean matched=false;
            for (Admin a : adminList) {
                if (a.getUser_name().equals(txtUserName.getText()) && a.getPassword().equals(txtPassword.getText())) {
                    matched=true;
                    MainPane.getChildren().clear();
                    Stage stage = (Stage) MainPane.getScene().getWindow();
                    Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/View/AdminDashBoardForm.fxml")));
                    scene.getStylesheets().add(getClass().getResource("/View/CSS/Text.css").toExternalForm());
                    stage.setScene(scene);
                    stage.centerOnScreen();
                    stage.setFullScreen(true);
                    stage.show();
                    Notifications notify = Notifications.create().title("Welcome "+a.getName());
                    notify.show();
                }

            }
            if(matched==false){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid UserName Or Password!", ButtonType.OK);
            alert.initOwner(MainPane.getScene().getWindow());
            alert.show();}

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.initOwner(MainPane.getScene().getWindow());
            alert.show();

        }
    }

    public void nextFieldOnAction(ActionEvent actionEvent) {
        txtPassword.requestFocus();
    }

    public void initialize(){
        txtUserName.requestFocus();
        txtUserName.requestFocus();
    }
}
