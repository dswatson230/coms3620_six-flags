package ui;

import models.item.CartItem;
import models.item.ItemConfirmation;

import java.util.List;
import java.util.Scanner;

public class ValidationUI {
    private final UserInterface    router;
    private final Scanner          scanner;
    private final ItemConfirmation itemConfirmation;

    private static final String ANSI_BOLD  = "\u001B[1m";
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED   = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";

    public ValidationUI(UserInterface router, Scanner scanner) {
        this.router           = router;
        this.scanner          = scanner;
        this.itemConfirmation = new ItemConfirmation();
    }

    public void showValidationMenu() {
        boolean running = true;
        while (running) {
            router.clearScreen();
            router.printBanner();
            System.out.println(ANSI_BOLD + "  Item Validation" + ANSI_RESET);
            System.out.println("  " + "-".repeat(34));
            System.out.println("  1. Validate Item");
            System.out.println("  2. Back");
            System.out.println("  " + "-".repeat(34));
            System.out.print("  Select an option: ");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1": showItemSelection(); break;
                case "2": running = false;     break;
                default:
                    System.out.println(ANSI_RED + "  Invalid option." + ANSI_RESET);
                    router.pause();
            }
        }
    }

    private void showItemSelection() {
        List<CartItem> items = itemConfirmation.getDataProvider().getAllItems();
        List<String> ids   = itemConfirmation.getDataProvider().getAllIds();

        boolean running = true;
        while (running) {
            router.clearScreen();
            router.printBanner();
            System.out.println(ANSI_BOLD + "  Select Item to Validate" + ANSI_RESET);
            System.out.println("  " + "-".repeat(60));
            System.out.printf("  %-4s %-25s %-20s %-10s%n", "No.", "Name", "Location", "Status");
            System.out.println("  " + "-".repeat(60));
            for (int i = 0; i < items.size(); i++) {
                CartItem item = items.get(i);
                System.out.printf("  %-4s %-25s %-20s %-10s%n",
                    (i + 1) + ".",
                    item.getName(),
                    item.getLocation().name().replace("_", " "),
                    item.getStatus());
            }
            System.out.println("  " + (items.size() + 1) + ". Back");
            System.out.println("  " + "-".repeat(60));
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
                validateItem(ids.get(selection - 1));
            } else {
                System.out.println(ANSI_RED + "  Invalid selection." + ANSI_RESET);
                router.pause();
            }
        }
    }

    private void validateItem(String itemId) {
        router.clearScreen();
        router.printBanner();
        boolean ok    = itemConfirmation.validateItem(itemId);
        String result = itemConfirmation.getGenMessage();
        CartItem item = itemConfirmation.getItem();

        System.out.println(ANSI_BOLD + "  Validation Result" + ANSI_RESET);
        System.out.println("  " + "-".repeat(40));
        if (ok) {
            System.out.println(ANSI_GREEN + "  " + result + ANSI_RESET);
            if (item != null) System.out.println("  " + item.getInfo());
        } else {
            System.out.println(ANSI_RED + "  " + result + ANSI_RESET);
        }
        System.out.println("  " + "-".repeat(40));
        router.pause();
    }
}