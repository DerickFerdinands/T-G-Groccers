package Controller.CRUD;

import Model.Payment;
import Util.CrudUtil;
import javafx.scene.control.Alert;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PaymentCRUDController {
    public static ArrayList<Payment> getAllPayments() throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT * FROM Payment");
        ArrayList<Payment>  paymentList= new ArrayList<>();
        while(result.next()) {
            paymentList.add(new Payment(result.getString(1),result.getString(2),result.getString(3),result.getDouble(4),result.getString(5)));
        }
        return paymentList;
    }

    public static ArrayList<Payment> getAllPendingPayments() throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT * FROM Payment WHERE paymentStatus='Pending'");
        ArrayList<Payment>  paymentList= new ArrayList<>();
        while(result.next()) {
            paymentList.add(new Payment(result.getString(1),result.getString(2),result.getString(3),result.getDouble(4),result.getString(5)));
        }
        return paymentList;
    }

    public static boolean updatePaymentStatus(String paymentID, String status) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("UPDATE Payment SET paymentStatus=? WHERE PaymentID=?",status,paymentID);
    }

    public static boolean updatePurchasePaymentStatus(String paymentID, String status) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("UPDATE Purchase_payment SET paymentStatus=? WHERE paymentId=?",status,paymentID);
    }

    public static ArrayList<Payment> getAllPurchasesOnCredit() throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT * FROM Purchase_payment WHERE paymentStatus='Pending'");
        ArrayList<Payment>  paymentList= new ArrayList<>();
        while(result.next()) {
            paymentList.add(new Payment(result.getString(1),result.getString(2),result.getString(3),result.getDouble(4),result.getString(5)));
        }
        return paymentList;
    }
}
