package View.TM;

public class UserTM {
    private String nic;
    private String name;
    private String mobile;
    private String email;
    private String designation;
    private String job_title;
    private String UserName;
    private String password;

    public UserTM() {
    }

    public UserTM(String nic, String name, String mobile, String email, String designation, String job_title, String userName, String password) {
        this.nic = nic;
        this.name = name;
        this.mobile = mobile;
        this.email = email;
        this.designation = designation;
        this.job_title = job_title;
        UserName = userName;
        this.password = password;
    }

    @Override
    public String toString() {
        return "UserTM{" +
                "nic='" + nic + '\'' +
                ", name='" + name + '\'' +
                ", mobile='" + mobile + '\'' +
                ", email='" + email + '\'' +
                ", designation='" + designation + '\'' +
                ", job_title='" + job_title + '\'' +
                ", UserName='" + UserName + '\'' +
                ", password='" + password + '\'' +
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

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getJob_title() {
        return job_title;
    }

    public void setJob_title(String job_title) {
        this.job_title = job_title;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
