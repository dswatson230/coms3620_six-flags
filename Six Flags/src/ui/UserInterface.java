package ui;

import controllers.ItemController;
import data.ItemInventory;

import java.util.Scanner;

public class UserInterface {
    private static final String ANSI_RED    = "\u001B[31m";
    private static final String ANSI_BOLD   = "\u001B[1m";
    private static final String ANSI_RESET  = "\u001B[0m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_CYAN   = "\u001B[36m";

    private final ItemController controller;
    private final PurchaseUI     purchaseUI;
    private final InventoryUI    inventoryUI;
    private final SchedulingUI   schedulingUI;
    private final ValidationUI   validationUI;
    private final RideStatusUI   rideStatusUI;
    private final RefundUI       refundUI;
    private final Scanner        scanner;

    public UserInterface() {
        this.scanner      = new Scanner(System.in);
        this.controller   = new ItemController();
        ItemInventory sharedInventory = controller.getSharedInventory();
        this.purchaseUI   = new PurchaseUI(controller, this, scanner);
        this.inventoryUI  = new InventoryUI(controller, this, scanner);
        this.schedulingUI = new SchedulingUI(scanner);
        this.validationUI = new ValidationUI(this, scanner);
        this.rideStatusUI = new RideStatusUI(this, scanner);
        this.refundUI     = new RefundUI(scanner, this, sharedInventory);
    }

    public void start() {
        showMainMenu();
    }

    public void showMainMenu() {
        boolean running = true;
        while (running) {
            clearScreen();
            printBanner();
            System.out.println(ANSI_BOLD + "  Main Menu" + ANSI_RESET);
            System.out.println("  " + "-".repeat(34));
            System.out.println("  1. Purchase");
            System.out.println("  2. Inventory");
            System.out.println("  3. Employee Management");
            System.out.println("  4. Validate Item");
            System.out.println("  5. Ride Status");
            System.out.println("  6. Process Refund");
            System.out.println("  7. Exit");
            System.out.println("  " + "-".repeat(34));
            System.out.print("  Select an option: ");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1": purchaseUI.showLocationSelection(); break;
                case "2": inventoryUI.showInventoryMenu();    break;
                case "3": schedulingUI.showSchedulingMenu();  break;
                case "4": validationUI.showValidationMenu();  break;
                case "5": rideStatusUI.showRideMenu();        break;
                case "6": refundUI.showRefundMenu();          break;
                case "7":
                    System.out.println(ANSI_YELLOW + "\n  Thanks for using Six Flags Management System. Goodbye!" + ANSI_RESET);
                    running = false;
                    break;
                default:
                    System.out.println(ANSI_RED + "  Invalid option. Please try again." + ANSI_RESET);
                    pause();
            }
        }
    }

    public void printBanner() {
        System.out.println(ANSI_RED + ANSI_BOLD +
            "  +================================+" + ANSI_RESET);
        System.out.println(ANSI_RED + ANSI_BOLD +
            "  |                                |" + ANSI_RESET);
        System.out.println(ANSI_RED + ANSI_BOLD +
            "  |       S I X   F L A G S        |" + ANSI_RESET);
        System.out.println(ANSI_YELLOW + ANSI_BOLD +
            "  |      Management  System        |" + ANSI_RESET);
        System.out.println(ANSI_RED + ANSI_BOLD +
            "  |                                |" + ANSI_RESET);
        System.out.println(ANSI_RED + ANSI_BOLD +
            "  +================================+" + ANSI_RESET);
        System.out.println();
    }

    public void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public void pause() {
        System.out.print("  Press Enter to continue...");
        scanner.nextLine();
    }
}