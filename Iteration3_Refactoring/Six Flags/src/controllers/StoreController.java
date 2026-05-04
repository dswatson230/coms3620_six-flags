package controllers;
 
import data.ItemInventory;
import factory.ItemFactory;
import interfaces.Location;
import models.Item;
import validation.InputValidator;
 
import java.util.ArrayList;
 
public class StoreController {
    private ItemInventory inventory;
    private InputValidator validator;
    private ItemFactory factory;
 
    private static final Object[][] ITEM_TEMPLATES = {
        { "General Admission",  39.99 },
        { "Gold Pass",          89.99 },
        { "Burger Combo",        9.99 },
        { "Hot Dog",             5.99 },
        { "Nachos",              6.99 },
        { "Funnel Cake",         7.49 },
        { "Pizza Slice",         5.49 },
        { "Lemonade",            3.99 },
        { "BBQ Sandwich",        8.99 },
        { "Cotton Candy",        3.49 },
        { "Popcorn",             4.49 },
        { "Soft Pretzel",        5.99 },
        { "Superman Ride Pass", 15.00 },
        { "Goliath Ride Pass",  15.00 },
        { "Iron Rattler Pass",  15.00 },
        { "Gold Striker Pass",  15.00 },
        { "Mindbender Pass",    15.00 },
        { "Comet Ride Pass",    15.00 },
    };
 
    public StoreController(ItemInventory inventory) {
        this.inventory = inventory;
        this.validator = new InputValidator();
        this.factory   = new ItemFactory();
    }
 
    public Object[][] getItemTemplates() {
        return ITEM_TEMPLATES;
    }
 
    public ArrayList<Item> getAllItems() {
        return inventory.getAllItems();
    }
 
    public int getQuantityFor(String name, Location location) {
        return inventory.getQuantityFor(name, location);
    }
 
    public int increaseQuantity(String name, double price, Location location, int amount) {
        return inventory.increaseQuantity(name, price, location, amount);
    }
 
    // Returns null on success, error message on failure
    public String storeItem(String name, String priceInput, String quantityInput, String locationInput) {
        double price;
        int quantity;
        Location location;
        String validatedName;
        try {
            validatedName = validator.validateName(name);
            price         = validator.validatePrice(priceInput);
            quantity      = validator.validateQuantity(quantityInput);
            location      = validator.validateLocation(locationInput);
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        }
 
        Item item = factory.createInventoryItem(validatedName, price, location, quantity);
        if (!inventory.addItem(item)) {
            return "Item already exists at this location.";
        }
        return null;
    }
}
 