package Controller.CRUD;

import Model.Driver;
import Util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class DriverCRUDController {
    public static ArrayList<Driver> getAllDrivers() throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT * FROM Driver");
        ArrayList<Driver> driverList = new ArrayList<>();

        while (result.next()) {
            driverList.add(new Driver(result.getString(1), result.getString(2), LocalDate.parse(result.getDate(3).toString()), result.getString(4), result.getString(5), result.getString(6), result.getBoolean(7)));
        }
        return driverList;
    }

    public static Driver getDriver(String nic) throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT * FROM Driver WHERE nic=?", nic);
        if (result.next()) {
            return new Driver(result.getString(1), result.getString(2), LocalDate.parse(result.getDate(3).toString()), result.getString(4), result.getString(5), result.getString(6), result.getBoolean(7));
        }
        return null;
    }

    public static boolean addDriver(String nic, String name, LocalDate dob, String license_number, String contact, String address, boolean availability) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("INSERT INTO Driver VALUES(?,?,?,?,?,?,?)",nic,name,dob,license_number,contact,address,availability);
    }

    public static boolean deleteDriver(String nic) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM Driver WHERE nic=?",nic);
    }

    public static boolean updateDriver(String nic, String name, LocalDate dob, String license_number, String contact, String address, boolean availability) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("UPDATE Driver SET name=?, dob=?, license_number=?, contact=?, address=?,availability=? WHERE nic=?",name,dob,license_number,contact,address,availability,nic);
    }

    public static ArrayList<Driver> getAllAvailableDrivers() throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT * FROM Driver WHERE availability =1");
        ArrayList<Driver> availableDrivers = new ArrayList<>();

        while(result.next()){
            availableDrivers.add(new Driver(result.getString(1), result.getString(2), LocalDate.parse(result.getDate(3).toString()), result.getString(4), result.getString(5), result.getString(6), result.getBoolean(7)));
        }
        return availableDrivers;
    }

    public static Driver getAvailableDriver() throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT * FROM Driver WHERE availability=1 LIMIT 1");
        if(result.next()){
            return  new Driver(result.getString(1), result.getString(2), LocalDate.parse(result.getDate(3).toString()), result.getString(4), result.getString(5), result.getString(6), result.getBoolean(7));
        }
        return null;
    }

    public static boolean updateDriverAvailability(String driverNIC, boolean Availability) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("UPDATE Driver SET availability=? WHERE nic=?",Availability,driverNIC);
    }


}
