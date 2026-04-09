import interfaces.ItemControllerInterface;

import javax.swing.JOptionPane;
import java.time.LocalDate;
import java.util.ArrayList;

public class ItemController implements ItemControllerInterface {
    private ArrayList<Item> customerCart = new ArrayList<>();

    public boolean purchaseItem() {
        /// TEMPPPP
        Item item;
        String operationNumber = JOptionPane.showInputDialog(
                "Selection a Location \n"+
                        " 1. Magic Mountain (Valencia, CA)\n" +
                        " 2. Fiesta Texas (San Antonio, TX)\n" +
                        " 3. Great America (Gurnee, IL)\n" +
                        " 4. Over Georgia (Austell, GA)\n" +
                        " 5. The Great Escape (Queensbury, NY)");

        int code = Integer.parseInt(operationNumber);
        Location location = Location.values()[code];

        String input = JOptionPane.showInputDialog("Enter Desired Date (MM/DD/YYYY):");
        String [] dateInfo = input.split("/");
        LocalDate date = LocalDate.of(Integer.parseInt(dateInfo[2]), Integer.parseInt(dateInfo[0]), Integer.parseInt(dateInfo[1]));

        ///TEMPPPP
        ArrayList<String> items = ItemInventory.findItemsByLocationDate(location, date);

        String output = "Select Ticket:\n";
        for (int i = 0; i < items.size(); i++) {
            output += " " + (i+1) + ". " + items.get(i) + "\n";
        }
        item = setItemType(items.get(Integer.parseInt(JOptionPane.showInputDialog(output))), location);

        int quantity = Integer.parseInt(JOptionPane.showInputDialog("Enter Quantity of Tickets to Purchase:"));
        boolean b = new ItemInventory().verifyQuantity(item, quantity);

        if (!b)
            return false;

        operationNumber = JOptionPane.showInputDialog("Verify Item Info:\n" +
                                    item.getInfo() +
                                    "\n 1. Confirm! Add to Cart\n" +
                                    " 2. Go Back");

        code = Integer.parseInt(operationNumber);
        if (code == 1) {
            customerCart.add(item);
            operationNumber = JOptionPane.showInputDialog("Successfully Added to Cart!\n" +
                                                            " 1. Continue Shopping\n" +
                                                            " 2. Go to Cart");
        }

        code = Integer.parseInt(operationNumber);
        if (code == 2) {
            output = "Cart:\n";
            for (Item i: customerCart) {
                output += i.getInfo();
            }
            operationNumber = JOptionPane.showInputDialog(output +
                                        "\n 1. Continue Shopping" +
                                        "\n 2. Remove Item" +
                                        "\n 3. Checkout");
        }

        code = Integer.parseInt(operationNumber);
        if (code == 2) {
            operationNumber = JOptionPane.showInputDialog("Select Item to Remove:\n" + output);
            customerCart.remove(Integer.parseInt(operationNumber));
        }
        if (code == 3) {
            JOptionPane.showInputDialog("Total: " + calculatePrice() +
                    "\nInput Card Info");
        }

        /// Remove Quantity from Inventory?

        sendConfirmation();

        return true;
    }

    private double calculatePrice() {
        double price = 0;
        for (Item item: customerCart) {
            price += item.getPrice();
            /// Quantity?
        }
        return price;
    }

    private void sendConfirmation() {
        System.out.println("CONFIRMATION TEST");
    }

    private Item setItemType(String s, Location location) {
        return new Ticket("Ticket", 39.99, location);
    }
}
