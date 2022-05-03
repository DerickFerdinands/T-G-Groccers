package Model;

import java.time.LocalDate;

public class Admin {
    private String nic;
    private String name;
    private LocalDate dob;
    private String email;
    private String mobile;
    private String address;
    private String user_name;
    private String password;
    private String image_path;

    public Admin() {
    }

    public Admin(String nic, String name, LocalDate dob, String email, String mobile, String address, String user_name, String password, String image_path) {
        this.nic = nic;
        this.name = name;
        this.dob = dob;
        this.email = email;
        this.mobile = mobile;
        this.address = address;
        this.user_name = user_name;
        this.password = password;
        this.image_path = image_path;
    }

    @Override
    public String toString() {
        return "Admin{" +
                "nic='" + nic + '\'' +
                ", name='" + name + '\'' +
                ", dob=" + dob +
                ", email='" + email + '\'' +
                ", mobile='" + mobile + '\'' +
                ", address='" + address + '\'' +
                ", user_name='" + user_name + '\'' +
                ", password='" + password + '\'' +
                ", image_path='" + image_path + '\'' +
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }
}
