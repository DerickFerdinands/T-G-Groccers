package Controller;

import Controller.CRUD.VehicleCRUDController;
import Model.Item;
import Model.Vehicle;
import Util.ValidationUtil;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import org.controlsfx.control.Notifications;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;

public class VehicleManagementFormController {

    public JFXButton btnAddVehicle;
    public TextField txtVehicleNumber;
    public TextField txtStorageCapacity;
    public JFXComboBox cmbVehicleType;
    public AnchorPane VehicleMainPane;
    public JFXButton btnDeleteVehicle;
    public JFXButton btnUpdateVehicle;
    public TextField txtUPVNumber;
    public TextField txtUPVStorageCapacity;
    public TextField txtAddVehicleType;
    public JFXComboBox cmbUPVType;
    public JFXComboBox cmbAvailability;
    public AnchorPane VehicleViewScrollerPane;
    public JFXTextField txtSearchCustomer;
    ArrayList<Double> LayoutYValues = new ArrayList<>();

    public void addVehicleOnAction(ActionEvent actionEvent) {
        try {
            if (VehicleCRUDController.addVehicle(txtVehicleNumber.getText(), (String) cmbVehicleType.getValue(), Double.valueOf(txtStorageCapacity.getText()), true)) {
                Notifications notify = Notifications.create().title("Vehicle Added!").text("Vehicle Number : " + txtVehicleNumber.getText() + " Vehicle Type: " + cmbVehicleType.getValue()).darkStyle();
                notify.show();
                setAllVehicles(VehicleCRUDController.getAllVehicles());
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Something Went Wrong!..", ButtonType.OK);
                alert.initOwner(VehicleMainPane.getScene().getWindow());
                alert.show();
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.initOwner(VehicleMainPane.getScene().getWindow());
            alert.show();
        }
    }
    LinkedHashMap<TextField, Pattern> RegexPatterns = new LinkedHashMap();
    LinkedHashMap<TextField, Pattern> RegexUpdatePatterns = new LinkedHashMap();
    public void initialize() {
        btnAddVehicle.setDisable(true);
        btnUpdateVehicle.setDisable(true);
        btnDeleteVehicle.setDisable(true);
        try {
            cmbUPVType.getItems().addAll(VehicleCRUDController.getAllVehicleTypes());
            cmbVehicleType.getItems().addAll(VehicleCRUDController.getAllVehicleTypes());
            cmbAvailability.getItems().addAll(true, false);
            setAllVehicles(VehicleCRUDController.getAllVehicles());
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.initOwner(VehicleMainPane.getScene().getWindow());
            alert.show();
        }

        Pattern VehicleNumber= Pattern.compile("^[A-z0-9]{1,3}(-)[0-9]{4}$");
        Pattern storageCapacity= Pattern.compile("^[0-9]+((.)[0-9]{1,3})?$");

        RegexPatterns.put(txtVehicleNumber,VehicleNumber);
        RegexPatterns.put(txtStorageCapacity,storageCapacity);

        RegexUpdatePatterns.put(txtUPVNumber,VehicleNumber);
        RegexUpdatePatterns.put(txtUPVStorageCapacity,storageCapacity);

        cmbVehicleType.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
         ValidationUtil.Validate(RegexPatterns,btnAddVehicle);
        });

