package ui;

import controllers.RideController;
import models.Ride;

import java.util.List;
import java.util.Scanner;

public class RideStatusUI {
    private final UserInterface        router;
    private final Scanner              scanner;
    private final RideController       controller;

    private static final String ANSI_BOLD  = "\u001B[1m";
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED   = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";

    private static final String[] STATUSES = {"OPEN", "CLOSED"};

    public RideStatusUI(UserInterface router, Scanner scanner, RideController controller) {
        this.router     = router;
        this.scanner    = scanner;
        this.controller = controller;
    }

    public void showRideMenu() {
        boolean running = true;
        while (running) {
            router.clearScreen();
            router.printBanner();
            System.out.println(ANSI_BOLD + "  Ride Status Management" + ANSI_RESET);
            System.out.println("  " + "-".repeat(34));
            System.out.println("  1. Update Ride Status");
            System.out.println("  2. Back");
            System.out.println("  " + "-".repeat(34));
            System.out.print("  Select an option: ");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1": showRideSelection(); break;
                case "2": running = false;     break;
                default:
                    System.out.println(ANSI_RED + "  Invalid option." + ANSI_RESET);
                    router.pause();
            }
        }
    }

    private void showRideSelection() {
        boolean running = true;
        while (running) {
            router.clearScreen();
            router.printBanner();
            List<Ride> rides = controller.loadRides();

            if (rides.isEmpty()) {
                System.out.println(ANSI_RED + "  No rides found." + ANSI_RESET);
                router.pause();
                return;
            }

            System.out.println(ANSI_BOLD + "  Select a Ride" + ANSI_RESET);
            System.out.println("  " + "-".repeat(60));
            System.out.printf("  %-4s %-20s %-20s %-12s%n", "No.", "Name", "Location", "Status");
            System.out.println("  " + "-".repeat(60));
            for (int i = 0; i < rides.size(); i++) {
                Ride ride = rides.get(i);
                System.out.printf("  %-4s %-20s %-20s %-12s%n",
                    (i + 1) + ".",
                    ride.getName(),
                    ride.getLocation(),
                    ride.getStatus());
            }
            System.out.println("  " + (rides.size() + 1) + ". Back");
            System.out.println("  " + "-".repeat(60));
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
                if (rides.get(selection-1).getStatus().equals("MAINTENANCE")) {
                    System.out.println(ANSI_YELLOW + "  Ride is currently in Maintenance Please Select a different ride." + ANSI_RESET);
                    router.pause();
                }
                else{
                    showUpdateForm(rides.get(selection - 1));
                }
            } else {
                System.out.println(ANSI_RED + "  Invalid selection." + ANSI_RESET);
                router.pause();
            }
        }
    }

    private void showUpdateForm(Ride ride) {
        router.clearScreen();
        router.printBanner();
        System.out.println(ANSI_BOLD + "  Update Ride: " + ride.getName() + ANSI_RESET);
        System.out.println("  " + "-".repeat(34));
        System.out.println("  Current Status: " + ride.getStatus());
        System.out.println();
        System.out.println("  New Status:");
        for (int i = 0; i < STATUSES.length; i++) {
            System.out.println("  " + (i + 1) + ". " + STATUSES[i]);
        }
        System.out.println("  " + (STATUSES.length + 1) + ". Cancel");
        System.out.println("  " + "-".repeat(34));
        System.out.print("  Select new status: ");

        String choice = scanner.nextLine().trim();
        int selection;
        try {
            selection = Integer.parseInt(choice);
        } catch (NumberFormatException e) {
            System.out.println(ANSI_RED + "  Invalid input." + ANSI_RESET);
            router.pause();
            return;
        }

        if (selection == STATUSES.length + 1) return;

        if (selection < 1 || selection > STATUSES.length) {
            System.out.println(ANSI_RED + "  Invalid selection." + ANSI_RESET);
            router.pause();
            return;
        }

        String newStatus = STATUSES[selection - 1];
        System.out.print("  Enter reason: ");
        String reason = scanner.nextLine().trim();

        String error = controller.updateRideStatus(ride.getRideID(), newStatus, reason);
        if (error != null) {
            System.out.println(ANSI_RED + "  Error: " + error + ANSI_RESET);
        } else {
            System.out.println(ANSI_GREEN + "  " + ride.getName() + " updated to " + newStatus + "." + ANSI_RESET);
        }
        router.pause();
    }
}