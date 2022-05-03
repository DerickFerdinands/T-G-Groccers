package Controller;

import Controller.CRUD.UserCRUDController;
import Util.ValidationUtil;
import View.TM.UserTM;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import org.controlsfx.control.Notifications;

import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;

public class UserManagementFormController {

    public TextField txtJobTitle;
    public TableView<UserTM> tblEmployeeDetails;
    public TableColumn colEmpNIC;
    public TableColumn colName;
    public TableColumn colMobile;
    public TableColumn colEmail;
    public TableColumn colDesignation;
    public TableColumn colJobTitle;
    public TableColumn colUserName;
    public TableColumn colPassword;
    public AnchorPane UserMainPane;
    public TextField txtDesignationType;
    public JFXComboBox cmbDEsignationType;
    public JFXComboBox cmbJobTitleType;
    public JFXComboBox cmbStatus;
    public JFXComboBox cmbNIC;
    public TextField txtUserName;
    public TextField txtPassword;
    public TextField txtReEnterPassword;
    public JFXButton btnAddUser;
    public JFXButton btnUpdateUser;
    public JFXButton btnDeleteUser;
    public TextField txtUserID;

    LinkedHashMap<TextField, Pattern> RegexPattern = new LinkedHashMap<>();

    public void initialize() {
        btnAddUser.setDisable(true);
        btnUpdateUser.setDisable(true);
        btnDeleteUser.setDisable(true);
        colEmpNIC.setCellValueFactory(new PropertyValueFactory<>("nic"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colMobile.setCellValueFactory(new PropertyValueFactory<>("mobile"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colDesignation.setCellValueFactory(new PropertyValueFactory<>("designation"));
        colJobTitle.setCellValueFactory(new PropertyValueFactory<>("job_title"));
        colUserName.setCellValueFactory(new PropertyValueFactory<>("UserName"));
        colPassword.setCellValueFactory(new PropertyValueFactory<>("password"));
        setAllUsers();

        Pattern UserID = Pattern.compile("^(U00-)[0-9]{3}$");
        Pattern UserName = Pattern.compile("^(.)+$");

        RegexPattern.put(txtUserID, UserID);
        RegexPattern.put(txtUserName, UserName);
        RegexPattern.put(txtPassword, UserName);
        RegexPattern.put(txtReEnterPassword, UserName);

        tblEmployeeDetails.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            UserTM tm = newValue;
            try {
                txtUserID.setText(UserCRUDController.getUserid(tm.getNic()));
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            cmbNIC.getSelectionModel().select(tm.getNic());
            txtUserName.setText(tm.getUserName());
            cmbDEsignationType.getSelectionModel().select(tm.getDesignation());
            txtPassword.setText(tm.getPassword());
            txtReEnterPassword.setText(tm.getPassword());
            cmbJobTitleType.getSelectionModel().select(tm.getJob_title());
            cmbStatus.getSelectionModel().select("Active");

            Object o = ValidationUtil.Validate(RegexPattern, btnAddUser);

            if (cmbNIC.getValue() != null && cmbDEsignationType.getValue() != null && cmbJobTitleType.getValue() != null && cmbStatus.getValue() != null) {
                btnAddUser.setDisable(false);
                btnUpdateUser.setDisable(false);
                btnDeleteUser.setDisable(false);
            }
            if (btnAddUser.isDisable()) {
                btnUpdateUser.setDisable(true);
                btnDeleteUser.setDisable(true);
            } else {
                btnUpdateUser.setDisable(false);
                btnDeleteUser.setDisable(false);
            }

        });
        try {
            cmbNIC.getItems().addAll(UserCRUDController.getAllEmployeeNICS());
            cmbStatus.getItems().addAll("Active", "Inactive");
            cmbJobTitleType.getItems().addAll(UserCRUDController.getAllJobTitles());
            cmbDEsignationType.getItems().addAll(UserCRUDController.getAllJobDesignationTypes());
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.initOwner(UserMainPane.getScene().getWindow());
            alert.show();
        }
    }

    private void setAllUsers() {
        try {
            ObservableList<UserTM> obList = FXCollections.observableArrayList();
            obList.addAll(UserCRUDController.getAllUserDetails());
            tblEmployeeDetails.setItems(obList);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.initOwner(UserMainPane.getScene().getWindow());
            alert.show();
        }
    }

    public void addDesignationTypeOnAction(ActionEvent actionEvent) {
        try {
            if (UserCRUDController.addDesignationType(txtDesignationType.getText())) {

                Notifications notify = Notifications.create().title("Job Designation Type Added Sucessfully!").text("Title: " + txtDesignationType.getText());
                notify.show();
                cmbDEsignationType.getItems().add(txtDesignationType.getText());
                setAllUsers();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Something Went Wrong!", ButtonType.OK);
                alert.initOwner(UserMainPane.getScene().getWindow());
                alert.show();
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.initOwner(UserMainPane.getScene().getWindow());
            alert.show();
        }
    }

    public void addJobTitleOnAction(ActionEvent actionEvent) {
        try {
            if (UserCRUDController.addJobTitle(txtJobTitle.getText())) {
                Notifications notify = Notifications.create().title("Job Title Added Sucessfully!").text("Title: " + txtJobTitle.getText());
                notify.show();
                cmbJobTitleType.getItems().add(txtJobTitle.getText());
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Something Went Wrong!", ButtonType.OK);
                alert.initOwner(UserMainPane.getScene().getWindow());
                alert.show();
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.initOwner(UserMainPane.getScene().getWindow());
            alert.show();
        }


    }

    public void addUserOnAction(ActionEvent actionEvent) {
        try {
            if (UserCRUDController.addUser(txtUserID.getText(), (String) cmbNIC.getValue(), txtUserName.getText(), txtReEnterPassword.getText(), (String) cmbJobTitleType.getValue(), (String) cmbDEsignationType.getValue(), (String) cmbStatus.getValue())) {
                Notifications notify = Notifications.create().title("USer Added Sucessfully!");
                notify.show();
                setAllUsers();
                tblEmployeeDetails.refresh();
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.initOwner(UserMainPane.getScene().getWindow());
            alert.show();
        }
    }

    public void updateUserOnAction(ActionEvent actionEvent) {
        try {
            if (UserCRUDController.updateUser(txtUserID.getText(), (String) cmbNIC.getValue(), txtUserName.getText(), txtReEnterPassword.getText(), (String) cmbJobTitleType.getValue(), (String) cmbDEsignationType.getValue(), true)) {
                Notifications notify = Notifications.create().title("User Updated Sucessfully!");
                notify.show();
                setAllUsers();
                tblEmployeeDetails.refresh();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Something Went Wrong!", ButtonType.OK);
                alert.initOwner(UserMainPane.getScene().getWindow());
                alert.show();

            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.initOwner(UserMainPane.getScene().getWindow());
            alert.show();
        }
    }

    public void deleteUserOnAction(ActionEvent actionEvent) {
        try {
            if (UserCRUDController.deleteUser(txtUserID.getText())) {
                Notifications notify = Notifications.create().title("User Deleted Sucessfully!");
                notify.show();
                setAllUsers();
                tblEmployeeDetails.refresh();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Something Went Wrong!", ButtonType.OK);
                alert.initOwner(UserMainPane.getScene().getWindow());
                alert.show();
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.initOwner(UserMainPane.getScene().getWindow());
            alert.show();
        }
    }

    public void searchUserIdOnAction(ActionEvent actionEvent) {
        try {
            UserTM tm = UserCRUDController.getUser(txtUserID.getText());
            if (tm != null) {
                cmbNIC.getSelectionModel().select(tm.getNic());
                txtUserName.setText(tm.getUserName());
                cmbDEsignationType.getSelectionModel().select(tm.getDesignation());
                txtPassword.setText(tm.getPassword());
                txtReEnterPassword.setText(tm.getPassword());
                cmbJobTitleType.getSelectionModel().select(tm.getJob_title());
                cmbStatus.getSelectionModel().select("Active");
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.initOwner(UserMainPane.getScene().getWindow());
            alert.show();
        }
        Object o = ValidationUtil.Validate(RegexPattern, btnAddUser);

        if (cmbNIC.getValue() != null && cmbDEsignationType.getValue() != null && cmbJobTitleType.getValue() != null && cmbStatus.getValue() != null) {
            btnAddUser.setDisable(false);
            btnUpdateUser.setDisable(false);
            btnDeleteUser.setDisable(false);
        }
        if (btnAddUser.isDisable()) {
            btnUpdateUser.setDisable(true);
            btnDeleteUser.setDisable(true);
        } else {
            btnUpdateUser.setDisable(false);
            btnDeleteUser.setDisable(false);
        }
    }


    public void validateFields(KeyEvent keyEvent) {
        Object o = ValidationUtil.Validate(RegexPattern, btnAddUser);

        if (keyEvent.getCode() == KeyCode.ENTER) {
            if (o instanceof TextField) {
                TextField field = (TextField) o;
                field.requestFocus();
            } else if (cmbNIC.getValue() != null && cmbDEsignationType.getValue() != null && cmbJobTitleType.getValue() != null && cmbStatus.getValue() != null) {
                btnAddUser.setDisable(false);
                btnUpdateUser.setDisable(false);
                btnDeleteUser.setDisable(false);
            }
        }
        if (btnAddUser.isDisable()) {
            btnUpdateUser.setDisable(true);
            btnDeleteUser.setDisable(true);
        } else {
            btnUpdateUser.setDisable(false);
            btnDeleteUser.setDisable(false);
        }
    }
}
