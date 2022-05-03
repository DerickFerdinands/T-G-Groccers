package Controller;

import Controller.CRUD.DeliveryCRUDController;
import Controller.CRUD.DriverCRUDController;
import Controller.CRUD.VehicleCRUDController;
import Model.Delivery;
import Model.Driver;
import Model.Vehicle;
import Util.CrudUtil;
import View.TM.DeliveryTM;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

import java.sql.SQLException;
import java.util.ArrayList;

public class DeliveryManagementFormController {
    public AnchorPane DriverScrollerAnchorPane;
    public AnchorPane VehicleScrollerAnchorPane;
    public AnchorPane DeliveryMainPane;
    public TableView<DeliveryTM> tblOnDeliveryDetails;
    public TableColumn colOdeliveryID;
    public TableColumn colOOrderID;
    public TableColumn colOCustomerID;
    public TableColumn colODriver;
    public TableColumn colOVehicle;
    public TableColumn colOstatus;
    public TableColumn colODate;
    public TableColumn colOOption;
    public TableView<DeliveryTM> tblDeliveryHistory;
    public TableColumn coldeliveryID;
    public TableColumn colOrderID1;
    public TableColumn colCustomerID1;
    public TableColumn colDriver1;
    public TableColumn colVehicle1;
    public TableColumn colstatus1;
    public TableColumn colDate1;
    public TableColumn colOption1;
    ArrayList<Double> LayoutYValues = new ArrayList<>();
    ArrayList<Double> LayoutYValues2 = new ArrayList<>();

