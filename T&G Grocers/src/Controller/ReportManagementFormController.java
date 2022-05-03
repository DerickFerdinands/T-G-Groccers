package Controller;

import Controller.CRUD.CustomerCRUDController;
import Controller.CRUD.OrderCrudController;
import Controller.CRUD.PurchaseCRUDController;
import Controller.CRUD.VendorCRUDController;
import Database.DBConnection;
import Model.Customer;
import Model.Vendor;
import View.TM.OrderTM;
import View.TM.PurchaseTM;
import View.TM.SalesTM;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class ReportManagementFormController {

    public AnchorPane NavPane;
    public AnchorPane MainPane;
    public TableView<SalesTM> tblSales;
    public TableColumn colOrderID;
    public TableColumn colNIC;
    public TableColumn colTotalPrice;
    public TableColumn colDate;
    public TableColumn colCost;
    public TableColumn colOption;
    public JFXComboBox cmbSales;
    public TableView tblPurchases;
    public TableColumn colPurchaseID;
    public TableColumn colVendorNIC;
    public TableColumn colPurchaseTotalPrice;
    public TableColumn colPurchaseDate;
    public TableColumn colTime;
    public TableColumn colViewReport;
    public JFXComboBox cmbPurchaseRecords;
    public JFXTextField txtPurchaseSearch;
    public JFXTextField txtSalesSearch;


    public void setUI(String URI) throws IOException {
        NavPane.getChildren().clear();
        NavPane.getChildren().add(FXMLLoader.load(getClass().getResource("../View/"+URI+".fxml")));
    }

    public void initialize(){
        cmbSales.getItems().addAll("All","Annually","Monthly","Daily");
        cmbPurchaseRecords.getItems().addAll("All","Annually","Monthly","Daily");
        cmbPurchaseRecords.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            try {
                switch ((String) newValue) {
                    case "All": {
                        purchaseObList.clear();
                        setAllPurchases();
                        tblPurchases.refresh();
                    }
                    break;
                    case "Annually": {
                        purchaseObList.clear();
                        purchaseObList.addAll(PurchaseCRUDController.getAnnualPurchases());
                        for (PurchaseTM tm : purchaseObList){
                            tm.getBtn().setOnAction(event -> {
                               try{
                                   HashMap map = new HashMap();
                                   map.put("Year",tm.getDate());

                                   JasperReport compiledReport = (JasperReport) JRLoader.loadObject(this.getClass().getResourceAsStream("/Reports/AnnualPurchaseReport.jasper"));
                                   JasperPrint jasperPrint = JasperFillManager.fillReport(compiledReport, map, DBConnection.getInstance().getConnection());
                                    JasperViewer.viewReport(jasperPrint,false);
                               }catch(SQLException | ClassNotFoundException | JRException e){
                                   e.printStackTrace();
                                   Alert alert = new Alert(Alert.AlertType.ERROR,e.getMessage(), ButtonType.OK);
                                   alert.initOwner(MainPane.getScene().getWindow());
                                   alert.show();
                               }
                            });
                        }
                        tblPurchases.refresh();
                    }
                    break;
                    case "Monthly": {
                        purchaseObList.clear();
                        purchaseObList.addAll(PurchaseCRUDController.getMonthlyPurchases());
                        for (PurchaseTM tm : purchaseObList){
                            tm.getBtn().setOnAction(event -> {
                                try {
                                    HashMap map = new HashMap();
                                    map.put("Month", tm.getDate());
                                    map.put("MonthName", String.valueOf(Month.of((Integer) tm.getDate())));

                                    JasperReport compiledReport = (JasperReport) JRLoader.loadObject(this.getClass().getResourceAsStream("/Reports/MonthlyPurchaseReport.jasper"));
                                    JasperPrint jasperPrint = JasperFillManager.fillReport(compiledReport, map, DBConnection.getInstance().getConnection());
                                    JasperViewer.viewReport(jasperPrint,false);
                                }catch(ClassNotFoundException | SQLException | JRException e){
                                    e.printStackTrace();
                                    Alert alert = new Alert(Alert.AlertType.ERROR,e.getMessage(), ButtonType.OK);
                                    alert.initOwner(MainPane.getScene().getWindow());
                                    alert.show();
                                }
                            });
                        }
                        tblPurchases.refresh();
                    }
                    break;
                    case "Daily": {
                        purchaseObList.clear();
                        purchaseObList.addAll(PurchaseCRUDController.getDailyPurchases());
                        for (PurchaseTM tm : purchaseObList){
                            tm.getBtn().setOnAction(event -> {
                                try{
                                    HashMap map = new HashMap();
                                    map.put("Date",tm.getDate());

                                    JasperReport compiledReport = (JasperReport) JRLoader.loadObject(this.getClass().getResourceAsStream("/Reports/DailyPurchaseRecord.jasper"));
                                    JasperPrint jasperPrint = JasperFillManager.fillReport(compiledReport, map, DBConnection.getInstance().getConnection());
                                    JasperViewer.viewReport(jasperPrint,false);
                                }catch(SQLException | ClassNotFoundException | JRException e){
                                    e.printStackTrace();
                                    Alert alert = new Alert(Alert.AlertType.ERROR,e.getMessage(), ButtonType.OK);
                                    alert.initOwner(MainPane.getScene().getWindow());
                                    alert.show();
                                }
                            });
                        }
                        tblPurchases.refresh();
                    }
                    break;
                }
            }catch(SQLException | ClassNotFoundException e){
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR,e.getMessage(), ButtonType.OK);
                alert.initOwner(MainPane.getScene().getWindow());
                alert.show();
            }
        });
        cmbSales.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            try {
                switch ((String) newValue) {
                    case "All": {
                        obList.clear();
                        setAllsales();
                        tblSales.refresh();
                    }
                    break;
                    case "Annually": {
                        obList.clear();
                        obList.addAll(OrderCrudController.getAnnualOrders());
                        for(SalesTM tm : obList){
                            tm.getBtn().setOnAction(event -> {
                                try {
                                    HashMap map = new HashMap();
                                    map.put("Year", tm.getDate());

                                    JasperReport compiledReport = (JasperReport) JRLoader.loadObject(this.getClass().getResourceAsStream("/Reports/AnnualOrderReport.jasper"));
                                    JasperPrint jasperPrint = JasperFillManager.fillReport(compiledReport, map, DBConnection.getInstance().getConnection());
                                    JasperViewer.viewReport(jasperPrint,false);

                                }catch(JRException | SQLException | ClassNotFoundException e){
                                    e.printStackTrace();
                                    Alert alert = new Alert(Alert.AlertType.ERROR,e.getMessage(), ButtonType.OK);
                                    alert.initOwner(MainPane.getScene().getWindow());
                                    alert.show();
                                }
                            });
                        }
                    }
                    break;
                    case "Monthly": {

                        obList.clear();
                        obList.addAll(OrderCrudController.getMonthlyOrders());
                        for(SalesTM tm : obList){
                            tm.getBtn().setOnAction(event -> {
                                try{

                                HashMap map = new HashMap();
                                map.put("Month", tm.getDate());
                                map.put("MonthName", String.valueOf(Month.of((Integer) tm.getDate())));


                                    JasperReport compiledReport = (JasperReport) JRLoader.loadObject(this.getClass().getResourceAsStream("/Reports/MonthlyOrderReport.jasper"));
                                    JasperPrint jasperPrint = JasperFillManager.fillReport(compiledReport, map, DBConnection.getInstance().getConnection());
                                    JasperViewer.viewReport(jasperPrint,false);
                                } catch (JRException | SQLException | ClassNotFoundException  e) {
                                    e.printStackTrace();
                                    Alert alert = new Alert(Alert.AlertType.ERROR,e.getMessage(), ButtonType.OK);
                                    alert.initOwner(MainPane.getScene().getWindow());
                                    alert.show();
                                }

                            });

                        }
                    }
                    break;
                    case "Daily": {
                        obList.clear();
                        obList.addAll(OrderCrudController.getDailyOrders());
                        for(SalesTM tm : obList){
                            tm.getBtn().setOnAction(event -> {
                                try{

                                    HashMap map = new HashMap();
                                    map.put("Date", tm.getDate());

                                    JasperReport compiledReport = (JasperReport) JRLoader.loadObject(this.getClass().getResourceAsStream("/Reports/DailyOrderReport.jasper"));
                                    JasperPrint jasperPrint = JasperFillManager.fillReport(compiledReport, map, DBConnection.getInstance().getConnection());
                                    JasperViewer.viewReport(jasperPrint,false);
                                } catch (JRException | SQLException | ClassNotFoundException  e) {
                                    e.printStackTrace();
                                    Alert alert = new Alert(Alert.AlertType.ERROR,e.getMessage(), ButtonType.OK);
                                    alert.initOwner(MainPane.getScene().getWindow());
                                    alert.show();
                                }
                            });
                        }
                    }
                    break;
                }
            }catch(SQLException | ClassNotFoundException e){
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR,e.getMessage(), ButtonType.OK);
                alert.initOwner(MainPane.getScene().getWindow());
                alert.show();
            }
        });

         colOrderID.setCellValueFactory(new PropertyValueFactory<>("OrderID"));
         colNIC.setCellValueFactory(new PropertyValueFactory<>("CustomerNIC"));
         colTotalPrice.setCellValueFactory(new PropertyValueFactory<>("TotalPrice"));
         colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
         colCost.setCellValueFactory(new PropertyValueFactory<>("time"));
         colOption.setCellValueFactory(new PropertyValueFactory<>("btn"));
        setAllsales();

        colPurchaseID.setCellValueFactory(new PropertyValueFactory<>("PurchaseID"));
        colVendorNIC.setCellValueFactory(new PropertyValueFactory<>("VendorNIC"));
        colPurchaseTotalPrice.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
        colPurchaseDate.setCellValueFactory(new PropertyValueFactory<>("Date"));
        colTime.setCellValueFactory(new PropertyValueFactory<>("time"));
        colViewReport.setCellValueFactory(new PropertyValueFactory<>("btn"));
        setAllPurchases();
    }

    ObservableList<PurchaseTM>  purchaseObList = FXCollections.observableArrayList();
    private void setAllPurchases() {
        try{
        purchaseObList.addAll(PurchaseCRUDController.getAllPurchases());
        for(PurchaseTM tm : purchaseObList){
            tm.getBtn().setOnAction(event -> {
                try {
                    Vendor v = VendorCRUDController.getVendor(tm.getVendorNIC());
                    HashMap map = new HashMap();
                    map.put("Vendor ID", tm.getVendorNIC());
                    map.put("Vendor Name",v.getNameOrCompany());
                    map.put("Vendor Contact",v.getContact());
                    map.put("PurchaseID",tm.getPurchaseID());
                    map.put("Date", Date.valueOf((LocalDate)tm.getDate()));
                    map.put("Time", Time.valueOf(tm.getTime()));
                    map.put("Total",tm.getTotalPrice());


                    JasperReport CompiledReport = (JasperReport) JRLoader.loadObject(this.getClass().getResourceAsStream("/Reports/StorePurchaseRecord.jasper"));
                    JasperPrint jasperPrint = JasperFillManager.fillReport(CompiledReport, map, DBConnection.getInstance().getConnection());
                    JasperViewer.viewReport(jasperPrint,false);

                }catch(SQLException | ClassNotFoundException | JRException e){
                    e.printStackTrace();
                    Alert alert = new Alert(Alert.AlertType.ERROR,e.getMessage(), ButtonType.OK);
                    alert.initOwner(MainPane.getScene().getWindow());
                    alert.show();
                }
            });
        }
        tblPurchases.setItems(purchaseObList);
    }catch(SQLException | ClassNotFoundException e){
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR,e.getMessage(), ButtonType.OK);
            alert.initOwner(MainPane.getScene().getWindow());
            alert.show();
        }
    }

    ObservableList<SalesTM>  obList = FXCollections.observableArrayList();

    private void setAllsales() {
        try {
            obList.addAll(OrderCrudController.getAllOrders());
            for(SalesTM tm : obList){
                tm.getBtn().setOnAction(event -> {
                    try {
                        Customer c = CustomerCRUDController.getCustomer(tm.getCustomerNIC());
                        HashMap map = new HashMap();
                        map.put("Customer NIC:", c.getCustomerNIC());
                        map.put("Customer Name:", c.getCustomerName());
                        map.put("Customer Contact:", c.getCustomerMobile());
                        map.put("OrderID", tm.getOrderID());
                        map.put("Total", tm.getTotalPrice());

                        JasperReport compiledReport = (JasperReport) JRLoader.loadObject(this.getClass().getResourceAsStream("/Reports/CustomerOrderReport.jasper"));
                        JasperPrint jasperPrint = JasperFillManager.fillReport(compiledReport, map, DBConnection.getInstance().getConnection());
                        JasperViewer.viewReport(jasperPrint,false);


                    } catch (JRException e) {
                        e.printStackTrace();
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                });
            }
            tblSales.setItems(obList);
        }catch(SQLException | ClassNotFoundException e){
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR,e.getMessage(), ButtonType.OK);
            alert.initOwner(MainPane.getScene().getWindow());
            alert.show();
        }
    }

    public void searchPurchases(KeyEvent keyEvent) {
        try {
            purchaseObList.clear();
            String search = txtPurchaseSearch.getText().toUpperCase();
            ArrayList<PurchaseTM> purchaseList = PurchaseCRUDController.getAllPurchases();
            for (PurchaseTM tm : purchaseList) {
                if (tm.getPurchaseID().toUpperCase().contains(search) || tm.getVendorNIC().toUpperCase().contains(search) || String.valueOf(tm.getTotalPrice()).toUpperCase().contains(search) || ((LocalDate) tm.getDate()).equals(search) || tm.getTime().equals(search)) {
                    purchaseObList.add(tm);
                    tblPurchases.refresh();
                }
            }
            if (purchaseObList.isEmpty()) {
            setAllPurchases();
            } else {
                tblPurchases.refresh();
            }
        }catch (SQLException|ClassNotFoundException e){
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR,e.getMessage(), ButtonType.OK);
            alert.initOwner(MainPane.getScene().getWindow());
            alert.show();
        }

    }

    public void SearchSales(KeyEvent keyEvent) {
        try {
            obList.clear();
            String search = txtSalesSearch.getText().toUpperCase();
            ArrayList<SalesTM> purchaseList = OrderCrudController.getAllOrders();
            for (SalesTM tm : purchaseList) {
                if (tm.getOrderID().toUpperCase().contains(search) || tm.getCustomerNIC().toUpperCase().contains(search) || String.valueOf(tm.getTotalPrice()).toUpperCase().contains(search) || ((LocalDate) tm.getDate()).equals(search) || tm.getTime().equals(search)) {
                    obList.add(tm);
                    tblSales.refresh();
                }
            }
            if (obList.isEmpty()) {
                setAllPurchases();
            } else {
                tblSales.refresh();
            }
        }catch (SQLException|ClassNotFoundException e){
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR,e.getMessage(), ButtonType.OK);
            alert.initOwner(MainPane.getScene().getWindow());
            alert.show();
        }

    }
}
