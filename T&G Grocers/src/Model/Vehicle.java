package Model;

public class Vehicle {
    private String vehicleNumber;
    private String VehicleType;
    private double storageCapacity;
    private boolean availability;

    public Vehicle() {
    }

    public Vehicle(String vehicleNumber, String vehicleType, double storageCapacity, boolean availability) {
        this.vehicleNumber = vehicleNumber;
        VehicleType = vehicleType;
        this.storageCapacity = storageCapacity;
        this.availability = availability;
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "vehicleNumber='" + vehicleNumber + '\'' +
                ", VehicleType='" + VehicleType + '\'' +
                ", storageCapacity=" + storageCapacity +
                ", availability=" + availability +
                '}';
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public String getVehicleType() {
        return VehicleType;
    }

    public void setVehicleType(String vehicleType) {
        VehicleType = vehicleType;
    }

    public double getStorageCapacity() {
        return storageCapacity;
    }

    public void setStorageCapacity(double storageCapacity) {
        this.storageCapacity = storageCapacity;
    }

    public boolean isAvailability() {
        return availability;
    }

    public void setAvailability(boolean availability) {
        this.availability = availability;
    }
}
