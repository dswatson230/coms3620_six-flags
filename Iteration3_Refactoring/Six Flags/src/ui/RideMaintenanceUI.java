package ui;

import controllers.RideController;
import interfaces.types.Location;
import interfaces.types.MaintenanceType;
import models.Ride;
import models.ScheduledMaintenance;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class RideMaintenanceUI {
    private static final String ANSI_BOLD   = "\u001B[1m";
    private static final String ANSI_RESET  = "\u001B[0m";
    private static final String ANSI_RED    = "\u001B[31m";
    private static final String ANSI_GREEN  = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";

    private final RideController controller;
    private final UserInterface             router;
    private final Scanner                   scanner;
    private final DateTimeFormatter         dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    private final DateTimeFormatter         timeFormatter = DateTimeFormatter.ofPattern("H:mm");

    public RideMaintenanceUI(UserInterface router, Scanner scanner, RideController controller) {
        this.router     = router;
        this.scanner    = scanner;
        this.controller = controller;
    }

    public void showMaintenanceMenu() {
        boolean running = true;
        while (running) {
            router.clearScreen();
            router.printBanner();
            System.out.println(ANSI_BOLD + "  Ride Maintenance" + ANSI_RESET);
            System.out.println("  " + "-".repeat(34));
            System.out.println("  1. Schedule Maintenance");
            System.out.println("  2. View Maintenance Schedule");
            System.out.println("  3. Back");
            System.out.println("  " + "-".repeat(34));
            System.out.print("  Select an option: ");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1": showLocationSelection(); break;
                case "2": showScheduleMenu();      break;
                case "3": running = false;         break;
                default:
                    System.out.println(ANSI_RED + "  Invalid option." + ANSI_RESET);
                    router.pause();
            }
        }
    }

    // Step 1: select location
    private void showLocationSelection() {
        Location[] locations = Location.values();
        boolean running = true;
        while (running) {
            router.clearScreen();
            router.printBanner();
            System.out.println(ANSI_BOLD + "  Select Location" + ANSI_RESET);
            System.out.println("  " + "-".repeat(34));
            for (int i = 0; i < locations.length; i++) {
                System.out.println("  " + (i + 1) + ". " + locations[i].name().replace("_", " "));
            }
            System.out.println("  " + (locations.length + 1) + ". Back");
            System.out.println("  " + "-".repeat(34));
            System.out.print("  Select a location: ");

            String choice = scanner.nextLine().trim();
            int selection;
            try {
                selection = Integer.parseInt(choice);
            } catch (NumberFormatException e) {
                System.out.println(ANSI_RED + "  Invalid input." + ANSI_RESET);
                router.pause();
                continue;
            }

            if (selection == locations.length + 1) {
                running = false;
            } else if (selection >= 1 && selection <= locations.length) {
                showRideSelection(locations[selection - 1]);
            } else {
                System.out.println(ANSI_RED + "  Invalid selection." + ANSI_RESET);
                router.pause();
            }
        }
    }

    // Step 2: select ride
    private void showRideSelection(Location location) {
        boolean running = true;
        while (running) {
            router.clearScreen();
            router.printBanner();
            List<Ride> rides = controller.loadRidesFromLocation(location);

            System.out.println(ANSI_BOLD + "  Rides at " + location.name().replace("_", " ") + ANSI_RESET);
            System.out.println("  " + "-".repeat(40));

            if (rides.isEmpty()) {
                System.out.println("  No rides found at this location.");
                router.pause();
                return;
            }

            for (int i = 0; i < rides.size(); i++) {
                System.out.println("  " + (i + 1) + ". " + rides.get(i).getName());
            }
            System.out.println("  " + (rides.size() + 1) + ". Back");
            System.out.println("  " + "-".repeat(40));
            System.out.print("  Select a ride: ");

            String choice = scanner.nextLine().trim();
            int selection;
            try {
                selection = Integer.parseInt(choice);
            } catch (NumberFormatException e) {
                System.out.println(ANSI_RED + "  Invalid input." + ANSI_RESET);
                router.pause();
                continue;
            }

            if (selection == rides.size() + 1) {
                running = false;
            } else if (selection >= 1 && selection <= rides.size()) {
                showMaintenanceTypeSelection(rides.get(selection - 1));
            } else {
                System.out.println(ANSI_RED + "  Invalid selection." + ANSI_RESET);
                router.pause();
            }
        }
    }

    // Step 3: select maintenance type
    private void showMaintenanceTypeSelection(Ride ride) {
        MaintenanceType[] types = MaintenanceType.values();
        boolean running = true;
        while (running) {
            router.clearScreen();
            router.printBanner();
            System.out.println(ANSI_BOLD + "  Maintenance Type for: " + ride.getName() + ANSI_RESET);
            System.out.println("  " + "-".repeat(34));
            for (int i = 0; i < types.length; i++) {
                System.out.println("  " + (i + 1) + ". " + types[i].name());
            }
            System.out.println("  " + (types.length + 1) + ". Back");
            System.out.println("  " + "-".repeat(34));
            System.out.print("  Select type: ");

            String choice = scanner.nextLine().trim();
            int selection;
            try {
                selection = Integer.parseInt(choice);
            } catch (NumberFormatException e) {
                System.out.println(ANSI_RED + "  Invalid input." + ANSI_RESET);
                router.pause();
                continue;
            }

            if (selection == types.length + 1) {
                running = false;
            } else if (selection >= 1 && selection <= types.length) {
                showDateTimeAndDetails(ride, types[selection - 1]);
            } else {
                System.out.println(ANSI_RED + "  Invalid selection." + ANSI_RESET);
                router.pause();
            }
        }
    }

    // Step 4: select date/time and enter details
    private void showDateTimeAndDetails(Ride ride, MaintenanceType type) {
        router.clearScreen();
        router.printBanner();
        System.out.println(ANSI_BOLD + "  Schedule Details" + ANSI_RESET);
        System.out.println("  " + "-".repeat(34));
        System.out.println("  Enter C at any prompt to cancel.");
        System.out.println("  " + "-".repeat(34));

        // Date input
        LocalDate date = null;
        while (date == null) {
            System.out.print("  Start Date (MM/dd/yyyy): ");
            String input = scanner.nextLine().trim();
            if (isCancel(input)) return;
            try {
                date = LocalDate.parse(input, dateFormatter);
                if (date.isBefore(LocalDate.now())) {
                    date = null;
                    System.out.println(ANSI_RED + "  Date has already passed. Please try again." + ANSI_RESET);
                }
            } catch (DateTimeParseException e) {
                System.out.println(ANSI_RED + "  Invalid date format. Please try again." + ANSI_RESET);
            }
        }

        // Time input
        LocalTime time = null;
        while (time == null) {
            System.out.print("  Start Time (H:mm): ");
            String input = scanner.nextLine().trim();
            if (isCancel(input)) return;
            try {
                time = LocalTime.parse(input, timeFormatter);
                if (date.isEqual(LocalDate.now()) && time.isBefore(LocalTime.now())) {
                    time = null;
                    System.out.println(ANSI_RED + "  Time has already passed. Please try again." + ANSI_RESET);
                }
            } catch (DateTimeParseException e) {
                System.out.println(ANSI_RED + "  Invalid time format. Please try again." + ANSI_RESET);
            }
        }

        // Duration input
        double duration = 0;
        boolean validDuration = false;
        while (!validDuration) {
            System.out.print("  Duration (hours): ");
            String input = scanner.nextLine().trim();
            if (isCancel(input)) return;
            try {
                duration = Double.parseDouble(input);
                if (duration <= 0) {
                    System.out.println(ANSI_RED + "  Duration must be greater than zero." + ANSI_RESET);
                } else {
                    validDuration = true;
                }
            } catch (NumberFormatException e) {
                System.out.println(ANSI_RED + "  Invalid input." + ANSI_RESET);
            }
        }

        // Reason input
        System.out.print("  Reason: ");
        String reason = scanner.nextLine().trim();
        if (isCancel(reason)) return;
        if (reason.isEmpty()) {
            System.out.println(ANSI_RED + "  Reason cannot be empty." + ANSI_RESET);
            router.pause();
            return;
        }

        LocalDateTime dateTime = LocalDateTime.of(date, time);
        ScheduledMaintenance request = new ScheduledMaintenance(
            ride.getRideID(), type, dateTime, duration, reason);

        // Step 5: check overlaps
        List<ScheduledMaintenance> all = controller.loadMaintenanceSchedule();
        List<ScheduledMaintenance> overlaps = controller.findOverlaps(request, all);

        if (!overlaps.isEmpty()) {
            boolean shouldContinue = showHandleOverlaps(request, overlaps);
            if (!shouldContinue) return;
        }

        all.add(request);
        List<ScheduledMaintenance> normalized = controller.normalize(all);

        ScheduledMaintenance finalRequest = normalized.stream()
            .filter(m -> m.getRideID().equals(request.getRideID())
                && m.getMaintenanceType() == request.getMaintenanceType()
                && controller.overlaps(request, m))
            .findFirst()
            .orElse(request);

        // Step 6: confirm
        showConfirmation(ride, finalRequest, normalized);
    }

    // Overlap handling
    private boolean showHandleOverlaps(ScheduledMaintenance request, List<ScheduledMaintenance> overlaps) {
        router.clearScreen();
        router.printBanner();
        boolean sameType = overlaps.stream()
            .anyMatch(x -> x.getMaintenanceType() == request.getMaintenanceType());

        System.out.println(ANSI_YELLOW + ANSI_BOLD + "  Overlap Detected!" + ANSI_RESET);
        System.out.println("  " + "-".repeat(50));
        for (ScheduledMaintenance m : overlaps) {
            System.out.println("  - " + printOverlapSummary(m));
        }
        System.out.println();
        if (sameType) {
            System.out.println(ANSI_YELLOW + "  Same-type overlap: Request will be auto-merged." + ANSI_RESET);
        } else {
            System.out.println(ANSI_YELLOW + "  Different-type overlap: Will co-exist." + ANSI_RESET);
        }
        System.out.println("  " + "-".repeat(50));
        System.out.println("  1. Continue");
        System.out.println("  2. Cancel Request");
        System.out.println("  " + "-".repeat(50));
        System.out.print("  Select an option: ");

        String input = scanner.nextLine().trim();
        return input.equals("1");
    }

    // Step 6 & 7: confirm and save
    private void showConfirmation(Ride ride, ScheduledMaintenance maintenance, List<ScheduledMaintenance> finalState) {
        router.clearScreen();
        router.printBanner();
        System.out.println(ANSI_BOLD + "  Confirm Maintenance Request" + ANSI_RESET);
        System.out.println("  " + "-".repeat(50));
        System.out.println(formatMaintenanceRequest(ride, maintenance));
        System.out.println("  " + "-".repeat(50));
        System.out.println("  1. Confirm");
        System.out.println("  2. Cancel");
        System.out.println("  " + "-".repeat(50));
        System.out.print("  Select an option: ");

        String choice = scanner.nextLine().trim();
        if (choice.equals("1")) {
            controller.saveScheduledMaintenance(finalState);
            System.out.println(ANSI_GREEN + "  Maintenance request submitted successfully!" + ANSI_RESET);
            router.pause();
        }
        // cancel just returns to menu
    }

    // View schedule
    private void showScheduleMenu() {
        router.clearScreen();
        router.printBanner();
        List<Ride> rides = controller.loadRides();
        List<ScheduledMaintenance> schedule = controller.loadMaintenanceSchedule();

        System.out.println(ANSI_BOLD + "  Maintenance Schedule" + ANSI_RESET);
        System.out.println("  " + "-".repeat(60));

        if (schedule.isEmpty()) {
            System.out.println("  No maintenance requests found.");
        } else {
            for (Location location : Location.values()) {
                System.out.println(ANSI_BOLD + "  " + location.name().replace("_", " ") + ":" + ANSI_RESET);
                for (Ride ride : rides) {
                    if (!ride.getLocation().equalsIgnoreCase(location.name().replace("_", " "))
                        && !ride.getLocation().equalsIgnoreCase(location.name())) continue;
                    boolean hasRequests = false;
                    for (ScheduledMaintenance m : schedule) {
                        if (m.getRideID().equals(ride.getRideID())) {
                            if (!hasRequests) {
                                System.out.println("\n  " + ride.getName() + ":");
                                hasRequests = true;
                            }
                            System.out.println(formatMaintenanceRequest(ride, m));
                        }
                    }
                }
                System.out.println("  " + "-".repeat(60));
            }
        }
        router.pause();
    }

    private String formatMaintenanceRequest(Ride ride, ScheduledMaintenance m) {
        String date      = dateFormatter.format(m.getStartTime());
        String startTime = timeFormatter.format(m.getStartTime());
        long minutes     = (long)(m.getDuration() * 60);
        String endTime   = timeFormatter.format(m.getStartTime().plusMinutes(minutes));

        String s = "  " + ride.getLocation() + ": " + ride.getName()
            + " | " + m.getMaintenanceType() + "\n"
            + "  START: " + date + " " + startTime + " - " + endTime + "\n"
            + "  REASON: " + m.getReason();
        return s;
    }

    private String printOverlapSummary(ScheduledMaintenance m) {
        String date  = dateFormatter.format(m.getStartTime());
        String start = timeFormatter.format(m.getStartTime());
        String end   = timeFormatter.format(m.getStartTime().plusMinutes((long)(m.getDuration() * 60)));
        return m.getMaintenanceType() + " | " + date + " " + start + "-" + end + " | " + m.getReason();
    }

    private boolean isCancel(String value) {
        return value.equalsIgnoreCase("C") || value.equalsIgnoreCase("CANCEL");
    }
}