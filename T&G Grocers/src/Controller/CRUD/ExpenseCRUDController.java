package Controller.CRUD;

import Model.Expense;
import Util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class ExpenseCRUDController {

    public static ArrayList<Expense> getAllExpenses() throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT * FROM Expense");
        ArrayList<Expense> expList = new ArrayList<>();

        while (result.next()) {
            expList.add(new Expense(result.getString(1), result.getString(2), result.getString(3), result.getDouble(4), LocalDate.parse(result.getDate(5).toString()), LocalTime.parse(result.getTime(6).toString())));
        }
        return expList;
    }

    public static Expense getExpense(String ExpenseID) throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT * FROM Expense WHERE expenseID=?",ExpenseID);

        if(result.next()){
            return  new Expense(result.getString(1), result.getString(2), result.getString(3), result.getDouble(4), LocalDate.parse(result.getDate(5).toString()), LocalTime.parse(result.getTime(6).toString()));
        }
        return null;
    }

    public static boolean addExpense(String expenseID, String expenseType, String description, double amount, LocalDate date, LocalTime time) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("INSERT INTO Expense VALUES (?,?,?,?,?,?)",expenseID,expenseType,description,amount,date,time);
    }

    public static boolean deleteExpense(String ExpenseID) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM Expense WHERE expenseID=?",ExpenseID);
    }

    public static boolean updateExpense(String expenseID, String expenseType, String description, double amount, LocalDate date, LocalTime time) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("UPDATE Expense SET expense_type=?,description=?,amount=?,`Date`=?,`Time`=? WHERE expenseID=?",expenseType,description,amount,date,time,expenseID);
    }

    public static boolean addExpenseType(String ExpenseType) throws SQLException, ClassNotFoundException {
        return  CrudUtil.execute("INSERT INTO expense_type VALUES(?)",ExpenseType);
    }

    public static ArrayList<String> getAllExpenseTypes() throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT * FROM expense_type");
        ArrayList<String> expenseTypes = new ArrayList<>();

        while(result.next()){
            expenseTypes.add(result.getString(1));
        }
        return expenseTypes;
    }

    public static ArrayList<Expense> getCategoryExpenses(String category) throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT * FROM Expense WHERE expense_type=? ",category);
        ArrayList<Expense> expList = new ArrayList<>();
        while(result.next()){
            expList.add(new Expense(result.getString(1), result.getString(2), result.getString(3), result.getDouble(4), LocalDate.parse(result.getDate(5).toString()), LocalTime.parse(result.getTime(6).toString())));
        }
        return expList;
    }
}

