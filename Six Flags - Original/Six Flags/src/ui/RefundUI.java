package ui;

import controllers.RefundController;
import controllers.RefundSystem;
import data.ItemInventory;
import data.PurchaseFileHandler;
import data.RefundFileHandler;
import interfaces.RefundControllerInterface;
import models.PurchaseRecord;
import models.item.CartItem;

import java.util.List;
import java.util.Scanner;

public class RefundUI {
    //private static final String PURCHASE_FILE_PATH = "data/purchases.txt";
    //private static final String REFUND_FILE_PATH   = "data/refunds.txt";
    private static final String PURCHASE_FILE_PATH = "C:/Users/longi/COMS/coms3620/six-flags/Six Flags/data/purchases.txt";
    private static final String REFUND_FILE_PATH   = "C:/Users/longi/COMS/coms3620/six-flags/Six Flags/data/refunds.txt";

    private static final String ANSI_BOLD  = "\u001B[1m";
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED   = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";

    private final RefundControllerInterface refundController;
    private final PurchaseFileHandler       purchaseFileHandler;
    private final Scanner                   scanner;
    private final UserInterface             router;

    public RefundUI(Scanner scanner, UserInterface router, ItemInventory sharedInventory) {
        this.scanner             = scanner;
        this.router              = router;
        PurchaseFileHandler pfh  = new PurchaseFileHandler(PURCHASE_FILE_PATH);
        RefundFileHandler rfh    = new RefundFileHandler(REFUND_FILE_PATH);
        this.purchaseFileHandler = pfh;
        // use the shared inventory instead of creating a new one
        this.refundController    = new RefundController(
            new RefundSystem(pfh, rfh, sharedInventory));
    }

    public void showRefundMenu() {
        boolean running = true;
        while (running) {
            router.clearScreen();
            router.printBanner();
            System.out.println(ANSI_BOLD + "  Process Item Refund" + ANSI_RESET);
            System.out.println("  " + "-".repeat(34));
            System.out.println("  1. Process Refund");
            System.out.println("  2. Back");
            System.out.println("  " + "-".repeat(34));
            System.out.print("  Select an option: ");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1": showRecordList(); break;
                case "2": running = false;  break;
                default:
                    System.out.println(ANSI_RED + "  Invalid option." + ANSI_RESET);
                    router.pause();
            }
        }
    }

    private void showRecordList() {
        boolean running = true;
        while (running) {
            router.clearScreen();
            router.printBanner();

            List<PurchaseRecord> records;
            try {
                records = purchaseFileHandler.readAllRecords();
            } catch (Exception e) {
                System.out.println(ANSI_RED + "  Could not load purchase records." + ANSI_RESET);
                router.pause();
                return;
            }

            if (records.isEmpty()) {
                System.out.println("  No purchase records found.");
                router.pause();
                return;
            }

            System.out.println(ANSI_BOLD + "  Purchase Records" + ANSI_RESET);
            System.out.println("  " + "-".repeat(65));
            System.out.printf("  %-5s %-14s %-16s %-30s%n", "No.", "Record ID", "Status", "Items");
            System.out.println("  " + "-".repeat(65));
            for (int i = 0; i < records.size(); i++) {
                PurchaseRecord record = records.get(i);
                StringBuilder itemNames = new StringBuilder();
                for (CartItem item : record.getItems()) {
                    if (itemNames.length() > 0) itemNames.append(", ");
                    itemNames.append(item.getName()).append(" x").append(item.getQuantity());
                }
                System.out.printf("  %-5s %-14s %-16s %-30s%n",
                    (i + 1) + ".",
                    record.getRecordId(),
                    record.getStatus(),
                    itemNames.toString());
            }
            System.out.println("  " + (records.size() + 1) + ". Back");
            System.out.println("  " + "-".repeat(65));
            System.out.print("  Enter record number: ");

            String choice = scanner.nextLine().trim();
            int index;
            try {
                index = Integer.parseInt(choice) - 1;
                if (index == records.size()) return;
                if (index < 0 || index >= records.size()) {
                    System.out.println(ANSI_RED + "  Invalid selection." + ANSI_RESET);
                    router.pause();
                    continue;
                }
            } catch (NumberFormatException e) {
                System.out.println(ANSI_RED + "  Invalid input." + ANSI_RESET);
                router.pause();
                continue;
            }

            showItemSelection(records.get(index));
        }
    }

    private void showItemSelection(PurchaseRecord record) {
        router.clearScreen();
        router.printBanner();
        List<CartItem> items = record.getItems();

        System.out.println(ANSI_BOLD + "  Record: " + record.getRecordId() + ANSI_RESET);
        System.out.println("  " + "-".repeat(55));
        System.out.printf("  %-5s %-25s %-10s %-6s%n", "No.", "Name", "Price", "Qty");
        System.out.println("  " + "-".repeat(55));
        for (int i = 0; i < items.size(); i++) {
            CartItem item = items.get(i);
            System.out.printf("  %-5s %-25s %-10s %-6s%n",
                (i + 1) + ".",
                item.getName(),
                String.format("$%.2f", item.getPrice()),
                item.getQuantity());
        }
        System.out.println("  " + (items.size() + 1) + ". Cancel");
        System.out.println("  " + "-".repeat(55));
        System.out.print("  Select item to refund: ");

        String choice = scanner.nextLine().trim();
        int itemIndex;
        try {
            itemIndex = Integer.parseInt(choice) - 1;
            if (itemIndex == items.size()) return;
            if (itemIndex < 0 || itemIndex >= items.size()) {
                System.out.println(ANSI_RED + "  Invalid selection." + ANSI_RESET);
                router.pause();
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println(ANSI_RED + "  Invalid input." + ANSI_RESET);
            router.pause();
            return;
        }

        showQuantityInput(record, itemIndex, items.get(itemIndex));
    }

    private void showQuantityInput(PurchaseRecord record, int itemIndex, CartItem item) {
        boolean entering = true;
        while (entering) {
            router.clearScreen();
            router.printBanner();
            System.out.println(ANSI_BOLD + "  Refund Item" + ANSI_RESET);
            System.out.println("  " + "-".repeat(34));
            System.out.println("  Item:          " + item.getName());
            System.out.println("  Location:      " + item.getLocation().name().replace("_", " "));
            System.out.println("  Purchased Qty: " + item.getQuantity());
            System.out.println("  " + "-".repeat(34));
            System.out.print("  Enter quantity to refund (0 to cancel): ");

            String input = scanner.nextLine().trim();
            int quantity;
            try {
                quantity = Integer.parseInt(input);
                if (quantity == 0) return;
            } catch (NumberFormatException e) {
                System.out.println(ANSI_RED + "  Invalid quantity." + ANSI_RESET);
                router.pause();
                continue;
            }

            String result = refundController.processRefund(
                record.getRecordId(), itemIndex, quantity);

            if (result.contains("successfully")) {
                System.out.println(ANSI_GREEN + "  " + result + ANSI_RESET);
            } else {
                System.out.println(ANSI_RED + "  " + result + ANSI_RESET);
            }
            router.pause();
            entering = false;
        }
    }
}