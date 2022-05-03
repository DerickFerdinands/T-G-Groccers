package Controller;

import Controller.CRUD.AdminCRUDController;
import Model.Admin;
import Model.Item;
import Util.ValidationUtil;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import org.controlsfx.control.Notifications;

import java.io.File;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;

public class AdminManagementFormController {
    public JFXButton btnAddAdmin;
    public AnchorPane AddImageAnchorPane;
    public AnchorPane IdPane;
    public TextField txtAdminNIC;
    public JFXDatePicker dtpckrDOB;
    public TextField txtAdminMobile;
    public TextField txtAdminEmail;
    public TextField txtAdminName;
    public TextField txtAdminAddress;
    public TextField txtAdminUserName;
    public TextField txtPassword;
    public TextField txtRe_enterPassword;
    public AnchorPane AdminMainPane;
    public ImageView UpdateImage;
    public TextField txtUPDARE_ENTERPassword;
    public TextField txtUPAPassword;
    public TextField txtUPAUser_Name;
    public TextField txtUPAAddress;
    public TextField txtUPAName;
    public TextField txtUPAEMail;
    public TextField txtUPAMobile;
    public JFXDatePicker dtpckrUPADOB;
    public TextField txtUPANIC;
    public JFXButton btnUpdateEmployee;
    public JFXButton btnDeleteEmployee;
    public AnchorPane AdminScrollerAnchorPane;
    public JFXTextField txtSearchAdmin;
    public AnchorPane AdminImagePane;
    File image;
    String lastSelectedImagePath;
    ArrayList<Double> LayoutYValues = new ArrayList<>();

    LinkedHashMap<TextField, Pattern> RegexPatterns = new LinkedHashMap<>();
    LinkedHashMap<TextField, Pattern> RegexUpdatePatterns = new LinkedHashMap<>();
    File UpdatedImage;

    public void initialize() {
        btnAddAdmin.setDisable(true);
        btnUpdateEmployee.setDisable(true);
        btnDeleteEmployee.setDisable(true);
        try {
            setAllAdmins(AdminCRUDController.getALlAdmins());
            Pattern nicPattern = Pattern.compile("^[0-9]{10,15}(V)?$");
            Pattern namePattern = Pattern.compile("^[A-z ]+$");
            Pattern emailPattern = Pattern.compile("^[A-z1-9._]+(@)[A-z]+(.com)$");
            Pattern addressPattern = Pattern.compile("^[A-z0-9 ,/]+$");
            Pattern mobilePattern = Pattern.compile("^[0-9]{10}$");
            Pattern userName = Pattern.compile("^(.)+$");

            RegexPatterns.put(txtAdminNIC, nicPattern);
            RegexPatterns.put(txtAdminName, namePattern);
            RegexPatterns.put(txtAdminAddress, addressPattern);
            RegexPatterns.put(txtAdminMobile, mobilePattern);
            RegexPatterns.put(txtAdminEmail, emailPattern);
            RegexPatterns.put(txtAdminUserName, userName);
            RegexPatterns.put(txtPassword, userName);
            RegexPatterns.put(txtRe_enterPassword, userName);

            RegexUpdatePatterns.put(txtUPANIC, nicPattern);
            RegexUpdatePatterns.put(txtUPAName, namePattern);
            RegexUpdatePatterns.put(txtUPAAddress, addressPattern);
            RegexUpdatePatterns.put(txtUPAMobile, mobilePattern);
            RegexUpdatePatterns.put(txtUPAEMail, emailPattern);
            RegexUpdatePatterns.put(txtUPAUser_Name, userName);
            RegexUpdatePatterns.put(txtUPAPassword, userName);
            RegexUpdatePatterns.put(txtUPDARE_ENTERPassword, userName);

            dtpckrDOB.valueProperty().addListener((observable, oldValue, newValue) -> {
                ValidationUtil.Validate(RegexPatterns, btnAddAdmin);
                if (dtpckrDOB.getValue() != null && image != null) {
                    btnAddAdmin.setDisable(false);
                } else {
                    btnAddAdmin.setDisable(false);
                }

            });

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.initOwner(AdminMainPane.getScene().getWindow());
            alert.show();
        }
    }

    public void addAdminOnAction(ActionEvent actionEvent) {
        addAdmin();
    }

