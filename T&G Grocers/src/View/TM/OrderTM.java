package View.TM;

import javafx.scene.control.Button;

public class OrderTM {
    private String itemCode;
    private String Description;
    private double unitPrice;
    private double Qty;
    private double cost;
    private Button btn;

    public OrderTM() {
    }

    public OrderTM(String itemCode, String description, double unitPrice, double qty, double cost, Button btn) {
        this.itemCode = itemCode;
        Description = description;
        this.unitPrice = unitPrice;
        Qty = qty;
        this.cost = cost;
        this.btn = btn;
    }

    @Override
    public String toString() {
        return "OrderTM{" +
                "itemCode='" + itemCode + '\'' +
                ", Description='" + Description + '\'' +
                ", unitPrice=" + unitPrice +
                ", Qty=" + Qty +
                ", cost=" + cost +
                ", btn=" + btn +
                '}';
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public double getQty() {
        return Qty;
    }

    public void setQty(double qty) {
        Qty = qty;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public Button getBtn() {
        return btn;
    }

    public void setBtn(Button btn) {
        this.btn = btn;
    }
}
