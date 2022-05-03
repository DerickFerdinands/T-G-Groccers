package Model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Expense {
    private String expenseID;
    private String expenseType;
    private String description;
    private Double amount;
    private LocalDate date;
    private LocalTime time;

    public Expense() {
    }

    public Expense(String expenseID, String expenseType, String description, Double amount, LocalDate date, LocalTime time) {
        this.expenseID = expenseID;
        this.expenseType = expenseType;
        this.description = description;
        this.amount = amount;
        this.date = date;
        this.time = time;
    }

    @Override
    public String toString() {
        return "Expense{" +
                "expenseID='" + expenseID + '\'' +
                ", expenseType='" + expenseType + '\'' +
                ", description='" + description + '\'' +
                ", amount=" + amount +
                ", date=" + date +
                ", time=" + time +
                '}';
    }

    public String getExpenseID() {
        return expenseID;
    }

    public void setExpenseID(String expenseID) {
        this.expenseID = expenseID;
    }

    public String getExpenseType() {
        return expenseType;
    }

    public void setExpenseType(String expenseType) {
        this.expenseType = expenseType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }
}
