package Controller;

import Controller.CRUD.ItemCRUDController;
import Controller.CRUD.OrderCrudController;
import Model.Item;
import Model.Order;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;

import java.sql.SQLException;
import java.util.ArrayList;

public class ViewStatsFormController {
    public BarChart BarChartCustomers;
    public BarChart BarChartItems;
    public Label lblHighestCustomer;
    public Label lblLowestCustomer;
    public Label lblHighestItem;
    public Label lblLowestItem;

    public void initialize(){

        try {
            XYChart.Series chart = new XYChart.Series<>();
            ArrayList<Order> DistinctCustomers = OrderCrudController.getDistinctCustomerSales();

            for (Order o : DistinctCustomers) {
                chart.getData().add(new XYChart.Data(o.getCustomerNIC(), o.getTotalPrice()));
            }

            BarChartCustomers.getData().add(chart);



            XYChart.Series chart2 = new XYChart.Series<>();
            ArrayList<Item> DistinctItems = ItemCRUDController.getDistinctItemSales();
            chart2.getData().add(new XYChart.Data("Derick", 10));

            for (Item i : DistinctItems) {
                chart2.getData().add(new XYChart.Data(i.getDescription(), i.getBuyingPrice()));
            }
            BarChartItems.getData().add(chart2);



        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
