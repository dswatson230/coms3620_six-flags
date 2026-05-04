package factory;
 
import interfaces.Location;
import models.InventoryItem;
import models.Item;
 
public class ItemFactory {
    public Item createInventoryItem(String name, double price, Location location, int quantity) {
        return new InventoryItem(name, price, location, quantity);
    }
}
 