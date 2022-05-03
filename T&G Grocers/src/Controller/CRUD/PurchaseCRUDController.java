package Controller.CRUD;

import Util.CrudUtil;
import View.TM.OrderTM;
import View.TM.PurchaseTM;
import View.TM.SalesTM;
import com.jfoenix.controls.JFXButton;
import javafx.scene.paint.Color;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;

public class PurchaseCRUDController {
    public static boolean recordPurchase(String Enic, double totalPrice, String paymentType, String PaymentStatus) throws SQLException, ClassNotFoundException {
        boolean order =  CrudUtil.execute("INSERT INTO store_purchase VALUES(?,?,?,?,?)", getPurchaseID(), Enic, LocalDate.now(), LocalTime.now(), totalPrice);
        boolean payment = CrudUtil.execute("INSERT INTO purchase_payment VALUES(?,?,?,?,?)",getLastPurchaseID(),getLastPurchaseID(),paymentType,totalPrice,PaymentStatus);
        if(order&&payment){
            return true;
        }
        return false;
    }

    public static String getPurchaseID() throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT purchaseId FROM store_purchase ORDER BY purchaseId DESC LIMIT 1");
        if (result.next()) {
            String id = result.getString(1);
            String[] splitted = id.split("(D00-)");
            return String.format("D00-%03d", (Integer.valueOf(splitted[1]) + 1));
        }
        return "D00-001";
    }

    public static boolean savePurchaseDetails(String purchaseId, ArrayList<OrderTM> items) throws SQLException, ClassNotFoundException {
        for (OrderTM tm : items) {
            boolean recorded = CrudUtil.execute("INSERT INTO purchased_item_detail VALUES(?,?,?,?)", purchaseId, tm.getItemCode(), Double.valueOf(tm.getUnitPrice()), Double.valueOf(tm.getQty()));
            if (recorded == false) {
                return false;
            }
        }
        return true;
    }

    public static String getLastPurchaseID() throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT purchaseId FROM store_purchase ORDER BY purchaseId DESC LIMIT 1");
        if(result.next())return result.getString(1);
        return null;
    }

    public static ArrayList<PurchaseTM> getAllPurchases() throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT * FROM Store_purchase");
        ArrayList<PurchaseTM> purchaseList = new ArrayList<>();

        while(result.next()) {
            JFXButton btn = new JFXButton("View/Print Bill");
            btn.setStyle("-fx-background-color: Green");
            btn.setTextFill(Color.WHITE);
            purchaseList.add(new PurchaseTM(result.getString(1), result.getString(2),result.getDouble(5),LocalDate.parse(result.getDate(3).toString()),LocalTime.parse(result.getTime(4).toString()),btn));
        }
        return purchaseList;
    }

    public static ArrayList<PurchaseTM> getAnnualPurchases() throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT DISTINCT YEAR(`date`), SUM(totalPrice) FROM store_purchase GROUP BY YEAR(`date`);");
        ArrayList<PurchaseTM> SalesList = new ArrayList<>();
        while(result.next()){
            JFXButton btn = new JFXButton("View/Print Bill");
            btn.setStyle("-fx-background-color: Green");
            btn.setTextFill(Color.WHITE);
            SalesList.add(new PurchaseTM(null,null,result.getDouble(2),result.getString(1),null,btn));
        }
        return SalesList;
    }

    public static ArrayList<PurchaseTM> getMonthlyPurchases() throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT DISTINCT MONTH(`date`), SUM(totalPrice) FROM store_purchase GROUP BY MONTH(`date`) ;");
        ArrayList<PurchaseTM> SalesList = new ArrayList<>();
        while(result.next()){
            JFXButton btn = new JFXButton("View/Print Bill");
            btn.setStyle("-fx-background-color: Green");
            btn.setTextFill(Color.WHITE);
            SalesList.add(new PurchaseTM(null,null,result.getDouble(2), result.getInt(1),null,btn));
        }
        return SalesList;

    }

    public static ArrayList<PurchaseTM> getDailyPurchases() throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT `Date`, SUM(totalPrice) FROM store_purchase GROUP BY `date` ;");
        ArrayList<PurchaseTM> SalesList = new ArrayList<>();
        while(result.next()){
            JFXButton btn = new JFXButton("View/Print Bill");
            btn.setStyle("-fx-background-color: Green");
            btn.setTextFill(Color.WHITE);
            SalesList.add(new PurchaseTM(null,null,result.getDouble(2),result.getDate(1),null,btn));
        }
        return SalesList;
    }
}
