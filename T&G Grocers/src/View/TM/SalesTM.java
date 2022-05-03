package View.TM;

import javafx.scene.control.Button;

import java.time.LocalDate;
import java.time.LocalTime;

public class SalesTM {
    private String OrderID;
    private String CustomerNIC;
    private double TotalPrice;
    private Object date;
    private LocalTime time;
    private Button btn;

    public SalesTM() {
    }

    public SalesTM(String orderID, String customerNIC, double totalPrice, Object date, LocalTime time, Button btn) {
        OrderID = orderID;
        CustomerNIC = customerNIC;
        TotalPrice = totalPrice;
        this.date = date;
        this.time = time;
        this.btn = btn;
    }

    @Override
    public String toString() {
        return "SalesTM{" +
                "OrderID='" + OrderID + '\'' +
                ", CustomerNIC='" + CustomerNIC + '\'' +
                ", TotalPrice=" + TotalPrice +
                ", date=" + date +
                ", time=" + time +
                ", btn=" + btn +
                '}';
    }

    public String getOrderID() {
        return OrderID;
    }

    public void setOrderID(String orderID) {
        OrderID = orderID;
    }

    public String getCustomerNIC() {
        return CustomerNIC;
    }

    public void setCustomerNIC(String customerNIC) {
        CustomerNIC = customerNIC;
    }

    public double getTotalPrice() {
        return TotalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        TotalPrice = totalPrice;
    }

    public Object getDate() {
        return date;
    }

    public void setDate(Object date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public Button getBtn() {
        return btn;
    }

    public void setBtn(Button btn) {
        this.btn = btn;
    }

}