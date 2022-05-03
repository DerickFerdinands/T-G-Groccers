package View.TM;

import javafx.scene.control.Button;

import java.time.LocalDate;

public class DeliveryTM {
    private String DeliveryID;
    private String OrderID;
    private String CustomerID;
    private String DriverNic;
    private String VehicleNumber;
    private String DeliveryStatus;
    private LocalDate date;
    private Button btn;

    @Override
    public String toString() {
        return "DeliveryTM{" +
                "DeliveryID='" + DeliveryID + '\'' +
                ", OrderID='" + OrderID + '\'' +
                ", CustomerID='" + CustomerID + '\'' +
                ", DriverNic='" + DriverNic + '\'' +
                ", VehicleNumber='" + VehicleNumber + '\'' +
                ", DeliveryStatus='" + DeliveryStatus + '\'' +
                ", date=" + date +
                ", btn=" + btn +
                '}';
    }

    public String getDeliveryID() {
        return DeliveryID;
    }

    public void setDeliveryID(String deliveryID) {
        DeliveryID = deliveryID;
    }

    public String getOrderID() {
        return OrderID;
    }

    public void setOrderID(String orderID) {
        OrderID = orderID;
    }

    public String getCustomerID() {
        return CustomerID;
    }

    public void setCustomerID(String customerID) {
        CustomerID = customerID;
    }

    public String getDriverNic() {
        return DriverNic;
    }

    public void setDriverNic(String driverNic) {
        DriverNic = driverNic;
    }

    public String getVehicleNumber() {
        return VehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        VehicleNumber = vehicleNumber;
    }

    public String getDeliveryStatus() {
        return DeliveryStatus;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        DeliveryStatus = deliveryStatus;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Button getBtn() {
        return btn;
    }

    public void setBtn(Button btn) {
        this.btn = btn;
    }

    public DeliveryTM() {
    }

    public DeliveryTM(String deliveryID, String orderID, String customerID, String driverNic, String vehicleNumber, String deliveryStatus, LocalDate date, Button btn) {
        DeliveryID = deliveryID;
        OrderID = orderID;
        CustomerID = customerID;
        DriverNic = driverNic;
        VehicleNumber = vehicleNumber;
        DeliveryStatus = deliveryStatus;
        this.date = date;
        this.btn = btn;
    }
}
