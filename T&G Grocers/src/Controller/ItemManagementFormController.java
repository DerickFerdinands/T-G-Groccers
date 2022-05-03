package Controller;

import Controller.CRUD.ItemCRUDController;
import Model.Item;
import Util.CrudUtil;
import Util.ValidationUtil;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;

import java.io.File;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;

public class ItemManagementFormController {
    public TextField ItemCode;
    public TextField ItemCategory;
    public TextField ItemUnit;
    public TextField ItemBuyingPrice;
    public TextField ItemSellingPrice;
    public TextField ItemNameOrDesc;
    public TextField ItemQty;
    public AnchorPane ItemMainPane;
    public AnchorPane AddImageAnchorPane;
    public ImageView ItemImageVIewer;
    public AnchorPane ItemScrollerAnchorPane;
    public JFXButton btnDeleteItem;
    public JFXButton btnUpdateItem;
    public ImageView UpdateItemImage;
    public TextField UPItemCode;
    public TextField UPItemCategory;
    public TextField UPItemUnit;
    public TextField UpItemBuyingPrice;
    public TextField UPItemSellingPrice;
    public TextField UPItemDesc;
    public TextField UPAvailableQty;
    public JFXTextField txtSearchItem;
    public JFXButton btnAddItem;
    public AnchorPane addImagePane;
    public AnchorPane updateImageViewer;
    File selectedImage;
    ArrayList<Double> LayoutYValues = new ArrayList<>();
    String UpdateImageLocation;
    LinkedHashMap<TextField, Pattern> RegexMap = new LinkedHashMap<>();
    LinkedHashMap<TextField, Pattern> RegexUpdateMap = new LinkedHashMap<>();

