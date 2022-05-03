package Controller.CRUD;

import Model.Customer;
import Model.Order;
import Util.CrudUtil;
import View.TM.OrderTM;
import View.TM.SalesTM;
import com.jfoenix.controls.JFXButton;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import net.sf.jasperreports.engine.util.JRLoader;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;

public class OrderCrudController {
    public static boolean recordOrder(String Cnic, double totalPrice, String paymentType, String PaymentStatus) throws SQLException, ClassNotFoundException {
        boolean order = CrudUtil.execute("INSERT INTO `Order` VALUES(?,?,?,?,?)", getOrderID(), Cnic, LocalDate.now(), LocalTime.now(), totalPrice);
        boolean payment = CrudUtil.execute("INSERT INTO Payment VALUES(?,?,?,?,?)", getLastOrderID(), getLastOrderID(), paymentType, totalPrice, PaymentStatus);
        return order && payment;
    }

    public static String getOrderID() throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT orderID FROM `Order`ORDER BY orderID DESC LIMIT 1;");
        if (result.next()) {
            String id = result.getString(1);
            String[] splitted = id.split("(O-)");
            return String.format("O-%03d", (Integer.valueOf(splitted[1]) + 1));
        }
        return "O00-001";
    }

    public static boolean saveOrderDetails(String OrderID, ArrayList<OrderTM> items) throws SQLException, ClassNotFoundException {
        for (OrderTM tm : items) {
            boolean recorded = CrudUtil.execute("INSERT INTO ORDER_DETAIL VALUES(?,?,?,?)", OrderID, tm.getItemCode(), Double.valueOf(tm.getUnitPrice()), Double.valueOf(tm.getQty()));
            if (recorded == false) {
                return false;
            }
        }
        return true;
    }

    public static String getLastOrderID() throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT orderID FROM `Order`ORDER BY orderID DESC LIMIT 1");
        if (result.next()) return result.getString(1);
        return null;
    }

    public static ArrayList<Order> getDistinctSalesDates() throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT `date`, SUM(totalPrice) FROM `Order` GROUP BY `Date`");
        ArrayList<Order> dailySales = new ArrayList<>();
        while (result.next()) {
            dailySales.add(new Order(null, null, LocalDate.parse(result.getDate(1).toString()), null, result.getDouble(2)));
        }
        return dailySales;
    }

    public static ArrayList<SalesTM> getAllOrders() throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT o.orderID, c.NIC, c.name, o.totalPrice, o.`Date`, o.`time` FROM `Order` o JOIN Customer c ON o.CustomerNIC = c.NIC");
        ArrayList<SalesTM> SalesList = new ArrayList<>();

        while (result.next()) {
            JFXButton btn = new JFXButton("View/Print Reciept");
            btn.setStyle("-fx-background-color:  Green");
            btn.setTextFill(Color.color(1.00, 1.00, 1.00));
            SalesList.add(new SalesTM(result.getString(1), result.getString(2), result.getDouble(4), LocalDate.parse(result.getDate(5).toString()), LocalTime.parse(result.getTime(6).toString()), btn));
        }

        return SalesList;
    }

    public static ArrayList<SalesTM> getAnnualOrders() throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT DISTINCT YEAR(`date`), SUM(totalPrice) FROM `Order` GROUP BY YEAR(`date`);");
        ArrayList<SalesTM> SalesList = new ArrayList<>();
        while (result.next()) {
            JFXButton btn = new JFXButton("View/Print Reciept");
            btn.setStyle("-fx-background-color:  Green");
            btn.setTextFill(Color.color(1.00, 1.00, 1.00));
            SalesList.add(new SalesTM(null, null, result.getDouble(2), result.getString(1), null, btn));
        }
        return SalesList;
    }

    public static ArrayList<SalesTM> getMonthlyOrders() throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT DISTINCT MONTH(`date`), SUM(totalPrice) FROM `Order` GROUP BY MONTH(`date`) ;");
        ArrayList<SalesTM> SalesList = new ArrayList<>();
        while (result.next()) {
            JFXButton btn = new JFXButton("View/Print Reciept");
            btn.setStyle("-fx-background-color:  Green");
            btn.setTextFill(Color.color(1.00, 1.00, 1.00));
            SalesList.add(new SalesTM(null, null, result.getDouble(2),result.getInt(1), null, btn));
        }
        return SalesList;

    }

    public static ArrayList<SalesTM> getDailyOrders() throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT `Date`, SUM(totalPrice) FROM `Order` GROUP BY `date` ;");
        ArrayList<SalesTM> SalesList = new ArrayList<>();
        while (result.next()) {
            JFXButton btn = new JFXButton("View/Print Reciept");
            btn.setStyle("-fx-background-color:  Green");
            btn.setTextFill(Color.color(1.00, 1.00, 1.00));
            SalesList.add(new SalesTM(null, null, result.getDouble(2), result.getDate(1), null, btn));
        }
        return SalesList;
    }

    public static ArrayList<Order> getDistinctCustomerSales() throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT CustomerNIC, SUM(totalPrice) FROM `Order` GROUP BY CustomerNIC");
        ArrayList<Order> orders = new ArrayList<>();
        while(result.next()) {
            orders.add(new Order(null,result.getString(1),null,null,result.getDouble(2)));
        }
        return orders;
    }
}
