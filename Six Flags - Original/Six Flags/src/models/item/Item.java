package models.item;
 
import interfaces.ItemInterface;
import interfaces.types.ItemType;

import java.util.ArrayList;
import java.util.List;

public abstract class Item implements ItemInterface {
    private String name;
    private double price;
    private List<String> benefits;
 
    public Item(String name, double price) {
        this.name = name;
        this.price = price;
        this.benefits = new ArrayList<>();
    }

    public String getName() { return name; }
    public double getPrice() { return price; }

    public String getInfo() { return "Type: " + getType() + " | Name: " + getName() + " | Price: $" + getPrice(); }
    public List<String> getBenefits() { return benefits; }

    public void setBenefits(List<String> benefits) { this.benefits = benefits; }

    @Override
    public String toString() {
        return getInfo();
    }

    public abstract ItemType getType();
}
 