    public void addItemOnAction(ActionEvent actionEvent) {
        try {
            boolean saved = ItemCRUDController.saveItem(ItemCode.getText(), ItemNameOrDesc.getText(), ItemCategory.getText(), ItemUnit.getText(), Double.valueOf(ItemBuyingPrice.getText()), Double.valueOf(ItemSellingPrice.getText()), Double.valueOf(ItemQty.getText()), selectedImage.getPath());
            if (saved) {
                setAllItems(ItemCRUDController.getAllItems());
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Item Added Sucesfully!..", ButtonType.OK);
                alert.initOwner(ItemMainPane.getScene().getWindow());
                alert.show();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Something Went Wrong!.", ButtonType.OK);
                alert.initOwner(ItemMainPane.getScene().getWindow());
                alert.show();
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.initOwner(ItemMainPane.getScene().getWindow());
            alert.show();
        }
        clearAddItemFields();
    }

    public void OpenImageFinder(MouseEvent mouseEvent) {
        FileChooser fileChooser = new FileChooser();
        selectedImage = fileChooser.showOpenDialog(ItemMainPane.getScene().getWindow());
        ImageView itemImageVIewer = new ImageView(new Image("file:" + selectedImage.getPath()));
        ItemSellingPrice.requestFocus();
        itemImageVIewer.setFitHeight(144);
        itemImageVIewer.setFitWidth(142);
        AddImageAnchorPane.getChildren().add(itemImageVIewer);

    }

    public void initialize() {

        btnAddItem.setDisable(true);
        btnUpdateItem.setDisable(true);
        btnDeleteItem.setDisable(true);

        Pattern ItemCodePattern = Pattern.compile("^(.)+$");
        Pattern ItemName = Pattern.compile("^(.)+$");
        Pattern ItemCategoryPattern = Pattern.compile("^(.)+$");
        Pattern AvailableQty = Pattern.compile("^[0-9]+([.][0-9]{1,3})?$");
        Pattern Unit = Pattern.compile("^(.)+$");
        Pattern BuyingPrice = Pattern.compile("^[0-9]+([.][0-9]{1,3})?$");
        Pattern SellingPrice = Pattern.compile("^[0-9]+([.][0-9]{1,3})?$");
        RegexMap.put(ItemCode, ItemCodePattern);
        RegexMap.put(ItemNameOrDesc, ItemName);
        RegexMap.put(ItemCategory, ItemCategoryPattern);
        RegexMap.put(ItemQty, AvailableQty);
        RegexMap.put(ItemUnit, Unit);
        RegexMap.put(ItemBuyingPrice, BuyingPrice);
        RegexMap.put(ItemSellingPrice, SellingPrice);

        RegexUpdateMap.put(UPItemCode, ItemCodePattern);
        RegexUpdateMap.put(UPItemDesc, ItemName);
        RegexUpdateMap.put(UPItemCategory, ItemCategoryPattern);
        RegexUpdateMap.put(UPAvailableQty, AvailableQty);
        RegexUpdateMap.put(UPItemUnit, Unit);
        RegexUpdateMap.put(UpItemBuyingPrice, BuyingPrice);
        RegexUpdateMap.put(UPItemSellingPrice, SellingPrice);



        try {
            setAllItems(ItemCRUDController.getAllItems());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void setAllItems(ArrayList<Item> itemList) {
        ItemScrollerAnchorPane.getChildren().clear();


        for (Item item : itemList) {
            setViewItemPane(item.getImage_path(), item.getDescription(), item.getItemCode(), item.getCategory(), String.valueOf(item.getAvailableQty()), String.valueOf(item.getBuyingPrice()), String.valueOf(item.getSellingPrice()), item.getUnit());
        }

        LayoutYValues.clear();
    }

    //Return Customized HBox.
    public HBox getHBox(int width, int layoutX, int layoutY, String text) {
        HBox box = new HBox();
        box.setAlignment(Pos.CENTER);
        box.setPrefWidth(width);
        box.setLayoutX(layoutX);
        box.setLayoutY(layoutY);
        Label label = new Label(text);
        label.setTextFill(Color.color(0.00, 0.45, 0.40));
        label.setStyle("-fx-font-size: 15");
        box.getChildren().add(label);

        return box;
    }

    //    Returns Customized Line.
    public Line getLine(int startX, int startY, int endX, int endY, int layoutX, int layoutY) {
        Line line = new Line();
        line.setStartX(startX);
        line.setStartY(startY);
        line.setEndX(endX);
        line.setEndY(endY);
        line.setLayoutX(layoutX);
        line.setLayoutY(layoutY);

        return line;
    }

    //    Creates Separate AnchorPanes For Customers to View.
    public void setViewItemPane(String imagePath, String desc, String itemCode, String Category, String availableQty, String buyingPrice, String sellingPrice, String unit) {
        //    Checks YPosition For Customer View Pane Placement.
        double layoutY;
        if (LayoutYValues.isEmpty()) {
            layoutY = 4;
            LayoutYValues.add(layoutY);
        } else {
            double size = LayoutYValues.get((LayoutYValues.size()) - 1) + 70;
            if (ItemScrollerAnchorPane.getHeight() < size) {
                ItemScrollerAnchorPane.setPrefHeight(size + 100);
            }
            layoutY = size;
            LayoutYValues.add(layoutY);
        }
        AnchorPane pane = new AnchorPane();
        pane.setPrefSize(942, 68);

        pane.setLayoutX(4);
        pane.setLayoutY(layoutY);

//      Created A Rectangle Because AnchorPane returns void and Cannot Change Background Color.

        Rectangle rec = new Rectangle();
        rec.setHeight(68);
        rec.setWidth(942);
        rec.setStroke(Color.WHITE);
        rec.setStyle("-fx-border-color: Black");
        rec.setFill(Color.color(1.00, 1.00, 1.00));

        pane.getChildren().add(rec);

//        Gets Customized HBox and Line.
        /*
         * //HBox//
         * Include HBox Width
         * Include HBox LayoutX
         * Include HBox LayoutY
         * Include Text Required
         * //Line//
         * Include StartX
         * Include StartY
         * Include EndX
         * Include EndY
         * Include LayoutX
         * Include LayoutY
         * */

        ImageView itemImage = new ImageView(new Image("file:" + imagePath));
        itemImage.setFitHeight(42);
        itemImage.setFitWidth(52);

        itemImage.setLayoutX(14);
        itemImage.setLayoutY(12);

        pane.getChildren().add(itemImage);

        pane.getChildren().add(getHBox(201, 10, 25, desc));
        pane.getChildren().add(getLine(-127, -15, -127, 24, 334, 29));

        pane.getChildren().add(getHBox(201, 167, 25, itemCode));
        pane.getChildren().add(getLine(-127, -15, -127, 24, 454, 29));

        pane.getChildren().add(getHBox(201, 285, 25, Category));
        pane.getChildren().add(getLine(-127, -15, -127, 24, 574, 29));

        pane.getChildren().add(getHBox(201, 454, 25, availableQty));
        pane.getChildren().add(getLine(-127, -15, -127, 24, 789, 29));

        pane.getChildren().add(getHBox(201, 605, 25, buyingPrice));
        pane.getChildren().add(getLine(-127, -15, -127, 24, 879, 29));

        pane.getChildren().add(getHBox(201, 702, 25, sellingPrice));
        pane.getChildren().add(getLine(-127, -15, -127, 24, 979, 29));


        ItemScrollerAnchorPane.getChildren().add(pane);

//      Set Customer Details On Update/ Delete Section On Click On Pane.
        pane.setOnMouseClicked(event -> {
            UpdateItemImage.setImage(new Image("file:" + imagePath));
            UPItemCode.setText(itemCode);
            UPItemDesc.setText(desc);
            UPItemCategory.setText(Category);
            UPAvailableQty.setText(availableQty);
            UPItemUnit.setText(unit);
            UpItemBuyingPrice.setText(buyingPrice);
            UPItemSellingPrice.setText(sellingPrice);
            ValidationUtil.Validate(RegexUpdateMap, btnUpdateItem);
            if(btnUpdateItem.isDisable()){
                btnDeleteItem.setDisable(true);
            }else{
                btnDeleteItem.setDisable(false);
            }

        });

    }

    public void deleteItemOnAction(ActionEvent actionEvent) {
        try {
            boolean deleted = ItemCRUDController.deleteItem(UPItemCode.getText());
            if (deleted) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Deleted!..", ButtonType.OK);
                alert.initOwner(ItemMainPane.getScene().getWindow());
                alert.show();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Something Went Wrong!..", ButtonType.OK);
                alert.initOwner(ItemMainPane.getScene().getWindow());
                alert.show();
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.initOwner(ItemMainPane.getScene().getWindow());
            alert.show();
        }
        try {
            setAllItems(ItemCRUDController.getAllItems());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        clearUpdateItemFields();
    }

    public void updateItemOnAction(ActionEvent actionEvent) {
        try {
            boolean updated = ItemCRUDController.updateItem(UPItemCode.getText(), UPItemDesc.getText(), UPItemCategory.getText(), UPItemUnit.getText(), Double.valueOf(UpItemBuyingPrice.getText()), Double.valueOf(UPItemSellingPrice.getText()), Double.valueOf(UPAvailableQty.getText()), UpdateImageLocation);
            if (updated) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Updated!..", ButtonType.OK);
                alert.initOwner(ItemMainPane.getScene().getWindow());
                alert.show();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Something Went Wrong!..", ButtonType.OK);
                alert.initOwner(ItemMainPane.getScene().getWindow());
                alert.show();
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.initOwner(ItemMainPane.getScene().getWindow());
            alert.show();
        }
        try {
            setAllItems(ItemCRUDController.getAllItems());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        clearUpdateItemFields();
    }

    public void updateItemPicture(MouseEvent mouseEvent) {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(ItemMainPane.getScene().getWindow());
        UpdateImageLocation = file.getPath();
        UpdateItemImage.setImage(new Image("file:" + file.getPath()));
    }

    public void searchItemOnAction(ActionEvent actionEvent) {
        String search = txtSearchItem.getText();
        try {
            ArrayList<Item> ItemList = ItemCRUDController.getAllItems();
            ArrayList<Item> addToView = new ArrayList<>();
            for (Item item : ItemList) {
                if (item.getItemCode().toUpperCase().contains(search.toUpperCase()) || item.getDescription().toUpperCase().contains(search.toUpperCase())) {
                    addToView.add(item);
                }
            }
            if (addToView.isEmpty()) {
                setAllItems(ItemCRUDController.getAllItems());
            } else {
                setAllItems(addToView);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.initOwner(ItemMainPane.getScene().getWindow());
            alert.show();
        }


    }

    public void SearchSpecificItemOnACtion(ActionEvent actionEvent) {
        String search = UPItemCode.getText();
        try {
            Item item = ItemCRUDController.getItem(search);

            if (item != null) {

                UpdateItemImage.setImage(new Image("file:" + item.getImage_path()));
                UPItemDesc.setText(item.getDescription());
                UPItemCategory.setText(item.getCategory());
                UPAvailableQty.setText(String.valueOf(item.getAvailableQty()));
                UPItemUnit.setText(item.getUnit());
                UpItemBuyingPrice.setText(String.valueOf(item.getBuyingPrice()));
                UPItemSellingPrice.setText(String.valueOf(item.getSellingPrice()));
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.initOwner(ItemMainPane.getScene().getWindow());
            alert.show();
        }
    }

    public void ValidateFiields(KeyEvent keyEvent) {
        Object o = ValidationUtil.Validate(RegexMap, btnAddItem);
        if (selectedImage != null) {
            addImagePane.setStyle("-fx-border-color: Green");
        } else {
            addImagePane.setStyle("-fx-border-color: red");
            btnAddItem.setDisable(true);
        }
        if (keyEvent.getCode() == KeyCode.ENTER) {
            if (o instanceof TextField) {
                TextField field = (TextField) o;
                field.requestFocus();
            }
        }


    }

    public void UpdateVaildation(KeyEvent keyEvent) {
        Object o = ValidationUtil.Validate(RegexUpdateMap, btnUpdateItem);
        if (UpdateItemImage.getImage()!=null) {
            addImagePane.setStyle("-fx-border-color: Green");
        } else {
            addImagePane.setStyle("-fx-border-color: red");
            btnAddItem.setDisable(true);
        }
        if (keyEvent.getCode() == KeyCode.ENTER) {
            if (o instanceof TextField) {
                TextField field = (TextField) o;
                field.requestFocus();
            }
        }
        if(btnUpdateItem.isDisable()){
            btnDeleteItem.setDisable(true);
        }else{
            btnDeleteItem.setDisable(false);
        }

    }

    public void clearAddItemFields(){
        ItemCode.clear();
        ItemNameOrDesc.clear();
        ItemCategory.clear();
        ItemQty.clear();
        ItemUnit.clear();
        ItemBuyingPrice.clear();
        ItemSellingPrice.clear();
        ItemCode.requestFocus();
        btnAddItem.setDisable(true);
    }

    public void clearUpdateItemFields(){
        UPItemCode.clear();
        UPItemCategory.clear();
        UPItemUnit.clear();
        UpItemBuyingPrice.clear();
        UPItemSellingPrice.clear();
        UPItemDesc.clear();
        UPAvailableQty.clear();
        UPItemCode.requestFocus();
        btnUpdateItem.setDisable(true);
        btnDeleteItem.setDisable(true);
    }
}
