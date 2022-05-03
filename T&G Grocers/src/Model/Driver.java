package Model;

import java.time.LocalDate;

public class Driver {
    private String nic;
    private String name;
    private LocalDate dob;
    private String license_number;
    private String Contact;
    private String address;
    private boolean availability;

    public Driver() {
    }

    public Driver(String nic, String name, LocalDate dob, String license_number, String contact, String address, boolean availability) {
        this.nic = nic;
        this.name = name;
        this.dob = dob;
        this.license_number = license_number;
        Contact = contact;
        this.address = address;
        this.availability = availability;
    }

    @Override
    public String toString() {
        return "Driver{" +
                "nic='" + nic + '\'' +
                ", name='" + name + '\'' +
                ", dob=" + dob +
                ", license_number='" + license_number + '\'' +
                ", Contact='" + Contact + '\'' +
                ", address='" + address + '\'' +
                ", availability=" + availability +
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

    public String getLicense_number() {
        return license_number;
    }

    public void setLicense_number(String license_number) {
        this.license_number = license_number;
    }

    public String getContact() {
        return Contact;
    }

    public void setContact(String contact) {
        Contact = contact;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isAvailability() {
        return availability;
    }

    public void setAvailability(boolean availability) {
        this.availability = availability;
    }
}
