package models;
 
import java.util.ArrayList;
 
public class CartManager {
    private ArrayList<Item> cart = new ArrayList<>();
 
    public void addItem(Item item) {
        cart.add(item);
    }
 
    public void removeItem(int index) {
        if (index >= 0 && index < cart.size()) {
            cart.remove(index);
        }
    }
 
    public void clear() {
        cart.clear();
    }
 
    public double calculateTotal() {
        double total = 0;
        for (Item item : cart) {
            total += item.getPrice() * item.getQuantity();
        }
        return total;
    }
 
    public String getCartInfo() {
        if (cart.isEmpty()) return "Cart is empty.";
        String output = "Cart:\n";
        for (int i = 0; i < cart.size(); i++) {
            output += " " + (i + 1) + ". " + cart.get(i).getInfo() + "\n";
        }
        return output;
    }
 
    public ArrayList<Item> getCart() {
        return cart;
    }
 
    public boolean isEmpty() {
        return cart.isEmpty();
    }
}