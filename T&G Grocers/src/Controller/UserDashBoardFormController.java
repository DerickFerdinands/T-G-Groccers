package Controller;

import Controller.CRUD.*;
import Database.DBConnection;
import Model.*;
import Util.CrudUtil;
import View.TM.OrderTM;
import View.TM.UserTM;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.stage.Stage;
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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class UserDashBoardFormController {
    public AnchorPane OptionPane;
    public AnchorPane userContext;
    public AnchorPane NavigationContext;
    public AnchorPane MainContext;
    public AnchorPane adminLoginContext;
    public AnchorPane SwitchUserLogin;
    public Label lblDate;
    public Label lblTime;
    public AnchorPane ItemViewPane;
    public Text CartItemCount;
    public Label lblTotalItemCount;
    public Label SouthItemCount;
    public Label SouthTotalPrice;
    public JFXButton btnNorthCheckOut;
    public JFXComboBox cmbItemCategory;
    public JFXComboBox cmbCustomerNIC;
    public JFXComboBox cmbCustomerName;
    public JFXTextField ttxSearch;
    public AnchorPane AnchorPanePLaceOrder;
    public AnchorPane subPOAP;
    public TableView<OrderTM> tblOrderItems;
    public TableColumn colItem;
    public TableColumn colDesc;
    public TableColumn colUnitPrice;
    public TableColumn colQty;
    public TableColumn colCost;
    public TableColumn colOption;
    public JFXComboBox cmbPaymentMethod;
    public JFXComboBox cmbPaymentStatus;
    public JFXButton btnPlaceOrder;
    public TextField txtCustomerName;
    public Label lblTotal;
    public JFXTextField txtAdminUserName;
    public JFXTextField txtAdminPassword;
    public JFXTextField txtSwitchUserName;
    public JFXTextField txtSwitchPassword;
    public ImageView imgViewerUSerPic;
    public Label txtUserName;
    public Text txtJobTitle;
    ArrayList<Double> LayoutXValues = new ArrayList<>();
    ArrayList<Double> LayoutYValues = new ArrayList<>();
    ObservableList<OrderTM> obList = FXCollections.observableArrayList();
    double grandTotal;
    Connection connection = null;

    public void ShowPaneOnDrag(MouseEvent mouseEvent) {
        OptionPane.setVisible(true);
    }


    public void initialize() {
        colItem.setCellValueFactory(new PropertyValueFactory<>("itemCode"));
        colDesc.setCellValueFactory(new PropertyValueFactory<>("Description"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("Qty"));
        colCost.setCellValueFactory(new PropertyValueFactory<>("cost"));
        colOption.setCellValueFactory(new PropertyValueFactory<>("btn"));
        tblOrderItems.setItems(obList);
        OptionPane.setVisible(false);
        userContext.setVisible(false);
        setDateAndTime();

        cmbPaymentMethod.getItems().addAll("Cash", "Credit / Debit Card");
        cmbPaymentStatus.getItems().addAll("Paid", "Pending");
        try {
            setItemView(ItemCRUDController.getAllItems());
            cmbItemCategory.getItems().add("All Items");
            cmbItemCategory.getItems().addAll(ItemCRUDController.getDistinctItemCategories());

            cmbItemCategory.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                checkCategory((String) newValue);
            });

            lblTotalItemCount.setText("Total Products: " + ItemCRUDController.getItemCount());

            cmbCustomerNIC.getItems().addAll(CustomerCRUDController.getAllCustomerID());
            cmbCustomerName.getItems().addAll(CustomerCRUDController.getAllCustomerMobileNumbers());

            cmbCustomerNIC.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                try {
                    Customer c = CustomerCRUDController.getCustomer((String) newValue);
                    cmbCustomerName.getSelectionModel().select(c.getCustomerMobile());
                    txtCustomerName.setText(c.getCustomerName());
                } catch (SQLException | ClassNotFoundException e) {

                }
            });

            cmbCustomerName.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                try {
                    Customer c = CustomerCRUDController.getCustomerByMobile((String) newValue);
                    cmbCustomerNIC.getSelectionModel().select(c.getCustomerNIC());
                    txtCustomerName.setText(c.getCustomerName());
                } catch (SQLException | ClassNotFoundException e) {

                }

            });
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.initOwner(MainContext.getScene().getWindow());
            alert.show();
        }


    }

    private void checkCategory(String Category) {
        if (Category.equals("All Items")) {
            try {
                setItemView(ItemCRUDController.getAllItems());
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
                alert.initOwner(MainContext.getScene().getWindow());
                alert.show();
            }
        } else {
            try {
                ResultSet result = CrudUtil.execute("SELECT * FROM Item WHERE category=?", Category);
                ArrayList<Item> itemList = new ArrayList<>();
                while (result.next()) {
                    itemList.add(new Item(result.getString(1), result.getString(2), result.getString(3), result.getString(4), result.getDouble(5), result.getDouble(6), result.getDouble(7), result.getString(8)));
                }
                setItemView(itemList);
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
                alert.initOwner(MainContext.getScene().getWindow());
                alert.show();

            }

        }
    }

    private void setItemView(ArrayList<Item> itemList) {
        ItemViewPane.getChildren().clear();
        double MaxlayoutX = 1500;
        double layoutX = 59;
        double layoutY = 19;


        for (Item item : itemList) {
            if (LayoutXValues.isEmpty()) {
                layoutX = 59;
                LayoutXValues.add(layoutX);
            } else if (layoutX >= MaxlayoutX) {
                layoutX = 59;
                layoutY += 290;
                ItemViewPane.setPrefHeight(layoutY + 260);
            } else {
                layoutX += 300;
                LayoutXValues.add(layoutX);

            }

            setItem(item.getItemCode(), item.getDescription(), String.valueOf(item.getSellingPrice()), String.valueOf(item.getAvailableQty()), item.getImage_path(), layoutX, layoutY);
        }

        LayoutYValues.clear();
        LayoutXValues.clear();

    }

    private void setItem(String itemCode, String desc, String unitPrice, String AvailableQty, String imageLocation, double layoutX, double layoutY) {
        AnchorPane pane = new AnchorPane();
        pane.setPrefWidth(262);
        pane.setPrefHeight(254);
        pane.setLayoutY(layoutY);
        pane.setLayoutX(layoutX);
        pane.setStyle("-fx-border-color:  #364FCD");
        ImageView itemImage = new ImageView("file:" + imageLocation);
        itemImage.setFitWidth(124);
        itemImage.setFitHeight(113);
        itemImage.setLayoutX(70);
        itemImage.setLayoutY(4);

        pane.getChildren().add(itemImage);

        HBox box = new HBox();
        box.setPrefWidth(258);
        box.setPrefHeight(27);
        box.setLayoutX(0);
        box.setLayoutY(-20);
        box.setAlignment(Pos.CENTER);

        Label ItemLabel = new Label(itemCode + " - " + AvailableQty);
        ItemLabel.setTextFill(Color.color(0.02, 0.02, 0.02));
        ItemLabel.setStyle("-fx-font-size: 10");
        box.getChildren().add(ItemLabel);

        pane.getChildren().add(box);

        HBox box1 = new HBox();
        box1.setPrefWidth(258);
        box1.setPrefHeight(27);
        box1.setLayoutX(2);
        box1.setLayoutY(112);
        box1.setAlignment(Pos.CENTER);

        Label label = new Label(desc);
        label.setTextFill(Color.color(0.02, 0.02, 0.02));
        label.setStyle("-fx-font-size: 20");
        box1.getChildren().add(label);

        pane.getChildren().add(box1);


        Label lable2 = new Label("Unit Price");
        lable2.setTextFill(Color.color(0.02, 0.02, 0.02));
        lable2.setStyle("-fx-font-size: 15");
        lable2.setLayoutX(103);
        lable2.setLayoutY(136);
        pane.getChildren().add(lable2);

        HBox box2 = new HBox();
        box2.setPrefWidth(258);
        box2.setPrefHeight(27);
        box2.setLayoutX(2);
        box2.setLayoutY(152);
        box2.setAlignment(Pos.CENTER);

        Label lable3 = new Label(unitPrice);
        lable3.setTextFill(Color.color(0.02, 0.02, 0.02));
        lable3.setStyle("-fx-font-size: 17");
        box2.getChildren().add(lable3);

        pane.getChildren().add(box2);

        JFXTextField field = new JFXTextField();
        field.setPrefWidth(79);
        field.setPrefHeight(25);
        field.setLayoutX(92);
        field.setLayoutY(171);
        field.setText("1");
        field.setAlignment(Pos.CENTER);
        pane.getChildren().add(field);

        JFXButton btn1 = new JFXButton("-");
        btn1.setPrefWidth(35);
        btn1.setPrefHeight(25);
        btn1.setLayoutX(53);
        btn1.setLayoutY(167);
        btn1.setStyle("-fx-background-color:  #00796B");
        btn1.setTextFill(Color.color(0.98, 0.99, 0.99));
        btn1.setOnAction(event -> {
            if (Double.valueOf(field.getText()) > 1) {
                field.setText(Double.valueOf(field.getText()) - 1 + "");
            }
        });

        JFXButton btn2 = new JFXButton("+");
        btn2.setPrefWidth(35);
        btn2.setPrefHeight(25);
        btn2.setLayoutX(174);
        btn2.setLayoutY(167);
        btn2.setStyle("-fx-background-color:  #00796B");
        btn2.setTextFill(Color.color(0.98, 0.99, 0.99));

        btn2.setOnAction(event -> {
            if (Double.valueOf(field.getText()) < Double.valueOf(AvailableQty)) {
                field.setText(Double.valueOf(field.getText()) + 1 + "");
            }
        });

        JFXButton btn3 = new JFXButton("ADD TO CART");
        btn3.setPrefWidth(177);
        btn3.setPrefHeight(33);
        btn3.setLayoutX(43);
        btn3.setLayoutY(210);
        btn3.setStyle("-fx-background-color:  #00796B");
        btn3.setTextFill(Color.color(0.98, 0.99, 0.99));

        btn3.setOnAction(event -> {
            OrderTM exists = null;
            for (OrderTM tm : obList) {
                if (tm.getItemCode().equals(itemCode)) {
                    exists = tm;
                    break;
                } else {
                    exists = null;
                }
            }
            if (exists == null) {
                double qty = Double.valueOf(field.getText());
                if (qty <= Double.valueOf(AvailableQty)) {
                    double tempunitPrice = Double.valueOf(unitPrice);
                    double cost = tempunitPrice * qty;
                    System.out.println(cost);
                    Button btn = new Button("Delete");
                    btn.setOnAction(event1 -> {
                        for (OrderTM tm : obList) {
                            if (tm.getItemCode().equals(itemCode)) {
                                obList.remove(tm);
                                break;
                            }
                        }
                    });
                    obList.add(new OrderTM(itemCode, desc, tempunitPrice, qty, cost, btn));

                    Notifications notify = Notifications.create().title("Item Added To Cart").hideAfter(Duration.seconds(4)).position(Pos.BASELINE_RIGHT).graphic(new ImageView(new Image("/Assets/2x/outline_shopping_cart_black_24dp.png"))).text("Item: " + desc + " Qty: " + qty).darkStyle();
                    notify.show();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Item Qty Exceeded Or Invalid Qty");
                    alert.initOwner(MainContext.getScene().getWindow());
                    alert.show();
                }
            } else {
                double qty = Double.valueOf(field.getText());
                double tempQty = exists.getQty() + qty;
                if (tempQty <= Double.valueOf(AvailableQty)) {
                    exists.setQty(tempQty);
                    exists.setCost(tempQty * (exists.getUnitPrice()));
                    Notifications notify = Notifications.create().title("Item Updated In Cart!").hideAfter(Duration.seconds(4)).position(Pos.BASELINE_RIGHT).graphic(new ImageView(new Image("/Assets/2x/outline_shopping_cart_black_24dp.png"))).text("Item: " + desc + " Updated Qty: " + exists.getQty()).darkStyle();
                    notify.show();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Item Qty Exceeded Or Invalid Qty");
                    alert.initOwner(MainContext.getScene().getWindow());
                    alert.show();
                }
            }
            SouthItemCount.setText("CART ITEM COUNT: " + obList.size());
            CartItemCount.setText(String.valueOf(obList.size()));
            double total = 0;
            for (OrderTM tm : obList) {
                total += tm.getCost();
            }
            SouthTotalPrice.setText("TOTAL: Rs." + total + "/=");
            btnNorthCheckOut.setText("Check Out: Rs." + total + "/=");
            lblTotal.setText("Total: Rs." + total + "/=");
            grandTotal = total;
            tblOrderItems.refresh();
        });

        pane.getChildren().add(btn1);
        pane.getChildren().add(btn2);
        pane.getChildren().add(btn3);

        ItemViewPane.getChildren().add(pane);
    }

    private void setDateAndTime() {
        lblDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));

        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            LocalTime currentTime = LocalTime.now();
            lblTime.setText(currentTime.getHour() + ":" + currentTime.getMinute() + ":" + currentTime.getSecond());
        }),
                new KeyFrame(Duration.seconds(1)));
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();

    }

    public void hideOnClick(MouseEvent mouseEvent) {
        OptionPane.setVisible(false);
        userContext.setVisible(false);
    }

    public void showOptionsOnClcick(MouseEvent mouseEvent) {
        userContext.setVisible(true);
    }

    public void exitApplicationOnAction(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are You Sure?", ButtonType.YES, ButtonType.NO);
        alert.initOwner(MainContext.getScene().getWindow());
        alert.showAndWait();
        ButtonType button = alert.getResult();
        if (button.equals(ButtonType.YES)) {
            System.exit(0);
        }
    }

    public void ManageCustomersOnAction(ActionEvent actionEvent) throws IOException {
        setUI("CustomerManagementForm");
    }

    public void manageItemOnAction(ActionEvent actionEvent) throws IOException {
        setUI("ItemManagementForm");
    }

    public void setUI(String URI) throws IOException {
        NavigationContext.getChildren().clear();
        NavigationContext.getChildren().add(FXMLLoader.load(getClass().getResource("/View/" + URI + ".fxml")));
    }

    public void goToDashBoardOnAction(ActionEvent actionEvent) throws IOException {
        MainContext.getChildren().clear();
        MainContext.getChildren().add(FXMLLoader.load(getClass().getResource("/View/UserDashBoardForm.fxml")));
    }

    public void manageStockOnAction(ActionEvent actionEvent) throws IOException {
        setUI("StockManagementForm");
    }

    public void manageOrdersOnAction(ActionEvent actionEvent) throws IOException {
        setUI("OrderManagementForm");
    }

    public void manageDeliveryOnAction(ActionEvent actionEvent) throws IOException {
        setUI("DeliveryManagementForm");
    }

    public void manageVendorsOnAction(ActionEvent actionEvent) throws IOException {
        setUI("VendorManagementForm");
    }

    public void managePurchasesOnAction(ActionEvent actionEvent) throws IOException {
        setUI("PurchaseManagementForm");
    }

    public void showLoginPaneOnAction(ActionEvent actionEvent) {
        adminLoginContext.setVisible(true);
    }

    public void hideAdminLoginContextOnAction(MouseEvent mouseEvent) {
        adminLoginContext.setVisible(false);
    }

    public void logInAsAdminOnAction(ActionEvent actionEvent) throws IOException {
       loginAsAdmin();
    }

    public void loginAsAdmin(){
        try {
            boolean matched =false;
            ArrayList<Admin> adminList = AdminCRUDController.getALlAdmins();
            for (Admin admin : adminList) {
                if (admin.getUser_name().equals(txtAdminUserName.getText()) && admin.getPassword().equals(txtAdminPassword.getText())) {
                    matched=true;
                    MainContext.getChildren().clear();
                    MainContext.getChildren().add(FXMLLoader.load(getClass().getResource("/View/AdminDashBoardForm.fxml")));
                    Notifications notify = Notifications.create().title("Welcome "+admin.getName());
                    notify.show();
                }
            }
            if(matched==false){
                Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid Username Or Password", ButtonType.OK);
                alert.initOwner(MainContext.getScene().getWindow());
                alert.show();
            }
        } catch (SQLException | ClassNotFoundException | IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.initOwner(MainContext.getScene().getWindow());
            alert.show();
        }
    }

    public void signOutOnAction(ActionEvent actionEvent) throws IOException {
        MainContext.getChildren().clear();
        Stage stage = (Stage) MainContext.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/View/LoginForm.fxml"))));
        stage.centerOnScreen();
    }

    public void switchUserOnAction(ActionEvent actionEvent) {
        SwitchUserLogin.setVisible(true);
    }

    public void hideSwitchUserLoginContextOnAction(MouseEvent mouseEvent) {
        SwitchUserLogin.setVisible(false);
    }

    public void checkCustomerMobile(KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.ENTER)) {
            try {
                Customer c = CustomerCRUDController.getCustomerByMobile((String) cmbCustomerName.getValue());
                if (c != null) {
                    cmbCustomerNIC.getSelectionModel().select(c.getCustomerNIC());
                }
            } catch (SQLException | ClassNotFoundException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
                alert.initOwner(MainContext.getScene().getWindow());
                alert.show();
            }
        }
    }

    public void checkCustomerNIC(KeyEvent keyEvent) {

        if (keyEvent.getCode().equals(KeyCode.ENTER)) {
            try {
                Customer c = CustomerCRUDController.getCustomer((String) cmbCustomerNIC.getValue());
                if (c != null) {
                    cmbCustomerName.getSelectionModel().select(c.getCustomerMobile());
                }
            } catch (SQLException | ClassNotFoundException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
                alert.initOwner(MainContext.getScene().getWindow());
                alert.show();
            }
        }
    }

    public void searchItem(ActionEvent actionEvent) {
        ArrayList<Item> addToView = new ArrayList<>();
        String search = ttxSearch.getText();
        if (!search.isEmpty()) {
            try {
                ArrayList<Item> ItemList = ItemCRUDController.getAllItems();
                for (Item item : ItemList) {
                    if (item.getItemCode().toUpperCase().contains(search.toUpperCase()) || item.getDescription().toUpperCase().contains(search.toUpperCase())) {
                        addToView.add(item);
                    }

                    if (addToView.isEmpty()) {
                        ItemViewPane.getChildren().clear();
                        Label label = new Label("No Items Found For " + search + " , Recheck Item Name/Code.");
                        label.setLayoutX(600);
                        label.setLayoutY(20);
                        label.setStyle("-fx-font-size: 30");
                        ItemViewPane.getChildren().add(label);
                    } else {
                        setItemView(addToView);
                    }
                }
            } catch (SQLException | ClassNotFoundException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
                alert.initOwner(MainContext.getScene().getWindow());
                alert.show();
            }
        } else {
            try {
                ArrayList<Item> ItemList = ItemCRUDController.getAllItems();
                setItemView(ItemList);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void placeOrderOnAction(ActionEvent actionEvent) throws SQLException {
        try {
            connection = DBConnection.getInstance().getConnection();
            connection.setAutoCommit(false);
            boolean recorded = OrderCrudController.recordOrder(String.valueOf(cmbCustomerNIC.getValue()), grandTotal, String.valueOf(cmbPaymentMethod.getValue()), String.valueOf(cmbPaymentStatus.getValue()));

            if (recorded) {
                boolean saved = OrderCrudController.saveOrderDetails(OrderCrudController.getLastOrderID(), new ArrayList<OrderTM>(obList));

                if (saved) {
                    connection.commit();
                    for (OrderTM tm : obList) {
                        ItemCRUDController.updateItemQty(tm.getItemCode(), tm.getQty());
                    }
                    CustomerCRUDController.updateCustomerPoints((String)cmbCustomerNIC.getValue(),(int)Math.round(grandTotal)/1000);
                    Notifications notify = Notifications.create().title("Order Recorded!");
                    notify.show();
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do You Wish To View/ Print Reciept?", ButtonType.YES, ButtonType.NO);
                    alert.initOwner(MainContext.getScene().getWindow());
                    alert.showAndWait();
                    ButtonType button = alert.getResult();
                    if (button.equals(ButtonType.YES)) {
                        try {
                            Customer c = CustomerCRUDController.getCustomer(String.valueOf(cmbCustomerNIC.getValue()));
                            HashMap map = new HashMap();
                            map.put("Customer NIC:", c.getCustomerNIC());
                            map.put("Customer Name:", c.getCustomerName());
                            map.put("Customer Contact:", c.getCustomerMobile());
                            map.put("OrderID", OrderCrudController.getLastOrderID());
                            map.put("Total", grandTotal);

                            JasperReport compiledReport = (JasperReport) JRLoader.loadObject(this.getClass().getResourceAsStream("/Reports/CustomerOrderReport.jasper"));
                            JasperPrint jasperPrint = JasperFillManager.fillReport(compiledReport, map, DBConnection.getInstance().getConnection());
                            JasperViewer.viewReport(jasperPrint,false);


                        } catch (JRException e) {
                            e.printStackTrace();
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                    MainContext.getChildren().clear();
                    MainContext.getChildren().add(FXMLLoader.load(getClass().getResource("/View/UserDashBoardForm.fxml")));
                } else {
                    connection.rollback();
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Something Went Wrong!..", ButtonType.OK);
                    alert.initOwner(MainContext.getScene().getWindow());
                    alert.show();
                }


            } else {
                connection.rollback();
                Alert alert = new Alert(Alert.AlertType.ERROR, "Something Went Wrong!..", ButtonType.OK);
                alert.initOwner(MainContext.getScene().getWindow());
                alert.show();
            }
        } catch (SQLException | ClassNotFoundException | IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.initOwner(MainContext.getScene().getWindow());
            alert.show();
        } finally {
            connection.setAutoCommit(true);
        }
    }

    public void sendOrderOnDelivery(ActionEvent actionEvent) throws SQLException {

        try {
            Vehicle v = VehicleCRUDController.getAvailableVehicle();
            Driver d = DriverCRUDController.getAvailableDriver();
            if (d != null && v != null) {
                connection = DBConnection.getInstance().getConnection();
                connection.setAutoCommit(false);
                boolean recorded = OrderCrudController.recordOrder(String.valueOf(cmbCustomerNIC.getValue()), grandTotal, String.valueOf(cmbPaymentMethod.getValue()), String.valueOf(cmbPaymentStatus.getValue()));

                if (recorded) {
                    boolean saved = OrderCrudController.saveOrderDetails(OrderCrudController.getLastOrderID(), new ArrayList<OrderTM>(obList));

                    if (saved) {
                        connection.commit();
                        Notifications notify1 = Notifications.create().title("Order Recorded!").hideAfter(Duration.seconds(4)).darkStyle();
                        notify1.show();
                        CustomerCRUDController.updateCustomerPoints((String)cmbCustomerNIC.getValue(),(int)Math.round(grandTotal)/1000);
                        for (OrderTM tm : obList) {
                            ItemCRUDController.updateItemQty(tm.getItemCode(), tm.getQty());
                        }
                        MainContext.getChildren().clear();
                        MainContext.getChildren().add(FXMLLoader.load(getClass().getResource("/View/UserDashBoardForm.fxml")));
                        if (DeliveryCRUDController.addDelivery(DeliveryCRUDController.getDeliveryID(), LocalDate.now(), v.getVehicleNumber(), d.getNic(), "Pending", OrderCrudController.getLastOrderID(), (String) cmbCustomerNIC.getValue())) {
                            Notifications notify = Notifications.create().title("Order Sent On Delivery").hideAfter(Duration.seconds(4)).darkStyle();
                            notify.show();
                            VehicleCRUDController.updateVehicleAvailability(v.getVehicleNumber(), false);
                            DriverCRUDController.updateDriverAvailability(d.getNic(), false);
                            Alert alert2 = new Alert(Alert.AlertType.CONFIRMATION, "Do You Wish To View/ Print Reciept?", ButtonType.YES, ButtonType.NO);
                            alert2.initOwner(MainContext.getScene().getWindow());
                            alert2.showAndWait();
                            ButtonType button = alert2.getResult();
                            if (button.equals(ButtonType.YES)) {
                                Customer c = CustomerCRUDController.getCustomer(String.valueOf(cmbCustomerNIC.getValue()));
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
                        }
                    } else {
                        connection.rollback();
                        Alert alert = new Alert(Alert.AlertType.ERROR, "OOPS, Something Went Wrong!..", ButtonType.OK);
                        alert.initOwner(MainContext.getScene().getWindow());
                        alert.show();
                    }


                } else {
                    connection.rollback();
                    Alert alert = new Alert(Alert.AlertType.ERROR, "OOPS, Something Went Wrong!..", ButtonType.OK);
                    alert.initOwner(MainContext.getScene().getWindow());
                    alert.show();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "No Available Drivers Or Vehicles Found", ButtonType.OK);
                alert.initOwner(MainContext.getScene().getWindow());
                alert.show();
            }
        } catch (SQLException | ClassNotFoundException | IOException | JRException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.initOwner(MainContext.getScene().getWindow());
            alert.show();
        } finally {
            connection.setAutoCommit(true);
        }
    }

    public void showCheckoutPaneOnAction(ActionEvent actionEvent) {
        AnchorPanePLaceOrder.setVisible(true);
    }

    public void closePaneOnAction(MouseEvent mouseEvent) {
        AnchorPanePLaceOrder.setVisible(false);
    }

    public void switchUserDetailsOnAction(ActionEvent actionEvent) {
    switchUser();
    }

    public  void switchUser(){
        boolean matched = false;
        try {
            ArrayList<UserTM> userList = UserCRUDController.getAllUserDetails();
            for (UserTM tm : userList) {
                if (tm.getUserName().equals(txtSwitchUserName.getText()) && tm.getPassword().equals(txtSwitchPassword.getText())) {
                    matched = true;

                    Notifications notify = Notifications.create().title("Welcome " + tm.getName());
                    notify.show();
                }
            }
            if (matched == false) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid UserName Or Password!", ButtonType.OK);
                alert.initOwner(MainContext.getScene().getWindow());
                alert.show();
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.initOwner(MainContext.getScene().getWindow());
            alert.show();
        }
    }

    public void nextAdminFieldOnAction(KeyEvent keyEvent) {
        if(keyEvent.getCode() == KeyCode.ENTER){
            if(keyEvent.getSource() instanceof TextField){
                TextField field = (TextField) keyEvent.getSource();
                switch(field.getId()){
                    case "txtAdminUserName": txtAdminPassword.requestFocus();break;
                    case "txtAdminPassword" : loginAsAdmin();break;
                }
            }
        }
    }

    public void nextFieldOnAction(KeyEvent keyEvent) {
        if(keyEvent.getCode() == KeyCode.ENTER){
            if(keyEvent.getSource() instanceof TextField){
                TextField field = (TextField) keyEvent.getSource();
                switch(field.getId()){
                    case "txtSwitchUserName": txtSwitchPassword.requestFocus();break;
                    case "txtSwitchPassword" : switchUser();break;
                }
            }
        }
    }
    final KeyCombination DashBoard = new KeyCodeCombination(KeyCode.DIGIT1,KeyCombination.CONTROL_DOWN);
    final KeyCombination Customer = new KeyCodeCombination(KeyCode.DIGIT2,KeyCombination.CONTROL_DOWN);
    final KeyCombination Item = new KeyCodeCombination(KeyCode.DIGIT3,KeyCombination.CONTROL_DOWN);
    final KeyCombination LowStock = new KeyCodeCombination(KeyCode.DIGIT4,KeyCombination.CONTROL_DOWN);
    final KeyCombination Orders = new KeyCodeCombination(KeyCode.DIGIT5,KeyCombination.CONTROL_DOWN);
    final KeyCombination Delivery = new KeyCodeCombination(KeyCode.DIGIT6,KeyCombination.CONTROL_DOWN);
    final KeyCombination vendors = new KeyCodeCombination(KeyCode.DIGIT7,KeyCombination.CONTROL_DOWN);
    final KeyCombination purchases = new KeyCodeCombination(KeyCode.DIGIT8,KeyCombination.CONTROL_DOWN);
   ;

    public void checkShortcutKeys(KeyEvent keyEvent) throws IOException {
        if(DashBoard.match(keyEvent)){
            MainContext.getChildren().clear();
            MainContext.getChildren().add(FXMLLoader.load(getClass().getResource("/View/UserDashBoardForm.fxml")));
        }else if(Customer.match(keyEvent)){
            setUI("CustomerManagementForm");
        }else if(Item.match(keyEvent)){
            setUI("ItemManagementForm");
        }else if(LowStock.match(keyEvent)){
            setUI("StockManagementForm");
        }else if(Orders.match(keyEvent)){
            setUI("OrderManagementForm");
        }else if(Delivery.match(keyEvent)){
            setUI("DeliveryManagementForm");
        }else if(vendors.match(keyEvent)){
            setUI("VendorManagementForm");
        }else if(purchases.match(keyEvent)){
            setUI("PurchaseManagementForm");
        }
    }
}
