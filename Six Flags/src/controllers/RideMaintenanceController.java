package controllers;

import interfaces.MaintenanceType;
import interfaces.Location;
import models.Ride;
import models.ScheduledMaintenance;

import java.io.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RideMaintenanceController {
    private static final String MAINTENANCE_FILE = "data/rides_maintenance.txt";
    private static final String RIDES_FILE       = "data/rides.txt";

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

    public List<Ride> loadRidesFromLocation(Location location) {
        return loadRides().stream()
            .filter(r -> r.getLocation().equalsIgnoreCase(location.name().replace("_", " "))
                      || r.getLocation().equalsIgnoreCase(location.name()))
            .collect(Collectors.toList());
    }

    public List<ScheduledMaintenance> loadMaintenanceSchedule() {
        List<ScheduledMaintenance> maintenance = new ArrayList<>();
        File file = new File(MAINTENANCE_FILE);
        if (!file.exists()) return maintenance;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) maintenance.add(ScheduledMaintenance.fromFileString(line));
            }
        } catch (IOException e) {
            System.out.println("Error loading maintenance schedule: " + e.getMessage());
        }
        return maintenance;
    }

    public void saveScheduledMaintenance(List<ScheduledMaintenance> maintenance) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(MAINTENANCE_FILE))) {
            for (ScheduledMaintenance m : maintenance) { bw.write(m.toFileString()); bw.newLine(); }
        } catch (IOException e) {
            System.out.println("Error saving maintenance: " + e.getMessage());
        }
    }

    public List<ScheduledMaintenance> normalize(List<ScheduledMaintenance> list) {
        List<ScheduledMaintenance> result = new ArrayList<>();
        for (ScheduledMaintenance incoming : list) {
            boolean merged = false;
            for (int i = 0; i < result.size(); i++) {
                ScheduledMaintenance existing = result.get(i);
                if (!existing.getRideID().equals(incoming.getRideID())) continue;
                if (!overlaps(existing, incoming)) continue;
                if (typeEquals(existing, incoming)) {
                    result.set(i, merge(existing, incoming));
                    merged = true;
                    break;
                }
            }
            if (!merged) result.add(incoming);
        }
        return result;
    }

    public List<ScheduledMaintenance> findOverlaps(ScheduledMaintenance request, List<ScheduledMaintenance> all) {
        List<ScheduledMaintenance> overlaps = new ArrayList<>();
        for (ScheduledMaintenance m : all) {
            if (!m.getRideID().equals(request.getRideID())) continue;
            if (overlaps(request, m)) overlaps.add(m);
        }
        return overlaps;
    }

    public boolean overlaps(ScheduledMaintenance a, ScheduledMaintenance b) {
        LocalTime aStart = a.getStartTime().toLocalTime();
        LocalTime aEnd   = aStart.plusMinutes((long)(a.getDuration() * 60));
        LocalTime bStart = b.getStartTime().toLocalTime();
        LocalTime bEnd   = bStart.plusMinutes((long)(b.getDuration() * 60));
        return aStart.isBefore(bEnd) && bStart.isBefore(aEnd);
    }

    public boolean typeEquals(ScheduledMaintenance a, ScheduledMaintenance b) {
        return a.getMaintenanceType() == b.getMaintenanceType();
    }

    public ScheduledMaintenance merge(ScheduledMaintenance a, ScheduledMaintenance b) {
        LocalDateTime start = a.getStartTime().isBefore(b.getStartTime()) ? a.getStartTime() : b.getStartTime();
        LocalDateTime endA  = a.getStartTime().plusMinutes((long)(a.getDuration() * 60));
        LocalDateTime endB  = b.getStartTime().plusMinutes((long)(b.getDuration() * 60));
        LocalDateTime end   = endA.isAfter(endB) ? endA : endB;
        double duration = java.time.Duration.between(start, end).toMinutes() / 60.0;
        return new ScheduledMaintenance(a.getRideID(), a.getMaintenanceType(), start, duration,
            a.getReason() + " | " + b.getReason());
    }
}