    public void addAdmin() {
        if (txtPassword.getText().equals(txtRe_enterPassword.getText())) {
            try {
                if (AdminCRUDController.addAdmin(txtAdminNIC.getText(), txtAdminName.getText(), dtpckrDOB.getValue(), txtAdminEmail.getText(), txtAdminMobile.getText(), txtAdminAddress.getText(), txtAdminUserName.getText(), txtRe_enterPassword.getText(), image.getPath())) {
                    Notifications notify = Notifications.create().title("Admin Added!").darkStyle();
                    notify.show();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "OOPS, Something Went Wrong!", ButtonType.OK);
                    alert.initOwner(AdminMainPane.getScene().getWindow());
                    alert.show();
                }
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
                alert.initOwner(AdminMainPane.getScene().getWindow());
                alert.show();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Passwords Doesn't Match!", ButtonType.OK);
            alert.initOwner(AdminMainPane.getScene().getWindow());
            alert.show();
        }
        try {
            setAllAdmins(AdminCRUDController.getALlAdmins());
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.initOwner(AdminMainPane.getScene().getWindow());
            alert.show();
        }
        clearAddAdminFields();
    }

    public void openImageChooser(MouseEvent mouseEvent) {
        FileChooser fileChooser = new FileChooser();
        image = fileChooser.showOpenDialog(AdminMainPane.getScene().getWindow());
        ImageView itemImageVIewer = new ImageView(new Image("file:" + image.getPath()));
        lastSelectedImagePath = image.getPath();
        itemImageVIewer.setFitHeight(144);
        itemImageVIewer.setFitWidth(142);
        AddImageAnchorPane.getChildren().add(itemImageVIewer);
    }

    public void openUpdateImageChooser(MouseEvent mouseEvent) {
        FileChooser fileChooser = new FileChooser();
        UpdatedImage = fileChooser.showOpenDialog(AdminMainPane.getScene().getWindow());
        lastSelectedImagePath = UpdatedImage.getPath();
        UpdateImage.setImage(new Image("file:" + lastSelectedImagePath));

    }

