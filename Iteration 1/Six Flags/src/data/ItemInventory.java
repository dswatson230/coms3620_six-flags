package data;
 
import interfaces.ItemInventoryInterface;
import interfaces.Location;
import models.InventoryItem;
import models.Item;
 
import java.util.ArrayList;
import java.util.List;
 
public class ItemInventory implements ItemInventoryInterface {
    private ArrayList<Item> inventory;
    private ItemFileHandler fileHandler;
 
    public ItemInventory() {
        this.fileHandler = new ItemFileHandler();
        this.inventory   = fileHandler.readItems();
    }
 
    @Override
    public List<String> getItems() {
        List<String> names = new ArrayList<>();
        for (Item item : inventory) {
            names.add(item.getInfo());
        }
        return names;
    }
 
    public ArrayList<Item> getAllItems() {
        return inventory;
    }
 
    public boolean verifyQuantity(Item item, int quantity) {
        return item.getQuantity() >= quantity;
    }
 
    public int getQuantityFor(String name, Location location) {
        for (Item item : inventory) {
            if (item.getName().equalsIgnoreCase(name) && item.getLocation() == location) {
                return item.getQuantity();
            }
        }
        return 0;
    }
 
    // Returns name of first cart item that can't be fulfilled, or null if all OK
    public String validateAllReductions(ArrayList<Item> cartItems) {
        for (Item cartItem : cartItems) {
            int available = getQuantityFor(cartItem.getName(), cartItem.getLocation());
            if (available < cartItem.getQuantity()) {
                return cartItem.getName();
            }
        }
        return null;
    }
 
    // Call only after validateAllReductions returns null
    public void commitAllReductions(ArrayList<Item> cartItems) {
        for (Item cartItem : cartItems) {
            for (int i = 0; i < inventory.size(); i++) {
                Item inv = inventory.get(i);
                if (inv.getName().equalsIgnoreCase(cartItem.getName()) &&
                    inv.getLocation() == cartItem.getLocation()) {
                    int newQty = inv.getQuantity() - cartItem.getQuantity();
                    inventory.set(i, new InventoryItem(inv.getName(), inv.getPrice(), inv.getLocation(), newQty));
                    break;
                }
            }
        }
        fileHandler.writeAllItems(inventory);
    }
 
    public int increaseQuantity(String name, double price, Location location, int amount) {
        for (int i = 0; i < inventory.size(); i++) {
            Item item = inventory.get(i);
            if (item.getName().equalsIgnoreCase(name) && item.getLocation() == location) {
                int newQuantity = item.getQuantity() + amount;
                inventory.set(i, new InventoryItem(name, item.getPrice(), location, newQuantity));
                fileHandler.writeAllItems(inventory);
                return newQuantity;
            }
        }
        Item newItem = new InventoryItem(name, price, location, amount);
        inventory.add(newItem);
        fileHandler.writeItem(newItem);
        return amount;
    }
 
    public boolean addItem(Item item) {
        for (Item existing : inventory) {
            if (existing.getName().equalsIgnoreCase(item.getName()) &&
                existing.getLocation() == item.getLocation()) {
                return false;
            }
        }
        inventory.add(item);
        return fileHandler.writeItem(item);
    }
 
    public boolean removeItem(Item item) {
        if (inventory.contains(item)) {
            inventory.remove(item);
            return true;
        }
        return false;
    }
}