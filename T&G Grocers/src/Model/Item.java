package Model;

public class Item {
    private String ItemCode;
    private String description;
    private String category;
    private String unit;
    private double buyingPrice;
    private double sellingPrice;
    private double availableQty;
    private String Image_path;

    public Item() {
    }

    @Override
    public String toString() {
        return "Item{" +
                "ItemCode='" + ItemCode + '\'' +
                ", description='" + description + '\'' +
                ", category='" + category + '\'' +
                ", unit='" + unit + '\'' +
                ", buyingPrice=" + buyingPrice +
                ", sellingPrice=" + sellingPrice +
                ", availableQty=" + availableQty +
                ", Image_path='" + Image_path + '\'' +
                '}';
    }

    public String getItemCode() {
        return ItemCode;
    }

    public void setItemCode(String itemCode) {
        ItemCode = itemCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public double getBuyingPrice() {
        return buyingPrice;
    }

    public void setBuyingPrice(double buyingPrice) {
        this.buyingPrice = buyingPrice;
    }

    public double getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public double getAvailableQty() {
        return availableQty;
    }

    public void setAvailableQty(double availableQty) {
        this.availableQty = availableQty;
    }

    public String getImage_path() {
        return Image_path;
    }

    public void setImage_path(String image_path) {
        Image_path = image_path;
    }

    public Item(String itemCode, String description, String category, String unit, double buyingPrice, double sellingPrice, double availableQty, String image_path) {
        ItemCode = itemCode;
        this.description = description;
        this.category = category;
        this.unit = unit;
        this.buyingPrice = buyingPrice;
        this.sellingPrice = sellingPrice;
        this.availableQty = availableQty;
        Image_path = image_path;
    }
}
