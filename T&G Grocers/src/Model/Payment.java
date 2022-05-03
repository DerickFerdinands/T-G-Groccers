package Model;

public class Payment {
    private String paymentID;
    private String OrderId;
    private String paymentType;
    private Double paymentAmount;
    private String paymentStatus;

    public Payment() {
    }

    public Payment(String paymentID, String orderId, String paymentType, Double paymentAmount, String paymentStatus) {
        this.paymentID = paymentID;
        OrderId = orderId;
        this.paymentType = paymentType;
        this.paymentAmount = paymentAmount;
        this.paymentStatus = paymentStatus;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "paymentID='" + paymentID + '\'' +
                ", OrderId='" + OrderId + '\'' +
                ", paymentType='" + paymentType + '\'' +
                ", paymentAmount=" + paymentAmount +
                ", paymentStatus='" + paymentStatus + '\'' +
                '}';
    }

    public String getPaymentID() {
        return paymentID;
    }

    public void setPaymentID(String paymentID) {
        this.paymentID = paymentID;
    }

    public String getOrderId() {
        return OrderId;
    }

    public void setOrderId(String orderId) {
        OrderId = orderId;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public Double getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(Double paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
}
