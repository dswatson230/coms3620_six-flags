package data;
 
import interfaces.ItemInventoryInterface;
import interfaces.types.ItemType;
import interfaces.types.Location;
import models.item.CartItem;
import models.item.InventoryItem;

import java.util.ArrayList;
import java.util.List;
 
public class ItemInventory implements ItemInventoryInterface {
    private ArrayList<InventoryItem> inventory;
    private ItemFileHandler fileHandler;
 
    public ItemInventory() {
        this.fileHandler = new ItemFileHandler();
        this.inventory   = fileHandler.readItems();
    }
 
    @Override
    public List<String> getItems() {
        List<String> names = new ArrayList<>();
        for (InventoryItem item : inventory) {
            names.add(item.getInfo());
        }
        return names;
    }
 
    public ArrayList<InventoryItem> getAllItems() {
        this.inventory = fileHandler.readItems();
        return inventory;
    }
 
    public boolean verifyQuantity(InventoryItem item, int quantity) {
        return item.getQuantity() >= quantity;
    }
 
    public int getQuantityFor(String name, Location location) {
        for (InventoryItem item : inventory) {
            if (item.getName().equalsIgnoreCase(name) && item.getLocation() == location) {
                return item.getQuantity();
            }
        }
        return 0;
    }
 
    // Returns name of first cart item that can't be fulfilled, or null if all OK
    public String validateAllReductions(List<CartItem> cartItems) {
        for (CartItem cartItem : cartItems) {
            int available = getQuantityFor(cartItem.getName(), cartItem.getLocation());
            if (available < cartItem.getQuantity()) {
                return cartItem.getName();
            }
        }
        return null;
    }
 
    // Call only after validateAllReductions returns null
    public void commitAllReductions(List<CartItem> cartItems) {
        for (CartItem cartItem : cartItems) {
            for (int i = 0; i < inventory.size(); i++) {
                InventoryItem inv = inventory.get(i);
                if (inv.getType() == cartItem.getType() &&
                    inv.getName().equalsIgnoreCase(cartItem.getName()) &&
                    inv.getLocation() == cartItem.getLocation()) {
                    int newQty = inv.getQuantity() - cartItem.getQuantity();
                    inventory.set(i, new InventoryItem(inv.getItem(), inv.getType(), inv.getLocation(), newQty));
                    break;
                }
            }
        }
        fileHandler.writeAllItems(inventory);
    }
 
    public int increaseQuantity(ItemType type, String name, double price, Location location, int amount) {
        for (int i = 0; i < inventory.size(); i++) {
            InventoryItem item = inventory.get(i);
            if (item.getType() == type &&
                item.getName().equalsIgnoreCase(name) &&
                item.getLocation() == location) {
                int newQuantity = item.getQuantity() + amount;
                inventory.set(i, new InventoryItem(item.getItem(), type, location, newQuantity));
                fileHandler.writeAllItems(inventory);
                return newQuantity;
            }
        }
        InventoryItem newItem = new InventoryItem(type.create(name, price), type, location, amount);
        inventory.add(newItem);
        fileHandler.writeItem(newItem);
        return amount;
    }
 
    public boolean addItem(InventoryItem item) {
        for (InventoryItem existing : inventory) {
            if (existing.getName().equalsIgnoreCase(item.getName()) &&
                existing.getLocation() == item.getLocation()) {
                return false;
            }
        }
        inventory.add(item);
        return fileHandler.writeItem(item);
    }
 
    public boolean removeItem(InventoryItem item) {
        if (inventory.contains(item)) {
            inventory.remove(item);
            return true;
        }
        return false;
    }
}