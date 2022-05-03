package Controller;

import Controller.CRUD.*;
import Database.DBConnection;
import Model.*;
import Util.CrudUtil;
import View.TM.OrderTM;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTreeTableView;
import com.mysql.cj.x.protobuf.MysqlxCrud;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import org.controlsfx.control.Notifications;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

public class OrderManagementFormController {

    public JFXComboBox cmbPaymentMethod;
    public JFXComboBox cmbPaymentStatus;
    public JFXButton btnPlaceOrder;
    public JFXComboBox cmbCustomerID;
    public JFXComboBox cmbCustomerMobile;
    public TextField txtCustomerName;
    public JFXComboBox cmbItemCode;
    public TextField txtItemName;
    public TextField txtQtyOnHand;
    public TextField txtUnitPrice;
    public TextField txtQty;
    public TableView<OrderTM> tblOrderItems;
    public TableColumn colItem;
    public TableColumn colDesc;
    public TableColumn colUnitPrice;
    public TableColumn colQty;
    public TableColumn colCost;
    public TableColumn colOption;
    public AnchorPane OrderMainPane;
    public ImageView ItemImage;
    public Label lblTotal;
    public AnchorPane OrderScrollerAnchorPane;
    public JFXButton btnAddToCart;
    public JFXButton btnRemoveFromCart;
    public JFXButton btnSendOnDelivery;
    public TextField txtDiscountsGiven;
    public Label lblDiscount;

    ObservableList<OrderTM> obList = FXCollections.observableArrayList();
    double grandTotal;
    Connection connection = null;
    ArrayList<Double> LayoutYValues = new ArrayList<>();
    private double discount;

    public void addItemToCartOnAction(ActionEvent actionEvent) {
        boolean exists = false;
        for (OrderTM tm : obList) {
            if (tm.getItemCode().equals(cmbItemCode.getValue())) {
                System.out.println();
                tm.setQty(tm.getQty() + Double.valueOf(txtQty.getText()));
                tm.setCost(tm.getQty() * tm.getUnitPrice());
                tblOrderItems.refresh();
                exists = true;
                break;

            }
        }
        if (exists == false) {
            JFXButton btn = new JFXButton("Delete");
            btn.setStyle("-fx-border-color: Black");
            double cost = (Double.valueOf(txtQty.getText())) * (Double.valueOf(txtUnitPrice.getText()));
            OrderTM tm = new OrderTM(String.valueOf(cmbItemCode.getValue()), txtItemName.getText(), Double.valueOf(txtUnitPrice.getText()), Double.valueOf(txtQty.getText()), cost, btn);
            obList.add(tm);
            btn.setOnAction(event -> {
                obList.remove(tm);
                tblOrderItems.refresh();
            });
            tblOrderItems.setItems(obList);
        }
        double total = 0;
        grandTotal = total;
        for (OrderTM tm : obList) {
            total += tm.getCost();
        }
        lblTotal.setText("Total:  Rs. " + total + " /=");
        grandTotal = total;
    }

    public void removeItemFromCartOnAction(ActionEvent actionEvent) {
        for (OrderTM tm : obList) {
            if (tm.getItemCode().equals(cmbItemCode.getValue())) ;
            {
                obList.remove(tm);
                tblOrderItems.refresh();
                break;
            }
        }
    }

