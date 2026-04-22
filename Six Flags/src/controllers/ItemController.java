package controllers;

import data.ItemInventory;
import data.PurchaseFileHandler;
import interfaces.ItemControllerInterface;
import interfaces.Location;
import models.Item;

import java.util.ArrayList;

public class ItemController implements ItemControllerInterface {
    private static final String PURCHASE_FILE_PATH = "data/purchases.txt";

    private PurchaseController purchaseController;
    private StoreController    storeController;
    private final ItemInventory    sharedInventory;

    public ItemController() {
        ItemInventory       sharedInventory     = new ItemInventory();
        PurchaseFileHandler purchaseFileHandler = new PurchaseFileHandler(PURCHASE_FILE_PATH);
        this.purchaseController = new PurchaseController(sharedInventory, purchaseFileHandler);
        this.storeController    = new StoreController(sharedInventory);
        this.sharedInventory = new ItemInventory();
    }

    @Override
    public boolean purchaseItem() {
        return true;
    }

    @Override
    public boolean storeItem(String name, String priceInput, String quantityInput, String locationInput) {
        return storeController.storeItem(name, priceInput, quantityInput, locationInput) == null;
    }

    public Object[][] getItemTemplates() {
        return storeController.getItemTemplates();
    }

    public ArrayList<Item> getAllItems() {
        return storeController.getAllItems();
    }

    public int getQuantityFor(String name, Location location) {
        return storeController.getQuantityFor(name, location);
    }

    public int increaseQuantity(String name, double price, Location location, int amount) {
        return storeController.increaseQuantity(name, price, location, amount);
    }

    public String storeItemWithFeedback(String name, String priceInput, String quantityInput, String locationInput) {
        return storeController.storeItem(name, priceInput, quantityInput, locationInput);
    }

    public ArrayList<Item> getAvailableItemsByLocation(Location location) {
        return purchaseController.getAvailableItems(location);
    }

    public String addToCart(Item item, int quantity) {
        return purchaseController.addToCart(item, quantity);
    }

    public void removeFromCart(int index) {
        purchaseController.removeFromCart(index);
    }

    public String checkout() {
        return purchaseController.checkout();
    }

    public double calculateTotal() {
        return purchaseController.calculateTotal();
    }

    public String getCartInfo() {
        return purchaseController.getCartInfo();
    }

    public boolean isCartEmpty() {
        return purchaseController.isCartEmpty();
    }
        public ItemInventory getSharedInventory() { 
        return sharedInventory; 
    }
}
