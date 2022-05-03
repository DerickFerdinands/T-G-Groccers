package Controller;

import Controller.CRUD.ExpenseCRUDController;
import Model.Expense;
import Util.ValidationUtil;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import org.controlsfx.control.Notifications;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;

public class ExpenseManagementFormController {
    public JFXButton btnAddExpense;
    public TextField txtExpenseID;
    public JFXComboBox cmbExpenseType;
    public TextField txtExpenseDesc;
    public TextField txtUPEID;
    public JFXComboBox cmbUPEType;
    public TextField txtUPEDESC;
    public JFXButton btnDeleteExpense;
    public JFXButton btnUpdateExpense;
    public TextField txtExpenseType;
    public JFXButton btnAddExpenseType;
    public TextField txtExpenseAmount;
    public TextField txtUPEAmount;
    public TableView<Expense> tblExpenses;
    public TableColumn colExpenseID;
    public TableColumn colType;
    public TableColumn colDescription;
    public TableColumn colAmount;
    public TableColumn colDate;
    public TableColumn colTime;
    public AnchorPane ExpenseMainPane;
    public JFXComboBox cmbSortBy;
    public JFXTextField txtSearchExpense;

    public void addExpenseOnAction(ActionEvent actionEvent) {
  addExpense();
    }

    public void addExpense(){
        try{
            if(ExpenseCRUDController.addExpense(txtExpenseID.getText(),(String)cmbExpenseType.getValue(),txtExpenseDesc.getText(),Double.valueOf(txtExpenseAmount.getText()), LocalDate.now(), LocalTime.now())){
                Notifications notify = Notifications.create().title("Expense Sucessfully Added!").darkStyle();
                notify.show();
            }else{
                Alert alert = new Alert(Alert.AlertType.ERROR,"OOps, SOmething Went Wrong!", ButtonType.OK);
                alert.initOwner(ExpenseMainPane.getScene().getWindow());
                alert.show();
            }
        }catch(SQLException|ClassNotFoundException e){
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR,e.getMessage(), ButtonType.OK);
            alert.initOwner(ExpenseMainPane.getScene().getWindow());
            alert.show();

        }
        setAllExpenses();
    }

    public void deleteExpenseOnAction(ActionEvent actionEvent) {
            try{
                if(ExpenseCRUDController.deleteExpense(txtUPEID.getText())){
                    Notifications notify = Notifications.create().title("Expense Deleted ").darkStyle();
                    notify.show();
                }else{
                    Alert alert = new Alert(Alert.AlertType.ERROR,"OOps, SOmething Went Wrong!", ButtonType.OK);
                    alert.initOwner(ExpenseMainPane.getScene().getWindow());
                    alert.show();
                }
            }catch(SQLException | ClassNotFoundException e){
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR,e.getMessage(), ButtonType.OK);
                alert.initOwner(ExpenseMainPane.getScene().getWindow());
                alert.show();
            }
            setAllExpenses();
    }

    public void updateExpenseOnACtion(ActionEvent actionEvent) {
        try{
            if(ExpenseCRUDController.updateExpense(txtUPEID.getText(),(String)cmbUPEType.getValue(),txtUPEDESC.getText(),Double.valueOf(txtUPEAmount.getText()),LocalDate.now(),LocalTime.now())){
                Notifications notify = Notifications.create().title("Expense Updated!").darkStyle();
                notify.show();
            }else{
                Alert alert = new Alert(Alert.AlertType.ERROR,"OOps, SOmething Went Wrong!", ButtonType.OK);
                alert.initOwner(ExpenseMainPane.getScene().getWindow());
                alert.show();
            }
        }catch(SQLException | ClassNotFoundException e){
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR,e.getMessage(), ButtonType.OK);
            alert.initOwner(ExpenseMainPane.getScene().getWindow());
            alert.show();
        }
        setAllExpenses();
    }


    public void addExpenseTypeOnAction(ActionEvent actionEvent) {
        try {
            if(ExpenseCRUDController.addExpenseType(txtExpenseType.getText())){
                Notifications notify = Notifications.create().title("Expense Type Sucessfully Added!").darkStyle();
                notify.show();
                cmbExpenseType.getItems().add(txtExpenseType.getText());
                cmbUPEType.getItems().add(txtExpenseType.getText());
                cmbSortBy.getItems().add(txtExpenseType.getText());
            }else{
                Alert alert = new Alert(Alert.AlertType.ERROR,"OOps, SOmething Went Wrong!", ButtonType.OK);
                alert.initOwner(ExpenseMainPane.getScene().getWindow());
                alert.show();
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR,e.getMessage(), ButtonType.OK);
            alert.initOwner(ExpenseMainPane.getScene().getWindow());
            alert.show();
        }
    }

    public void searchREcordOnACtion(ActionEvent actionEvent) {
        try {
            Expense e = ExpenseCRUDController.getExpense(txtUPEID.getText());
            if(e!=null){
                txtUPEDESC.setText(e.getDescription());
                txtUPEAmount.setText(String.valueOf(e.getAmount()));
                cmbUPEType.getSelectionModel().select(e.getExpenseType());
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException classNotFoundException) {
            classNotFoundException.printStackTrace();
        }
    }

    LinkedHashMap<TextField, Pattern> RegexPattern = new LinkedHashMap<>();
    LinkedHashMap<TextField, Pattern> RegexUpdatePattern = new LinkedHashMap<>();

    public void initialize(){
        btnAddExpense.setDisable(true);
        btnUpdateExpense.setDisable(true);
        btnDeleteExpense.setDisable(true);
        colExpenseID.setCellValueFactory(new PropertyValueFactory<>("expenseID"));
        colType.setCellValueFactory(new PropertyValueFactory<>("expenseType"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colTime.setCellValueFactory(new PropertyValueFactory<>("time"));

        Pattern ExpenseId = Pattern.compile("^(E00-)[0-9]{3}$");
        Pattern description = Pattern.compile("^(.)+$");
        Pattern Amount = Pattern.compile("^[0-9]+((.)[0-9]{1,3})?$");

        RegexPattern.put(txtExpenseID,ExpenseId);
        RegexPattern.put(txtExpenseDesc,description);
        RegexPattern.put(txtExpenseAmount,Amount);

        RegexUpdatePattern.put(txtUPEID,ExpenseId);
        RegexUpdatePattern.put(txtUPEDESC,description);
        RegexUpdatePattern.put(txtUPEAmount,Amount);


        setAllExpenses();
        tblExpenses.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
        if(newValue instanceof Expense) {
            Expense e = (Expense) newValue;
            txtUPEID.setText(e.getExpenseID());
            txtUPEDESC.setText(e.getDescription());
            txtUPEAmount.setText(String.valueOf(e.getAmount()));
            cmbUPEType.getSelectionModel().select(e.getExpenseType());

            ValidationUtil.Validate(RegexUpdatePattern,btnUpdateExpense);
            if(btnUpdateExpense.isDisable()){
                btnDeleteExpense.setDisable(true);
            }else{
                btnDeleteExpense.setDisable(false);
            }
        }
        });
        try{
            cmbSortBy.getItems().add("ALL");
            cmbExpenseType.getItems().addAll(ExpenseCRUDController.getAllExpenseTypes());
            cmbUPEType.getItems().addAll(ExpenseCRUDController.getAllExpenseTypes());
            cmbSortBy.getItems().addAll(ExpenseCRUDController.getAllExpenseTypes());

            cmbSortBy.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if(String.valueOf(newValue).equals("ALL")){
                    obList.clear();
                    try {
                        obList.addAll(ExpenseCRUDController.getAllExpenses());
                        tblExpenses.refresh();
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }else{
                    obList.clear();
                    try {
                        obList.addAll(ExpenseCRUDController.getCategoryExpenses(String.valueOf(newValue)));
                        tblExpenses.refresh();
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            });
        }catch(SQLException | ClassNotFoundException e){
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR,e.getMessage(), ButtonType.OK);
            alert.initOwner(ExpenseMainPane.getScene().getWindow());
            alert.show();
        }

    }
    ObservableList<Expense> obList;
    private void setAllExpenses() {
        try {
             obList = FXCollections.observableArrayList();
            obList.addAll(ExpenseCRUDController.getAllExpenses());
            tblExpenses.setItems(obList);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR,e.getMessage(), ButtonType.OK);
            alert.initOwner(ExpenseMainPane.getScene().getWindow());
            alert.show();
        }
    }

    public void searchExpenseOnAction(ActionEvent actionEvent) {
        String search = txtSearchExpense.getText().toUpperCase();
        ArrayList<Expense> addToTable = new ArrayList<>();
        try {
            ArrayList<Expense> ExpenseList = ExpenseCRUDController.getAllExpenses();
            for (Expense e : ExpenseList){
                if(e.getExpenseID().toUpperCase().contains(search)||e.getExpenseType().toUpperCase().contains(search)||e.getDescription().toUpperCase().contains(search)){
                    addToTable.add(e);
                }
            }
            if(addToTable.isEmpty()){
                obList.clear();
                obList.addAll(ExpenseList);
            }else{
                obList.clear();
                obList.addAll(addToTable);
            }
            ValidationUtil.Validate(RegexUpdatePattern,btnUpdateExpense);
            if(btnUpdateExpense.isDisable()){
                btnDeleteExpense.setDisable(true);
            }else{
                btnDeleteExpense.setDisable(false);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void validateAddExpenseFields(KeyEvent keyEvent) {
        Object o = ValidationUtil.Validate(RegexPattern,btnAddExpense);

        if(keyEvent.getCode() == KeyCode.ENTER){
            if(o instanceof TextField){
                TextField field = (TextField) o;
                field.requestFocus();
            }else if(cmbExpenseType.getValue()!=null){
                btnAddExpense.setDisable(false);
                addExpense();
            }else{
                btnAddExpense.setDisable(true);
            }
        }
    }

    public void validateUpdateFields(KeyEvent keyEvent) {
        Object o = ValidationUtil.Validate(RegexUpdatePattern,btnUpdateExpense);


        if(cmbUPEType.getValue()!=null){
                btnAddExpense.setDisable(false);
            }else{
                btnAddExpense.setDisable(true);
        }
        if(btnUpdateExpense.isDisable()){
            btnDeleteExpense.setDisable(true);
        }else{
            btnDeleteExpense.setDisable(false);
        }
    }
}
