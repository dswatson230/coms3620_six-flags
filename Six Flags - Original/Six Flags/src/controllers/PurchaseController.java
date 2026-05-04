package controllers;

import data.ItemInventory;
import data.PurchaseFileHandler;
import factory.ItemFactory;
import interfaces.types.Location;
import models.CartManager;
import models.PurchaseRecord;
import models.item.decorators.AddOnSelection;
import models.item.decorators.AddOnService;
import models.item.CartItem;
import models.item.InventoryItem;
import validation.InputValidator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PurchaseController {
    private ItemInventory       inventory;
    private CartManager         cartManager;
    private InputValidator      validator;
    private ItemFactory         factory;
    private PurchaseFileHandler purchaseFileHandler;
    private AddOnService        addOnService;

    public PurchaseController(ItemInventory inventory, PurchaseFileHandler purchaseFileHandler) {
        this.inventory           = inventory;
        this.cartManager         = new CartManager();
        this.validator           = new InputValidator();
        this.factory             = new ItemFactory();
        this.purchaseFileHandler = purchaseFileHandler;
        addOnService = new AddOnService();
    }

    public ArrayList<InventoryItem> getAvailableItems(Location location) {
        return inventory.getAllItems().stream()
            .filter(i -> i.getLocation() == location && i.getQuantity() > 0)
            .collect(java.util.stream.Collectors.toCollection(ArrayList::new));
    }

    public String addToCart(InventoryItem selectedItem, int quantity, List<AddOnSelection> addOns) {
        if (!inventory.verifyQuantity(selectedItem, quantity)) {
            return "Insufficient quantity. Only " + selectedItem.getQuantity() + " available.";
        }

        InventoryItem item = selectedItem;

        for (AddOnSelection addOn : addOns) {
            item = addOnService.apply(addOn.getAddOn(), item, addOn.getConfig());
        }

        CartItem cartItem = factory.createCartItem(item.getItem(), selectedItem, quantity);
        cartManager.addItem(cartItem);
        return null;
    }

    public void removeFromCart(int index) {
        cartManager.removeItem(index);
    }

    public String checkout() {
        ArrayList<CartItem> cart = cartManager.getCart();
        String failedItem = inventory.validateAllReductions(cart);
        if (failedItem != null) {
            return "Transaction cancelled. Insufficient stock for: " + failedItem +
                   "\nNo changes were made.";
        }

        inventory.commitAllReductions(cart);

        String recordId = generateRecordId();
        List<CartItem> purchasedItems = new ArrayList<>(cart);
        PurchaseRecord record = new PurchaseRecord(recordId, purchasedItems, "PURCHASED");
        try {
            purchaseFileHandler.writeRecord(record);
        } catch (IOException e) {
            System.out.println("Warning: Purchase record could not be saved: " + e.getMessage());
        }

        cartManager.clear();
        return null;
    }

    public double calculateTotal() {
        return cartManager.calculateTotal();
    }

    public List<String> getCartInfo() {
        return cartManager.getCartInfo();
    }

    public boolean isCartEmpty() {
        return cartManager.isEmpty();
    }

    private String generateRecordId() {
        return "P" + UUID.randomUUID().toString().substring(0, 7).toUpperCase();
    }
}