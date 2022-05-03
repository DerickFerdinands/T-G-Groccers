package Controller;

import Controller.CRUD.CustomerCRUDController;
import Model.Customer;
import Util.CrudUtil;
import Util.ValidationUtil;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import org.controlsfx.control.Notifications;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.regex.Pattern;

public class CustomerManagementFormController {
    public AnchorPane CustomerManagementForm;
    public TextField CNIC;
    public TextField CName;
    public TextField CEmail;
    public TextField CAddress;
    public TextField CMobile;
    public JFXDatePicker CDOB;
    public TextField UPCNIC;
    public TextField UPCName;
    public TextField UPCEmail;
    public TextField UPCAddress;
    public TextField UPCMobile;
    public JFXDatePicker UPCDOB;
    public AnchorPane CustomerScrollerAnchorPane;
    public AnchorPane CustomerMainPane;
    public ScrollPane CustomerScrollerPane;
    public JFXButton btnSaveCustomer;
    public ScrollPane CustomerViewScroller;
    public JFXTextField txtSearchCustomer;
    public Label lblNICExists;
    public Label lblContactExists;
    public Label lblDOB;
    public JFXButton btnDeleteCustomer;
    public JFXButton btnUpdateCustomer;
    ArrayList<Double> LayoutYValues = new ArrayList<>();

    LinkedHashMap<TextField, Pattern> RegexMap = new LinkedHashMap<>();
    LinkedHashMap<TextField, Pattern> RegexUpdateMap = new LinkedHashMap<>();

    public void addCustomerOnAction(ActionEvent actionEvent) {
        saveCUstomerOnACtion();
    }

