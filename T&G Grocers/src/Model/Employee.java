package Model;

import java.time.LocalDate;

public class Employee {

    private String nic;
    private String name;
    private LocalDate dob;
    private String address;
    private String mobile;
    private String email;
    private String image_Location;

    public Employee() {
    }

    public Employee(String nic, String name, LocalDate dob, String address, String mobile, String email, String image_Location) {
        this.nic = nic;
        this.name = name;
        this.dob = dob;
        this.address = address;
        this.mobile = mobile;
        this.email = email;
        this.image_Location = image_Location;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "nic='" + nic + '\'' +
                ", name='" + name + '\'' +
                ", dob=" + dob +
                ", address='" + address + '\'' +
                ", mobile='" + mobile + '\'' +
                ", email='" + email + '\'' +
                ", image_Location='" + image_Location + '\'' +
                '}';
    }

    public String getNic() {
        return nic;
    }

    public void setNic(String nic) {
        this.nic = nic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage_Location() {
        return image_Location;
    }

    public void setImage_Location(String image_Location) {
        this.image_Location = image_Location;
    }
}
