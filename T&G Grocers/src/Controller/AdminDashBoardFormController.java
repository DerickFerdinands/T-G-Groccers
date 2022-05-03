package Controller;

import Controller.CRUD.AdminCRUDController;
import Controller.CRUD.ItemCRUDController;
import Controller.CRUD.OrderCrudController;
import Controller.CRUD.UserCRUDController;
import Model.Admin;
import Model.Item;
import Model.Order;
import View.TM.UserTM;
import com.jfoenix.controls.JFXTextField;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class AdminDashBoardFormController {
    public AnchorPane OptionPane;
    public AnchorPane leftOptionPane;
    public AnchorPane navigationPane;
    public AnchorPane MainContext;
    public AnchorPane UserLoginContext;
    public AnchorPane userContext;
    public AnchorPane SwitchAdminPane;
    public Label lblDate;
    public Label lblTime;
    public BarChart productSalesChart;
    public LineChart salesLineChart;
    public JFXTextField txtSwitchUserName;
    public JFXTextField txtSwitchPassword;
    public JFXTextField txtUserName;
    public JFXTextField txtPassword;

    public void initialize() {
        setDateAndTime();
        try {
            XYChart.Series chart = new XYChart.Series<>();
            ArrayList<Item> DistinctItems = ItemCRUDController.getDistinctItemSales();
            chart.getData().add(new XYChart.Data("Derick", 10));

            for (Item i : DistinctItems) {
                chart.getData().add(new XYChart.Data(i.getDescription(), i.getBuyingPrice()));
            }
            productSalesChart.getData().add(chart);

            XYChart.Series chart2 = new XYChart.Series<>();
            ArrayList<Order> list = OrderCrudController.getDistinctSalesDates();
            for (Order o : list) {
                chart2.getData().add(new XYChart.Data(String.valueOf(o.getDate()), o.getTotalPrice()));
            }
            salesLineChart.getData().add(chart2);

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    private void setDateAndTime() {
        lblDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));

        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            LocalTime currentTime = LocalTime.now();
            lblTime.setText(currentTime.getHour() + ":" + currentTime.getMinute() + ":" + currentTime.getSecond());
        }),
                new KeyFrame(Duration.seconds(1)));
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();

    }

    public void hideOnClick(MouseEvent mouseEvent) {
        userContext.setVisible(false);
        OptionPane.setVisible(false);

    }

    public void manageVehicleOnAction(ActionEvent actionEvent) throws IOException {
        setUI("VehicleManagementForm");
    }

    public void manageDriverOnAction(ActionEvent actionEvent) throws IOException {
        setUI("DriverManagementForm");
    }

    public void manageExpenseOnAction(ActionEvent actionEvent) throws IOException {
        setUI("ExpenseManagementForm");
    }

    public void manageReportsOnAction(ActionEvent actionEvent) throws IOException {
        setUI("ReportManagementForm");
    }

    public void manageEmployeeOnAction(ActionEvent actionEvent) throws IOException {
        setUI("EmployeeManagementForm");
    }

    public void goToDashBoardOnAction(ActionEvent actionEvent) throws IOException {
        MainContext.getChildren().clear();
        MainContext.getChildren().add(FXMLLoader.load(getClass().getResource("/View/AdminDashBoardForm.fxml")));
    }

    public void showOptionsOnClcick(MouseEvent mouseEvent) {
        userContext.setVisible(true);
    }

    public void ShowPaneOnDrag(MouseEvent mouseEvent) {
        OptionPane.setVisible(true);
    }

    public void setUI(String URI) throws IOException {
        navigationPane.getChildren().clear();
        navigationPane.getChildren().add(FXMLLoader.load(getClass().getResource("/View/" + URI + ".fxml")));

    }

    public void manageUsersOnAction(ActionEvent actionEvent) throws IOException {
        setUI("UserManagementForm");
    }

    public void manageAdminOnAction(ActionEvent actionEvent) throws IOException {
        setUI("AdminManagementForm");
    }

    public void viewStatsOnAction(ActionEvent actionEvent) throws IOException {
        setUI("ViewStatsForm");
    }


    public void showLoginPaneOnAction(ActionEvent actionEvent) {
        UserLoginContext.setVisible(true);
    }

    public void exitApplicationOnAction(ActionEvent actionEvent) {
        System.exit(0);
    }

    public void hideUserLoginContextOnAction(MouseEvent mouseEvent) {
        UserLoginContext.setVisible(false);
    }

    public void logInAsUserOnAction(ActionEvent actionEvent) throws IOException {
        boolean mathced=false;
        try {
            ArrayList<UserTM> userList = UserCRUDController.getAllUserDetails();
            for (UserTM tm : userList) {
                if (tm.getUserName().equals(txtUserName.getText()) && tm.getPassword().equals(txtPassword.getText())) {
                    mathced=true;
                    MainContext.getChildren().clear();
                    MainContext.getChildren().add(FXMLLoader.load(getClass().getResource("/View/UserDashBoardForm.fxml")));
                    Notifications notify = Notifications.create().title("Welcome "+tm.getName());
                    notify.show();
                }
            }
            if(mathced==false){
                Alert alert = new Alert(Alert.AlertType.ERROR,"Invalid Username Or Password!", ButtonType.OK);
                alert.initOwner(MainContext.getScene().getWindow());
                alert.show();
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void singOutOnAction(ActionEvent actionEvent) throws IOException {
        MainContext.getChildren().clear();
        Stage stage = (Stage) MainContext.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/View/LoginForm.fxml"))));
        stage.centerOnScreen();
    }

    public void SwitchAdminOnAction(ActionEvent actionEvent) {
        SwitchAdminPane.setVisible(true);
    }

    public void hideSwitchAdminPane(MouseEvent mouseEvent) {
        SwitchAdminPane.setVisible(false);
    }

    public void SwitchAdminDetailsOnAction(ActionEvent actionEvent) {
        boolean matched=false;
        try {
            ArrayList<Admin> adminList = AdminCRUDController.getALlAdmins();
            for (Admin admin : adminList) {
                if (admin.getUser_name().equals(txtSwitchUserName.getText()) && admin.getPassword().equals(txtSwitchPassword.getText())) {
                    matched=true;
                    Notifications notify = Notifications.create().title("Welcome " + admin.getName());
                    notify.show();
                }
            }
            if(matched==false){
                Alert alert = new Alert(Alert.AlertType.ERROR,"Invalid Username Or Password!", ButtonType.OK);
                alert.initOwner(MainContext.getScene().getWindow());
                alert.show();
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void nextFieldOnAction(ActionEvent actionEvent) {
        txtPassword.requestFocus();
    }

    public void nextAdminFIeldOnAction(ActionEvent actionEvent) {
        txtSwitchPassword.requestFocus();
    }

    public void toDiscountsOnAction(ActionEvent actionEvent) throws IOException {
        setUI("ViewDiscountsForm");
    }

    final KeyCombination DashBoard = new KeyCodeCombination(KeyCode.DIGIT1,KeyCombination.CONTROL_DOWN);
    final KeyCombination Employee = new KeyCodeCombination(KeyCode.DIGIT2,KeyCombination.CONTROL_DOWN);
    final KeyCombination Vehicle = new KeyCodeCombination(KeyCode.DIGIT3,KeyCombination.CONTROL_DOWN);
    final KeyCombination Driver = new KeyCodeCombination(KeyCode.DIGIT4,KeyCombination.CONTROL_DOWN);
    final KeyCombination Expenses = new KeyCodeCombination(KeyCode.DIGIT5,KeyCombination.CONTROL_DOWN);
    final KeyCombination Reports = new KeyCodeCombination(KeyCode.DIGIT6,KeyCombination.CONTROL_DOWN);
    final KeyCombination users = new KeyCodeCombination(KeyCode.DIGIT7,KeyCombination.CONTROL_DOWN);
    final KeyCombination Admin = new KeyCodeCombination(KeyCode.DIGIT8,KeyCombination.CONTROL_DOWN);
    final KeyCombination Stats = new KeyCodeCombination(KeyCode.DIGIT9,KeyCombination.CONTROL_DOWN);
    final KeyCombination discounts = new KeyCodeCombination(KeyCode.DIGIT0,KeyCombination.CONTROL_DOWN);
    public void checkShortcutKeys(KeyEvent keyEvent) throws IOException {
    if(DashBoard.match(keyEvent)){
        MainContext.getChildren().clear();
        MainContext.getChildren().add(FXMLLoader.load(getClass().getResource("/View/AdminDashBoardForm.fxml")));
    }else if(Employee.match(keyEvent)){
        setUI("EmployeeManagementForm");
    }else if(Vehicle.match(keyEvent)){
        setUI("VehicleManagementForm");
    }else if(Driver.match(keyEvent)){
        setUI("DriverManagementForm");
    }else if(Expenses.match(keyEvent)){
        setUI("ExpenseManagementForm");
    }else if(Reports.match(keyEvent)){
        setUI("ReportManagementForm");
    }else if(users.match(keyEvent)){
        setUI("UserManagementForm");
    }else if(Admin.match(keyEvent)){
        setUI("AdminManagementForm");
    }else if(Stats.match(keyEvent)){
        setUI("ViewStatsForm");
    }else if(discounts.match(keyEvent)){
        setUI("ViewDiscountsForm");
    }
    }
}
