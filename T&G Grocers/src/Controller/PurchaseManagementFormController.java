package Controller;

import Controller.CRUD.*;
import Database.DBConnection;
import Model.Item;
import Model.Payment;
import Model.Vendor;
import View.TM.OrderTM;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTreeTableView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class PurchaseManagementFormController {

    public JFXComboBox cmbPaymentMethod;
    public JFXComboBox cmbPaymentStatus;
    public Label lblTotal;
    public JFXComboBox cmbVendorID;
    public JFXComboBox cmbVendorMobile;
    public TextField txtCusID;
    public JFXComboBox cmbItemCode;
    public TextField txtName;
    public TextField txtQtyOnHand;
    public TextField txtBuyingPrice;
    public TextField txtBuyingQty;
    public TableView<OrderTM> tblPurchaseItems;
    public TableColumn colItem;
    public TableColumn colDesc;
    public TableColumn colUnitPrice;
    public TableColumn colQty;
    public TableColumn colCost;
    public TableColumn colOption;
    public JFXButton btnPurchase;
    public AnchorPane PurchaseMainPane;
    public AnchorPane PurchaseScrollerAnchorPane;
    public JFXButton btnAddToCart;
    public JFXButton btnDeleteFromCart;

    ObservableList<OrderTM> obList = FXCollections.observableArrayList();
    double grandTotal;
    Connection connection = null;
    ArrayList<Double> LayoutYValues = new ArrayList<>();

    public void addTocartOnAction(ActionEvent actionEvent) {
        boolean exists = false;
        for (OrderTM tm : obList) {
            if (tm.getItemCode().equals(cmbItemCode.getValue())) {
                System.out.println();
                tm.setQty(tm.getQty() + Double.valueOf(txtBuyingQty.getText()));
                tm.setCost(tm.getQty() * tm.getUnitPrice());
                tblPurchaseItems.refresh();
                exists = true;
                break;

            }
        }
        if (exists == false) {
            JFXButton btn = new JFXButton("Delete");
            btn.setStyle("-fx-border-color: Black");
            double cost = (Double.valueOf(txtBuyingQty.getText())) * (Double.valueOf(txtBuyingQty.getText()));
            OrderTM tm = new OrderTM(String.valueOf(cmbItemCode.getValue()), txtName.getText(), Double.valueOf(txtBuyingPrice.getText()), Double.valueOf(txtBuyingQty.getText()), cost, btn);
            obList.add(tm);
            btn.setOnAction(event -> {
                obList.remove(tm);
                tblPurchaseItems.refresh();
            });
            tblPurchaseItems.setItems(obList);
        }
        double total = 0;
        grandTotal = total;
        for (OrderTM tm : obList) {
            total += tm.getCost();
        }
        lblTotal.setText("Total:  Rs. " + total + " /=");
        grandTotal = total;
    }

    public void removeFromCartOnAction(ActionEvent actionEvent) {
        for (OrderTM tm : obList) {
            if (tm.getItemCode().equals(cmbItemCode.getValue())) ;
            {
                obList.remove(tm);
                tblPurchaseItems.refresh();
                break;
            }
        }
    }

    public void purchaseItemsOnAction(ActionEvent actionEvent) throws SQLException {
        try {
            connection = DBConnection.getInstance().getConnection();
            connection.setAutoCommit(false);
            System.out.println(grandTotal);
            boolean recorded = PurchaseCRUDController.recordPurchase(String.valueOf(cmbVendorID.getValue()), grandTotal, String.valueOf(cmbPaymentMethod.getValue()), String.valueOf(cmbPaymentStatus.getValue()));

            if (recorded) {
                boolean saved = PurchaseCRUDController.savePurchaseDetails(PurchaseCRUDController.getLastPurchaseID(), new ArrayList<OrderTM>(obList));

                if (saved) {
                    connection.commit();

                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Purchase Recorded!..", ButtonType.OK);
                    alert.initOwner(PurchaseMainPane.getScene().getWindow());
                    alert.show();

                    //  setAllCustomers(PaymentCRUDController.getAllPendingPayments());
                    for (OrderTM tm : obList) {
                        ItemCRUDController.addItemQty(tm.getItemCode(), tm.getQty());
                    }
                    setAllCustomers(PaymentCRUDController.getAllPurchasesOnCredit());
                } else {
                    connection.rollback();
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Something Went Wrong!..", ButtonType.OK);
                    alert.initOwner(PurchaseMainPane.getScene().getWindow());
                    alert.show();
                }

            } else {
                connection.rollback();
                Alert alert = new Alert(Alert.AlertType.ERROR, "Something Went Wrong!..", ButtonType.OK);
                alert.initOwner(PurchaseMainPane.getScene().getWindow());
                alert.show();
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.initOwner(PurchaseMainPane.getScene().getWindow());
            alert.show();
        } finally {
            connection.setAutoCommit(true);
        }
    }

    public void initialize() {
        try {
            setAllCustomers(PaymentCRUDController.getAllPurchasesOnCredit());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        colItem.setCellValueFactory(new PropertyValueFactory<>("itemCode"));
        colDesc.setCellValueFactory(new PropertyValueFactory<>("Description"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("Qty"));
        colCost.setCellValueFactory(new PropertyValueFactory<>("cost"));
        colOption.setCellValueFactory(new PropertyValueFactory<>("btn"));
        try {
            cmbVendorID.getItems().addAll(VendorCRUDController.getAllVendorIDS());
            cmbVendorMobile.getItems().addAll(VendorCRUDController.getALlVendorMobiles());
            cmbItemCode.getItems().addAll(ItemCRUDController.getAllItemCodes());
            cmbPaymentMethod.getItems().addAll("Cash", "Credit/Debit Card");
            cmbPaymentStatus.getItems().addAll("Paid", "Pending");

//
            cmbVendorID.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                try {
                    Vendor v = VendorCRUDController.getVendor((String) newValue);

                    cmbVendorMobile.getSelectionModel().select(v.getContact());
                    txtCusID.setText(v.getNameOrCompany());

                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
                if (cmbVendorMobile.getValue() != null && cmbVendorID.getValue() != null && cmbPaymentStatus.getValue() != null && cmbPaymentMethod.getValue() != null && !(obList.isEmpty())) {

                    btnPurchase.setDisable(false);

                }
            });

            cmbVendorMobile.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                try {
                    Vendor v = VendorCRUDController.getVendorByMobile((String) newValue);

                    cmbVendorID.getSelectionModel().select(v.getVendorID());
                    txtCusID.setText(v.getNameOrCompany());

                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
                if (cmbVendorMobile.getValue() != null && cmbVendorID.getValue() != null && cmbPaymentStatus.getValue() != null && cmbPaymentMethod.getValue() != null && !(obList.isEmpty())) {

                    btnPurchase.setDisable(false);

                }
            });

            cmbItemCode.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                try {
                    Item item = ItemCRUDController.getItem((String) newValue);
                    txtName.setText(item.getDescription());
                    txtQtyOnHand.setText(String.valueOf(item.getAvailableQty()));
                    txtBuyingPrice.setText(String.valueOf(item.getBuyingPrice()));

                    if (Pattern.compile("^[0-9]+([.][0-9]{1,3})?$").matcher(txtBuyingQty.getText()).matches()) {
                        btnAddToCart.setDisable(false);
                    }

                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
                if (cmbVendorMobile.getValue() != null && cmbVendorID.getValue() != null && cmbPaymentStatus.getValue() != null && cmbPaymentMethod.getValue() != null && !(obList.isEmpty())) {

                    btnPurchase.setDisable(false);

                }
            });
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        cmbPaymentMethod.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (cmbVendorMobile.getValue() != null && cmbVendorID.getValue() != null && cmbPaymentStatus.getValue() != null && cmbPaymentMethod.getValue() != null && !(obList.isEmpty())) {

                btnPurchase.setDisable(false);

            }
        });

        cmbPaymentStatus.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (cmbVendorMobile.getValue() != null && cmbVendorID.getValue() != null && cmbPaymentStatus.getValue() != null && cmbPaymentMethod.getValue() != null && !(obList.isEmpty())) {

                btnPurchase.setDisable(false);
            }
        });
        btnAddToCart.setDisable(true);
        btnDeleteFromCart.setDisable(true);
        btnPurchase.setDisable(true);
    }

    private void setAllCustomers(ArrayList<Payment> paymentList) {
        PurchaseScrollerAnchorPane.getChildren().clear();

//Sets View For Each Customer.
        for (Payment p : paymentList) {
            setViewCustomerPane(p.getPaymentID(), p.getOrderId(), p.getPaymentType(), String.valueOf(p.getPaymentAmount()), p.getPaymentStatus());
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
    public void setViewCustomerPane(String pymentID, String purchaseID, String paymentType, String paymentAmount, String paymentStatus) {
//        Checks YPosition For Customer View Pane Placement.
        double layoutY;
        if (LayoutYValues.isEmpty()) {
            layoutY = 4;
            LayoutYValues.add(layoutY);
        } else {
            double size = LayoutYValues.get((LayoutYValues.size()) - 1) + 70;
            if (PurchaseScrollerAnchorPane.getHeight() < size) {
                PurchaseScrollerAnchorPane.setPrefHeight(size + 100);
            }
            layoutY = size;
            LayoutYValues.add(layoutY);
        }
        AnchorPane pane = new AnchorPane();
        pane.setPrefSize(700, 68);

        pane.setLayoutX(4);
        pane.setLayoutY(layoutY);

//      Created A Rectangle Because AnchorPane returns void and Cannot Change Background Color.

        Rectangle rec = new Rectangle();
        rec.setHeight(68);
        rec.setWidth(700);
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
        pane.getChildren().add(getHBox(201, 10, 25, pymentID));
        pane.getChildren().add(getLine(-127, -15, -127, 24, 334, 29));

        pane.getChildren().add(getHBox(201, 167, 25, paymentType));
        pane.getChildren().add(getLine(-127, -15, -127, 24, 454, 29));

        pane.getChildren().add(getHBox(201, 285, 25, paymentAmount));
        pane.getChildren().add(getLine(-127, -15, -127, 24, 574, 29));

        pane.getChildren().add(getHBox(201, 454, 25, paymentStatus));
        pane.getChildren().add(getLine(-127, -15, -127, 24, 789, 29));

        JFXButton btn = new JFXButton("PAY");
        btn.setStyle("-fx-border-color: Black");
        btn.setLayoutX(10);
        btn.setPrefWidth(50);
        btn.setLayoutY(22);
        btn.setOnAction(event -> {
            try {
                if (PaymentCRUDController.updatePurchasePaymentStatus(pymentID, "Paid")) {
                    Notifications notify = Notifications.create().title("Purchase Status Updated!").hideAfter(Duration.seconds(4)).darkStyle();
                    notify.show();
                    setAllCustomers(PaymentCRUDController.getAllPurchasesOnCredit());
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });
        pane.getChildren().add(btn);

        PurchaseScrollerAnchorPane.getChildren().add(pane);


    }

    public void ValidateBuyingQty(KeyEvent keyEvent) {
        if (Pattern.compile("^[0-9]+([.][0-9]{1,3})?$").matcher(txtBuyingQty.getText()).matches() && (cmbItemCode.getValue() != null)) {
            btnAddToCart.setDisable(false);
            btnDeleteFromCart.setDisable(false);
        } else {
            btnAddToCart.setDisable(true);
            btnDeleteFromCart.setDisable(true);
        }
    }
}
