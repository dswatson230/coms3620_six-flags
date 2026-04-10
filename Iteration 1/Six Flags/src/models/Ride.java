package models;
 
public class Ride {
    private String rideID;
    private String name;
    private String status;
    private String location;
 
    public Ride(String rideID, String name, String status, String location) {
        this.rideID   = rideID;
        this.name     = name;
        this.status   = status;
        this.location = location;
    }
 
    public String getRideID()   { return rideID; }
    public String getName()     { return name; }
    public String getStatus()   { return status; }
    public String getLocation() { return location; }
    public void setStatus(String status) { this.status = status; }
 
    public String toFileString() {
        return rideID + "," + name + "," + status + "," + location;
    }
 
    public static Ride fromFileString(String line) {
        String[] parts = line.split(",");
        return new Ride(parts[0], parts[1], parts[2], parts[3]);
    }
}
 