package factory;
 
import interfaces.ItemInterface;
import interfaces.types.ItemType;
import interfaces.types.Location;
import models.item.CartItem;
import models.item.InventoryItem;

public class ItemFactory {
    public ItemInterface createItem(ItemType type, String name, double price) {
        return type.create(name, price);
    }

    public InventoryItem createInventoryItem(ItemType type, String name, double price, Location location, int quantity) {
        return new InventoryItem(createItem(type, name, price), type, location, quantity);
    }

    public CartItem createCartItem(ItemInterface item, InventoryItem sourceItem, int quantity) {
        return new CartItem(item, sourceItem.getType(), sourceItem.getLocation(), quantity);
    }

    public CartItem createCartItem(ItemInterface item, ItemType type, Location location, int quantity) {
        return new CartItem(item, type, location, quantity);
    }
}
 