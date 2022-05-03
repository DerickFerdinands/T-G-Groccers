package Controller.CRUD;

import Model.Employee;
import Util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class EmployeeCRUDController {

    public static ArrayList<Employee> getAllEployees() throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT * FROM Employee");
        ArrayList<Employee> empList = new ArrayList<>();
        while (result.next()) {
            empList.add(new Employee(result.getString(1), result.getString(2), LocalDate.parse(result.getDate(3).toString()), result.getString(4), result.getString(5), result.getString(6), result.getString(7)));

        }
        return empList;
    }

    public static Employee getEmployee(String nic) throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT * FROM Employee WHERE nic=?",nic);
        if(result.next()){
            return new Employee(result.getString(1), result.getString(2), LocalDate.parse(result.getDate(3).toString()), result.getString(4), result.getString(5), result.getString(6), result.getString(7));
        }
        return null;
    }

    public static boolean addEmployee(String nic, String name, LocalDate dob, String address, String mobile, String email, String imageLocation) throws SQLException, ClassNotFoundException {
            return CrudUtil.execute("INSERT INTO Employee VALUES (?,?,?,?,?,?,?)",nic,name,dob,address,mobile,email,imageLocation);
    }

    public static boolean deleteEmployee(String nic) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM Employee WHERE nic=?",nic);
    }

    public static boolean updateEmployee(String nic, String name, LocalDate dob, String address, String mobile, String email, String imageLocation) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("UPDATE Employee SET name=?, dob=?, address=?, mobile=?, email=?,profile_pic_location=? WHERE nic=?",name,dob,address,mobile,email,imageLocation,nic);
    }

    public static String getEmployeeImageLocation(String empNIc) throws SQLException, ClassNotFoundException {
        ResultSet result= CrudUtil.execute("SELECT * FROM Employee");
        while(result.next()) {
            if(result.getString(1).equals("200416803088")){
                System.out.println(result.getString(7));
                return result.getString(7);
            }
        }
        return null;
    }
}
