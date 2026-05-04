package ui;

import controllers.WaitTimeController;
import models.Ride;

import java.util.List;
import java.util.Scanner;

public class WaitTimeUI {
    private static final String ANSI_BOLD  = "\u001B[1m";
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED   = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";

    private final WaitTimeController controller;
    private final UserInterface      router;
    private final Scanner            scanner;

    public WaitTimeUI(UserInterface router, Scanner scanner) {
        this.router     = router;
        this.scanner    = scanner;
        this.controller = new WaitTimeController();
    }

    public void showWaitTimeMenu() {
        boolean running = true;
        while (running) {
            router.clearScreen();
            router.printBanner();
            System.out.println(ANSI_BOLD + "  Manage Ride Wait Times" + ANSI_RESET);
            System.out.println("  " + "-".repeat(34));
            System.out.println("  1. Update Wait Time");
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
            System.out.println("  " + "-".repeat(65));
            System.out.printf("  %-4s %-20s %-20s %-12s %-8s%n",
                "No.", "Name", "Location", "Status", "Wait");
            System.out.println("  " + "-".repeat(65));
            for (int i = 0; i < rides.size(); i++) {
                Ride r = rides.get(i);
                System.out.printf("  %-4s %-20s %-20s %-12s %-8s%n",
                    (i + 1) + ".",
                    r.getName(),
                    r.getLocation(),
                    r.getStatus(),
                    r.getWaitTime() + " min");
            }
            System.out.println("  " + (rides.size() + 1) + ". Back");
            System.out.println("  " + "-".repeat(65));
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
                showWaitTimeInput(rides.get(selection - 1));
            } else {
                System.out.println(ANSI_RED + "  Invalid selection." + ANSI_RESET);
                router.pause();
            }
        }
    }

    private void showWaitTimeInput(Ride ride) {
        boolean entering = true;
        while (entering) {
            router.clearScreen();
            router.printBanner();
            System.out.println(ANSI_BOLD + "  Update Wait Time: " + ride.getName() + ANSI_RESET);
            System.out.println("  " + "-".repeat(34));
            System.out.println("  Status:       " + ride.getStatus());
            System.out.println("  Current Wait: " + ride.getWaitTime() + " min");
            System.out.println("  " + "-".repeat(34));
            System.out.print("  Enter new wait time in minutes (0 to cancel): ");

            String input = scanner.nextLine().trim();
            int waitTime;
            try {
                waitTime = Integer.parseInt(input);
                if (waitTime == 0) return;
                if (waitTime < 0) {
                    System.out.println(ANSI_RED + "  Wait time cannot be negative." + ANSI_RESET);
                    router.pause();
                    continue;
                }
            } catch (NumberFormatException e) {
                System.out.println(ANSI_RED + "  Invalid input — please enter a number." + ANSI_RESET);
                router.pause();
                continue;
            }

            String error = controller.updateWaitTime(ride.getRideID(), waitTime);
            if (error != null) {
                System.out.println(ANSI_RED + "  " + error + ANSI_RESET);
                router.pause();
                entering = false;
            } else {
                System.out.println(ANSI_GREEN + "  Wait time updated to " + waitTime + " minutes." + ANSI_RESET);
                router.pause();
                entering = false;
            }
        }
    }
}