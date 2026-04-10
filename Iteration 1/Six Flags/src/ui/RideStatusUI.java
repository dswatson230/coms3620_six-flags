package ui;

import controllers.RideStatusController;
import models.Ride;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class RideStatusUI {
    private final JFrame frame;
    private final UserInterface router;
    private final RideStatusController controller;

    private static final String[] STATUSES = {"OPEN", "CLOSED", "MAINTENANCE"};

    public RideStatusUI(JFrame frame, UserInterface router) {
        this.frame      = frame;
        this.router     = router;
        this.controller = new RideStatusController();
    }

    public void showRideMenu() {
        JPanel panel = new JPanel(new GridLayout(3, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(40, 80, 40, 80));
        JLabel label = new JLabel("Ride Status Management", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 18));
        JButton updateBtn = new JButton("Update Ride Status");
        JButton backBtn   = new JButton("Back");
        updateBtn.addActionListener(e -> showRideSelection());
        backBtn.addActionListener(e   -> router.showMainMenu());
        panel.add(label); panel.add(updateBtn); panel.add(backBtn);
        router.setPanel(panel);
    }

    private void showRideSelection() {
        List<Ride> rides = controller.loadRides();

        JPanel panel = new JPanel(new GridLayout(rides.size() + 2, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 60, 20, 60));
        JLabel label = new JLabel("Select a Ride:", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(label);

        if (rides.isEmpty()) {
            panel.add(new JLabel("No rides found in data/rides.txt.", SwingConstants.CENTER));
        } else {
            for (Ride ride : rides) {
                JButton btn = new JButton(ride.getName() + " — " + ride.getLocation() + " [" + ride.getStatus() + "]");
                btn.addActionListener(e -> showUpdateForm(ride));
                panel.add(btn);
            }
        }

        JButton back = new JButton("Back");
        back.addActionListener(e -> showRideMenu());
        panel.add(back);
        router.setPanel(panel);
    }

    private void showUpdateForm(Ride ride) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JLabel title = new JLabel("Update: " + ride.getName(), SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 16));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel currentLabel = new JLabel("Current Status: " + ride.getStatus());
        currentLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Status dropdown
        JPanel statusRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusRow.add(new JLabel("New Status:"));
        JComboBox<String> statusBox = new JComboBox<>(STATUSES);
        statusBox.setSelectedItem(ride.getStatus());
        statusRow.add(statusBox);

        // Reason input
        JPanel reasonRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        reasonRow.add(new JLabel("Reason:     "));
        JTextField reasonField = new JTextField(20);
        reasonRow.add(reasonField);

        // Buttons
        JButton submitBtn = new JButton("Submit");
        JButton backBtn   = new JButton("Back");
        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        btnRow.add(backBtn); btnRow.add(submitBtn);

        panel.add(title);
        panel.add(Box.createVerticalStrut(8));
        panel.add(currentLabel);
        panel.add(Box.createVerticalStrut(12));
        panel.add(statusRow);
        panel.add(reasonRow);
        panel.add(Box.createVerticalStrut(15));
        panel.add(btnRow);

        submitBtn.addActionListener(e -> {
            String newStatus = (String) statusBox.getSelectedItem();
            String reason    = reasonField.getText().trim();
            String error     = controller.updateRideStatus(ride.getRideID(), newStatus, reason);
            if (error != null) {
                JOptionPane.showMessageDialog(frame, error, "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(frame,
                    ride.getName() + " status updated to " + newStatus + ".",
                    "Update Confirmed", JOptionPane.INFORMATION_MESSAGE);
                showRideSelection();
            }
        });
        backBtn.addActionListener(e -> showRideSelection());

        router.setPanel(panel);
    }
}