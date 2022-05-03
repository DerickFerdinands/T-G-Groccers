package View.TM;

import com.jfoenix.controls.JFXButton;

public class DiscountTM {
    private String OrderID;
    private String CustomerNIC;
    private double totalPrice;
    private double discountGiven;
    private double discountedPrice;
    private JFXButton btn;

    public DiscountTM() {
    }

    public DiscountTM(String orderID, String customerNIC, double totalPrice, double discountGiven, double discountedPrice, JFXButton btn) {
        OrderID = orderID;
        CustomerNIC = customerNIC;
        this.totalPrice = totalPrice;
        this.discountGiven = discountGiven;
        this.discountedPrice = discountedPrice;
        this.btn = btn;
    }

    @Override
    public String toString() {
        return "DiscountTM{" +
                "OrderID='" + OrderID + '\'' +
                ", CustomerNIC='" + CustomerNIC + '\'' +
                ", totalPrice=" + totalPrice +
                ", discountGiven=" + discountGiven +
                ", discountedPrice=" + discountedPrice +
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
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public double getDiscountGiven() {
        return discountGiven;
    }

    public void setDiscountGiven(double discountGiven) {
        this.discountGiven = discountGiven;
    }

    public double getDiscountedPrice() {
        return discountedPrice;
    }

    public void setDiscountedPrice(double discountedPrice) {
        this.discountedPrice = discountedPrice;
    }

    public JFXButton getBtn() {
        return btn;
    }

    public void setBtn(JFXButton btn) {
        this.btn = btn;
    }
}
