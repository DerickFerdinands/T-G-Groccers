package Controller;

import Controller.CRUD.ItemCRUDController;
import Model.Item;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;
import java.util.ArrayList;

public class StockManagementFormController {
    public TableView<Item> tblLowStock;
    public TableColumn colItemCode;
    public TableColumn colName;
    public TableColumn colCategory;
    public TableColumn colAvailableQty;
    public TableColumn colBuyingPrice;
    public TableColumn colSellingPrice;
    public TableColumn colImage;

    public void initialize(){
         colItemCode.setCellValueFactory(new PropertyValueFactory<>("ItemCode"));
         colName.setCellValueFactory(new PropertyValueFactory<>("description"));
         colCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
         colAvailableQty.setCellValueFactory(new PropertyValueFactory<>("availableQty"));
         colBuyingPrice.setCellValueFactory(new PropertyValueFactory<>("buyingPrice"));
         colSellingPrice.setCellValueFactory(new PropertyValueFactory<>("sellingPrice"));
         colImage.setCellValueFactory(new PropertyValueFactory<>("Image_path"));
         
         setAllLowStock();
    }

    private void setAllLowStock() {
        try {
            ObservableList<Item> obList = FXCollections.observableArrayList();
            obList.addAll(ItemCRUDController.getLowStock());
            tblLowStock.setItems(obList);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
