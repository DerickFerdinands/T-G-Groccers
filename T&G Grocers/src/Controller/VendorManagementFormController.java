package Controller;

import Controller.CRUD.VendorCRUDController;
import Model.Item;
import Model.Vendor;
import Util.ValidationUtil;
import com.jfoenix.controls.JFXButton;
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
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;

public class VendorManagementFormController {

    public AnchorPane CustomerManagementForm;
    public JFXTextField txtSearchVendor;
    public TextField txtVendorID;
    public TextField txtNameOrCompany;
    public TextField txtEMail;
    public TextField txtMobile;
    public TextField txtExtraDetails;
    public TextField txtUPVID;
    public TextField txtUPVNameOrCompany;
    public TextField txtUPVEmail;
    public TextField txtUPVMobile;
    public TextField txtUPVDetails;
    public AnchorPane VendorScrollerAnchorPane;
    public AnchorPane MainVendorPane;
    public JFXButton btnAddVendor;
    public JFXButton btnDeleteVendor;
    public JFXButton btnUpdateVendor;
    ArrayList<Double> LayoutYValues = new ArrayList<>();
    LinkedHashMap<TextField, Pattern> RegexMap = new LinkedHashMap<>();
    LinkedHashMap<TextField, Pattern> RegexUpdateMap = new LinkedHashMap<>();

