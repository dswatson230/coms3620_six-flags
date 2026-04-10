package ui;

import controllers.ItemController;
import interfaces.Location;
import models.Item;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class PurchaseUI {
    private JFrame frame;
    private ItemController controller;
    private UserInterface router;

    private static final String[] LOCATION_NAMES = {
        "Magic Mountain", "Fiesta Texas", "Great America",
        "Over Georgia", "The Great Escape"
    };

    public PurchaseUI(JFrame frame, ItemController controller, UserInterface router) {
        this.frame      = frame;
        this.controller = controller;
        this.router     = router;
    }

    public void showLocationSelection() {
        JPanel panel = new JPanel(new GridLayout(7, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 60, 20, 60));

        JLabel label = new JLabel("Select a Location:", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(label);

        Location[] locations = Location.values();
        for (int i = 0; i < locations.length; i++) {
            JButton btn = new JButton(LOCATION_NAMES[i]);
            Location loc = locations[i];
            btn.addActionListener(e -> showItemSelection(loc));
            panel.add(btn);
        }

        JButton backBtn = new JButton("Back");
        backBtn.addActionListener(e -> router.showMainMenu());
        panel.add(backBtn);

        router.setPanel(panel);
    }

    private void showItemSelection(Location location) {
        ArrayList<Item> items = controller.getAvailableItemsByLocation(location);

        JPanel panel = new JPanel(new GridLayout(items.size() + 2, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 60, 20, 60));

        JLabel label = new JLabel("Select Item to Purchase:", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(label);

        if (items.isEmpty()) {
            panel.add(new JLabel("No items available at this location.", SwingConstants.CENTER));
        } else {
            for (Item item : items) {
                JButton btn = new JButton(item.getName() + " - $" + item.getPrice() +
                                          " (Qty: " + item.getQuantity() + ")");
                btn.addActionListener(e -> showQuantityInput(item));
                panel.add(btn);
            }
        }

        JButton backBtn = new JButton("Back");
        backBtn.addActionListener(e -> showLocationSelection());
        panel.add(backBtn);

        router.setPanel(panel);
    }

    private void showQuantityInput(Item item) {
        String quantityInput = JOptionPane.showInputDialog(frame,
            "Enter quantity for " + item.getName() + ":\n(Available: " + item.getQuantity() + ")");
        if (quantityInput == null) return;

        int quantity;
        try {
            quantity = Integer.parseInt(quantityInput.trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Invalid quantity.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String error = controller.addToCart(item, quantity);
        if (error != null) {
            JOptionPane.showMessageDialog(frame, error, "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        showCartMenu();
    }

    public void showCartMenu() {
        JPanel panel = new JPanel(new GridLayout(5, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 60, 20, 60));

        JLabel cartLabel = new JLabel("<html>" +
            controller.getCartInfo().replace("\n", "<br>") + "</html>",
            SwingConstants.CENTER);
        panel.add(cartLabel);

        JButton continueBtn = new JButton("Continue Shopping");
        JButton removeBtn   = new JButton("Remove Item");
        JButton checkoutBtn = new JButton("Checkout");
        JButton backBtn     = new JButton("Back to Main");

        continueBtn.addActionListener(e -> showLocationSelection());
        removeBtn.addActionListener(e   -> showRemoveItem());
        checkoutBtn.addActionListener(e -> showCheckout());
        backBtn.addActionListener(e     -> router.showMainMenu());

        panel.add(continueBtn);
        panel.add(removeBtn);
        panel.add(checkoutBtn);
        panel.add(backBtn);

        router.setPanel(panel);
    }

    private void showRemoveItem() {
        String input = JOptionPane.showInputDialog(frame,
            "Enter item number to remove:\n" + controller.getCartInfo());
        if (input == null) return;
        try {
            controller.removeFromCart(Integer.parseInt(input) - 1);
            showCartMenu();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Invalid selection.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showCheckout() {
        String cardInfo = JOptionPane.showInputDialog(frame,
            "Total: $" + String.format("%.2f", controller.calculateTotal()) + "\nInput Card Info:");
        if (cardInfo == null) return;

        double total = controller.calculateTotal();
        String error = controller.checkout();

        if (error != null) {
            JOptionPane.showMessageDialog(frame, error, "Transaction Failed", JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(frame,
                String.format("Purchase successful!\nTotal charged: $%.2f\nThank you for your order.", total),
                "Purchase Complete", JOptionPane.INFORMATION_MESSAGE);
            router.showMainMenu();
        }
    }
}