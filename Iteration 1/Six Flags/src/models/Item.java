package models;
 
import interfaces.ItemInterface;
import interfaces.ItemStatus;
import interfaces.Location;
 
public abstract class Item implements ItemInterface {
    private String name;
    private double price;
    private Location location;
    private ItemStatus status;
 
    public Item(String name, double price, Location location) {
        this.name     = name;
        this.price    = price;
        this.location = location;
        this.status   = ItemStatus.VALID;
    }
 
    @Override public String getName()       { return name; }
    @Override public double getPrice()      { return price; }
    @Override public Location getLocation() { return location; }
    @Override public ItemStatus getStatus() { return status; }
    @Override public abstract int getQuantity();
 
    public void setStatus(ItemStatus status) { this.status = status; }
 
    @Override
    public String getInfo() {
        return "Name: " + name + " | Location: " + location + " | Price: $" + price + " | Status: " + status;
    }
}
 