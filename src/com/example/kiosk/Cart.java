package com.example.kiosk;

import java.util.*;

public class Cart {
    private final Map<MenuItem, CartItem> cart = new HashMap<>();

    public void addCartItem(MenuItem menuItem, int quantity) {
        CartItem cartItem = cart.computeIfAbsent(menuItem, k -> new CartItem(menuItem));
        cartItem.increaseQuantity(quantity);
    }
    public void removeCartItem(MenuItem menuItem) {
        cart.remove(menuItem);
    }
    public Collection<CartItem> getCart() {
        return cart.values();
    }
    public int getTotalPrice() {
        return cart.values().stream()
                .mapToInt(cartItem -> cartItem.getMenuPrice() * cartItem.getQuantity())
                .sum();
    }


}
