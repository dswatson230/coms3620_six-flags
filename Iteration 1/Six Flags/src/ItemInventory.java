import interfaces.ItemInventoryInterface;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ItemInventory implements ItemInventoryInterface {
    /// TEMP
    static ArrayList<String> items = new ArrayList<>(List.of("Option 1", "Option 2", "Option 3"));

    public ArrayList<String> getItems() {
        return items;
    }

    /// TEMPPPPP
    public static ArrayList<String> findItemsByLocationDate(Location location, LocalDate date) {
        return items;
    }

    /*
    public boolean addItem(Item item) {
        items.add(item);
        return true;
    }

    public boolean removeItem(Item item) {
        if (items.contains(item)) {
            items.remove(item);
            return true;
        }
        return false;
    }
    */

    public boolean verifyQuantity(Item item, int quantity) {
        return true;
    }

}
