package Controller.CRUD;

import Model.Delivery;
import Util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class DeliveryCRUDController {

    public static ArrayList<Delivery> getAllDeliveryDetails() throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT * FROM delivery_detail");
        ArrayList<Delivery> deliveryList = new ArrayList<>();

        while(result.next()){
            deliveryList.add(new Delivery(result.getString(1), LocalDate.parse(result.getDate(2).toString()),result.getString(3),result.getString(4),result.getString(5),result.getString(6),result.getString(7)));
        }
        return deliveryList;
    }

    public static boolean addDelivery(String deliverID, LocalDate date, String vehicleNumber,String driverID, String deliveryStatus, String orderID, String customerID) throws SQLException, ClassNotFoundException {
    return CrudUtil.execute("INSERT INTO delivery_detail VALUES (?,?,?,?,?,?,?)",deliverID,date,vehicleNumber,driverID,deliveryStatus,orderID,customerID);
    }

    public static boolean updateDeleiveryStatus(String deliveryID, String deliverStatus) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("UPDATE delivery_detail SET Delivery_Status=? WHERE DeliverID=?",deliverStatus,deliveryID);
    }

    public static ArrayList<Delivery> getPendingDeliveries() throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT * FROM delivery_detail WHERE Delivery_Status=?","Pending");
        ArrayList<Delivery> pendingDeliveries = new ArrayList<>();

        while(result.next()){
            pendingDeliveries.add(new Delivery(result.getString(1), LocalDate.parse(result.getDate(2).toString()),result.getString(3),result.getString(4),result.getString(5),result.getString(6),result.getString(7)));
        }
        return pendingDeliveries;
    }
    public static ArrayList<Delivery> getDeliveredHistory() throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT * FROM delivery_detail WHERE Delivery_Status=?","Delivered");
        ArrayList<Delivery> pendingDeliveries = new ArrayList<>();

        while(result.next()){
            pendingDeliveries.add(new Delivery(result.getString(1), LocalDate.parse(result.getDate(2).toString()),result.getString(3),result.getString(4),result.getString(5),result.getString(6),result.getString(7)));
        }
        return pendingDeliveries;
    }

    public static String getDeliveryID() throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT DeliverID FROM delivery_detail ORDER BY DeliverID DESC LIMIT 1");

        if(result.next()){
            String[] splitted = result.getString(1).split("D00-");
            return String.format("D00-%03d",Integer.valueOf(splitted[1])+1);
        }else{
           return  "D00-0001";
        }

    }

    public static boolean deleteDelivery(String deliveryID) throws SQLException, ClassNotFoundException {
        return  CrudUtil.execute("DELETE FROM delivery_detail WHERE DeliverID=?",deliveryID);
    }
}
