package Controller.CRUD;

import Model.Customer;
import Util.CrudUtil;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class CustomerCRUDController {

    public static boolean saveCustomer(String nic, String name, String email, String address, LocalDate DOB, String mobile) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("INSERT INTO Customer VALUES(?,?,?,?,?,?,?)", nic, name, DOB, address, mobile, 0, email);
    }

    public static ArrayList<Customer> getAllCustomers() throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT * FROM Customer");
        ArrayList<Customer> customerList = new ArrayList<>();
        while (result.next()) {
            customerList.add(new Customer(result.getString(1), result.getString(2), result.getString(7), result.getString(4), LocalDate.parse(result.getDate(3).toString()), result.getString(5), result.getInt(6)));

        }
        return customerList;
    }

    public static Customer getCustomer(String NIC) throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT * FROM Customer WHERE nic=?", NIC);
        if (result.next()) {
            return new Customer(result.getString(1), result.getString(2), result.getString(7), result.getString(4), LocalDate.parse(result.getDate(3).toString()), result.getString(5), result.getInt(6));
        }
        return null;
    }

    public static Customer getCustomerByMobile(String Mobile) throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT * FROM Customer WHERE contact=?", Mobile);
        if (result.next()) {
            return new Customer(result.getString(1), result.getString(2), result.getString(7), result.getString(4), LocalDate.parse(result.getDate(3).toString()), result.getString(5), result.getInt(6));
        }
        return null;

    }

    public static Boolean deleteCustomer(String nic) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM Customer WHERE nic=?", nic);
    }

    public static boolean updateCustomer(String nic, String name, String email, String address, LocalDate dob, String mobile) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("UPDATE CUSTOMER SET name =?,email=?,address=?,dob=?,contact=? WHERE nic =?", name, email, address, "2022-03-10", mobile, nic);
    }

    public static ArrayList<String> getAllCustomerID() throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT nic FROM Customer");
        ArrayList<String> CustomernicList = new ArrayList<>();
        while (result.next()) {
            CustomernicList.add(result.getString(1));
        }
        return CustomernicList;
    }

    public static ArrayList<String> getAllCustomerMobileNumbers() throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT contact FROM Customer");
        ArrayList<String> customerContacts = new ArrayList<>();
        while (result.next()) {
            customerContacts.add(result.getString(1));
        }
        return customerContacts;
    }

    public static boolean updateCustomerPoints(String nic, int points) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("UPDATE Customer SET points=? WHERE NIC=?", points, nic);

    }

}
