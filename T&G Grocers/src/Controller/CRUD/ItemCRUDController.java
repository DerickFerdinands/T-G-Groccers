package Controller.CRUD;

import Model.Item;
import Util.CrudUtil;
import com.mysql.cj.protocol.Resultset;
import javafx.scene.control.TextField;

import javax.xml.transform.Result;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;

public class ItemCRUDController {

    public static ArrayList<Item> getAllItems() throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT * FROM Item");
        ArrayList<Item> ItemList = new ArrayList<>();
        while (result.next()) {
            ItemList.add(new Item(result.getString(1), result.getString(2), result.getString(3), result.getString(4), result.getDouble(5), result.getDouble(6), result.getDouble(7), result.getString(8)));
        }
        return ItemList;
    }

    public static Item getItem(String itemCode) throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT * FROM Item WHERE itemcode=?", itemCode);
        if (result.next()) {
            return new Item(result.getString(1), result.getString(2), result.getString(3), result.getString(4), result.getDouble(5), result.getDouble(6), result.getDouble(7), result.getString(8));
        }
        return null;
    }

    public static boolean deleteItem(String ItemCode) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM Item WHERE ItemCode=?", ItemCode);
    }

    public static boolean updateItem(String ItemCode, String ItemName, String category, String unit, double buyingPrice, double sellingPrice, double AvailableQty, String ImageLocation) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("UPDATE Item SET description=?,Category=?,unit=?,buying_Price=?,selling_Price=?,available_qty=?,Item_Icon=? WHERE ItemCode=?", ItemName, category, unit, buyingPrice, sellingPrice, AvailableQty, ImageLocation, ItemCode);
    }

    public static boolean saveItem(String ItemCode, String ItemName, String category, String unit, double buyingPrice, double sellingPrice, double AvailableQty, String ImageLocation) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("INSERT INTO Item VALUES(?,?,?,?,?,?,?,?)", ItemCode, ItemName, category, unit, buyingPrice, sellingPrice, AvailableQty, ImageLocation);
    }

    public static ArrayList<String> getAllItemCodes() throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT ItemCode FROM Item");
        ArrayList<String> ItemCodes = new ArrayList<>();
        while(result.next()) {
            ItemCodes.add(result.getString(1));
        }
        return ItemCodes;
    }

    public static ArrayList<String> getDistinctItemCategories() throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT DISTINCT Category FROM ITEM");
        ArrayList<String> distinctCategories = new ArrayList<>();
        while(result.next()){
            distinctCategories.add(result.getString(1));
        }
        return distinctCategories;
    }

    public static int getItemCount() throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT COUNT(ItemCODE) FROM Item");
        if(result.next()){
            return result.getInt(1);
        }
        return 0;
    }

    public static boolean updateItemQty(String itemCOde, double qty) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("UPDATE Item SET available_qty=available_qty-? WHERE ItemCode=?",qty, itemCOde);
    }
    public static boolean addItemQty(String itemCOde, double qty) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("UPDATE Item SET available_qty=available_qty+? WHERE ItemCode=?",qty, itemCOde);
    }

    public static ArrayList<Item> getLowStock() throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT * FROM Item WHERE available_qty<=10");
        ArrayList<Item> lowStock = new ArrayList<>();

        while(result.next()){
            lowStock.add(new Item(result.getString(1), result.getString(2), result.getString(3), result.getString(4), result.getDouble(5), result.getDouble(6), result.getDouble(7), result.getString(8)));
        }
        return lowStock;
    }

    public static String getDescription(String itemCode) throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT Description FROM Item WHERE ItemCode=?",itemCode);
        if(result.next()){
            return result.getString(1);
        }
        return null;
    }

    public static ArrayList<Item> getDistinctItemSales() throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT Itemcode,SUM(qty) FROM order_detail GROUP BY ItemCode");
        ArrayList<Item> itemList = new ArrayList<>();

        while(result.next()){
            itemList.add(new Item(result.getString(1),getDescription(result.getString(1)),null,null,result.getDouble(2),0,0,null));
        }
        System.out.println(itemList.size());
        return itemList;

    }

}
