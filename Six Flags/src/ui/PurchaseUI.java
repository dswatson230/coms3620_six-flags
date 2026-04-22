package ui;

import controllers.ItemController;
import interfaces.Location;
import models.Item;

import java.util.ArrayList;
import java.util.Scanner;

public class PurchaseUI {
    private final ItemController controller;
    private final UserInterface  router;
    private final Scanner        scanner;

    private static final String ANSI_BOLD  = "\u001B[1m";
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED   = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";

    public PurchaseUI(ItemController controller, UserInterface router, Scanner scanner) {
        this.controller = controller;
        this.router     = router;
        this.scanner    = scanner;
    }

    public void showLocationSelection() {
        Location[] locations = Location.values();
        boolean running = true;
        while (running) {
            router.clearScreen();
            router.printBanner();
            System.out.println(ANSI_BOLD + "  Purchase -- Select Location" + ANSI_RESET);
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
                showItemSelection(locations[selection - 1]);
            } else {
                System.out.println(ANSI_RED + "  Invalid selection." + ANSI_RESET);
                router.pause();
            }
        }
    }

    private void showItemSelection(Location location) {
        boolean running = true;
        while (running) {
            router.clearScreen();
            router.printBanner();
            ArrayList<Item> items = controller.getAvailableItemsByLocation(location);
            System.out.println(ANSI_BOLD + "  Items at " + location.name().replace("_", " ") + ANSI_RESET);
            System.out.println("  " + "-".repeat(50));
            if (items.isEmpty()) {
                System.out.println("  No items available at this location.");
                System.out.println("  " + "-".repeat(50));
                router.pause();
                return;
            }
            System.out.printf("  %-4s %-25s %-10s %-6s%n", "No.", "Name", "Price", "Qty");
            System.out.println("  " + "-".repeat(50));
            for (int i = 0; i < items.size(); i++) {
                Item item = items.get(i);
                System.out.printf("  %-4s %-25s %-10s %-6s%n",
                    (i + 1) + ".",
                    item.getName(),
                    String.format("$%.2f", item.getPrice()),
                    item.getQuantity());
            }
            System.out.println("  " + (items.size() + 1) + ". Back");
            System.out.println("  " + "-".repeat(50));
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

            if (selection == items.size() + 1) {
                running = false;
            } else if (selection >= 1 && selection <= items.size()) {
                showQuantityInput(items.get(selection - 1));
            } else {
                System.out.println(ANSI_RED + "  Invalid selection." + ANSI_RESET);
                router.pause();
            }
        }
    }

    private void showQuantityInput(Item item) {
        boolean entering = true;
        while (entering) {
            router.clearScreen();
            router.printBanner();
            System.out.println(ANSI_BOLD + "  Add to Cart" + ANSI_RESET);
            System.out.println("  " + "-".repeat(34));
            System.out.println("  Item:      " + item.getName());
            System.out.println("  Price:     $" + String.format("%.2f", item.getPrice()));
            System.out.println("  Available: " + item.getQuantity());
            System.out.println("  " + "-".repeat(34));
            System.out.print("  Enter quantity (0 to cancel): ");

            String input = scanner.nextLine().trim();
            int quantity;
            try {
                quantity = Integer.parseInt(input);
                if (quantity == 0) return;
                if (quantity < 0) {
                    System.out.println(ANSI_RED + "  Quantity must be positive." + ANSI_RESET);
                    router.pause();
                    continue;
                }
            } catch (NumberFormatException e) {
                System.out.println(ANSI_RED + "  Invalid number." + ANSI_RESET);
                router.pause();
                continue;
            }

            String error = controller.addToCart(item, quantity);
            if (error != null) {
                System.out.println(ANSI_RED + "  Error: " + error + ANSI_RESET);
                router.pause();
            } else {
                System.out.println(ANSI_GREEN + "  Added to cart." + ANSI_RESET);
                entering = false;
                showCartMenu();
            }
        }
    }

    public void showCartMenu() {
        boolean running = true;
        while (running) {
            router.clearScreen();
            router.printBanner();
            System.out.println(ANSI_BOLD + "  Cart" + ANSI_RESET);
            System.out.println("  " + "-".repeat(50));
            for (String line : controller.getCartInfo().split("\n")) {
                System.out.println("  " + line);
            }
            System.out.println("  " + "-".repeat(50));
            System.out.printf("  %-30s $%.2f%n", "Total:", controller.calculateTotal());
            System.out.println("  " + "-".repeat(50));
            System.out.println("  1. Continue Shopping");
            System.out.println("  2. Remove Item");
            System.out.println("  3. Checkout");
            System.out.println("  4. Back to Main");
            System.out.println("  " + "-".repeat(50));
            System.out.print("  Select an option: ");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1": showLocationSelection(); running = false; break;
                case "2": showRemoveItem();                        break;
                case "3": showCheckout();          running = false; break;
                case "4":                          running = false; break;
                default:
                    System.out.println(ANSI_RED + "  Invalid option." + ANSI_RESET);
                    router.pause();
            }
        }
    }

    private void showRemoveItem() {
        router.clearScreen();
        router.printBanner();
        System.out.println(ANSI_BOLD + "  Remove Item" + ANSI_RESET);
        System.out.println("  " + "-".repeat(50));
        for (String line : controller.getCartInfo().split("\n")) {
            System.out.println("  " + line);
        }
        System.out.println("  " + "-".repeat(50));
        System.out.print("  Enter item number to remove (0 to cancel): ");
        String input = scanner.nextLine().trim();
        try {
            int index = Integer.parseInt(input) - 1;
            if (index == -1) return;
            controller.removeFromCart(index);
            System.out.println(ANSI_GREEN + "  Item removed." + ANSI_RESET);
        } catch (NumberFormatException e) {
            System.out.println(ANSI_RED + "  Invalid selection." + ANSI_RESET);
        }
        router.pause();
    }

    private void showCheckout() {
        router.clearScreen();
        router.printBanner();
        System.out.println(ANSI_BOLD + "  Checkout" + ANSI_RESET);
        System.out.println("  " + "-".repeat(50));
        if (controller.isCartEmpty()) {
            System.out.println(ANSI_RED + "  Cart is empty." + ANSI_RESET);
            router.pause();
            return;
        }
        for (String line : controller.getCartInfo().split("\n")) {
            System.out.println("  " + line);
        }
        System.out.println("  " + "-".repeat(50));
        double total = controller.calculateTotal();
        System.out.printf("  %-30s $%.2f%n", "Total:", total);
        System.out.println("  " + "-".repeat(50));
        System.out.print("  Enter card info: ");
        scanner.nextLine();

        String error = controller.checkout();
        if (error != null) {
            System.out.println(ANSI_RED + "  Transaction failed: " + error + ANSI_RESET);
        } else {
            System.out.println(ANSI_GREEN + "  Purchase successful!" + ANSI_RESET);
            System.out.printf("  Total charged: $%.2f%n", total);
            System.out.println("  Thank you for your order.");
        }
        router.pause();
    }
}