    public void placeOrderOnAction(ActionEvent actionEvent) throws SQLException {
        try {
            connection = DBConnection.getInstance().getConnection();
            connection.setAutoCommit(false);
            boolean recorded = OrderCrudController.recordOrder(String.valueOf(cmbCustomerID.getValue()), (grandTotal-discount), String.valueOf(cmbPaymentMethod.getValue()), String.valueOf(cmbPaymentStatus.getValue()));

            if (recorded) {
                boolean saved = OrderCrudController.saveOrderDetails(OrderCrudController.getLastOrderID(), new ArrayList<OrderTM>(obList));

                if (saved) {
                    connection.commit();
                    if (!txtDiscountsGiven.getText().isEmpty()) {
                        DiscountCRUDController.addDiscount(OrderCrudController.getLastOrderID(), String.valueOf(cmbCustomerID.getValue()), grandTotal, Double.valueOf(txtDiscountsGiven.getText()), (grandTotal - Double.valueOf(txtDiscountsGiven.getText())));
                    }
                    for (OrderTM tm : obList) {
                        ItemCRUDController.updateItemQty(tm.getItemCode(), tm.getQty());
                    }
                    CustomerCRUDController.updateCustomerPoints((String) cmbCustomerID.getValue(), (int) Math.round(grandTotal) / 1000);
                    Notifications notify = Notifications.create().title("Order Recorded!");
                    notify.show();
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do You Wish To View/ Print Reciept?", ButtonType.YES, ButtonType.NO);
                    alert.initOwner(OrderMainPane.getScene().getWindow());
                    alert.showAndWait();
                    ButtonType button = alert.getResult();
                    if (button.equals(ButtonType.YES)) {
                        Customer c = CustomerCRUDController.getCustomer(String.valueOf(cmbCustomerID.getValue()));
                        HashMap map = new HashMap();
                        map.put("Customer NIC:", c.getCustomerNIC());
                        map.put("Customer Name:", c.getCustomerName());
                        map.put("Customer Contact:", c.getCustomerMobile());
                        map.put("OrderID", OrderCrudController.getLastOrderID());
                        map.put("Total", grandTotal);

                        JasperReport compiledReport = (JasperReport) JRLoader.loadObject(this.getClass().getResourceAsStream("/Reports/CustomerOrderReport.jasper"));
                        JasperPrint jasperPrint = JasperFillManager.fillReport(compiledReport, map, DBConnection.getInstance().getConnection());
                        JasperViewer.viewReport(jasperPrint, false);
                        setAllCustomers(PaymentCRUDController.getAllPendingPayments());

                    }
                    clearForm();
                } else {
                    connection.rollback();
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Something Went Wrong!..", ButtonType.OK);
                    alert.initOwner(OrderMainPane.getScene().getWindow());
                    alert.show();
                }

            } else {
                connection.rollback();
                Alert alert = new Alert(Alert.AlertType.ERROR, "Something Went Wrong!..", ButtonType.OK);
                alert.initOwner(OrderMainPane.getScene().getWindow());
                alert.show();
            }
        } catch (SQLException | ClassNotFoundException | JRException e) {
            e.printStackTrace();
//            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
//            alert.initOwner(OrderMainPane.getScene().getWindow());
//            alert.show();
        } finally {
            connection.setAutoCommit(true);
        }

    }

    public void initialize() {
        lblDiscount.setVisible(false);
        try {
            cmbCustomerID.getItems().addAll(CustomerCRUDController.getAllCustomerID());
            cmbCustomerMobile.getItems().addAll(CustomerCRUDController.getAllCustomerMobileNumbers());
            cmbItemCode.getItems().addAll(ItemCRUDController.getAllItemCodes());
            cmbPaymentMethod.getItems().addAll("Cash", "Credit / Debit Card");
            cmbPaymentStatus.getItems().addAll("Paid", "Pending");

            setAllCustomers(PaymentCRUDController.getAllPendingPayments());
            cmbCustomerMobile.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                try {
                    Customer customer = CustomerCRUDController.getCustomerByMobile(String.valueOf(newValue));
                    cmbCustomerID.getSelectionModel().select(customer.getCustomerNIC());
                    txtCustomerName.setText(customer.getCustomerName());
                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                    Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
                    alert.initOwner(OrderMainPane.getScene().getWindow());
                    alert.show();

                }
                if (cmbCustomerMobile.getValue() != null && cmbCustomerID.getValue() != null && cmbPaymentStatus.getValue() != null && cmbPaymentMethod.getValue() != null && obList.size() > 0) {

                    btnPlaceOrder.setDisable(false);
                    btnSendOnDelivery.setDisable(false);
                }
            });

