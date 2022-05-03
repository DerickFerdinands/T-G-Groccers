package Model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Order {
    private String orderID;
    private String CustomerNIC;
    private LocalDate date;
    private LocalTime time;
    private double totalPrice;

    public Order() {
    }

    public Order(String orderID, String customerNIC, LocalDate date, LocalTime time, double totalPrice) {
        this.orderID = orderID;
        CustomerNIC = customerNIC;
        this.date = date;
        this.time = time;
        this.totalPrice = totalPrice;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderID='" + orderID + '\'' +
                ", CustomerNIC='" + CustomerNIC + '\'' +
                ", date=" + date +
                ", time=" + time +
                ", totalPrice=" + totalPrice +
                '}';
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getCustomerNIC() {
        return CustomerNIC;
    }

    public void setCustomerNIC(String customerNIC) {
        CustomerNIC = customerNIC;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
