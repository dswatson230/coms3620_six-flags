package controllers;

import interfaces.types.Location;
import models.Ride;
import models.ScheduledMaintenance;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RideController {
    private final RideStatusController statusController;
    private final RideMaintenanceController maintenanceController;

    public RideController() {
        this.statusController = new RideStatusController();
        this.maintenanceController = new RideMaintenanceController();
    }

    //-----------------STATUS-----------------//

    public List<Ride> loadRides() {
        applyMaintenanceStatus(loadMaintenanceSchedule());
        return statusController.loadRides();
    }

    public List<Ride> loadRidesFromLocation(Location location) {
        List<Ride> allRides = loadRides();
        List<Ride> locationRides = new ArrayList<>();

        for (Ride ride: allRides) {
            if (ride.getLocation().equalsIgnoreCase(location.name().replace("_", " ")))
                locationRides.add(ride);
        }

        return locationRides;
    }

    public String updateRideStatus(String rideID, String newStatus, String reason) { return statusController.updateRideStatus(rideID, newStatus, reason); }

    //-----------------MAINTENANCE-----------------//

    public List<ScheduledMaintenance> loadMaintenanceSchedule() { return maintenanceController.loadMaintenanceSchedule(); }

    public void saveScheduledMaintenance(List<ScheduledMaintenance> maintenance) {
        maintenanceController.saveScheduledMaintenance(maintenance);
        applyMaintenanceStatus(maintenance);
    }

    public void applyMaintenanceStatus(List<ScheduledMaintenance> schedule) {
        List<Ride> rides = statusController.loadRides();
        LocalDateTime now = LocalDateTime.now();

        for (Ride ride : rides) {
            boolean inMaintenance = schedule.stream().anyMatch(m -> {
                if (!m.getRideID().equals(ride.getRideID())) return false;

                LocalDateTime start = m.getStartTime();
                LocalDateTime end = start.plusMinutes((long)(m.getDuration() * 60));

                return now.isAfter(start) && now.isBefore(end);
            });

            if (inMaintenance && !ride.getStatus().equals("MAINTENANCE")) {
                updateRideStatus(ride.getRideID(), "MAINTENANCE", "Scheduled Maintenance");
            }

            if (!inMaintenance && ride.getStatus().equals("MAINTENANCE")) {
                updateRideStatus(ride.getRideID(), "OPEN", "Scheduled Maintenance Ended");
            }
        }
    }

    public List<ScheduledMaintenance> normalize(List<ScheduledMaintenance> maintenance) { return maintenanceController.normalize(maintenance); }
    public List<ScheduledMaintenance> findOverlaps(ScheduledMaintenance request, List<ScheduledMaintenance> all) { return maintenanceController.findOverlaps(request, all); }
    public boolean overlaps(ScheduledMaintenance a, ScheduledMaintenance b) { return maintenanceController.overlaps(a,b); }
}
