package Model;

import java.time.LocalDate;
import java.util.Date;

public class Customer {
    private String customerNIC;
    private String customerName;
    private String customerEmail;
    private String customerAddress;
    private LocalDate DOB;
    private String CustomerMobile;
    private int points;

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public Customer() {
    }

    public Customer(String customerNIC, String customerName, String customerEmail, String customerAddress, LocalDate DOB, String customerMobile, int points) {
        this.customerNIC = customerNIC;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.customerAddress = customerAddress;
        this.DOB = DOB;
        CustomerMobile = customerMobile;
        this.points = points;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "customerNIC='" + customerNIC + '\'' +
                ", customerName='" + customerName + '\'' +
                ", customerEmail='" + customerEmail + '\'' +
                ", customerAddress='" + customerAddress + '\'' +
                ", DOB=" + DOB +
                ", CustomerMobile='" + CustomerMobile + '\'' +
                ", points=" + points +
                '}';
    }

    public String getCustomerNIC() {
        return customerNIC;
    }

    public void setCustomerNIC(String customerNIC) {
        this.customerNIC = customerNIC;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public LocalDate getDOB() {
        return DOB;
    }

    public void setDOB(LocalDate DOB) {
        this.DOB = DOB;
    }

    public String getCustomerMobile() {
        return CustomerMobile;
    }

    public void setCustomerMobile(String customerMobile) {
        CustomerMobile = customerMobile;
    }
}
