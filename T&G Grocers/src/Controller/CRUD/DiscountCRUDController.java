package Controller.CRUD;

import Model.Discount;
import Util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DiscountCRUDController {

    public static boolean addDiscount(String OrderID, String CustomerNIC, double totalPrice, double DiscountGiven, double discountedPrice) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("INSERT INTO Discount VALUES(?,?,?,?,?)",OrderID,CustomerNIC,totalPrice,DiscountGiven,discountedPrice);
    }

    public static ArrayList<Discount> getALlDiscounts() throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT * FROM Discount");
        ArrayList<Discount> list = new ArrayList<>();
        while(result.next()) {
            list.add(new Discount(result.getString(1),result.getString(2), result.getDouble(3),result.getDouble(4),result.getDouble(5)));
        }
        return list;
    }
}
