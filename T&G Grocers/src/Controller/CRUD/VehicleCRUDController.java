package Controller.CRUD;

import Model.Vehicle;
import Util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class VehicleCRUDController {

    public static ArrayList<Vehicle> getAllVehicles() throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT * FROM Vehicle");
        ArrayList<Vehicle> vehicleList = new ArrayList<>();
        while (result.next()) {
            vehicleList.add(new Vehicle(result.getString(1), result.getString(2), result.getDouble(3), result.getBoolean(4)));
        }
        return vehicleList;
    }

    public static Vehicle getVehicle(String vehicleNumber) throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT * FROM Vehicle WHERE vehicleNumber=?", vehicleNumber);
        if (result.next()) {
            return new Vehicle(result.getString(1), result.getString(2), result.getDouble(3), result.getBoolean(4));
        }
        return null;
    }

    public static boolean addVehicle(String vehicleNumber, String vehicleType, double storageCapacity, boolean availability) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("INSERT INTO Vehicle VALUES(?,?,?,?)", vehicleNumber, vehicleType, storageCapacity, availability);
    }

    public static boolean deleteVehicle(String VehicleNumber) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM Vehicle WHERE vehicleNumber =?", VehicleNumber);
    }

    public static boolean updateVehicle(String vehicleNumber, String vehicleType, double storageCapacity, boolean availability) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("UPDATE Vehicle SET vehicleType=?, storageCapacity=?,availability=? WHERE vehicleNumber=?", vehicleType, storageCapacity, availability, vehicleNumber);
    }

    public static ArrayList<String> getAllVehicleTypes() throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT * FROM Vehicle_Type");
        ArrayList<String> VehicleTypeList = new ArrayList<>();

        while (result.next()) {
            VehicleTypeList.add(result.getString(1));
        }
        return VehicleTypeList;
    }

    public static boolean addVehicleType(String vehicleType) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("INSERT INTO Vehicle_Type VALUES(?)", vehicleType);
    }

    public static ArrayList<Vehicle> getAllAvailableVehicles() throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT * FROM Vehicle WHERE availability=1");
        ArrayList<Vehicle> availableVehicles = new ArrayList<>();

        while (result.next()) {
            availableVehicles.add(new Vehicle(result.getString(1), result.getString(2), result.getDouble(3), result.getBoolean(4)));
        }

        return availableVehicles;
    }

    public static Vehicle getAvailableVehicle() throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT * FROM Vehicle WHERE availability=1 LIMIT 1");
        if(result.next()){
            return new Vehicle(result.getString(1), result.getString(2), result.getDouble(3), result.getBoolean(4));
        }
        return null;
    }

    public static boolean updateVehicleAvailability(String vehicleNumber, boolean availability) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("UPDATE Vehicle SET availability=? WHERE vehicleNumber=?",availability,vehicleNumber);
    }
}
