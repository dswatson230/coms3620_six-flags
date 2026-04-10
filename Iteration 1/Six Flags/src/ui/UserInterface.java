package ui;
 
import controllers.ItemController;
 
import javax.swing.*;
import java.awt.*;
 
public class UserInterface {
    private JFrame frame;
    private ItemController controller;
    private PurchaseUI purchaseUI;
    private InventoryUI inventoryUI;
 
    public UserInterface() {
        this.controller  = new ItemController();
        this.frame       = new JFrame("Six Flags Management System");
        this.purchaseUI  = new PurchaseUI(frame, controller, this);
        this.inventoryUI = new InventoryUI(frame, controller, this);
    }
 
    public void start() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);
        showMainMenu();
        frame.setVisible(true);
    }
 
    public void showMainMenu() {
        JPanel panel = new JPanel(new GridLayout(3, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(40, 80, 40, 80));
 
        JLabel title = new JLabel("Welcome to Six Flags!", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
 
        JButton purchaseBtn  = new JButton("Purchase");
        JButton inventoryBtn = new JButton("Inventory");
 
        purchaseBtn.addActionListener(e  -> purchaseUI.showLocationSelection());
        inventoryBtn.addActionListener(e -> inventoryUI.showInventoryMenu());
 
        panel.add(title);
        panel.add(purchaseBtn);
        panel.add(inventoryBtn);
 
        setPanel(panel);
    }
 
    public void setPanel(JPanel panel) {
        frame.setContentPane(panel);
        frame.revalidate();
        frame.repaint();
    }
 
    public void setContentPane(java.awt.Container pane) {
        frame.setContentPane(pane);
        frame.revalidate();
        frame.repaint();
    }
}