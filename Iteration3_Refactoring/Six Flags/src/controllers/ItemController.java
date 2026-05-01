package controllers;

import data.ItemInventory;
import data.PurchaseFileHandler;
import interfaces.ItemControllerInterface;
import interfaces.types.ItemAddOns;
import interfaces.types.ItemType;
import interfaces.types.Location;
import models.item.InventoryItem;
import models.item.decorators.AddOnSelection;

import java.util.ArrayList;
import java.util.List;

public class ItemController implements ItemControllerInterface {
    //private static final String PURCHASE_FILE_PATH = "data/purchases.txt";
    private static final String PURCHASE_FILE_PATH = "C:/Users/longi/COMS/coms3620/six-flags/Six Flags/data/purchases.txt";

    private PurchaseController purchaseController;
    private StoreController    storeController;
    private final ItemInventory    sharedInventory;

    public ItemController() {
        this.sharedInventory     = new ItemInventory();
        PurchaseFileHandler purchaseFileHandler = new PurchaseFileHandler(PURCHASE_FILE_PATH);
        this.purchaseController = new PurchaseController(sharedInventory, purchaseFileHandler);
        this.storeController    = new StoreController(sharedInventory);
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

    public ArrayList<InventoryItem> getAllItems() {
        return storeController.getAllItems();
    }

    public int getQuantityFor(String name, Location location) {
        return storeController.getQuantityFor(name, location);
    }

    public int increaseQuantity(ItemType type, String name, double price, Location location, int amount) {
        return storeController.increaseQuantity(type, name, price, location, amount);
    }

    public String storeItemWithFeedback(String name, String priceInput, String quantityInput, String locationInput) {
        return storeController.storeItem(name, priceInput, quantityInput, locationInput);
    }

    public ArrayList<InventoryItem> getAvailableItemsByLocation(Location location) {
        return purchaseController.getAvailableItems(location);
    }

    public String addToCart(InventoryItem item, int quantity, List<AddOnSelection> addOns) {
        return purchaseController.addToCart(item, quantity, addOns);
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

    public List<String> getCartInfo() {
        return purchaseController.getCartInfo();
    }

    public boolean isCartEmpty() {
        return purchaseController.isCartEmpty();
    }
        public ItemInventory getSharedInventory() { 
        return sharedInventory; 
    }
}
