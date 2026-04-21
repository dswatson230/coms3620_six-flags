package ui;
 
import controllers.ItemController;
import interfaces.Location;
import models.Item;
 
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
 
public class InventoryUI {
    private JFrame frame;
    private ItemController controller;
    private UserInterface router;
 
    private static final String[] LOCATION_NAMES = {
        "Magic Mountain", "Fiesta Texas", "Great America",
        "Over Georgia", "The Great Escape"
    };
 
    public InventoryUI(JFrame frame, ItemController controller, UserInterface router) {
        this.frame      = frame;
        this.controller = controller;
        this.router     = router;
    }
 
    public void showInventoryMenu() {
        JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(40, 80, 40, 80));
 
        JLabel label = new JLabel("Inventory Management", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 18));
 
        JButton addBtn  = new JButton("Add Item");
        JButton viewBtn = new JButton("View Inventory");
        JButton backBtn = new JButton("Back");
 
        addBtn.addActionListener(e  -> showLocationSelection());
        viewBtn.addActionListener(e -> showViewInventory());
        backBtn.addActionListener(e -> router.showMainMenu());
 
        panel.add(label);
        panel.add(addBtn);
        panel.add(viewBtn);
        panel.add(backBtn);
 
        router.setPanel(panel);
    }
 
    private void showViewInventory() {
        ArrayList<Item> allItems = controller.getAllItems();
 
        JPanel outer = new JPanel(new BorderLayout());
 
        JLabel label = new JLabel("Current Inventory", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        label.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        outer.add(label, BorderLayout.NORTH);
 
        String[] columns = { "Name", "Price", "Quantity", "Location" };
        Object[][] data = new Object[allItems.size()][4];
        for (int i = 0; i < allItems.size(); i++) {
            Item item = allItems.get(i);
            data[i][0] = item.getName();
            data[i][1] = String.format("$%.2f", item.getPrice());
            data[i][2] = item.getQuantity();
            data[i][3] = item.getLocation().name().replace("_", " ");
        }
 
        JTable table = new JTable(data, columns) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        table.setFillsViewportHeight(true);
        table.getTableHeader().setReorderingAllowed(false);
 
        JScrollPane scroll = new JScrollPane(table);
        outer.add(scroll, BorderLayout.CENTER);
 
        JButton backBtn = new JButton("Back");
        backBtn.addActionListener(e -> showInventoryMenu());
        JPanel south = new JPanel();
        south.add(backBtn);
        outer.add(south, BorderLayout.SOUTH);
 
        router.setContentPane(outer);
    }
 
    private void showLocationSelection() {
        JPanel panel = new JPanel(new GridLayout(7, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 60, 20, 60));
 
        JLabel label = new JLabel("Select a Location:", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(label);
 
        Location[] locations = Location.values();
        for (int i = 0; i < locations.length; i++) {
            JButton btn = new JButton(LOCATION_NAMES[i]);
            Location loc = locations[i];
            btn.addActionListener(e -> showItemTemplates(loc));
            panel.add(btn);
        }
 
        JButton backBtn = new JButton("Back");
        backBtn.addActionListener(e -> showInventoryMenu());
        panel.add(backBtn);
 
        router.setPanel(panel);
    }
 
    private void showItemTemplates(Location location) {
        Object[][] templates = controller.getItemTemplates();
 
        JPanel outer = new JPanel(new BorderLayout());
 
        JLabel label = new JLabel(
            "Select Item to Add at " + location.name().replace("_", " ") + ":",
            SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        label.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        outer.add(label, BorderLayout.NORTH);
 
        JPanel btnPanel = new JPanel(new GridLayout(0, 2, 8, 8));
        btnPanel.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 20));
 
        for (Object[] template : templates) {
            String name    = (String) template[0];
            double price   = (double) template[1];
            int currentQty = controller.getQuantityFor(name, location);
 
            JButton btn = new JButton(name + " ($" + price + ") — In Stock: " + currentQty);
            btn.addActionListener(e -> showQuantityInput(name, price, location, currentQty));
            btnPanel.add(btn);
        }
 
        JScrollPane scroll = new JScrollPane(btnPanel);
        scroll.setBorder(null);
        outer.add(scroll, BorderLayout.CENTER);
 
        JButton backBtn = new JButton("Back");
        backBtn.addActionListener(e -> showLocationSelection());
        JPanel south = new JPanel();
        south.add(backBtn);
        outer.add(south, BorderLayout.SOUTH);
 
        router.setContentPane(outer);
    }
 
    private void showQuantityInput(String name, double price, Location location, int currentQty) {
        String quantityInput = JOptionPane.showInputDialog(frame,
            name + "\nCurrent stock: " + currentQty + "\n\nEnter quantity to add:");
        if (quantityInput == null) return;
 
        int amount;
        try {
            amount = Integer.parseInt(quantityInput.trim());
            if (amount <= 0) {
                JOptionPane.showMessageDialog(frame, "Quantity must be greater than zero.",
                    "Invalid Input", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Please enter a valid number.",
                "Invalid Input", JOptionPane.ERROR_MESSAGE);
            return;
        }
 
        int newTotal = controller.increaseQuantity(name, price, location, amount);
 
        JOptionPane.showMessageDialog(frame,
            "✓ " + name + " updated!\nAdded: " + amount + "\nNew total stock: " + newTotal,
            "Inventory Updated", JOptionPane.INFORMATION_MESSAGE);
 
        showItemTemplates(location);
    }
}