package View.TM;

import com.jfoenix.controls.JFXButton;

import java.time.LocalTime;

public class PurchaseTM {
    private String PurchaseID;
    private String VendorNIC;
    private double totalPrice;
    private Object Date;
    private LocalTime time;
    private JFXButton btn;

    public PurchaseTM() {
    }

    public PurchaseTM(String purchaseID, String vendorNIC, double totalPrice, Object date, LocalTime time, JFXButton btn) {
        PurchaseID = purchaseID;
        VendorNIC = vendorNIC;
        this.totalPrice = totalPrice;
        Date = date;
        this.time = time;
        this.btn = btn;
    }

    @Override
    public String toString() {
        return "PurchaseTM{" +
                "PurchaseID='" + PurchaseID + '\'' +
                ", VendorNIC='" + VendorNIC + '\'' +
                ", totalPrice=" + totalPrice +
                ", Date=" + Date +
                ", time=" + time +
                ", btn=" + btn +
                '}';
    }

    public String getPurchaseID() {
        return PurchaseID;
    }

    public void setPurchaseID(String purchaseID) {
        PurchaseID = purchaseID;
    }

    public String getVendorNIC() {
        return VendorNIC;
    }

    public void setVendorNIC(String vendorNIC) {
        VendorNIC = vendorNIC;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Object getDate() {
        return Date;
    }

    public void setDate(Object date) {
        Date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public JFXButton getBtn() {
        return btn;
    }

    public void setBtn(JFXButton btn) {
        this.btn = btn;
    }
}
