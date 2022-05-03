package Controller;

import Controller.CRUD.CustomerCRUDController;
import Controller.CRUD.DiscountCRUDController;
import Database.DBConnection;
import Model.Customer;
import Model.Discount;
import View.TM.DiscountTM;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class ViewDiscountsFormController {
    public JFXTextField txtSalesSearch;
    public JFXComboBox cmbCat;
    public TableView<DiscountTM> tblDiscounts;
    public TableColumn colOrderID;
    public TableColumn colNIC;
    public TableColumn colTotalPrice;
    public TableColumn colDiscountGiven;
    public TableColumn colDiscountedPrice;
    public TableColumn colOption;
    public TableView tblCatDiscount;
    public TableColumn colDate;
    public TableColumn colCatTotalPrice;
    public TableColumn colCatDiscountGiven;
    public TableColumn colCatDiscountedPrice;
    public TableColumn colCatOption;

    public void SearchDIscounts(KeyEvent keyEvent) {
        String search = txtSalesSearch.getText().toUpperCase();
        try {
            ArrayList<Discount> discountList = DiscountCRUDController.getALlDiscounts();
            ArrayList<DiscountTM> tempList = new ArrayList<>();
            for (Discount d: discountList){
                if(d.getOrderID().toUpperCase().contains(search)||d.getCustomerNIC().toUpperCase().contains(search)){
                    JFXButton btn = new JFXButton("View/ Print Reciept");
                    btn.setStyle("-fx-border-color: Black");
                    btn.setOnAction(event -> {
                        try {
                            HashMap map = new HashMap();
                            map.put("OrderID", d.getOrderID());
                            map.put("CustomerNIC", d.getCustomerNIC());
                            Customer c = CustomerCRUDController.getCustomer(d.getCustomerNIC());
                            map.put("CustomerName", c.getCustomerName());
                            map.put("CustomerMobile", c.getCustomerMobile());
                            map.put("Total Price", d.getTotalPrice());
                            map.put("Discount", d.getDiscountGiven());
                            map.put("Discounted Price", d.getDiscountedPrice());
                            JasperReport compiledReport = (JasperReport) JRLoader.loadObject(this.getClass().getResourceAsStream("/Reports/DiscountDetail.jasper"));
                            JasperPrint jasperPrint = JasperFillManager.fillReport(compiledReport, map, DBConnection.getInstance().getConnection());
                            JasperViewer.viewReport(jasperPrint, false);
                        } catch (SQLException | ClassNotFoundException | JRException e) {
                            e.printStackTrace();
                        }
                    });
                    tempList.add(new DiscountTM(d.getOrderID(),d.getCustomerNIC(),d.getTotalPrice(),d.getDiscountGiven(),d.getDiscountedPrice(),btn));
                }
            }
            if(tempList.isEmpty()){
                loadAllDiscounts();
            }else{
                obList.clear();
                obList.addAll(tempList);
                tblDiscounts.refresh();
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void initialize() {
        colOrderID.setCellValueFactory(new PropertyValueFactory<>("OrderID"));
        colNIC.setCellValueFactory(new PropertyValueFactory<>("CustomerNIC"));
        colTotalPrice.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
        colDiscountGiven.setCellValueFactory(new PropertyValueFactory<>("discountGiven"));
        colDiscountedPrice.setCellValueFactory(new PropertyValueFactory<>("discountedPrice"));
        colOption.setCellValueFactory(new PropertyValueFactory<>("btn"));

        loadAllDiscounts();
    }
    ObservableList<DiscountTM> obList = FXCollections.observableArrayList();
    private void loadAllDiscounts() {
        obList.clear();
        try {
            ArrayList<Discount> list = DiscountCRUDController.getALlDiscounts();

            for (Discount d : list) {
                JFXButton btn = new JFXButton("View/ Print Reciept");
                btn.setStyle("-fx-border-color: Black");
                btn.setOnAction(event -> {
                    try {
                        HashMap map = new HashMap();
                        map.put("OrderID", d.getOrderID());
                        map.put("CustomerNIC", d.getCustomerNIC());
                        Customer c = CustomerCRUDController.getCustomer(d.getCustomerNIC());
                        map.put("CustomerName", c.getCustomerName());
                        map.put("CustomerMobile", c.getCustomerMobile());
                        map.put("Total Price", d.getTotalPrice());
                        map.put("Discount", d.getDiscountGiven());
                        map.put("Discounted Price", d.getDiscountedPrice());
                        JasperReport compiledReport = (JasperReport) JRLoader.loadObject(this.getClass().getResourceAsStream("/Reports/DiscountDetail.jasper"));
                        JasperPrint jasperPrint = JasperFillManager.fillReport(compiledReport, map, DBConnection.getInstance().getConnection());
                        JasperViewer.viewReport(jasperPrint, false);
                    } catch (SQLException | ClassNotFoundException | JRException e) {
                        e.printStackTrace();
                    }
                });
                obList.add(new DiscountTM(d.getOrderID(), d.getCustomerNIC(), d.getTotalPrice(), d.getDiscountGiven(), d.getDiscountedPrice(), btn));
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        tblDiscounts.setItems(obList);
    }
}
