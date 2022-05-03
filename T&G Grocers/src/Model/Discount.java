package Model;

public class Discount {
    private String OrderID;
    private String CustomerNIC;
    private double TotalPrice;
    private double DiscountGiven;
    private double discountedPrice;

    public Discount() {
    }

    public Discount(String orderID, String customerNIC, double totalPrice, double discountGiven, double discountedPrice) {
        OrderID = orderID;
        CustomerNIC = customerNIC;
        TotalPrice = totalPrice;
        DiscountGiven = discountGiven;
        this.discountedPrice = discountedPrice;
    }

    @Override
    public String toString() {
        return "Discount{" +
                "OrderID='" + OrderID + '\'' +
                ", CustomerNIC='" + CustomerNIC + '\'' +
                ", TotalPrice=" + TotalPrice +
                ", DiscountGiven=" + DiscountGiven +
                ", discountedPrice=" + discountedPrice +
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

    public double getDiscountGiven() {
        return DiscountGiven;
    }

    public void setDiscountGiven(double discountGiven) {
        DiscountGiven = discountGiven;
    }

    public double getDiscountedPrice() {
        return discountedPrice;
    }

    public void setDiscountedPrice(double discountedPrice) {
        this.discountedPrice = discountedPrice;
    }
}
