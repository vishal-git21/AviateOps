// Aircraft.java
public class Aircraft {
    private int aircraftID;
    private String manufacturer;
    private Date dateOfManufacture;
    private BigDecimal distanceTravelled;
    private Date lastMaintenanceCheck;

    // Constructor
    public Aircraft(int aircraftID, String manufacturer, Date dateOfManufacture, BigDecimal distanceTravelled, Date lastMaintenanceCheck) {
        this.aircraftID = aircraftID;
        this.manufacturer = manufacturer;
        this.dateOfManufacture = dateOfManufacture;
        this.distanceTravelled = distanceTravelled;
        this.lastMaintenanceCheck = lastMaintenanceCheck;
    }

    // Getters and Setters
    public int getAircraftID() {
        return aircraftID;
    }

    public void setAircraftID(int aircraftID) {
        this.aircraftID = aircraftID;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public Date getDateOfManufacture() {
        return dateOfManufacture;
    }

    public void setDateOfManufacture(Date dateOfManufacture) {
        this.dateOfManufacture = dateOfManufacture;
    }

    public BigDecimal getDistanceTravelled() {
        return distanceTravelled;
    }

    public void setDistanceTravelled(BigDecimal distanceTravelled) {
        this.distanceTravelled = distanceTravelled;
    }

    public Date getLastMaintenanceCheck() {
        return lastMaintenanceCheck;
    }

    public void setLastMaintenanceCheck(Date lastMaintenanceCheck) {
        this.lastMaintenanceCheck = lastMaintenanceCheck;
    }
}
