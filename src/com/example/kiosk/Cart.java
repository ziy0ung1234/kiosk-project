package com.example.kiosk;

import java.util.*;

public class Cart {
    private final Map<MenuItem, CartItem> cart = new HashMap<>();

    public CartItem addCartItem(MenuItem menuItem, int quantity) {
        cart.compute(menuItem, (key, cartItem) -> {
            if (cartItem == null) cartItem = new CartItem(menuItem);
            cartItem.increaseQuantity(quantity);
            return cartItem;
        });
        return cart.get(menuItem);
    }
    public void clearCartItem() {
        cart.clear();
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
