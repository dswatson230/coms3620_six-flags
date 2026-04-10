package ui;
 
import models.Item;
import models.ItemConfirmation;
 
import javax.swing.*;
import java.awt.*;
import java.util.List;
 
public class ValidationUI {
    private final JFrame frame;
    private final UserInterface router;
    private final ItemConfirmation itemConfirmation;
 
    public ValidationUI(JFrame frame, UserInterface router) {
        this.frame            = frame;
        this.router           = router;
        this.itemConfirmation = new ItemConfirmation();
    }
 
    public void showValidationMenu() {
        JPanel panel = new JPanel(new GridLayout(3, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(40, 80, 40, 80));
        JLabel label = new JLabel("Item Validation", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 18));
        JButton validateBtn = new JButton("Validate Item");
        JButton backBtn     = new JButton("Back");
        validateBtn.addActionListener(e -> showItemSelection());
        backBtn.addActionListener(e     -> router.showMainMenu());
        panel.add(label); panel.add(validateBtn); panel.add(backBtn);
        router.setPanel(panel);
    }
 
    private void showItemSelection() {
        List<Item>   items = itemConfirmation.getDataProvider().getAllItems();
        List<String> ids   = itemConfirmation.getDataProvider().getAllIds();
 
        JPanel panel = new JPanel(new GridLayout(items.size() + 2, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 60, 20, 60));
        JLabel label = new JLabel("Select Item to Validate:", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(label);
 
        for (int i = 0; i < items.size(); i++) {
            Item item   = items.get(i);
            String id   = ids.get(i);
            JButton btn = new JButton(item.getName() + " — " + item.getLocation() + " (" + item.getStatus() + ")");
            btn.addActionListener(e -> validateItem(id));
            panel.add(btn);
        }
 
        JButton back = new JButton("Back");
        back.addActionListener(e -> showValidationMenu());
        panel.add(back);
        router.setPanel(panel);
    }
 
    private void validateItem(String itemId) {
        boolean ok    = itemConfirmation.validateItem(itemId);
        String result = itemConfirmation.getGenMessage();
        Item item     = itemConfirmation.getItem();
        String display = ok && item != null ? result + "\n\n" + item.getInfo() : result;
        JOptionPane.showMessageDialog(frame, display, "Validation Result",
            ok ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE);
        showItemSelection();
    }
}