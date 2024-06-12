// FlightSchedule.java
public class FlightSchedule {
    private int scheduleID;
    private Date departureTime;
    private Date arrivalTime;
    private String status;
    private int aircraftID;
    private int pilotID;
    private int coPilotID;
    private int staffmember1;
    private int staffmember2;
    private int staffmember3;
    private String departure;
    private String arrival;

    // Constructor
    public FlightSchedule(int scheduleID, Date departureTime, Date arrivalTime, String status, int aircraftID, int pilotID, int coPilotID, int staffmember1, int staffmember2, int staffmember3, String departure, String arrival) {
        this.scheduleID = scheduleID;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.status = status;
        this.aircraftID = aircraftID;
        this.pilotID = pilotID;
        this.coPilotID = coPilotID;
        this.staffmember1 = staffmember1;
        this.staffmember2 = staffmember2;
        this.staffmember3 = staffmember3;
        this.departure = departure;
        this.arrival = arrival;
    }

    // Getters and Setters
    public int getScheduleID() {
        return scheduleID;
    }

    public void setScheduleID(int scheduleID) {
        this.scheduleID = scheduleID;
    }

    public Date getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(Date departureTime) {
        this.departureTime = departureTime;
    }

    public Date getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(Date arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getAircraftID() {
        return aircraftID;
    }

    public void setAircraftID(int aircraftID) {
        this.aircraftID = aircraftID;
    }

    public int getPilotID() {
        return pilotID;
    }

    public void setPilotID(int pilotID) {
        this.pilotID = pilotID;
    }

    public int getCoPilotID() {
        return coPilotID;
    }

    public void setCoPilotID(int coPilotID) {
        this.coPilotID = coPilotID;
    }

    public int getStaffmember1() {
        return staffmember1;
    }

    public void setStaffmember1(int staffmember1) {
        this.staffmember1 = staffmember1;
    }

    public int getStaffmember2() {
        return staffmember2;
    }

    public void setStaffmember2(int staffmember2) {
        this.staffmember2 = staffmember2;
    }

    public int getStaffmember3() {
        return staffmember3;
    }

    public void setStaffmember3(int staffmember3) {
        this.staffmember3 = staffmember3;
    }

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public String getArrival() {
        return arrival;
    }

    public void setArrival(String arrival) {
        this.arrival = arrival;
    }
}
