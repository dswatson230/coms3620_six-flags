package controllers;
 
import data.ItemInventory;
import factory.ItemFactory;
import interfaces.Location;
import models.CartManager;
import models.Item;
import validation.InputValidator;
 
import java.util.ArrayList;
 
public class PurchaseController {
    private ItemInventory inventory;
    private CartManager cartManager;
    private InputValidator validator;
    private ItemFactory factory;
 
    public PurchaseController(ItemInventory inventory) {
        this.inventory   = inventory;
        this.cartManager = new CartManager();
        this.validator   = new InputValidator();
        this.factory     = new ItemFactory();
    }
 
    public ArrayList<Item> getAvailableItems(Location location) {
        return inventory.getAllItems().stream()
            .filter(i -> i.getLocation() == location && i.getQuantity() > 0)
            .collect(java.util.stream.Collectors.toCollection(ArrayList::new));
    }
 
    // Returns null on success, error message on failure
    public String addToCart(Item selectedItem, int quantity) {
        if (!inventory.verifyQuantity(selectedItem, quantity)) {
            return "Insufficient quantity. Only " + selectedItem.getQuantity() + " available.";
        }
        Item cartItem = factory.createInventoryItem(
            selectedItem.getName(), selectedItem.getPrice(), selectedItem.getLocation(), quantity);
        cartManager.addItem(cartItem);
        return null;
    }
 
    public void removeFromCart(int index) {
        cartManager.removeItem(index);
    }
 
    // Returns null on success, error message if transaction aborted
    public String checkout() {
        ArrayList<Item> cart = cartManager.getCart();
        String failedItem = inventory.validateAllReductions(cart);
        if (failedItem != null) {
            return "Transaction cancelled. Insufficient stock for: " + failedItem +
                   "\nNo changes were made.";
        }
        inventory.commitAllReductions(cart);
        cartManager.clear();
        return null;
    }
 
    public double calculateTotal() {
        return cartManager.calculateTotal();
    }
 
    public String getCartInfo() {
        return cartManager.getCartInfo();
    }
 
    public boolean isCartEmpty() {
        return cartManager.isEmpty();
    }
}