package Controller;

import Controller.CRUD.DriverCRUDController;
import Model.Customer;
import Model.Driver;
import Util.ValidationUtil;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import org.controlsfx.control.Notifications;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;

public class DriverManagementFormController {
    public AnchorPane DriverMainPane;
    public JFXButton btnAddEmployee;
    public TextField txtDriverNIC;
    public JFXDatePicker dtpckrDriverDOB;
    public TextField txtDriverMobile;
    public TextField txtDriverLicenseNumber;
    public TextField txtDriverName;
    public TextField txtDriverAddress;
    public TextField txtUPDAddress;
    public TextField txtUPDName;
    public TextField txtUPDEmail;
    public TextField txtUPDMobile;
    public JFXDatePicker dtpckrUPDDOB;
    public TextField txtUPDNIC;
    public JFXButton btnDeleteEmployee;
    public JFXButton btnUpdateEmployee;
    public JFXComboBox cmbAvailability;
    public AnchorPane DriverScrollerAnchorPane;
    public JFXTextField txtSearchDriver;
    public JFXButton btnAddDriver;
    public JFXButton btnDeleteDriver;
    public JFXButton btnUpdateDriver;

    public void addEmployeeonACtion(ActionEvent actionEvent) {
       addDriver();
    }

    public void addDriver(){
        try{
            if(DriverCRUDController.addDriver(txtDriverNIC.getText(),txtDriverName.getText(),dtpckrDriverDOB.getValue(),txtDriverLicenseNumber.getText(),txtDriverMobile.getText(),txtDriverAddress.getText(),true)){
                Notifications notify = Notifications.create().title("Driver Added Sucesfully!").text("Driver NIC: "+txtDriverNIC.getText()+" Driver Name: "+txtDriverName.getText()).darkStyle();
                notify.show();
            }else{
                Alert alert = new Alert(Alert.AlertType.WARNING,"OOps, Something Went Wrong!..", ButtonType.OK);
                alert.initOwner(DriverMainPane.getScene().getWindow());
                alert.show();
            }
        }catch(SQLException | ClassNotFoundException e){
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.WARNING,e.getMessage(), ButtonType.OK);
            alert.initOwner(DriverMainPane.getScene().getWindow());
            alert.show();
        }
        try {
            setAllCustomers(DriverCRUDController.getAllDrivers());
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.WARNING,e.getMessage(), ButtonType.OK);
            alert.initOwner(DriverMainPane.getScene().getWindow());
            alert.show();
        }
    }

    public void updateEmployeeOnAction(ActionEvent actionEvent) {
        try{
            if(DriverCRUDController.updateDriver(txtUPDNIC.getText(),txtUPDName.getText(),dtpckrUPDDOB.getValue(),txtUPDEmail.getText(),txtUPDMobile.getText(),txtUPDAddress.getText(),(Boolean)cmbAvailability.getValue())){
                Notifications notify = Notifications.create().title("Driver Updated Sucesfully!").text("Driver NIC: "+txtUPDNIC.getText()+" Driver Name: "+txtUPDName.getText()).darkStyle();
                notify.show();
            }else{
                Alert alert = new Alert(Alert.AlertType.WARNING,"OOps, Something Went Wrong!..", ButtonType.OK);
                alert.initOwner(DriverMainPane.getScene().getWindow());
                alert.show();
            }

        }catch(SQLException | ClassNotFoundException e ){
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.WARNING,e.getMessage(), ButtonType.OK);
            alert.initOwner(DriverMainPane.getScene().getWindow());
            alert.show();
        }
        try {
            setAllCustomers(DriverCRUDController.getAllDrivers());
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.WARNING,e.getMessage(), ButtonType.OK);
            alert.initOwner(DriverMainPane.getScene().getWindow());
            alert.show();
        }
    }

    public void deleteEmployeeOnAction(ActionEvent actionEvent) {
        try{
            if(DriverCRUDController.deleteDriver(txtUPDNIC.getText())){
                Notifications notify = Notifications.create().title("Driver Deleted Sucesfully!").text("Driver NIC: "+txtUPDNIC.getText()+" Driver Name: "+txtUPDName.getText()).darkStyle();
                notify.show();
            }else{
                Alert alert = new Alert(Alert.AlertType.WARNING,"OOps, Something Went Wrong!..", ButtonType.OK);
                alert.initOwner(DriverMainPane.getScene().getWindow());
                alert.show();
            }
        }catch(SQLException | ClassNotFoundException e){
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.WARNING,e.getMessage(), ButtonType.OK);
            alert.initOwner(DriverMainPane.getScene().getWindow());
            alert.show();
        }
        try {
            setAllCustomers(DriverCRUDController.getAllDrivers());
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.WARNING,e.getMessage(), ButtonType.OK);
            alert.initOwner(DriverMainPane.getScene().getWindow());
            alert.show();
        }
    }

    public void searchOnAction(ActionEvent actionEvent) {

      setSearchedCustomer();
    }

    public void setSearchedCustomer(){
        try {
            Driver d = DriverCRUDController.getDriver(txtUPDNIC.getText());
            if(d!=null){
                txtUPDName.setText(d.getName());
                dtpckrUPDDOB.setValue(d.getDob());
                txtUPDAddress.setText(d.getAddress());
                txtUPDMobile.setText(d.getContact());
                txtUPDEmail.setText(d.getLicense_number());
                cmbAvailability.getSelectionModel().select(d.isAvailability());
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.WARNING,e.getMessage(), ButtonType.OK);
            alert.initOwner(DriverMainPane.getScene().getWindow());
            alert.show();
        }
    }
    LinkedHashMap<TextField, Pattern> RegexPatterns = new LinkedHashMap<>();
    LinkedHashMap<TextField, Pattern> RegexUpdatePatterns = new LinkedHashMap<>();
    public void initialize(){
        btnAddDriver.setDisable(true);
        btnUpdateDriver.setDisable(true);
        btnDeleteDriver.setDisable(true);
        cmbAvailability.getItems().addAll(true, false);
        try {
            setAllCustomers(DriverCRUDController.getAllDrivers());
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.WARNING,e.getMessage(), ButtonType.OK);
            alert.initOwner(DriverMainPane.getScene().getWindow());
            alert.show();
        }
        Pattern nicPattern = Pattern.compile("^[0-9]{10,15}(V)?$");
        Pattern namePattern = Pattern.compile("^[A-z ]+$");
        Pattern addressPattern = Pattern.compile("^[A-z0-9 ,/]+$");
        Pattern mobilePattern = Pattern.compile("^[0-9]{10}$");
        Pattern licenseNumber = Pattern.compile("^[A-z0-9]{8,20}$");

        RegexPatterns.put(txtDriverNIC,nicPattern);
        RegexPatterns.put(txtDriverName,namePattern);
        RegexPatterns.put(txtDriverAddress,addressPattern);
        RegexPatterns.put(txtDriverMobile,mobilePattern);
        RegexPatterns.put(txtDriverLicenseNumber,licenseNumber);

        RegexUpdatePatterns.put(txtUPDNIC,nicPattern);
        RegexUpdatePatterns.put(txtUPDName,namePattern);
        RegexUpdatePatterns.put(txtUPDAddress,addressPattern);
        RegexUpdatePatterns.put(txtUPDMobile,mobilePattern);
        RegexUpdatePatterns.put(txtUPDEmail,licenseNumber);

        dtpckrDriverDOB.valueProperty().addListener((observable, oldValue, newValue) -> {
            LocalDate temp =(LocalDate) newValue;
            if(temp.isAfter(LocalDate.now())||temp.equals(LocalDate.now())){
                btnAddDriver.setDisable(true);
                Alert alert = new Alert(Alert.AlertType.WARNING,"Invalid Date Of Birth", ButtonType.OK);
                alert.initOwner(DriverMainPane.getScene().getWindow());
                alert.show();
            }else{
            ValidationUtil.Validate(RegexPatterns,btnAddDriver);}
        });

        dtpckrUPDDOB.valueProperty().addListener((observable, oldValue, newValue) -> {
            LocalDate temp =(LocalDate) newValue;
            if(temp.isAfter(LocalDate.now())||temp.equals(LocalDate.now())){
                btnUpdateDriver.setDisable(true);
                btnDeleteDriver.setDisable(true);
                Alert alert = new Alert(Alert.AlertType.WARNING,"Invalid Date Of Birth", ButtonType.OK);
                alert.initOwner(DriverMainPane.getScene().getWindow());
                alert.show();
            }else{
                ValidationUtil.Validate(RegexUpdatePatterns,btnUpdateDriver);}
            if(btnUpdateDriver.isDisable()){
                btnDeleteDriver.setDisable(true);
            }else{
                btnDeleteDriver.setDisable(false);
            }
        });

        cmbAvailability.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(dtpckrUPDDOB.getValue()!=null){
                btnUpdateDriver.setDisable(false);
                btnDeleteDriver.setDisable(false);
                ValidationUtil.Validate(RegexUpdatePatterns,btnUpdateDriver);
            }else{
                btnUpdateDriver.setDisable(true);
                btnDeleteDriver.setDisable(true);
            }
            if(btnUpdateDriver.isDisable()){
                btnDeleteDriver.setDisable(true);
            }else{
                btnDeleteDriver.setDisable(false);
            }
        });

    }
    ArrayList<Double> LayoutYValues = new ArrayList<>();
    private void setAllCustomers(ArrayList<Driver> driverList) {
        DriverScrollerAnchorPane.getChildren().clear();

//Sets View For Each Customer.
        for (Driver d : driverList) {
            setViewCustomerPane(d.getNic(),d.getName(),String.valueOf(d.getDob()),d.getLicense_number(),d.getContact(),d.getAddress(),String.valueOf(d.isAvailability()));
        }
        LayoutYValues.clear();
    }

    //Return Customized HBox.
    public HBox getHBox(int width, int layoutX, int layoutY, String text) {
        HBox box = new HBox();
        box.setAlignment(Pos.CENTER);
        box.setPrefWidth(width);
        box.setLayoutX(layoutX);
        box.setLayoutY(layoutY);
        Label label = new Label(text);
        label.setTextFill(Color.color(0.00, 0.45, 0.40));
        label.setStyle("-fx-font-size: 15");
        box.getChildren().add(label);

        return box;
    }

    //    Returns Customized Line.
    public Line getLine(int startX, int startY, int endX, int endY, int layoutX, int layoutY) {
        Line line = new Line();
        line.setStartX(startX);
        line.setStartY(startY);
        line.setEndX(endX);
        line.setEndY(endY);
        line.setLayoutX(layoutX);
        line.setLayoutY(layoutY);

        return line;
    }

    //    Creates Separate AnchorPanes For Customers to View.
    public void setViewCustomerPane(String nic, String name, String dob, String license_Number, String contact, String address, String availability) {
//        Checks YPosition For Customer View Pane Placement.
        double layoutY;
        if (LayoutYValues.isEmpty()) {
            layoutY = 4;
            LayoutYValues.add(layoutY);
        } else {
            double size = LayoutYValues.get((LayoutYValues.size()) - 1) + 70;
            if (DriverScrollerAnchorPane.getHeight() < size) {
                DriverScrollerAnchorPane.setPrefHeight(size + 100);
            }
            layoutY = size;
            LayoutYValues.add(layoutY);
        }
        AnchorPane pane = new AnchorPane();
        pane.setPrefSize(942, 68);

        pane.setLayoutX(4);
        pane.setLayoutY(layoutY);

//      Created A Rectangle Because AnchorPane returns void and Cannot Change Background Color.

        Rectangle rec = new Rectangle();
        rec.setHeight(68);
        rec.setWidth(942);
        rec.setStroke(Color.WHITE);
        rec.setStyle("-fx-border-color: Black");
        rec.setFill(Color.color(1.00, 1.00, 1.00));

        pane.getChildren().add(rec);

//        Gets Customized HBox and Line.
        /*
         * //HBox//
         * Include HBox Width
         * Include HBox LayoutX
         * Include HBox LayoutY
         * Include Text Required
         * //Line//
         * Include StartX
         * Include StartY
         * Include EndX
         * Include EndY
         * Include LayoutX
         * Include LayoutY
         * */
        pane.getChildren().add(getHBox(201, 10, 25, nic));
        pane.getChildren().add(getLine(-127, -15, -127, 24, 300, 29));

        pane.getChildren().add(getHBox(201, 167, 25, name));
        pane.getChildren().add(getLine(-127, -15, -127, 24, 490, 29));

        pane.getChildren().add(getHBox(201, 315, 25, dob));
        pane.getChildren().add(getLine(-127, -15, -127, 24, 600, 29));

        pane.getChildren().add(getHBox(201, 454, 25, license_Number));
        pane.getChildren().add(getLine(-127, -15, -127, 24, 770, 29));

        pane.getChildren().add(getHBox(201, 605, 25, contact));
        pane.getChildren().add(getLine(-127, -15, -127, 24, 875, 29));

        pane.getChildren().add(getHBox(201, 702, 25, address));
        pane.getChildren().add(getLine(-127, -15, -127, 24, 979, 29));

        pane.getChildren().add(getHBox(201, 790, 25, availability));

        DriverScrollerAnchorPane.getChildren().add(pane);

//        Set Customer Details On Update/ Delete Section On Click On Pane.
        pane.setOnMouseClicked(event -> {
            txtUPDNIC.setText(nic);
            setSearchedCustomer();
            ValidationUtil.Validate(RegexUpdatePatterns,btnUpdateDriver);

        });

    }

    public void searchDriverOnAction(ActionEvent actionEvent) {
        String search = txtSearchDriver.getText().toUpperCase();
        try{
            ArrayList<Driver> driverList = DriverCRUDController.getAllDrivers();
            ArrayList<Driver> addToView = new ArrayList<>();

            for(Driver d : driverList){
                if(d.getNic().toUpperCase().contains(search)||d.getName().toUpperCase().contains(search)){
                    addToView.add(d);
                }
            }

            if(addToView.isEmpty()){
                setAllCustomers(driverList);
            }else{
                setAllCustomers(addToView);
            }

        }catch(SQLException | ClassNotFoundException e){
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.WARNING,e.getMessage(), ButtonType.OK);
            alert.initOwner(DriverMainPane.getScene().getWindow());
            alert.show();
        }

    }

    public void validateAddDriverFields(KeyEvent keyEvent) {
        Object o = ValidationUtil.Validate(RegexPatterns,btnAddDriver);
        if(keyEvent.getCode()== KeyCode.ENTER){
            if(o instanceof TextField){
                TextField field = (TextField) o;
                field.requestFocus();
            }else if(dtpckrDriverDOB.getValue()!=null){
                addDriver();
            }else{
                btnAddDriver.setDisable(true);
            }
        }
    }

    public void validateUpdateFields(KeyEvent keyEvent) {
        Object o = ValidationUtil.Validate(RegexUpdatePatterns,btnUpdateDriver);
        if(keyEvent.getCode()== KeyCode.ENTER){
            if(o instanceof TextField){
                TextField field = (TextField) o;
                field.requestFocus();
            }else if(dtpckrDriverDOB.getValue()!=null&&cmbAvailability.getValue()!=null){
                btnAddDriver.setDisable(false);
            }else{
                btnAddDriver.setDisable(true);
            }
        }
        if(btnUpdateDriver.isDisable()){
            btnDeleteDriver.setDisable(true);
        }else{
            btnDeleteDriver.setDisable(false);
        }
    }
}
