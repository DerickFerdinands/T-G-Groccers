package Controller;

import Controller.CRUD.EmployeeCRUDController;
import Model.Employee;
import Model.Item;
import Model.Vendor;
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

public class EmployeeManagementFormController {
    public AnchorPane AddImageAnchorPane;
    public AnchorPane employeeMainPane;
    public JFXButton btnAddEmployee;
    public TextField txtENIC;
    public JFXDatePicker txtEDOB;
    public TextField txtEMobile;
    public TextField txtEmail;
    public TextField txtEName;
    public TextField txtEAddress;
    public AnchorPane EmployeeScrollerAnchorPane;
    public ImageView UpdateImageView;
    public JFXButton btnDeleteEmployee;
    public JFXButton btnUpdateEmployee;
    public TextField txtUPENIC;
    public JFXDatePicker dtpckrUPEDOB;
    public TextField txtUPEMobile;
    public TextField txtUPEmail;
    public TextField txtUPEName;
    public TextField txtUPEAddress;
    public JFXTextField txtSearch;
    File selectedImage;
    ArrayList<Double> LayoutYValues = new ArrayList<>();
    String lastSelectedImagePath;

    LinkedHashMap<TextField, Pattern> RegexPattern = new LinkedHashMap<>();
    LinkedHashMap<TextField, Pattern> RegexUpdatePattern = new LinkedHashMap<>();

