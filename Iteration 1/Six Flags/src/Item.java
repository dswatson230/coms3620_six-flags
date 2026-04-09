import interfaces.ItemInterface;

public abstract class Item implements ItemInterface {
    private String name;
    private double price;
    private Location location;

    Item(String name, double price, Location location) {
        this.name = name;
        this.price = price;
        this.location = location;
    }

    public double getPrice() {
        return price;
    }

    public String getInfo() {
        return "Name: " + name + "\nLocation: " + location + "\nPrice: " + price;
    }
}
