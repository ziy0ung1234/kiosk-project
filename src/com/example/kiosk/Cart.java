package com.example.kiosk;

import java.util.*;

/**
 *  전체적인 장바구니 관리 클래스
 * */
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
    public void removeCartItem(String menuName) {
        if (cart.isEmpty()) {
            System.out.println("장바구니가 비어있습니다");
            return;
        }
        boolean isExist = cart.keySet().stream()
                        .anyMatch(menuItem -> menuItem.getName().equals(menuName.trim()));
        if(!isExist){
            System.out.println("일치하는 메뉴가 없습니다.");
        } else {
            cart.entrySet().stream()
                    .filter(entry -> entry.getKey().getName().equals(menuName))
                    .findFirst()
                    .ifPresent(entry -> cart.remove(entry.getKey()));
            System.out.println("취소되었습니다.");
        }

    }
    public void clearCartItem() {
        cart.clear();
    }
    public Collection<CartItem> getCart() {
        return Collections.unmodifiableCollection(cart.values());
    }
    public int getTotalPrice() {
        return cart.values().stream()
                .mapToInt(cartItem -> cartItem.getMenuPrice() * cartItem.getQuantity())
                .sum();
    }


}