            cmbCustomerID.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                try {
                    Customer customer = CustomerCRUDController.getCustomer(String.valueOf(newValue));
                    cmbCustomerMobile.getSelectionModel().select(customer.getCustomerMobile());
                    txtCustomerName.setText(customer.getCustomerName());
                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                    Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
                    alert.initOwner(OrderMainPane.getScene().getWindow());
                    alert.show();

                }
                if (cmbCustomerMobile.getValue() != null && cmbCustomerID.getValue() != null && cmbPaymentStatus.getValue() != null && cmbPaymentMethod.getValue() != null && !(obList.isEmpty())) {

                    btnPlaceOrder.setDisable(false);
                    btnSendOnDelivery.setDisable(false);
                }
            });

            cmbItemCode.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                try {
                    Item item = ItemCRUDController.getItem(String.valueOf(newValue));
                    if (item != null) {
                        txtItemName.setText(item.getDescription());
                        txtQtyOnHand.setText(String.valueOf(item.getAvailableQty()));
                        txtUnitPrice.setText(String.valueOf(item.getSellingPrice()));
                        ItemImage.setImage(new Image("file:" + item.getImage_path()));
                    }
                    if (Pattern.compile("^[0-9]+([.][0-9]{1,3})?$").matcher(txtQty.getText()).matches()) {
                        btnAddToCart.setDisable(false);
                        btnRemoveFromCart.setDisable(false);
                    }
                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                    Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
                    alert.initOwner(OrderMainPane.getScene().getWindow());
                    alert.show();
                }
                if (cmbCustomerMobile.getValue() != null && cmbCustomerID.getValue() != null && cmbPaymentStatus.getValue() != null && cmbPaymentMethod.getValue() != null && !(obList.isEmpty())) {

                    btnPlaceOrder.setDisable(false);
                    btnSendOnDelivery.setDisable(false);
                }
            });
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.initOwner(OrderMainPane.getScene().getWindow());
            alert.show();
        }
        cmbPaymentMethod.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (cmbCustomerMobile.getValue() != null && cmbCustomerID.getValue() != null && cmbPaymentStatus.getValue() != null && cmbPaymentMethod.getValue() != null && !(obList.isEmpty())) {

                btnPlaceOrder.setDisable(false);
                btnSendOnDelivery.setDisable(false);
            }
        });

        cmbPaymentStatus.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (cmbCustomerMobile.getValue() != null && cmbCustomerID.getValue() != null && cmbPaymentStatus.getValue() != null && cmbPaymentMethod.getValue() != null && !(obList.isEmpty())) {

                btnPlaceOrder.setDisable(false);
                btnSendOnDelivery.setDisable(false);
            }
        });

        colItem.setCellValueFactory(new PropertyValueFactory<>("itemCode"));
        colDesc.setCellValueFactory(new PropertyValueFactory<>("Description"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("Qty"));
        colCost.setCellValueFactory(new PropertyValueFactory<>("cost"));
        colOption.setCellValueFactory(new PropertyValueFactory<>("btn"));

        btnAddToCart.setDisable(true);
        btnRemoveFromCart.setDisable(true);
        btnPlaceOrder.setDisable(true);
        btnSendOnDelivery.setDisable(true);
    }

    public void sendOnDeliveryOnAction(ActionEvent actionEvent) throws SQLException {

        try {
            Vehicle v = VehicleCRUDController.getAvailableVehicle();
            Driver d = DriverCRUDController.getAvailableDriver();
            if (d != null && v != null) {
                connection = DBConnection.getInstance().getConnection();
                connection.setAutoCommit(false);
                boolean recorded = OrderCrudController.recordOrder(String.valueOf(cmbCustomerID.getValue()), (grandTotal-discount), String.valueOf(cmbPaymentMethod.getValue()), String.valueOf(cmbPaymentStatus.getValue()));

                if (recorded) {
                    boolean saved = OrderCrudController.saveOrderDetails(OrderCrudController.getLastOrderID(), new ArrayList<OrderTM>(obList));

                    if (saved) {
                        connection.commit();
                        if (!txtDiscountsGiven.getText().isEmpty()) {
                            DiscountCRUDController.addDiscount(OrderCrudController.getLastOrderID(), String.valueOf(cmbCustomerID.getValue()), grandTotal, Double.valueOf(txtDiscountsGiven.getText()), (grandTotal - Double.valueOf(txtDiscountsGiven.getText())));
                        }
                        Notifications notify2 = Notifications.create().title("Order Recorded!");
                        notify2.show();
                        for (OrderTM tm : obList) {
                            ItemCRUDController.updateItemQty(tm.getItemCode(), tm.getQty());
                        }
                        Alert alert2 = new Alert(Alert.AlertType.CONFIRMATION, "Do You Wish To View/ Print Reciept?", ButtonType.YES, ButtonType.NO);
                        alert2.initOwner(OrderMainPane.getScene().getWindow());
                        alert2.showAndWait();
                        ButtonType button = alert2.getResult();
                        if (button.equals(ButtonType.YES)) {
                            Customer c = CustomerCRUDController.getCustomer(String.valueOf(cmbCustomerID.getValue()));
                            HashMap map = new HashMap();
                            map.put("Customer NIC:", c.getCustomerNIC());
                            map.put("Customer Name:", c.getCustomerName());
                            map.put("Customer Contact:", c.getCustomerMobile());
                            map.put("OrderID", OrderCrudController.getLastOrderID());
                            map.put("Total", grandTotal);

                            JasperReport compiledReport = (JasperReport) JRLoader.loadObject(this.getClass().getResourceAsStream("/Reports/CustomerOrderReport.jasper"));
                            JasperPrint jasperPrint = JasperFillManager.fillReport(compiledReport, map, DBConnection.getInstance().getConnection());
                            JasperViewer.viewReport(jasperPrint, false);
                        }
                        CustomerCRUDController.updateCustomerPoints((String) cmbCustomerID.getValue(), (int) Math.round(grandTotal) / 1000);
                        OrderMainPane.getChildren().clear();
                        OrderMainPane.getChildren().add(FXMLLoader.load(getClass().getResource("/View/OrderManagementForm.fxml")));
                        if (DeliveryCRUDController.addDelivery(DeliveryCRUDController.getDeliveryID(), LocalDate.now(), v.getVehicleNumber(), d.getNic(), "Pending", OrderCrudController.getLastOrderID(), (String) cmbCustomerID.getValue())) {
                            Notifications notify = Notifications.create().title("Order Sent On Delivery").hideAfter(Duration.seconds(4)).darkStyle();
                            notify.show();
                            VehicleCRUDController.updateVehicleAvailability(v.getVehicleNumber(), false);
                            DriverCRUDController.updateDriverAvailability(d.getNic(), false);
                        }
                    } else {
                        connection.rollback();
                        Alert alert = new Alert(Alert.AlertType.ERROR, "OOPS, Something Went Wrong!..", ButtonType.OK);
                        alert.initOwner(OrderMainPane.getScene().getWindow());
                        alert.show();
                    }


                } else {
                    connection.rollback();
                    Alert alert = new Alert(Alert.AlertType.ERROR, "OOPS, Something Went Wrong!..", ButtonType.OK);
                    alert.initOwner(OrderMainPane.getScene().getWindow());
                    alert.show();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "No Available Drivers Or Vehicles Found", ButtonType.OK);
                alert.initOwner(OrderMainPane.getScene().getWindow());
                alert.show();
            }
        } catch (SQLException | ClassNotFoundException | IOException | JRException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.initOwner(OrderMainPane.getScene().getWindow());
            alert.show();
        } finally {
            connection.setAutoCommit(true);
        }
        clearForm();
    }

    private void setAllCustomers(ArrayList<Payment> paymentList) {
        OrderScrollerAnchorPane.getChildren().clear();

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
    public void setViewCustomerPane(String pymentID, String OrderId, String paymentType, String paymentAmount, String paymentStatus) {
//        Checks YPosition For Customer View Pane Placement.
        double layoutY;
        if (LayoutYValues.isEmpty()) {
            layoutY = 4;
            LayoutYValues.add(layoutY);
        } else {
            double size = LayoutYValues.get((LayoutYValues.size()) - 1) + 70;
            if (OrderScrollerAnchorPane.getHeight() < size) {
                OrderScrollerAnchorPane.setPrefHeight(size + 100);
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
                if (PaymentCRUDController.updatePaymentStatus(pymentID, "Paid")) {
                    Notifications notify = Notifications.create().title("Payment Status Updated!").hideAfter(Duration.seconds(4)).darkStyle();
                    notify.show();
                    setAllCustomers(PaymentCRUDController.getAllPendingPayments());
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });
        pane.getChildren().add(btn);

        OrderScrollerAnchorPane.getChildren().add(pane);


    }

    public void validateQty(KeyEvent keyEvent) {
        if (Pattern.compile("^[0-9]+([.][0-9]{1,3})?$").matcher(txtQty.getText()).matches() && (cmbItemCode.getValue() != null)) {
            btnAddToCart.setDisable(false);
            btnRemoveFromCart.setDisable(false);
        } else {
            btnAddToCart.setDisable(true);
            btnRemoveFromCart.setDisable(true);
        }
    }

    public void clearForm() {
        cmbCustomerID.getSelectionModel().clearSelection();
        cmbCustomerMobile.getSelectionModel().clearSelection();
        txtCustomerName.clear();
        cmbPaymentStatus.getSelectionModel().clearSelection();
        cmbPaymentMethod.getSelectionModel().clearSelection();
        txtDiscountsGiven.clear();
        cmbItemCode.getSelectionModel().clearSelection();
        txtQty.clear();
        txtQtyOnHand.clear();
        txtUnitPrice.clear();
        txtItemName.clear();
        obList.clear();
        tblOrderItems.refresh();
        btnAddToCart.setDisable(true);
        btnRemoveFromCart.setDisable(true);
        btnSendOnDelivery.setDisable(true);
        btnPlaceOrder.setDisable(true);
        lblTotal.setText("Total: ");
        lblDiscount.setVisible(false);
    }

    public void ValidateField(KeyEvent keyEvent) {

        Pattern Discount = Pattern.compile("^[0-9]*$");
        if (Discount.matcher(txtDiscountsGiven.getText()).matches()) {
            if (txtDiscountsGiven.getText().isEmpty()) {
                lblDiscount.setVisible(false);
                discount=0;
            }
            if (cmbCustomerMobile.getValue() != null && cmbCustomerID.getValue() != null && cmbPaymentStatus.getValue() != null && cmbPaymentMethod.getValue() != null && obList.size() > 0) {
                btnPlaceOrder.setDisable(false);
                btnSendOnDelivery.setDisable(false);
            }

            if (Double.valueOf(txtDiscountsGiven.getText()) > grandTotal) {
                btnPlaceOrder.setDisable(true);
                btnSendOnDelivery.setDisable(true);
            } else {
                lblDiscount.setVisible(true);
                discount=Double.valueOf(txtDiscountsGiven.getText());
                lblDiscount.setText("- Discount: " + discount);
            }


        } else {

            btnPlaceOrder.setDisable(true);
            btnSendOnDelivery.setDisable(true);
        }
    }
}
