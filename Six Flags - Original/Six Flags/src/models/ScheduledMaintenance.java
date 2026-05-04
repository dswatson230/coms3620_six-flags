package models;

import interfaces.types.MaintenanceType;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ScheduledMaintenance {
    private static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("MM/dd/yyy HH:mm");

    private String rideID;
    private MaintenanceType type;
    private LocalDateTime startTime;
    private double duration;
    private String reason;

    public ScheduledMaintenance(String rideID, MaintenanceType type, LocalDateTime startTime, double duration, String reason) {
        this.rideID = rideID;
        this.type = type;
        this.startTime = startTime;
        this.duration = duration;
        this.reason = reason;
    }

    public String getRideID()   { return rideID; }
    public MaintenanceType getMaintenanceType()     { return type; }
    public LocalDateTime getStartTime()   { return startTime; }
    public double getDuration() { return duration; }
    public String getReason() { return reason; }

    public String toFileString() {
        return rideID + "," + type + "," + startTime.format(FORMAT) + "," + duration + "," + reason;
    }

    public static ScheduledMaintenance fromFileString(String line) {
        String[] parts = line.split(",");
        return new ScheduledMaintenance(parts[0], MaintenanceType.valueOf(parts[1]), LocalDateTime.parse(parts[2], FORMAT), Double.parseDouble(parts[3]), parts[4]);
    }
}
