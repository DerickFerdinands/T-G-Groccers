package Controller.CRUD;

import Model.Admin;
import Util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class AdminCRUDController {

    public static ArrayList<Admin> getALlAdmins() throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT * FROM Admin");
        ArrayList<Admin> adminList = new ArrayList<>();

        while (result.next()) {
            adminList.add(new Admin(result.getString(1), result.getString(2), LocalDate.parse(result.getDate(3).toString()), result.getString(4), result.getString(5), result.getString(6), result.getString(7), result.getString(8), result.getString(9)));
        }
        return adminList;
    }

    public static Admin getAdmin(String nic) throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT * FROM Admin WHERE nic =?", nic);
        if (result.next()) {
            return new Admin(result.getString(1), result.getString(2), LocalDate.parse(result.getDate(3).toString()), result.getString(4), result.getString(5), result.getString(6), result.getString(7), result.getString(8), result.getString(9));
        }
        return null;
    }

    public static boolean addAdmin(String nic, String name, LocalDate dob, String email, String mobile, String address, String userName, String password, String imageLocation) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("INSERT INTO Admin VALUES (?,?,?,?,?,?,?,?,?)",nic, name,dob,email,mobile,address,userName,password,imageLocation);
    }

    public static boolean deleteAdmin(String nic) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM Admin WHERE nic=?",nic);
    }

    public static boolean updateAdmin(String nic, String name, LocalDate dob, String email, String mobile, String address, String userName, String password, String imageLocation) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("UPDATE ADMIN SET name=?, dob=?, email=?, mobile=?,address=?,user_name=?,password=?,profile_pic_location=? WHERE nic=? ",name,dob, email,mobile,address,userName,password,imageLocation,nic);
    }
}
