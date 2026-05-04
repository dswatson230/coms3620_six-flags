package controllers;

import models.Ride;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class WaitTimeController {
    //private static final String RIDES_FILE = "data/rides.txt";
    private static final String RIDES_FILE = "C:/Users/longi/COMS/coms3620/six-flags/Six Flags/data/rides.txt";

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
            for (Ride r : rides) {
                bw.write(r.toFileString());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving rides: " + e.getMessage());
        }
    }

    // Returns null on success, error message on failure
    public String updateWaitTime(String rideID, int waitTime) {
        if (waitTime < 0) return "Wait time cannot be negative.";

        List<Ride> rides = loadRides();
        for (Ride r : rides) {
            if (r.getRideID().equals(rideID)) {
                if (!r.getStatus().equals("OPEN"))
                    return "Cannot update wait time — ride is not currently OPEN.";
                r.setWaitTime(waitTime);
                saveRides(rides);
                return null;
            }
        }
        return "Ride not found in records.";
    }
}