    public void initialize() {
        btnAddEmployee.setDisable(true);
        btnUpdateEmployee.setDisable(true);
        btnDeleteEmployee.setDisable(true);
        try {
            setAllEmployees(EmployeeCRUDController.getAllEployees());

            Pattern nicPattern = Pattern.compile("^[0-9]{10,15}(V)?$");
            Pattern namePattern = Pattern.compile("^[A-z ]+$");
            Pattern emailPattern = Pattern.compile("^[A-z1-9._]+(@)[A-z]+(.com)$");
            Pattern addressPattern = Pattern.compile("^[A-z0-9 ,/]+$");
            Pattern mobilePattern = Pattern.compile("^[0-9]{10}$");

            RegexPattern.put(txtENIC, nicPattern);
            RegexPattern.put(txtEName, namePattern);
            RegexPattern.put(txtEAddress, addressPattern);
            RegexPattern.put(txtEMobile, mobilePattern);
            RegexPattern.put(txtEmail, emailPattern);

            RegexUpdatePattern.put(txtUPENIC, nicPattern);
            RegexUpdatePattern.put(txtUPEName, namePattern);
            RegexUpdatePattern.put(txtUPEAddress, addressPattern);
            RegexUpdatePattern.put(txtUPEMobile, mobilePattern);
            RegexUpdatePattern.put(txtUPEmail, emailPattern);

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.initOwner(employeeMainPane.getScene().getWindow());
            alert.show();
        }

        txtEDOB.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (txtEDOB.getValue().isAfter(LocalDate.now()) || txtEDOB.getValue().equals(LocalDate.now())) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid Date", ButtonType.OK);
                alert.initOwner(employeeMainPane.getScene().getWindow());
                alert.show();
            } else {
                if (selectedImage != null) {
                    ValidationUtil.Validate(RegexPattern, btnAddEmployee);
                }
            }
        });
        dtpckrUPEDOB.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (txtEDOB.getValue().isAfter(LocalDate.now()) || txtEDOB.getValue().equals(LocalDate.now())) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid Date", ButtonType.OK);
                alert.initOwner(employeeMainPane.getScene().getWindow());
                alert.show();
            } else {
                ValidationUtil.Validate(RegexUpdatePattern, btnUpdateEmployee);

                if (txtEDOB.getValue() != null && !(lastSelectedImagePath.isEmpty())) {
                    btnDeleteEmployee.setDisable(false);
                    btnUpdateEmployee.setDisable(false);
                } else {
                    btnDeleteEmployee.setDisable(true);
                    btnUpdateEmployee.setDisable(true);
                }

                btnDeleteEmployee.setDisable(btnUpdateEmployee.isDisable());
            }
        });
    }

    public void selectImageOnAction(MouseEvent mouseEvent) {
        FileChooser fileChooser = new FileChooser();
        selectedImage = fileChooser.showOpenDialog(employeeMainPane.getScene().getWindow());
        ImageView itemImageVIewer = new ImageView(new Image("file:" + selectedImage.getPath()));
        itemImageVIewer.setFitHeight(144);
        itemImageVIewer.setFitWidth(142);
        AddImageAnchorPane.getChildren().add(itemImageVIewer);

        if (txtEDOB.getValue() != null) {
            ValidationUtil.Validate(RegexPattern, btnAddEmployee);
        }

    }

    public void selectUpdateImageOnAction(MouseEvent mouseEvent) {
        FileChooser fileChooser = new FileChooser();
        selectedImage = fileChooser.showOpenDialog(employeeMainPane.getScene().getWindow());
        lastSelectedImagePath = selectedImage.getPath();
        UpdateImageView.setImage(new Image("file:" + lastSelectedImagePath));
        if (dtpckrUPEDOB.getValue() != null) {
            ValidationUtil.Validate(RegexPattern, btnAddEmployee);
        }
    }

    public void addEmployeeOnACtion(ActionEvent actionEvent) {
        addEmployee();
    }

    public void addEmployee() {
        try {
            boolean saved = EmployeeCRUDController.addEmployee(txtENIC.getText(), txtEName.getText(), txtEDOB.getValue(), txtEAddress.getText(), txtEMobile.getText(), txtEmail.getText(), selectedImage.getPath());
            if (saved) {
                Notifications notify = Notifications.create().title("Employee Added!").text("NIC: " + txtENIC.getText() + " Name: " + txtEName.getText()).darkStyle().graphic(new ImageView(new Image("/Assets/2x/outline_badge_black_24dp.png")));
                notify.show();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Something Went Wrong!..", ButtonType.OK);
                alert.initOwner(employeeMainPane.getScene().getWindow());
                alert.show();
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.initOwner(employeeMainPane.getScene().getWindow());
            alert.show();
        }
        try {
            setAllEmployees(EmployeeCRUDController.getAllEployees());
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.initOwner(employeeMainPane.getScene().getWindow());
            alert.show();
        }
    }

    private void setAllEmployees(ArrayList<Employee> empList) {
        EmployeeScrollerAnchorPane.getChildren().clear();


        for (Employee emp : empList) {
            setViewItemPane(emp.getImage_Location(), emp.getNic(), emp.getName(), String.valueOf(emp.getDob()), emp.getAddress(), emp.getMobile(), emp.getEmail());
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
    public void setViewItemPane(String imageLocation, String nic, String name, String dob, String address, String mobile, String email) {
        //    Checks YPosition For Customer View Pane Placement.
        double layoutY;
        if (LayoutYValues.isEmpty()) {
            layoutY = 4;
            LayoutYValues.add(layoutY);
        } else {
            double size = LayoutYValues.get((LayoutYValues.size()) - 1) + 70;
            if (EmployeeScrollerAnchorPane.getHeight() < size) {
                EmployeeScrollerAnchorPane.setPrefHeight(size + 130);
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

        ImageView itemImage = new ImageView(new Image("file:" + imageLocation));
        itemImage.setFitHeight(42);
        itemImage.setFitWidth(40);

        itemImage.setLayoutX(14);
        itemImage.setLayoutY(12);

        pane.getChildren().add(itemImage);

        pane.getChildren().add(getHBox(201, 15, 25, nic));
        pane.getChildren().add(getLine(-127, -15, -127, 24, 290, 29));

        pane.getChildren().add(getHBox(170, 170, 25, name));
        pane.getChildren().add(getLine(-127, -15, -127, 24, 470, 29));

        pane.getChildren().add(getHBox(201, 285, 25, dob));
        pane.getChildren().add(getLine(-127, -15, -127, 24, 555, 29));

        pane.getChildren().add(getHBox(201, 400, 25, address));
        pane.getChildren().add(getLine(-127, -15, -127, 24, 700, 29));

        pane.getChildren().add(getHBox(201, 520, 25, mobile));
        pane.getChildren().add(getLine(-127, -15, -127, 24, 790, 29));

        pane.getChildren().add(getHBox(201, 670, 25, email));


        EmployeeScrollerAnchorPane.getChildren().add(pane);

//      Set Customer Details On Update/ Delete Section On Click On Pane.
        pane.setOnMouseClicked(event -> {
            UpdateImageView.setImage(new Image("file:" + imageLocation));
            lastSelectedImagePath = imageLocation;
            txtUPENIC.setText(nic);
            txtUPEName.setText(name);
            dtpckrUPEDOB.setValue(LocalDate.parse(dob));
            txtUPEAddress.setText(address);
            txtUPEMobile.setText(mobile);
            txtUPEmail.setText(email);
            ValidationUtil.Validate(RegexUpdatePattern, btnUpdateEmployee);
            btnDeleteEmployee.setDisable(btnUpdateEmployee.isDisable());

        });

    }

    public void deleteEmployeeOnAction(ActionEvent actionEvent) {
        try {
            if (EmployeeCRUDController.deleteEmployee(txtUPENIC.getText())) {
                Notifications notify = Notifications.create().title("Employee Deleted!").text("NIC: " + txtUPENIC.getText() + " Name: " + txtUPEName.getText()).darkStyle().graphic(new ImageView(new Image("/Assets/2x/outline_badge_black_24dp.png")));
                notify.show();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Something went Wrong!..", ButtonType.OK);
                alert.initOwner(employeeMainPane.getScene().getWindow());
                alert.show();
            }
            setAllEmployees(EmployeeCRUDController.getAllEployees());
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.initOwner(employeeMainPane.getScene().getWindow());
            alert.show();
        }
    }

    public void updateEmployeeOnAction(ActionEvent actionEvent) {
        try {
            if (EmployeeCRUDController.updateEmployee(txtUPENIC.getText(), txtUPEName.getText(), dtpckrUPEDOB.getValue(), txtUPEAddress.getText(), txtUPEMobile.getText(), txtUPEmail.getText(), lastSelectedImagePath)) {
                Notifications notify = Notifications.create().title("Employee Updated!").text("NIC: " + txtUPENIC.getText() + " Name: " + txtUPEName.getText()).darkStyle().graphic(new ImageView(new Image("/Assets/2x/outline_badge_black_24dp.png")));
                notify.show();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Something Went Wrong!..", ButtonType.OK);
                alert.initOwner(employeeMainPane.getScene().getWindow());
                alert.show();
            }
            setAllEmployees(EmployeeCRUDController.getAllEployees());
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.initOwner(employeeMainPane.getScene().getWindow());
            alert.show();
        }
    }

    public void searchEmployeeOnAction(ActionEvent actionEvent) {
        String search = txtSearch.getText().toUpperCase();
        ArrayList<Employee> addTOView = new ArrayList<>();
        try {
            ArrayList<Employee> empList = EmployeeCRUDController.getAllEployees();
            for (Employee e : empList) {
                if (e.getNic().toUpperCase().contains(search) || e.getName().toUpperCase().contains(search)) {
                    addTOView.add(e);
                }
            }
            if (empList.isEmpty()) {
                setAllEmployees(EmployeeCRUDController.getAllEployees());
            } else {
                setAllEmployees(addTOView);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.initOwner(employeeMainPane.getScene().getWindow());
            alert.show();
        }
    }

    public void ValidateAddEmployeeFields(KeyEvent keyEvent) {
        Object o = ValidationUtil.Validate(RegexPattern, btnAddEmployee);

        if (keyEvent.getCode() == KeyCode.ENTER) {
            if (o instanceof TextField) {
                TextField field = (TextField) o;
                field.requestFocus();
            } else if (txtEDOB.getValue() != null && selectedImage != null) {
                addEmployee();
            }
        }
    }

    public void validateUpdateFields(KeyEvent keyEvent) {
        Object o = ValidationUtil.Validate(RegexUpdatePattern, btnUpdateEmployee);


        btnDeleteEmployee.setDisable(btnUpdateEmployee.isDisable());
    }


}

