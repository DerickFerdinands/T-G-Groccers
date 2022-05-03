package Model;

import java.time.LocalDate;

public class Delivery {
    private String deliveryID;
    private LocalDate date;
    private String vehicelNUmber;
    private String DriverID;
    private String deliveryStatus;
    private String OrderID;
    private String customerID;

    public Delivery() {
    }

    public Delivery(String deliveryID, LocalDate date, String vehicelNUmber, String driverID, String deliveryStatus, String orderID, String customerID) {
        this.deliveryID = deliveryID;
        this.date = date;
        this.vehicelNUmber = vehicelNUmber;
        DriverID = driverID;
        this.deliveryStatus = deliveryStatus;
        OrderID = orderID;
        this.customerID = customerID;
    }

    @Override
    public String toString() {
        return "Delivery{" +
                "deliveryID='" + deliveryID + '\'' +
                ", date=" + date +
                ", vehicelNUmber='" + vehicelNUmber + '\'' +
                ", DriverID='" + DriverID + '\'' +
                ", deliveryStatus='" + deliveryStatus + '\'' +
                ", OrderID='" + OrderID + '\'' +
                ", customerID='" + customerID + '\'' +
                '}';
    }

    public String getDeliveryID() {
        return deliveryID;
    }

    public void setDeliveryID(String deliveryID) {
        this.deliveryID = deliveryID;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getVehicelNUmber() {
        return vehicelNUmber;
    }

    public void setVehicelNUmber(String vehicelNUmber) {
        this.vehicelNUmber = vehicelNUmber;
    }

    public String getDriverID() {
        return DriverID;
    }

    public void setDriverID(String driverID) {
        DriverID = driverID;
    }

    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public String getOrderID() {
        return OrderID;
    }

    public void setOrderID(String orderID) {
        OrderID = orderID;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }
}