    public void initialize() {
         colOdeliveryID.setCellValueFactory(new PropertyValueFactory<>("DeliveryID"));
         colOOrderID.setCellValueFactory(new PropertyValueFactory<>("OrderID"));
         colOCustomerID.setCellValueFactory(new PropertyValueFactory<>("CustomerID"));
         colODriver.setCellValueFactory(new PropertyValueFactory<>("DriverNic"));
         colOVehicle.setCellValueFactory(new PropertyValueFactory<>("VehicleNumber"));
         colOstatus.setCellValueFactory(new PropertyValueFactory<>("DeliveryStatus"));
         colODate.setCellValueFactory(new PropertyValueFactory<>("date"));
         colOOption.setCellValueFactory(new PropertyValueFactory<>("btn"));

        coldeliveryID.setCellValueFactory(new PropertyValueFactory<>("DeliveryID"));
        colOrderID1.setCellValueFactory(new PropertyValueFactory<>("OrderID"));
        colCustomerID1.setCellValueFactory(new PropertyValueFactory<>("CustomerID"));
        colDriver1.setCellValueFactory(new PropertyValueFactory<>("DriverNic"));
        colVehicle1.setCellValueFactory(new PropertyValueFactory<>("VehicleNumber"));
        colstatus1.setCellValueFactory(new PropertyValueFactory<>("DeliveryStatus"));
        colDate1.setCellValueFactory(new PropertyValueFactory<>("date"));
        colOption1.setCellValueFactory(new PropertyValueFactory<>("btn"));
        try {
            setAllDrivers(DriverCRUDController.getAllAvailableDrivers());
            setAllVehicles(VehicleCRUDController.getAllAvailableVehicles());
            loadOnDeliverDetails();
            loadDeliveryHistory();

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.WARNING, e.getMessage(), ButtonType.OK);
            alert.initOwner(DeliveryMainPane.getScene().getWindow());
            alert.show();
        }
    }

    private void loadDeliveryHistory() {
        ObservableList<DeliveryTM> obList =FXCollections.observableArrayList();
        try {
            ArrayList<Delivery> onDeliveryList = DeliveryCRUDController.getDeliveredHistory();
            for(Delivery d : onDeliveryList){
                Button btn = new Button("Delete");
                obList.add(new DeliveryTM(d.getDeliveryID(),d.getOrderID(),d.getCustomerID(),d.getDriverID(),d.getVehicelNUmber(),d.getDeliveryStatus(),d.getDate(),btn));
                btn.setOnAction(event -> {
                    try {
                       DeliveryCRUDController.deleteDelivery(d.getDeliveryID());
                        setAllDrivers(DriverCRUDController.getAllAvailableDrivers());
                        setAllVehicles(VehicleCRUDController.getAllAvailableVehicles());
                        loadOnDeliverDetails();
                        loadDeliveryHistory();
                    } catch (SQLException | ClassNotFoundException e) {
                        e.printStackTrace();
                        Alert alert = new Alert(Alert.AlertType.WARNING, e.getMessage(), ButtonType.OK);
                        alert.initOwner(DeliveryMainPane.getScene().getWindow());
                        alert.show();
                    }

                });
            }
            tblDeliveryHistory.setItems(obList);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.WARNING, e.getMessage(), ButtonType.OK);
            alert.initOwner(DeliveryMainPane.getScene().getWindow());
            alert.show();
        }
    }

    private void loadOnDeliverDetails() {
        ObservableList<DeliveryTM> obList =FXCollections.observableArrayList();
        try {
            ArrayList<Delivery> onDeliveryList = DeliveryCRUDController.getPendingDeliveries();
            for(Delivery d : onDeliveryList){
                Button btn = new Button("Delivered");
                obList.add(new DeliveryTM(d.getDeliveryID(),d.getOrderID(),d.getCustomerID(),d.getDriverID(),d.getVehicelNUmber(),d.getDeliveryStatus(),d.getDate(),btn));
                btn.setOnAction(event -> {
                    try {
                        VehicleCRUDController.updateVehicleAvailability(d.getVehicelNUmber(),true);
                        DriverCRUDController.updateDriverAvailability(d.getDriverID(),true);
                        DeliveryCRUDController.updateDeleiveryStatus(d.getDeliveryID(),"Delivered");
                        setAllDrivers(DriverCRUDController.getAllAvailableDrivers());
                        setAllVehicles(VehicleCRUDController.getAllAvailableVehicles());
                        loadOnDeliverDetails();
                        loadDeliveryHistory();
                    } catch (SQLException | ClassNotFoundException e) {
                        e.printStackTrace();
                        Alert alert = new Alert(Alert.AlertType.WARNING, e.getMessage(), ButtonType.OK);
                        alert.initOwner(DeliveryMainPane.getScene().getWindow());
                        alert.show();
                    }

                });
            }
            tblOnDeliveryDetails.setItems(obList);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.WARNING, e.getMessage(), ButtonType.OK);
            alert.initOwner(DeliveryMainPane.getScene().getWindow());
            alert.show();
        }
    }

    private void setAllDrivers(ArrayList<Driver> driverList) {
        DriverScrollerAnchorPane.getChildren().clear();

//Sets View For Each Customer.
        for (Driver d : driverList) {
            setViewCustomerPane(d.getNic(), d.getName(), String.valueOf(d.getDob()), d.getLicense_number(), d.getContact(), d.getAddress(), String.valueOf(d.isAvailability()));
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
    public void setViewCustomerPane(String nic, String name, String dob, String license_Number, String contact, String address, String availability) {
//        Checks YPosition For Customer View Pane Placement.
        double layoutY;
        if (LayoutYValues.isEmpty()) {
            layoutY = 4;
            LayoutYValues.add(layoutY);
        } else {
            double size = LayoutYValues.get((LayoutYValues.size()) - 1) + 70;
            if (DriverScrollerAnchorPane.getHeight() < size) {
                DriverScrollerAnchorPane.setPrefHeight(size + 100);
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
        pane.getChildren().add(getHBox(201, 10, 25, nic));
        pane.getChildren().add(getLine(-127, -15, -127, 24, 300, 29));

        pane.getChildren().add(getHBox(201, 167, 25, name));
        pane.getChildren().add(getLine(-127, -15, -127, 24, 490, 29));

        pane.getChildren().add(getHBox(201, 315, 25, dob));
        pane.getChildren().add(getLine(-127, -15, -127, 24, 600, 29));

        pane.getChildren().add(getHBox(201, 454, 25, license_Number));
        pane.getChildren().add(getLine(-127, -15, -127, 24, 770, 29));

        pane.getChildren().add(getHBox(201, 605, 25, contact));
        pane.getChildren().add(getLine(-127, -15, -127, 24, 875, 29));

        pane.getChildren().add(getHBox(201, 702, 25, address));
        pane.getChildren().add(getLine(-127, -15, -127, 24, 979, 29));

        pane.getChildren().add(getHBox(201, 790, 25, availability));

        DriverScrollerAnchorPane.getChildren().add(pane);


    }

    private void setAllVehicles(ArrayList<Vehicle> vehicleList) {
        VehicleScrollerAnchorPane.getChildren().clear();


        for (Vehicle V : vehicleList) {
            setViewItemPane(V.getVehicleNumber(), V.getVehicleType(), String.valueOf(V.getStorageCapacity()), String.valueOf(V.isAvailability()));
        }

        LayoutYValues2.clear();
    }


    //    Creates Separate AnchorPanes For Customers to View.
    public void setViewItemPane(String vehicleNumber, String vehicleType, String storageCapacity, String availability) {
        //    Checks YPosition For Customer View Pane Placement.
        double layoutY;
        if (LayoutYValues2.isEmpty()) {
            layoutY = 4;
            LayoutYValues2.add(layoutY);
        } else {
            double size = LayoutYValues2.get((LayoutYValues2.size()) - 1) + 70;
            if (VehicleScrollerAnchorPane.getHeight() < size) {
                VehicleScrollerAnchorPane.setPrefHeight(size + 100);
            }
            layoutY = size;
            LayoutYValues2.add(layoutY);
        }
        AnchorPane pane = new AnchorPane();
        pane.setPrefSize(760, 68);

        pane.setLayoutX(30);
        pane.setLayoutY(layoutY);

//      Created A Rectangle Because AnchorPane returns void and Cannot Change Background Color.

        Rectangle rec = new Rectangle();
        rec.setHeight(68);
        rec.setWidth(760);
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


        pane.getChildren().add(getHBox(201, 10, 25, vehicleNumber));
        pane.getChildren().add(getLine(-127, -15, -127, 24, 300, 29));

        pane.getChildren().add(getHBox(201, 170, 25, vehicleType));
        pane.getChildren().add(getLine(-127, -15, -127, 24, 500, 29));

        pane.getChildren().add(getHBox(201, 380, 25, storageCapacity));
        pane.getChildren().add(getLine(-127, -15, -127, 24, 700, 29));

        pane.getChildren().add(getHBox(201, 560, 25, availability));


        VehicleScrollerAnchorPane.getChildren().add(pane);


    }


}


