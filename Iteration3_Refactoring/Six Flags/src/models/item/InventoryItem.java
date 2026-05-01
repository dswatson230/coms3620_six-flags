package models.item;

import interfaces.ItemInterface;
import interfaces.types.ItemType;
import interfaces.types.Location;

import java.util.List;

public class InventoryItem {
    private final ItemInterface item;
    private ItemType type;
    private Location location;
    private int quantity;

    public InventoryItem(ItemInterface item, ItemType type, Location location, int quantity) {
        this.item = item;
        this.type = type;
        this.location = location;
        this.quantity = quantity;
    }

    public ItemInterface getItem() { return item; }
    public ItemType getType() { return type; }
    public String getName() { return item.getName(); }
    public double getPrice() { return item.getPrice(); }
    public List<String> getBenefits() { return item.getBenefits(); }
    public Location getLocation() { return location; }
    public void setLocation(Location location) {this.location = location; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) {this.quantity = quantity; }

    public String getInfo() {
        return item.getInfo() + " | Location: " + location.name().replace("_", " ") + " | Quantity: " + quantity;
    }

    @Override
    public String toString() {
        return getInfo();
    }
}
