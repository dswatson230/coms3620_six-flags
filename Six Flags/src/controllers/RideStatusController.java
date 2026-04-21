package controllers;
 
import models.Ride;
 
import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
 
public class RideStatusController {
    private static final String RIDES_FILE = "C:/Users/longi/COMS/coms3620/six-flags/Iteration 1/Six Flags/data/rides.txt";
    private static final String LOG_FILE   = "C:/Users/longi/COMS/coms3620/six-flags/Iteration 1/Six Flags/data/ride_status_log.txt";
 
    private static final String[] VALID_STATUSES = {"OPEN", "CLOSED", "MAINTENANCE"};
 
    public List<Ride> loadRides() {
        List<Ride> rides = new ArrayList<>();
        File file = new File(RIDES_FILE);
        if (!file.exists()) return rides;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) rides.add(Ride.fromFileString(line));
            }
        } catch (IOException e) {
            System.out.println("Error loading rides: " + e.getMessage());
        }
        return rides;
    }
 
    public void saveRides(List<Ride> rides) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(RIDES_FILE))) {
            for (Ride r : rides) { bw.write(r.toFileString()); bw.newLine(); }
        } catch (IOException e) {
            System.out.println("Error saving rides: " + e.getMessage());
        }
    }
 
    public void logChange(String rideID, String oldStatus, String newStatus, String reason) {
        try (FileWriter fw = new FileWriter(LOG_FILE, true)) {
            fw.write(rideID + "," + oldStatus + "," + newStatus + "," + reason + "," + LocalDateTime.now() + "\n");
        } catch (IOException e) {
            System.out.println("Error writing log: " + e.getMessage());
        }
    }
 
    // Returns null on success, error message on failure
    public String updateRideStatus(String rideID, String newStatus, String reason) {
        if (newStatus == null || newStatus.isBlank()) return "Status cannot be empty.";
        if (reason == null || reason.isBlank())       return "Reason cannot be empty.";
 
        List<Ride> rides = loadRides();
        for (Ride r : rides) {
            if (r.getRideID().equals(rideID)) {
                if (r.getStatus().equals(newStatus))
                    return "Status is already set to " + newStatus + ". No changes made.";
                String oldStatus = r.getStatus();
                r.setStatus(newStatus);
                saveRides(rides);
                logChange(rideID, oldStatus, newStatus, reason);
                return null; // success
            }
        }
        return "Ride not found in records.";
    }
}