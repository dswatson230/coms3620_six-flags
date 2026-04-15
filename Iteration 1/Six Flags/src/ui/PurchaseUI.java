package ui;

import controllers.ItemController;
import interfaces.Location;
import models.Item;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Scanner;

public class PurchaseUI {
    private ItemController controller;
    private UserInterface router;
    private Scanner sc;

    public PurchaseUI(ItemController controller, UserInterface router) {
        this.controller = controller;
        this.router = router;
        sc = new Scanner(System.in);
    }

    public void showLocationSelection() {
        System.out.println();

        Location[] locations = Location.values();
        for (int i = 0; i < locations.length; i++) {
            System.out.println(i+1 + ". " + locations[i]);
        }
        System.out.println(locations.length + 1 + ". Back\n");

        int selection = 0;
        boolean selected = false;
        while (!selected) {
            System.out.print("Select a Location: ");
            selection = sc.nextInt();
            if (selection >= 1 && selection <= locations.length + 1) {
                selected = true;
            }
            else {
                System.out.println("Invalid Input, Please Try Again");
            }
        }

        if (selection == locations.length+1) {
            router.showMainMenu();
        }
        else {
            showItemSelection(locations[selection - 1]);
        }
    }

    private void showItemSelection(Location location) {
        System.out.println();
        sc.nextLine();

        ArrayList<Item> items = controller.getAvailableItemsByLocation(location);

        if (items.isEmpty()) {
            System.out.println("No items available at this location.");
        } else {
            for (Item item : items) {
                System.out.println(items.indexOf(item)+1 + ". " + item.getName() + " - $" + item.getPrice() +
                                          " (Qty: " + item.getQuantity() + ")");
            }
        }
        System.out.println(items.size() + 1 + ". Back\n");

        int selection = 0;
        boolean selected = false;
        while (!selected) {
            System.out.print("Selection: ");
            selection = sc.nextInt();
            if (selection >= 1 && selection <= items.size() + 1) {
                selected = true;
            }
            else {
                System.out.println("Invalid Input, Please Try Again");
            }
        }

        if (selection == items.size()+1) {
            showLocationSelection();
        }
        else {
            showQuantityInput(items.get(selection-1));
        }
    }


    private void showQuantityInput(Item item) {
        System.out.println();
        sc.nextLine();

        System.out.print("Enter Desired Quantity: ");
        String quantityInput = sc.nextLine();

        int quantity = 0;
        try {
            quantity = Integer.parseInt(quantityInput.trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid quantity");
            showQuantityInput(item);
        }

        String error = controller.addToCart(item, quantity);
        if (error != null) {
            System.out.println("Error: " + error);
            showQuantityInput(item);
        }
        showCartMenu();
    }

    public void showCartMenu() {
        System.out.println();

        System.out.println(controller.getCartInfo());

        System.out.println("ACTIONS:");
        System.out.println("1. Continue Shopping");
        System.out.println("2. Remove Item");
        System.out.println("3. Checkout");
        System.out.println("4. Back to Main");

        int selection = 0;
        boolean selected = false;
        while (!selected) {
            System.out.print("Select Action: ");
            selection = sc.nextInt();
            selected = true;
            switch (selection) {
                case 1: showLocationSelection(); break;
                case 2: showRemoveItem(); break;
                case 3: showCheckout(); break;
                case 4: router.showMainMenu(); break;
                default: {
                    System.out.println("Invalid Input, Please Try Again");
                    selected = false;
                }
            }
        }
    }

    private void showRemoveItem() {
        System.out.println();
        sc.nextLine();

        System.out.println(controller.getCartInfo());
        System.out.print("Enter item number to remove:\n");
        String input = sc.nextLine();
        if (input == null) return;
        try {
            controller.removeFromCart(Integer.parseInt(input) - 1);
            showCartMenu();
        } catch (NumberFormatException e) {
            System.out.println("Invalid selection");
        }
    }

    private void showCheckout() {
        System.out.println();
        sc.nextLine();

        double total = controller.calculateTotal();
        System.out.println("Total: $" + String.format("%.2f", total));
        System.out.print("\nInput Card Info: ");
        String cardInfo = sc.nextLine();
        if (cardInfo == null) return;

        String error = controller.checkout();

        if (error != null) {
            System.out.println("\nTransaction Failed: " + error);
        } else {
            System.out.println("\nPurchase successful!\nTotal charged:" + String.format("$%.2f\nThank you for your order.", total));
            router.showMainMenu();
        }
    }
}