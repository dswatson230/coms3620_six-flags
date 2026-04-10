package interfaces;

import interfaces.ItemConfirmationInterface;
import javax.swing.JOptionPane;
 
public class ItemConfirmation implements ItemConfirmationInterface {
 
    @Override
    public void confirmError(String reason) {
        JOptionPane.showMessageDialog(null,
            "Operation failed.\nReason: " + reason,
            "Error",
            JOptionPane.ERROR_MESSAGE);
    }
}
 