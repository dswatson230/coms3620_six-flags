package models;

public class Ride {
    private String rideID;
    private String name;
    private String status;
    private String location;
    private int    waitTime;

    public Ride(String rideID, String name, String status, String location) {
        this.rideID    = rideID;
        this.name      = name;
        this.status    = status;
        this.location  = location;
        this.waitTime  = 0;
    }

    public Ride(String rideID, String name, String status, String location, int waitTime) {
        this.rideID    = rideID;
        this.name      = name;
        this.status    = status;
        this.location  = location;
        this.waitTime  = waitTime;
    }

    public String getRideID()              { return rideID; }
    public String getName()                { return name; }
    public String getStatus()              { return status; }
    public String getLocation()            { return location; }
    public int    getWaitTime()            { return waitTime; }
    public void   setStatus(String status) { this.status = status; }
    public void   setWaitTime(int waitTime){ this.waitTime = waitTime; }

    public String toFileString() {
        return rideID + "," + name + "," + status + "," + location + "," + waitTime;
    }

    public static Ride fromFileString(String line) {
        String[] parts = line.split(",");
        if (parts.length == 5) {
            return new Ride(parts[0], parts[1], parts[2], parts[3],
                Integer.parseInt(parts[4].trim()));
        }
        // backwards compatible with old format (no waitTime)
        return new Ride(parts[0], parts[1], parts[2], parts[3]);
    }
}