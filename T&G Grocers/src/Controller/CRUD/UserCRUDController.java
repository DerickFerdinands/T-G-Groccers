package Controller.CRUD;

import Util.CrudUtil;
import View.TM.UserTM;
import javafx.collections.FXCollections;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UserCRUDController {

    public static boolean addJobTitle(String JobTitle) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("INSERT INTO Job_title_Type VALUES (?)", JobTitle);
    }

    public static boolean addDesignationType(String DesignationType) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("INSERT INTO designation_type VALUES (?)", DesignationType);
    }

    public static ArrayList<String> getAllEmployeeNICS() throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT NIC FROM Employee");
        ArrayList<String> nicList = new ArrayList<>();
        while (result.next()) {
            nicList.add(result.getString(1));
        }
        return nicList;
    }

    public static ArrayList<String> getAllJobTitles() throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT * FROM Job_Title_Type");
        ArrayList<String> titleList = new ArrayList<>();
        while (result.next()) {
            titleList.add(result.getString(1));
        }
        return titleList;
    }

    public static ArrayList<String> getAllJobDesignationTypes() throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT * FROM Designation_Type");
        ArrayList<String> titleList = new ArrayList<>();
        while (result.next()) {
            titleList.add(result.getString(1));
        }
        return titleList;
    }

    public static boolean addUser(String userID, String nic, String userName, String password, String JobTitle, String Designation, String Status) throws SQLException, ClassNotFoundException {
        boolean user = CrudUtil.execute("INSERT INTO user_accounts VALUES (?,?,?,?)", userID, nic, userName, password);
        boolean designation = CrudUtil.execute("INSERT INTO Designation VALUES (?,?,?,?)", userID, nic, Designation, 1);
        boolean jobTitle = CrudUtil.execute("INSERT INTO Job_Title VALUES (?,?,?,?)", userID, nic, JobTitle, 1);

        return user && designation && jobTitle;
    }

    public static UserTM getUser(String userID) throws SQLException, ClassNotFoundException {
        ResultSet r1 = CrudUtil.execute("SELECT * FROM user_accounts WHERE userId =?", userID);
        if (r1.next()) {
            ResultSet r2 = CrudUtil.execute("SELECT * FROM Employee WHERE nic=?", r1.getString(2));
            if (r2.next()) {
                ResultSet r3 = CrudUtil.execute("SELECT * FROM Job_Title WHERE id=?", userID);
                if (r3.next()) {
                    ResultSet r4 = CrudUtil.execute("SELECT * FROM designation WHERE id=?", userID);
                    if (r4.next()) {
                        return new UserTM(r1.getString(2), r2.getString(2), r2.getString(5), r2.getString(6), r4.getString(3), r3.getString(3), r1.getString(3), r1.getNString(4));
                    }
                }
            }
        }
        return null;
    }

    public static ArrayList<UserTM> getAllUserDetails() throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT * FROM USer_Accounts");
        ArrayList<UserTM> tnList = new ArrayList<>();
        while (result.next()) {

            ResultSet r1 = CrudUtil.execute("SELECT * FROM user_accounts WHERE userId =?", result.getString(1));
            if (r1.next()) {
                ResultSet r2 = CrudUtil.execute("SELECT * FROM Employee WHERE nic=?", r1.getString(2));
                if (r2.next()) {
                    ResultSet r3 = CrudUtil.execute("SELECT * FROM Job_Title WHERE id=?", result.getString(1));
                    if (r3.next()) {
                        ResultSet r4 = CrudUtil.execute("SELECT * FROM designation WHERE id=?", result.getString(1));
                        if (r4.next()) {
                            tnList.add(new UserTM(r1.getString(2), r2.getString(2), r2.getString(5), r2.getString(6), r4.getString(3), r3.getString(3), r1.getString(3), r1.getNString(4)));
                        }
                    }
                }
            }
        }
        return tnList;
    }

    public static boolean deleteUser(String userID) throws SQLException, ClassNotFoundException {
        boolean user = CrudUtil.execute("DELETE FROM User_Accounts WHERE userId=?", userID);
        boolean designation = CrudUtil.execute("DELETE FROM designation WHERE id=?", userID);
        boolean title = CrudUtil.execute("DELETE FROM Job_Title WHERE id=?", userID);
        return user && designation && title;
    }

    public static boolean updateUser(String userID, String nic, String userName, String password, String JobTitle, String Designation, boolean Status) throws SQLException, ClassNotFoundException {
        boolean user = CrudUtil.execute("UPDATE user_accounts SET user_name=?, password=? WHERE userID =?", userName,password,userID);
        boolean designation = CrudUtil.execute("UPDATE Designation SET type=?, status=? WHERE id=?", Designation, Status, userID);
        boolean jobTitle = CrudUtil.execute("UPDATE Job_Title SET type=?, status=? WHERE id=?", JobTitle, Status, userID);

        if (user && designation&&jobTitle){
            return true;
        }else{
            return false;
        }
    }

    public static String getUserid(String nic) throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT * FROM user_accounts WHERE nic=?",nic);
        if(result.next()){
            return result.getString(1);
        }
        return null;
    }
}
