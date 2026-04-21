package validation;
 
import interfaces.Location;
 
public class InputValidator {
    public double validatePrice(String input) throws IllegalArgumentException {
        double price = Double.parseDouble(input);
        if (price < 0) throw new IllegalArgumentException("Price cannot be negative.");
        return price;
    }
 
    public int validateQuantity(String input) throws IllegalArgumentException {
        int quantity = Integer.parseInt(input);
        if (quantity <= 0) throw new IllegalArgumentException("Quantity must be greater than zero.");
        return quantity;
    }
 
    public Location validateLocation(String input) throws IllegalArgumentException {
        int index = Integer.parseInt(input) - 1;
        if (index < 0 || index >= Location.values().length)
            throw new IllegalArgumentException("Invalid location selection.");
        return Location.values()[index];
    }
 
    public String validateName(String input) throws IllegalArgumentException {
        if (input == null || input.isBlank())
            throw new IllegalArgumentException("Name cannot be empty.");
        return input;
    }
}
 