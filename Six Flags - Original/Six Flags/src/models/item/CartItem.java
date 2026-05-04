package models.item;

import interfaces.ItemInterface;
import interfaces.types.ItemStatus;
import interfaces.types.ItemType;
import interfaces.types.Location;

import java.util.List;

public class CartItem {
    private final ItemInterface item;
    private final ItemType type;
    private final Location location;
    private int quantity;
    private ItemStatus status;

    public CartItem(ItemInterface item, ItemType type, Location location, int quantity) {
        this.item = item;
        this.type = type;
        this.location = location;
        this.quantity = quantity;
        this.status = ItemStatus.VALID;
    }

    public ItemInterface getItem() { return item; }
    public ItemType getType() { return type; }
    public String getName() { return item.getName(); }
    public double getPrice() { return item.getPrice(); }
    public List<String> getBenefits() { return item.getBenefits(); }
    public Location getLocation() { return location; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public ItemStatus getStatus() { return status; }
    public void setStatus(ItemStatus status) { this.status =status; }

    public String getInfo() {
        return item.getInfo() + " | Location: " + location.name().replace("_", " ") +
            " | Quantity: " + quantity + " | Status: " + status;
    }
}