        cmbUPVType.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(cmbAvailability.getValue()!=null){
                ValidationUtil.Validate(RegexUpdatePatterns,btnUpdateVehicle);
            }
            if(btnUpdateVehicle.isDisable()){
                btnDeleteVehicle.setDisable(true);
            }else{
                btnDeleteVehicle.setDisable(false);
            }
        });

        cmbAvailability.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            if(cmbUPVType.getValue()!=null){
                ValidationUtil.Validate(RegexUpdatePatterns,btnUpdateVehicle);
            }
            if(btnUpdateVehicle.isDisable()){
                btnDeleteVehicle.setDisable(true);
            }else{
                btnDeleteVehicle.setDisable(false);
            }
        }));
    }

    public void deleteVehicleOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        try {
            if (VehicleCRUDController.deleteVehicle(txtUPVNumber.getText())) {
                Notifications notify = Notifications.create().title("Vehicle Deleted!").text("Vehicle Number : " + txtUPVNumber.getText()).darkStyle();
                notify.show();

            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Something Went Wrong!..", ButtonType.OK);
                alert.initOwner(VehicleMainPane.getScene().getWindow());
                alert.show();
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.initOwner(VehicleMainPane.getScene().getWindow());
            alert.show();
        }
        setAllVehicles(VehicleCRUDController.getAllVehicles());
    }

    public void updateVehicleOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        try {
            if (VehicleCRUDController.updateVehicle(txtUPVNumber.getText(), (String) cmbUPVType.getValue(), Double.valueOf(txtUPVStorageCapacity.getText()), (Boolean) cmbAvailability.getValue())) {
                Notifications notify = Notifications.create().title("Vehicle Updated!").text("Vehicle Number : " + txtUPVNumber.getText()).darkStyle();
                notify.show();

            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Something Went Wrong!..", ButtonType.OK);
                alert.initOwner(VehicleMainPane.getScene().getWindow());
                alert.show();
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.initOwner(VehicleMainPane.getScene().getWindow());
            alert.show();
        }
        setAllVehicles(VehicleCRUDController.getAllVehicles());
    }

    public void searchVehicleOnAction(ActionEvent actionEvent) {
        try {
            Vehicle v = VehicleCRUDController.getVehicle(txtUPVNumber.getText());
            if (v != null) {
                txtUPVNumber.setText(v.getVehicleNumber());
                cmbUPVType.getSelectionModel().select(v.getVehicleType());
                txtUPVStorageCapacity.setText(String.valueOf(v.getStorageCapacity()));
                cmbAvailability.getSelectionModel().select(v.isAvailability());
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.initOwner(VehicleMainPane.getScene().getWindow());
            alert.show();
        }
    }

    public void addVehicleTypeOnAction(ActionEvent actionEvent) {
     addVehicle();
    }

    public void addVehicle(){
        String vehicleType = txtAddVehicleType.getText();
        try {
            if (VehicleCRUDController.addVehicleType(vehicleType)) {
                Notifications notify = Notifications.create().title("Vehicle Type Added!").darkStyle();
                notify.show();
                cmbVehicleType.getItems().add(vehicleType);
                cmbUPVType.getItems().add(vehicleType);
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Something Went Wrong!..", ButtonType.OK);
                alert.initOwner(VehicleMainPane.getScene().getWindow());
                alert.show();
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.initOwner(VehicleMainPane.getScene().getWindow());
            alert.show();
        }
    }

    private void setAllVehicles(ArrayList<Vehicle> vehicleList) {
        VehicleViewScrollerPane.getChildren().clear();


        for (Vehicle V : vehicleList) {
            setViewItemPane(V.getVehicleNumber(),V.getVehicleType(),String.valueOf(V.getStorageCapacity()),String.valueOf(V.isAvailability()));
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
    public void setViewItemPane(String vehicleNumber, String vehicleType, String storageCapacity, String availability) {
        //    Checks YPosition For Customer View Pane Placement.
        double layoutY;
        if (LayoutYValues.isEmpty()) {
            layoutY = 4;
            LayoutYValues.add(layoutY);
        } else {
            double size = LayoutYValues.get((LayoutYValues.size()) - 1) + 70;
            if (VehicleViewScrollerPane.getHeight() < size) {
                VehicleViewScrollerPane.setPrefHeight(size + 100);
            }
            layoutY = size;
            LayoutYValues.add(layoutY);
        }
        AnchorPane pane = new AnchorPane();
        pane.setPrefSize(1150, 68);

        pane.setLayoutX(15);
        pane.setLayoutY(layoutY);

//      Created A Rectangle Because AnchorPane returns void and Cannot Change Background Color.

        Rectangle rec = new Rectangle();
        rec.setHeight(68);
        rec.setWidth(1150);
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


        pane.getChildren().add(getHBox(201, 50, 25, vehicleNumber));
        pane.getChildren().add(getLine(-127, -15, -127, 24, 400, 29));

        pane.getChildren().add(getHBox(201, 320, 25, vehicleType));
        pane.getChildren().add(getLine(-127, -15, -127, 24, 700, 29));

        pane.getChildren().add(getHBox(201, 630, 25, storageCapacity));
        pane.getChildren().add(getLine(-127, -15, -127, 24, 1000, 29));

        pane.getChildren().add(getHBox(201, 900, 25, availability));



        VehicleViewScrollerPane.getChildren().add(pane);

//      Set Customer Details On Update/ Delete Section On Click On Pane.
        pane.setOnMouseClicked(event -> {
            txtUPVNumber.setText(vehicleNumber);
            cmbUPVType.getSelectionModel().select(vehicleType);
            txtUPVStorageCapacity.setText(storageCapacity);
            cmbAvailability.getSelectionModel().select(Boolean.valueOf(availability));

        });

    }

    public void SearchCustomerVIewoNAction(ActionEvent actionEvent) {
        String search = txtSearchCustomer.getText().toUpperCase();
        ArrayList<Vehicle> addToView = new ArrayList<>();
        try {
            ArrayList<Vehicle> VList = VehicleCRUDController.getAllVehicles();
            for (Vehicle v : VList){
                if(v.getVehicleNumber().toUpperCase().contains(search)||v.getVehicleType().toUpperCase().contains(search)){
                    addToView.add(v);
                }
            }
            if(addToView.isEmpty()){
                setAllVehicles(VList);
            }else{
                setAllVehicles(addToView);
            }
        } catch (SQLException | ClassNotFoundException e) {
        e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.initOwner(VehicleMainPane.getScene().getWindow());
            alert.show();
        }
    }

    public void validateAddFields(KeyEvent keyEvent) {
        Object o = ValidationUtil.Validate(RegexPatterns,btnAddVehicle);
        if(keyEvent.getCode()== KeyCode.ENTER){
            if(o instanceof TextField){
                TextField field = (TextField) o;
                field.requestFocus();
            }else if(cmbVehicleType.getValue()!=null){
                addVehicle();
            }else{
                cmbVehicleType.show();
            }
        }
    }


    public void validateUpdateFields(KeyEvent keyEvent) {
        Object o = ValidationUtil.Validate(RegexUpdatePatterns,btnUpdateVehicle);

        if(cmbUPVType.getValue()!=null&&cmbAvailability!=null){
            btnUpdateVehicle.setDisable(false);
            btnDeleteVehicle.setDisable(false);
        }else{
            btnUpdateVehicle.setDisable(true);
            btnDeleteVehicle.setDisable(true);
        }
        if(btnUpdateVehicle.isDisable()){
            btnDeleteVehicle.setDisable(true);
        }else{
            btnDeleteVehicle.setDisable(false);
        }
    }
}
