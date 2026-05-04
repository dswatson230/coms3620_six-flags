package models;
 
import models.item.CartItem;

import java.util.ArrayList;
import java.util.List;

public class CartManager {
    private ArrayList<CartItem> cart = new ArrayList<>();
 
    public void addItem(CartItem item) {
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
        for (CartItem item : cart) {
            total += item.getItem().getPrice() * item.getQuantity();
        }
        return total;
    }

    public List<String> getCartInfo() {
        List<String> output = new ArrayList<>();

        if (cart.isEmpty()) {
            output.add("Cart is empty.");
            return output;
        }

        output.add("Cart:");

        for (int i = 0; i < cart.size(); i++) {
            CartItem cartItem = cart.get(i);

            output.add(" " + (i + 1) + ". " + cartItem.getInfo());
            for (String benefit : cartItem.getItem().getBenefits()) {
                output.add("      - " + benefit);
            }
        }

        return output;
    }
 
    public ArrayList<CartItem> getCart() {
        return cart;
    }
 
    public boolean isEmpty() {
        return cart.isEmpty();
    }
}