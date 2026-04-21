package ui;
 
import controllers.ItemController;
 
import javax.swing.*;
import java.awt.*;
 
public class UserInterface {
    private JFrame frame;
    private ItemController controller;
    private PurchaseUI purchaseUI;
    private InventoryUI inventoryUI;
    private SchedulingUI schedulingUI;
    private ValidationUI validationUI;
    private RideStatusUI rideStatusUI;
 
    public UserInterface() {
        this.controller   = new ItemController();
        this.frame        = new JFrame("Six Flags Management System");
        this.purchaseUI   = new PurchaseUI(controller, this);
        this.inventoryUI  = new InventoryUI(frame, controller, this);
        this.schedulingUI = new SchedulingUI(frame, this);
        this.validationUI = new ValidationUI(frame, this);
        this.rideStatusUI = new RideStatusUI(frame, this);
    }
 
    public void start() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 450);
        showMainMenu();
        frame.setVisible(true);
    }
 
    public void showMainMenu() {
        JPanel panel = new JPanel(new GridLayout(6, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 80, 30, 80));
 
        JLabel title = new JLabel("Welcome to Six Flags!", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
 
        JButton purchaseBtn  = new JButton("Purchase");
        JButton inventoryBtn = new JButton("Inventory");
        JButton employeeBtn  = new JButton("Employee Scheduling");
        JButton validateBtn  = new JButton("Validate Item");
        JButton rideBtn      = new JButton("Ride Status");
 
        purchaseBtn.addActionListener(e  -> purchaseUI.showLocationSelection());
        inventoryBtn.addActionListener(e -> inventoryUI.showInventoryMenu());
        employeeBtn.addActionListener(e  -> schedulingUI.showSchedulingMenu());
        validateBtn.addActionListener(e  -> validationUI.showValidationMenu());
        rideBtn.addActionListener(e      -> rideStatusUI.showRideMenu());
 
        panel.add(title);
        panel.add(purchaseBtn);
        panel.add(inventoryBtn);
        panel.add(employeeBtn);
        panel.add(validateBtn);
        panel.add(rideBtn);
 
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