    public void initialize() {
        btnAddVendor.setDisable(true);
        btnUpdateVendor.setDisable(true);
        btnDeleteVendor.setDisable(true);
        try {
            setAllVendors(VendorCRUDController.getAllVendors());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Pattern vendorID = Pattern.compile("^(V00-)[0-9]{3}$");
        Pattern Name = Pattern.compile("^[A-z ]+$");
        Pattern Email = Pattern.compile("^[A-z_.]+(@)[A-z]+(.com)$");
        Pattern Mobile = Pattern.compile("^[+94]?[0-9]{10,11}$");
        Pattern details = Pattern.compile("^(.)+$");

        RegexMap.put(txtVendorID, vendorID);
        RegexMap.put(txtNameOrCompany, Name);
        RegexMap.put(txtEMail, Email);
        RegexMap.put(txtMobile, Mobile);
        RegexMap.put(txtExtraDetails, details);

        RegexUpdateMap.put(txtUPVID, vendorID);
        RegexUpdateMap.put(txtUPVNameOrCompany, Name);
        RegexUpdateMap.put(txtUPVEmail, Email);
        RegexUpdateMap.put(txtUPVMobile, Mobile);
        RegexUpdateMap.put(txtUPVDetails, details);
    }

    public void addVendorOnAction(ActionEvent actionEvent) {
        addVendor();
    }

    public void addVendor() {
        try {
            if (VendorCRUDController.addVendor(txtVendorID.getText(), txtNameOrCompany.getText(), txtEMail.getText(), txtMobile.getText(), txtExtraDetails.getText())) {
                setAllVendors(VendorCRUDController.getAllVendors());
                Notifications notification = Notifications.create().title("Vendor Added!").text("Name" + txtNameOrCompany.getText() + " ID: " + txtVendorID.getText()).graphic(new ImageView(new Image("/Assets/outline_storefront_black_24dp.png"))).darkStyle().hideAfter(Duration.seconds(3));
                notification.show();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Something Went Wrong!", ButtonType.OK);
                alert.initOwner(MainVendorPane.getScene().getWindow());
                alert.show();
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.initOwner(MainVendorPane.getScene().getWindow());
            alert.show();
        }
        clearAddVendorFields();
    }

    public void updateVendorOnAction(ActionEvent actionEvent) {
        try {
            if (VendorCRUDController.updateVendor(txtUPVID.getText(), txtUPVNameOrCompany.getText(), txtUPVMobile.getText(), txtUPVEmail.getText(), txtUPVDetails.getText())) {
                Notifications notification = Notifications.create().title("Vendor Updated!").text("Name" + txtUPVNameOrCompany.getText() + " ID: " + txtUPVID.getText()).graphic(new ImageView(new Image("/Assets/outline_storefront_black_24dp.png"))).darkStyle().hideAfter(Duration.seconds(3));
                notification.show();
                setAllVendors(VendorCRUDController.getAllVendors());
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Something Went Wrong!", ButtonType.OK);
                alert.initOwner(MainVendorPane.getScene().getWindow());
                alert.show();
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.initOwner(MainVendorPane.getScene().getWindow());
            alert.show();
        }
        clearUpdateVendorFields();

    }

    public void searchVendorOnAction(ActionEvent actionEvent) {
        String search = txtSearchVendor.getText();
        try {
            ArrayList<Vendor> addTOView = new ArrayList<>();
            ArrayList<Vendor> vendorList = VendorCRUDController.getAllVendors();
            for (Vendor v : vendorList) {
                if (v.getVendorID().toUpperCase().contains(search.toUpperCase()) || v.getNameOrCompany().toUpperCase().contains(search.toUpperCase())) {
                    addTOView.add(v);
                }
            }

            if (addTOView.isEmpty()) {
                setAllVendors(VendorCRUDController.getAllVendors());
            } else {
                setAllVendors(addTOView);
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.initOwner(MainVendorPane.getScene().getWindow());
            alert.show();
        }
    }

    public void deleteVendorOnACtion(ActionEvent actionEvent) {
        try {
            if (VendorCRUDController.deleteCustomer(txtUPVID.getText())) {
                Notifications notification = Notifications.create().title("Vendor Deleted!").text("Name" + txtUPVNameOrCompany.getText() + " ID: " + txtUPVID.getText()).graphic(new ImageView(new Image("/Assets/outline_storefront_black_24dp.png"))).darkStyle().hideAfter(Duration.seconds(3));
                notification.show();
                setAllVendors(VendorCRUDController.getAllVendors());
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Something Went Wring!", ButtonType.OK);
                alert.initOwner(MainVendorPane.getScene().getWindow());
                alert.show();
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.initOwner(MainVendorPane.getScene().getWindow());
            alert.show();
        }
clearUpdateVendorFields();
    }

    private void setAllVendors(ArrayList<Vendor> vendorList) {
        VendorScrollerAnchorPane.getChildren().clear();


        for (Vendor vendor : vendorList) {
            setViewItemPane(vendor.getVendorID(), vendor.getNameOrCompany(), vendor.getContact(), vendor.getEmail(), vendor.getExtra_details());
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
    public void setViewItemPane(String VendorID, String name, String contact, String email, String extra_details) {
        //    Checks YPosition For Customer View Pane Placement.
        double layoutY;
        if (LayoutYValues.isEmpty()) {
            layoutY = 4;
            LayoutYValues.add(layoutY);
        } else {
            double size = LayoutYValues.get((LayoutYValues.size()) - 1) + 70;
            if (VendorScrollerAnchorPane.getHeight() < size) {
                VendorScrollerAnchorPane.setPrefHeight(size + 100);
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

        pane.getChildren().add(getHBox(201, 10, 25, VendorID));
        pane.getChildren().add(getLine(-127, -15, -127, 24, 334, 29));

        pane.getChildren().add(getHBox(201, 167, 25, name));
        pane.getChildren().add(getLine(-127, -15, -127, 24, 454, 29));

        pane.getChildren().add(getHBox(201, 285, 25, contact));
        pane.getChildren().add(getLine(-127, -15, -127, 24, 574, 29));

        pane.getChildren().add(getHBox(201, 454, 25, email));
        pane.getChildren().add(getLine(-127, -15, -127, 24, 789, 29));

        pane.getChildren().add(getHBox(201, 605, 25, extra_details));


        VendorScrollerAnchorPane.getChildren().add(pane);

//      Set Customer Details On Update/ Delete Section On Click On Pane.
        pane.setOnMouseClicked(event -> {
            txtUPVID.setText(VendorID);
            txtUPVNameOrCompany.setText(name);
            txtUPVEmail.setText(email);
            txtUPVMobile.setText(contact);
            txtUPVDetails.setText(extra_details);
        });

    }

    public void validateFields(KeyEvent keyEvent) {
        Object o = ValidationUtil.Validate(RegexMap, btnAddVendor);
        if (keyEvent.getCode() == KeyCode.ENTER) {
            if (o instanceof TextField) {
                TextField field = (TextField) o;
                field.requestFocus();
            } else {
                addVendor();
            }
        }
    }

    public void clearAddVendorFields() {
        txtVendorID.clear();
        txtNameOrCompany.clear();
        txtEMail.clear();
        txtMobile.clear();
        txtExtraDetails.clear();
        txtVendorID.requestFocus();

    }

    public void validateUpdateFields(KeyEvent keyEvent) {
        Object o = ValidationUtil.Validate(RegexUpdateMap, btnUpdateVendor);
        if (keyEvent.getCode() == KeyCode.ENTER) {
            if (o instanceof TextField) {
                TextField field = (TextField) o;
                field.requestFocus();
            }
        }
        if(btnUpdateVendor.isDisable()){
            btnDeleteVendor.setDisable(true);
        }else{
            btnDeleteVendor.setDisable(false);
        }
    }
    public void clearUpdateVendorFields() {
        txtUPVID.clear();
        txtUPVNameOrCompany.clear();
        txtUPVEmail.clear();
        txtUPVEmail.clear();
        txtUPVDetails.clear();
        txtUPVID.requestFocus();

    }
}
