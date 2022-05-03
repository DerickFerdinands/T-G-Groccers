package Model;

public class Vendor {
    private String vendorID;
    private String NameOrCompany;
    private String contact;
    private String email;
    private String extra_details;

    @Override
    public String toString() {
        return "Vendor{" +
                "vendorID='" + vendorID + '\'' +
                ", NameOrCompany='" + NameOrCompany + '\'' +
                ", contact='" + contact + '\'' +
                ", email='" + email + '\'' +
                ", extra_details='" + extra_details + '\'' +
                '}';
    }

    public String getVendorID() {
        return vendorID;
    }

    public void setVendorID(String vendorID) {
        this.vendorID = vendorID;
    }

    public String getNameOrCompany() {
        return NameOrCompany;
    }

    public void setNameOrCompany(String nameOrCompany) {
        NameOrCompany = nameOrCompany;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getExtra_details() {
        return extra_details;
    }

    public void setExtra_details(String extra_details) {
        this.extra_details = extra_details;
    }

    public Vendor() {
    }

    public Vendor(String vendorID, String nameOrCompany, String contact, String email, String extra_details) {
        this.vendorID = vendorID;
        NameOrCompany = nameOrCompany;
        this.contact = contact;
        this.email = email;
        this.extra_details = extra_details;
    }
}
