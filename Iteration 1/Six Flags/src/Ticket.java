public class Ticket extends Item {
    private Park park;

    Ticket(String name, double price, Location location) {
        super(name, price, location);
    }

    public void setPark(Park park) {
        this.park = park;
    }
}