    public void saveCUstomerOnACtion() {
        try {
            if(CustomerCRUDController.saveCustomer(CNIC.getText(), CName.getText(), CEmail.getText(), CAddress.getText(), CDOB.getValue(), CMobile.getText())){
                Notifications notify = Notifications.create().title("Customer Added Sucessfully!");
                notify.show();
            }
            LayoutYValues.clear();
            setAllCustomers(CustomerCRUDController.getAllCustomers());
            clearAllAddCustomerFields();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR,e.getMessage(),ButtonType.OK);
            alert.initOwner(CustomerMainPane.getScene().getWindow());
            alert.show();
        }
    }

    //    Return Customer If Exists.
    public void searchCustomerOnAction(ActionEvent actionEvent) {

        Customer c = null;
        try {
            c = CustomerCRUDController.getCustomer(UPCNIC.getText());


            UPCName.setText(c.getCustomerName());
            UPCEmail.setText(c.getCustomerEmail());
            UPCAddress.setText(c.getCustomerAddress());
            UPCMobile.setText(c.getCustomerMobile());
            UPCDOB.setValue(c.getDOB());
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR,e.getMessage(),ButtonType.OK);
            alert.initOwner(CustomerMainPane.getScene().getWindow());
            alert.show();
        }
    }

    public void initialize() {

        btnSaveCustomer.setDisable(true);
        btnUpdateCustomer.setDisable(true);
        btnDeleteCustomer.setDisable(true);
        CustomerScrollerAnchorPane.getChildren().clear();
        try {
            setAllCustomers(CustomerCRUDController.getAllCustomers());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        Pattern nicPattern = Pattern.compile("^[0-9]{10,15}(V)?$");
        Pattern namePattern = Pattern.compile("^[A-z ]+$");
        Pattern emailPattern = Pattern.compile("^[A-z1-9._]+(@)[A-z]+(.com)$");
        Pattern addressPattern = Pattern.compile("^[A-z0-9 ,/]+$");
        Pattern mobilePattern = Pattern.compile("^[0-9]{10}$");

        RegexMap.put(CNIC, nicPattern);
        RegexMap.put(CName, namePattern);
        RegexMap.put(CEmail, emailPattern);
        RegexMap.put(CAddress, addressPattern);
        RegexMap.put(CMobile, mobilePattern);

        RegexUpdateMap.put(UPCNIC, nicPattern);
        RegexUpdateMap.put(UPCName, namePattern);
        RegexUpdateMap.put(UPCEmail, emailPattern);
        RegexUpdateMap.put(UPCAddress, addressPattern);
        RegexUpdateMap.put(UPCMobile, mobilePattern);

        CDOB.valueProperty().addListener((observable, oldValue, newValue) -> {
            if(CDOB.getValue()!=null){
                ValidationUtil.Validate(RegexMap, btnSaveCustomer);
                btnSaveCustomer.setDisable(false);
            }
        });

    }

    //    Loads All Customers TO VIew
    private void setAllCustomers(ArrayList<Customer> cusList) {
        CustomerScrollerAnchorPane.getChildren().clear();

//Sets View For Each Customer.
        for (Customer c : cusList) {
            setViewCustomerPane(c.getCustomerName(), c.getCustomerNIC(), c.getCustomerMobile(), c.getCustomerEmail(), c.getCustomerAddress(), String.valueOf(c.getDOB()), String.valueOf(c.getPoints()));
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
    public void setViewCustomerPane(String name, String nic, String mobile, String email, String address, String dob, String points) {
//        Checks YPosition For Customer View Pane Placement.
        double layoutY;
        if (LayoutYValues.isEmpty()) {
            layoutY = 4;
            LayoutYValues.add(layoutY);
        } else {
            double size = LayoutYValues.get((LayoutYValues.size()) - 1) + 70;
            if (CustomerScrollerAnchorPane.getHeight() < size) {
                CustomerScrollerAnchorPane.setPrefHeight(size + 100);
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
        pane.getChildren().add(getHBox(201, 10, 25, name));
        pane.getChildren().add(getLine(-127, -15, -127, 24, 334, 29));

        pane.getChildren().add(getHBox(201, 167, 25, nic));
        pane.getChildren().add(getLine(-127, -15, -127, 24, 454, 29));

        pane.getChildren().add(getHBox(201, 285, 25, mobile));
        pane.getChildren().add(getLine(-127, -15, -127, 24, 574, 29));

        pane.getChildren().add(getHBox(201, 454, 25, email));
        pane.getChildren().add(getLine(-127, -15, -127, 24, 789, 29));

        pane.getChildren().add(getHBox(201, 605, 25, address));
        pane.getChildren().add(getLine(-127, -15, -127, 24, 879, 29));

        pane.getChildren().add(getHBox(201, 702, 25, dob));
        pane.getChildren().add(getLine(-127, -15, -127, 24, 979, 29));

        pane.getChildren().add(getHBox(201, 790, 25, points));

        CustomerScrollerAnchorPane.getChildren().add(pane);

//        Set Customer Details On Update/ Delete Section On Click On Pane.
        pane.setOnMouseClicked(event -> {
            UPCNIC.setText(nic);
            UPCName.setText(name);
            UPCEmail.setText(email);
            UPCAddress.setText(address);
            UPCDOB.setValue(LocalDate.parse(dob));
            UPCMobile.setText(mobile);
            ValidationUtil.Validate(RegexUpdateMap,btnUpdateCustomer);
            if(btnUpdateCustomer.isDisable()){
                btnDeleteCustomer.setDisable(true);
            }else{
                btnDeleteCustomer.setDisable(false);
            }
        });

    }

    //Clears All TextFields Of Update/ Delete Section & Request Focus Back To UPCNIC.
    public void clearAllAddCustomerFields() {
        CNIC.clear();
        CName.clear();
        CEmail.clear();
        CAddress.clear();
        CDOB.setValue(null);
        CMobile.clear();
        CNIC.requestFocus();
        lblDOB.setVisible(false);
        btnSaveCustomer.setDisable(true);
    }

    //Clears All TextFields Of Update/ Delete Section & Request Focus Back To UPCNIC.
    public void clearAllUpdateCustomerFields() {
        UPCNIC.clear();
        UPCName.clear();
        UPCEmail.clear();
        UPCAddress.clear();
        UPCDOB.setValue(null);
        CDOB.setPromptText("");
        UPCNIC.requestFocus();
    }

    public void deleteCustomerOnAction(ActionEvent actionEvent) {
        try {
            boolean deleted = CustomerCRUDController.deleteCustomer(UPCNIC.getText());
            setAllCustomers(CustomerCRUDController.getAllCustomers());
            if (deleted) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Deleted Sucesfully!..", ButtonType.OK);
                alert.initOwner(CustomerMainPane.getScene().getWindow());
                alert.show();
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Something Went Wrong!..", ButtonType.OK);
                alert.initOwner(CustomerMainPane.getScene().getWindow());
                alert.show();
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR,e.getMessage(),ButtonType.OK);
            alert.initOwner(CustomerMainPane.getScene().getWindow());
            alert.show();
        }
        clearAllUpdateCustomerFields();
    }

    public void updateCustomerOnAction(ActionEvent actionEvent) {
        try {
            System.out.println("Begining");
            String name = CName.getText();
            String email = CEmail.getText();
            String address = CAddress.getText();
            System.out.println("middle");
            //  LocalDate dob = LocalDate.parse(CDOB.getValue().toString());
            String mobile = CMobile.getText();
            String nic = CNIC.getText();
            System.out.println("Works");
            boolean updated = CustomerCRUDController.updateCustomer(UPCNIC.getText(), UPCName.getText(), UPCEmail.getText(), UPCAddress.getText(), UPCDOB.getValue(), UPCMobile.getText());
            System.out.println("Works 22");
            if (updated) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Updated Sucessfully!..", ButtonType.OK);
                alert.initOwner(CustomerMainPane.getScene().getWindow());
                alert.show();
            } else {

                Alert alert = new Alert(Alert.AlertType.WARNING, "Something Went Wrong!..", ButtonType.OK);
                alert.initOwner(CustomerMainPane.getScene().getWindow());
                alert.show();
            }
            setAllCustomers(CustomerCRUDController.getAllCustomers());
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR,e.getMessage(),ButtonType.OK);
            alert.initOwner(CustomerMainPane.getScene().getWindow());
            alert.show();
        }
        clearAllUpdateCustomerFields();
    }

    public void validateAddCustomerFields(KeyEvent keyEvent) {
        try {
            boolean validated = false;
            for (Customer c : CustomerCRUDController.getAllCustomers()) {
                if (c.getCustomerNIC().equals(CNIC.getText()) && (!CNIC.getText().isEmpty())) {
                    lblNICExists.setVisible(true);
                    validated = true;
                    break;
                } else {
                    lblNICExists.setVisible(false);
                }
                if (c.getCustomerMobile().equals(CMobile.getText()) && (!CMobile.getText().isEmpty())) {
                    lblContactExists.setVisible(true);
                    validated = true;
                    break;
                } else {
                    lblContactExists.setVisible(false);
                }
            }


            btnSaveCustomer.setDisable(validated == true);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        Object O = ValidationUtil.Validate(RegexMap, btnSaveCustomer);
        if (keyEvent.getCode() == KeyCode.ENTER) {
            if (O instanceof TextField) {
                TextField field = (TextField) O;
                field.requestFocus();
            } else {
                if (CDOB.getValue() != null) {
                    btnSaveCustomer.setDisable(false);
                    saveCUstomerOnACtion();
                    lblDOB.setVisible(false);
                } else {
                    CDOB.requestFocus();
                    CDOB.show();
                    if ((CDOB.getValue() == null || CDOB.getValue().isAfter(LocalDate.now()) || CDOB.getValue().isEqual(LocalDate.now()))) {
                        btnSaveCustomer.setDisable(true);
                        lblDOB.setVisible(true);
                    } else {
                        lblDOB.setVisible(false);
                    }
                }
            }


        }


    }

    public void searchOnAction(ActionEvent actionEvent) {
        String search = txtSearchCustomer.getText();
        ArrayList<Customer> AddToVIew = new ArrayList<>();
        try {
            ArrayList<Customer> customerList = CustomerCRUDController.getAllCustomers();
            for (Customer c : customerList) {
                if (c.getCustomerNIC().toUpperCase().contains(search.toUpperCase()) || c.getCustomerName().toUpperCase().contains(search.toUpperCase())) {
                    AddToVIew.add(c);
                }
            }
            if (customerList.isEmpty()) {
                Label label = new Label("No Results Found For " + search + ".");
                label.setStyle("-fx-font-size: 40");
                label.setLayoutY(20);
                label.setLayoutX(5);
                CustomerScrollerAnchorPane.getChildren().add(label);
            } else {
                setAllCustomers(AddToVIew);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();

        }
        if (search.isEmpty()) {
            try {
                setAllCustomers(CustomerCRUDController.getAllCustomers());
            } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR,e.getMessage(),ButtonType.OK);
            alert.initOwner(CustomerMainPane.getScene().getWindow());
            alert.show();
            }
        }

    }

    public void HideLabel(Event event) {
        lblDOB.setVisible(false);
    }

    public void UpdateValidation(KeyEvent keyEvent) {
        if(UPCDOB.getValue()==null||UPCDOB.getValue().isAfter(LocalDate.now())||UPCDOB.getValue().equals(LocalDate.now())){
            btnUpdateCustomer.setDisable(true);
            UPCDOB.getParent().setStyle("-fx-border-color: Red");
        }else{
            UPCDOB.getParent().setStyle("-fx-border-color: Green");
            btnUpdateCustomer.setDisable(false);
        }
        ValidationUtil.Validate(RegexUpdateMap,btnUpdateCustomer);
        if(btnUpdateCustomer.isDisable()){
            btnDeleteCustomer.setDisable(true);
        }else{
            btnDeleteCustomer.setDisable(false);
        }
    }
}