    public void searchAdminOnAction(ActionEvent actionEvent) {
        try {
            Admin admin = AdminCRUDController.getAdmin(txtUPANIC.getText());
            if (admin != null) {
                lastSelectedImagePath = admin.getImage_path();
                UpdateImage.setImage(new Image("file:" + admin.getImage_path()));
                txtUPAName.setText(admin.getName());
                dtpckrUPADOB.setValue(admin.getDob());
                txtUPAAddress.setText(admin.getAddress());
                txtUPAMobile.setText(admin.getMobile());
                txtUPAEMail.setText(admin.getEmail());
                txtUPAUser_Name.setText(admin.getUser_name());
                txtUPAPassword.setText(admin.getPassword());
                txtUPDARE_ENTERPassword.setText(admin.getPassword());
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.initOwner(AdminMainPane.getScene().getWindow());
            alert.show();
        }

    }

    public void deleteEmployeeOnAction(ActionEvent actionEvent) {
        try {
            if (AdminCRUDController.deleteAdmin(txtUPANIC.getText())) {
                Notifications notify = Notifications.create().title("Admin Deleted!").darkStyle();
                notify.show();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "OOPS, SOmething Went Wrong!", ButtonType.OK);
                alert.initOwner(AdminMainPane.getScene().getWindow());
                alert.show();
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.initOwner(AdminMainPane.getScene().getWindow());
            alert.show();
        }
        try {
            setAllAdmins(AdminCRUDController.getALlAdmins());
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.initOwner(AdminMainPane.getScene().getWindow());
            alert.show();
        }
        clearAllUpdateFields();
    }

    public void updateEmployeeOnAction(ActionEvent actionEvent) {
        if (txtUPDARE_ENTERPassword.getText().equals(txtUPAPassword.getText())) {
            try {
                if (AdminCRUDController.updateAdmin(txtUPANIC.getText(), txtUPAName.getText(), dtpckrUPADOB.getValue(), txtUPAEMail.getText(), txtUPAMobile.getText(), txtUPAAddress.getText(), txtUPAUser_Name.getText(), txtUPDARE_ENTERPassword.getText(), lastSelectedImagePath)) {
                    Notifications notify = Notifications.create().title("Admin Updated!").darkStyle();
                    notify.show();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "OOPS, SOmething Went Wrong!", ButtonType.OK);
                    alert.initOwner(AdminMainPane.getScene().getWindow());
                    alert.show();
                }
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
                alert.initOwner(AdminMainPane.getScene().getWindow());
                alert.show();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Passwords Doesn't Match!", ButtonType.OK);
            alert.initOwner(AdminMainPane.getScene().getWindow());
            alert.show();
        }
        try {
            setAllAdmins(AdminCRUDController.getALlAdmins());
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.initOwner(AdminMainPane.getScene().getWindow());
            alert.show();
        }
        clearAllUpdateFields();
    }

    private void setAllAdmins(ArrayList<Admin> adminList) {
        AdminScrollerAnchorPane.getChildren().clear();


        for (Admin a : adminList) {
            setViewItemPane(a.getImage_path(), a.getNic(), a.getName(), String.valueOf(a.getDob()), a.getEmail(), a.getMobile(), a.getAddress(), a.getUser_name(), a.getPassword());
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
    public void setViewItemPane(String imagePath, String nic, String name, String dob, String email, String mobile, String Address, String userName, String password) {
        //    Checks YPosition For Customer View Pane Placement.
        double layoutY;
        if (LayoutYValues.isEmpty()) {
            layoutY = 4;
            LayoutYValues.add(layoutY);
        } else {
            double size = LayoutYValues.get((LayoutYValues.size()) - 1) + 70;
            if (AdminScrollerAnchorPane.getHeight() < size) {
                AdminScrollerAnchorPane.setPrefHeight(size + 100);
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
        rec.setWidth(1142);
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

        ImageView itemImage = new ImageView(new Image("file:" + imagePath));
        itemImage.setFitHeight(42);
        itemImage.setFitWidth(40);

        itemImage.setLayoutX(14);
        itemImage.setLayoutY(12);

        pane.getChildren().add(itemImage);

        pane.getChildren().add(getHBox(201, 15, 25, nic));
        pane.getChildren().add(getLine(-127, -15, -127, 24, 300, 29));

        pane.getChildren().add(getHBox(201, 167, 25, name));
        pane.getChildren().add(getLine(-127, -15, -127, 24, 480, 29));

        pane.getChildren().add(getHBox(201, 300, 25, dob));
        pane.getChildren().add(getLine(-127, -15, -127, 24, 574, 29));

        pane.getChildren().add(getHBox(201, 454, 25, email));
        pane.getChildren().add(getLine(-127, -15, -127, 24, 789, 29));

        pane.getChildren().add(getHBox(201, 605, 25, mobile));
        pane.getChildren().add(getLine(-127, -15, -127, 24, 879, 29));

        pane.getChildren().add(getHBox(201, 702, 25, Address));
        pane.getChildren().add(getLine(-127, -15, -127, 24, 979, 29));

        pane.getChildren().add(getHBox(201, 850, 25, userName));
        pane.getChildren().add(getLine(-127, -15, -127, 24, 1160, 29));

        pane.getChildren().add(getHBox(201, 1000, 25, password));


        AdminScrollerAnchorPane.getChildren().add(pane);

        //     Set Customer Details On Update/ Delete Section On Click On Pane.
        pane.setOnMouseClicked(event -> {
            lastSelectedImagePath = imagePath;
            UpdateImage.setImage(new Image("file:" + imagePath));
            txtUPANIC.setText(nic);
            txtUPAName.setText(name);
            dtpckrUPADOB.setValue(LocalDate.parse(dob));
            txtUPAAddress.setText(Address);
            txtUPAMobile.setText(mobile);
            txtUPAEMail.setText(email);
            txtUPAUser_Name.setText(userName);
            txtUPAPassword.setText(password);
            txtUPDARE_ENTERPassword.setText(password);

            ValidationUtil.Validate(RegexUpdatePatterns, btnUpdateEmployee);
            if (dtpckrUPADOB.getValue() != null && !lastSelectedImagePath.isEmpty()) {
                btnUpdateEmployee.setDisable(false);
                btnDeleteEmployee.setDisable(false);
            } else {
                btnUpdateEmployee.setDisable(true);
                btnDeleteEmployee.setDisable(true);
            }

            if (dtpckrUPADOB.getValue() != null && !lastSelectedImagePath.isEmpty()) {
                btnUpdateEmployee.setDisable(false);
                btnDeleteEmployee.setDisable(false);
            } else {
                btnUpdateEmployee.setDisable(true);
                btnDeleteEmployee.setDisable(true);
            }
            btnDeleteEmployee.setDisable(btnUpdateEmployee.isDisable());
        });

    }

    public void searchViewForAdminOnAction(ActionEvent actionEvent) {
        String search = txtSearchAdmin.getText().toUpperCase();

        try {
            ArrayList<Admin> adminList = AdminCRUDController.getALlAdmins();
            ArrayList<Admin> addToView = new ArrayList<>();
            for (Admin a : adminList) {
                if (a.getNic().toUpperCase().contains(search) || a.getName().toUpperCase().contains(search)) {
                    addToView.add(a);
                }
            }
            if (addToView.isEmpty()) {
                setAllAdmins(adminList);
            } else {
                setAllAdmins(addToView);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.initOwner(AdminMainPane.getScene().getWindow());
            alert.show();
        }
        ValidationUtil.Validate(RegexUpdatePatterns, btnUpdateEmployee);
        if (dtpckrUPADOB.getValue() != null && !lastSelectedImagePath.isEmpty()) {
            btnUpdateEmployee.setDisable(false);
            btnDeleteEmployee.setDisable(false);
        } else {
            btnUpdateEmployee.setDisable(true);
            btnDeleteEmployee.setDisable(true);
        }

        if (dtpckrUPADOB.getValue() != null && !lastSelectedImagePath.isEmpty()) {
            btnUpdateEmployee.setDisable(false);
            btnDeleteEmployee.setDisable(false);
        } else {
            btnUpdateEmployee.setDisable(true);
            btnDeleteEmployee.setDisable(true);
        }
        btnDeleteEmployee.setDisable(btnUpdateEmployee.isDisable());
    }

    public void ValidateAddFields(KeyEvent keyEvent) {
        Object o = ValidationUtil.Validate(RegexPatterns, btnAddAdmin);
        if (keyEvent.getCode() == KeyCode.ENTER) {
            if (o instanceof TextField) {
                TextField field = (TextField) o;
                field.requestFocus();
            } else if (dtpckrDOB.getValue() != null && image != null) {
                btnAddAdmin.setDisable(false);
                addAdmin();
            } else {
                btnAddAdmin.setDisable(true);
            }
        }
        btnAddAdmin.setDisable(dtpckrDOB.getValue() == null || image == null);
    }


    public void validateUpdateFields(KeyEvent keyEvent) {
        Object o = ValidationUtil.Validate(RegexUpdatePatterns, btnUpdateEmployee);
        if (keyEvent.getCode() == KeyCode.ENTER) {
            if (o instanceof TextField) {
                TextField field = (TextField) o;
                field.requestFocus();
            } else if (dtpckrUPADOB.getValue() != null && !lastSelectedImagePath.isEmpty()) {
                btnUpdateEmployee.setDisable(false);
                btnDeleteEmployee.setDisable(false);
            } else {
                btnUpdateEmployee.setDisable(true);
                btnDeleteEmployee.setDisable(true);
            }
        }
        if (dtpckrUPADOB.getValue() != null && !lastSelectedImagePath.isEmpty()) {
            btnUpdateEmployee.setDisable(false);
            btnDeleteEmployee.setDisable(false);
        } else {
            btnUpdateEmployee.setDisable(true);
            btnDeleteEmployee.setDisable(true);
        }
        btnDeleteEmployee.setDisable(btnUpdateEmployee.isDisable());
    }

    public void clearAddAdminFields() {
        txtAdminNIC.clear();
        txtAdminName.clear();
        txtAdminAddress.clear();
        txtAdminMobile.clear();
        txtAdminEmail.clear();
        txtAdminUserName.clear();
        txtPassword.clear();
        txtRe_enterPassword.clear();
        dtpckrDOB.setValue(null);
        image=null;
        txtAdminNIC.requestFocus();
    }

    public void clearAllUpdateFields(){
        txtUPANIC.clear();
        txtUPAName.clear();
        txtUPAAddress.clear();
        txtUPAMobile.clear();
        txtUPAEMail.clear();
        txtUPAUser_Name.clear();
        txtUPAPassword.clear();
        txtUPDARE_ENTERPassword.clear();
        dtpckrUPADOB.setValue(null);
        UpdatedImage=null;
        txtUPANIC.requestFocus();


    }
}
