package Controller.CRUD;

import Model.Vendor;
import Util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class VendorCRUDController {

    public static ArrayList<Vendor> getAllVendors() throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT * FROM Vendor");
        ArrayList<Vendor> vendorList = new ArrayList<>();
        while (result.next()) {
            vendorList.add(new Vendor(result.getString(1), result.getString(2), result.getString(3), result.getString(4), result.getString(5)));
        }
        return vendorList;
    }

    public static Vendor getVendor(String vendorID) throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT * FROM Vendor WHERE VendorID=?", vendorID);
        if (result.next()) {
            return new Vendor(result.getString(1), result.getString(2), result.getString(3), result.getString(4), result.getString(5));
        }
        return null;
    }

    public static boolean updateVendor(String vendorID, String name, String contact, String email, String extra_details) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("UPDATE Vendor SET name_or_comapny=?, contact=?,email=?,extra_Details=? WHERE VendorID=?", name, contact, email, extra_details, vendorID);
    }

    public static boolean deleteCustomer(String vendorID) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM Vendor WHERE VendorID =?", vendorID);
    }

    public static boolean addVendor(String VendorID, String name, String email, String conatact, String extra_details) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("INSERT INTO VENDOR VALUES(?,?,?,?,?)", VendorID, name, conatact, email, extra_details);
    }

    public static ArrayList<String> getAllVendorIDS() throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT VendorID FROM Vendor");
        ArrayList<String> VendorIdList = new ArrayList<>();
        while (result.next()) {
            VendorIdList.add(result.getString(1));
        }
        return VendorIdList;
    }

    public static ArrayList<String> getALlVendorMobiles() throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT contact FROM Vendor");
        ArrayList<String> VendorContactList = new ArrayList<>();
        while(result.next()){
            VendorContactList.add(result.getString(1));
        }
        return VendorContactList;
    }

    public static Vendor getVendorByMobile(String contact) throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT * FROM Vendor WHERE contact =?",contact);
        if (result.next()){
            return new Vendor(result.getString(1),result.getString(2),result.getString(3),result.getString(4),result.getString(5));
        }
        return null;
    }
}
