package models;
 
import interfaces.ItemStatus;
import interfaces.Location;
 
public class InventoryItem extends Item {
    private int quantity;
 
    public InventoryItem(String name, double price, Location location, int quantity) {
        super(name, price, location);
        this.quantity = quantity;
    }
 
    @Override 
    public int getQuantity() { return quantity; }
 
    @Override
    public String getInfo() {
        return "Name: " + getName() + " | Price: $" + getPrice() + " | Quantity: " + quantity + " | Location: " + getLocation() + " | Status: " + getStatus();
    }
}
 