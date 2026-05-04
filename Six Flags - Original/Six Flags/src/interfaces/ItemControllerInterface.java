package interfaces;

public interface ItemControllerInterface {
    boolean purchaseItem();
    boolean storeItem(String name, String priceInput, String quantityInput, String locationInput);
}