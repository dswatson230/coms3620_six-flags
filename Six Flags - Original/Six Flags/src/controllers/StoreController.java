package controllers;
 
import data.ItemInventory;
import factory.ItemFactory;
import interfaces.types.ItemType;
import interfaces.types.Location;
import models.item.InventoryItem;
import validation.InputValidator;
 
import java.util.ArrayList;
 
public class StoreController {
    private ItemInventory inventory;
    private InputValidator validator;
    private ItemFactory factory;
 
    private static final Object[][] ITEM_TEMPLATES = {
        { ItemType.DAILY_TICKET, "General Admission",  39.99 },
        { ItemType.SEASON_PASS,  "Gold Pass",          89.99 },
        { ItemType.CONCESSION,   "Burger Combo",        9.99 },
        { ItemType.CONCESSION,   "Hot Dog",             5.99 },
        { ItemType.CONCESSION,   "Nachos",              6.99 },
        { ItemType.CONCESSION,   "Funnel Cake",         7.49 },
        { ItemType.CONCESSION,   "Pizza Slice",         5.49 },
        { ItemType.CONCESSION,   "Lemonade",            3.99 },
        { ItemType.CONCESSION,   "BBQ Sandwich",        8.99 },
        { ItemType.CONCESSION,   "Cotton Candy",        3.49 },
        { ItemType.CONCESSION,   "Popcorn",             4.49 },
        { ItemType.CONCESSION,   "Soft Pretzel",        5.99 },
        { ItemType.DAILY_TICKET, "Superman Ride Pass", 15.00 },
        { ItemType.DAILY_TICKET, "Goliath Ride Pass",  15.00 },
        { ItemType.DAILY_TICKET, "Iron Rattler Pass",  15.00 },
        { ItemType.DAILY_TICKET, "Gold Striker Pass",  15.00 },
        { ItemType.DAILY_TICKET, "Mindbender Pass",    15.00 },
        { ItemType.DAILY_TICKET, "Comet Ride Pass",    15.00 },
    };
 
    public StoreController(ItemInventory inventory) {
        this.inventory = inventory;
        this.validator = new InputValidator();
        this.factory   = new ItemFactory();
    }
 
    public Object[][] getItemTemplates() {
        return ITEM_TEMPLATES;
    }
 
    public ArrayList<InventoryItem> getAllItems() {
        return inventory.getAllItems();
    }
 
    public int getQuantityFor(String name, Location location) {
        return inventory.getQuantityFor(name, location);
    }
 
    public int increaseQuantity(ItemType type, String name, double price, Location location, int amount) {
        return inventory.increaseQuantity(type, name, price, location, amount);
    }
 
    // Returns null on success, error message on failure
    public String storeItem(String name, String priceInput, String quantityInput, String locationInput) {
        return storeItem(ItemType.DAILY_TICKET, name, priceInput, quantityInput, locationInput);
    }

    public String storeItem(ItemType type, String name, String priceInput, String quantityInput, String locationInput) {
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
 
        InventoryItem item = factory.createInventoryItem(type, validatedName, price, location, quantity);
        if (!inventory.addItem(item)) {
            return "Item already exists at this location.";
        }
        return null;
    }
}
 