package ui;

import controllers.ItemController;
import interfaces.Location;
import models.Item;

import java.util.ArrayList;
import java.util.Scanner;

public class InventoryUI {
    private final ItemController controller;
    private final UserInterface  router;
    private final Scanner        scanner;

    private static final String ANSI_BOLD  = "\u001B[1m";
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED   = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";

    private static final String[] LOCATION_NAMES = {
        "Magic Mountain", "Fiesta Texas", "Great America",
        "Over Georgia", "The Great Escape"
    };

    public InventoryUI(ItemController controller, UserInterface router, Scanner scanner) {
        this.controller = controller;
        this.router     = router;
        this.scanner    = scanner;
    }

    public void showInventoryMenu() {
        boolean running = true;
        while (running) {
            router.clearScreen();
            router.printBanner();
            System.out.println(ANSI_BOLD + "  Inventory Management" + ANSI_RESET);
            System.out.println("  " + "-".repeat(34));
            System.out.println("  1. Add Item");
            System.out.println("  2. View Inventory");
            System.out.println("  3. Back");
            System.out.println("  " + "-".repeat(34));
            System.out.print("  Select an option: ");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1": showLocationSelection(); break;
                case "2": showViewInventory();     break;
                case "3": running = false;         break;
                default:
                    System.out.println(ANSI_RED + "  Invalid option." + ANSI_RESET);
                    router.pause();
            }
        }
    }

    private void showViewInventory() {
        router.clearScreen();
        router.printBanner();
        ArrayList<Item> allItems = controller.getAllItems();
        System.out.println(ANSI_BOLD + "  Current Inventory" + ANSI_RESET);
        System.out.println("  " + "-".repeat(70));
        System.out.printf("  %-25s %-10s %-10s %-20s%n", "Name", "Price", "Qty", "Location");
        System.out.println("  " + "-".repeat(70));
        if (allItems.isEmpty()) {
            System.out.println("  No items in inventory.");
        } else {
            for (Item item : allItems) {
                System.out.printf("  %-25s %-10s %-10s %-20s%n",
                    item.getName(),
                    String.format("$%.2f", item.getPrice()),
                    item.getQuantity(),
                    item.getLocation().name().replace("_", " "));
            }
        }
        System.out.println("  " + "-".repeat(70));
        router.pause();
    }

    private void showLocationSelection() {
        Location[] locations = Location.values();
        boolean running = true;
        while (running) {
            router.clearScreen();
            router.printBanner();
            System.out.println(ANSI_BOLD + "  Add Item -- Select Location" + ANSI_RESET);
            System.out.println("  " + "-".repeat(34));
            for (int i = 0; i < locations.length; i++) {
                System.out.println("  " + (i + 1) + ". " + LOCATION_NAMES[i]);
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
                showItemTemplates(locations[selection - 1]);
            } else {
                System.out.println(ANSI_RED + "  Invalid selection." + ANSI_RESET);
                router.pause();
            }
        }
    }

    private void showItemTemplates(Location location) {
        boolean running = true;
        while (running) {
            router.clearScreen();
            router.printBanner();
            Object[][] templates = controller.getItemTemplates();
            System.out.println(ANSI_BOLD + "  Add Item at " + location.name().replace("_", " ") + ANSI_RESET);
            System.out.println("  " + "-".repeat(55));
            System.out.printf("  %-4s %-25s %-10s %-10s%n", "No.", "Name", "Price", "In Stock");
            System.out.println("  " + "-".repeat(55));
            for (int i = 0; i < templates.length; i++) {
                String name  = (String) templates[i][0];
                double price = (double) templates[i][1];
                int qty      = controller.getQuantityFor(name, location);
                System.out.printf("  %-4s %-25s %-10s %-10s%n",
                    (i + 1) + ".",
                    name,
                    String.format("$%.2f", price),
                    qty);
            }
            System.out.println("  " + (templates.length + 1) + ". Back");
            System.out.println("  " + "-".repeat(55));
            System.out.print("  Select an item: ");

            String choice = scanner.nextLine().trim();
            int selection;
            try {
                selection = Integer.parseInt(choice);
            } catch (NumberFormatException e) {
                System.out.println(ANSI_RED + "  Invalid input." + ANSI_RESET);
                router.pause();
                continue;
            }

            if (selection == templates.length + 1) {
                running = false;
            } else if (selection >= 1 && selection <= templates.length) {
                String name  = (String) templates[selection - 1][0];
                double price = (double) templates[selection - 1][1];
                int currentQty = controller.getQuantityFor(name, location);
                showQuantityInput(name, price, location, currentQty);
            } else {
                System.out.println(ANSI_RED + "  Invalid selection." + ANSI_RESET);
                router.pause();
            }
        }
    }

    private void showQuantityInput(String name, double price, Location location, int currentQty) {
        router.clearScreen();
        router.printBanner();
        System.out.println(ANSI_BOLD + "  Add Stock" + ANSI_RESET);
        System.out.println("  " + "-".repeat(34));
        System.out.println("  Item:          " + name);
        System.out.println("  Current Stock: " + currentQty);
        System.out.println("  " + "-".repeat(34));
        System.out.print("  Enter quantity to add (0 to cancel): ");
        String input = scanner.nextLine().trim();
        int amount;
        try {
            amount = Integer.parseInt(input);
            if (amount == 0) return;
            if (amount < 0) {
                System.out.println(ANSI_RED + "  Quantity must be greater than zero." + ANSI_RESET);
                router.pause();
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println(ANSI_RED + "  Invalid number." + ANSI_RESET);
            router.pause();
            return;
        }

        int newTotal = controller.increaseQuantity(name, price, location, amount);
        System.out.println(ANSI_GREEN + "  " + name + " updated! Added: " + amount + " | New total: " + newTotal + ANSI_RESET);
        router.pause();
    }
}