package models;
 
import interfaces.ItemInterface;
import interfaces.Location;
 
public abstract class Item implements ItemInterface {
    private String name;
    private double price;
    private Location location;
 
    public Item(String name, double price, Location location) {
        this.name     = name;
        this.price    = price;
        this.location = location;
    }
 
    @Override public String getName()     { return name; }
    @Override public double getPrice()    { return price; }
    @Override public Location getLocation() { return location; }
    @Override public abstract int getQuantity();
 
    @Override
    public String getInfo() {
        return "Name: " + name + " | Location: " + location + " | Price: $" + price;
    }
}