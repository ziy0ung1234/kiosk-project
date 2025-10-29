package com.example.kiosk;

import java.util.*;

/**
 *  전체적인 장바구니 관리 클래스
 * */
public class Cart {
    private final Map<String,CartItem> cart = new HashMap<>();

    /**
     * 장바구니에 아이템 추가
     * @param menuItem : 아이템 구분 키(menuName)와 CartItem 생성을 위한 객체
     * @param quantity : 추가 수량
     * @return 최종 장바구니에 담긴 아이템
     */
    public CartItem addCartItem(MenuItem menuItem, int quantity) {
        String menuName = menuItem.getName();
        cart.compute(menuName, (name, cartItem) -> {
            if (cartItem == null) cartItem = new CartItem(menuItem);
            cartItem.increaseQuantity(quantity);
            return cartItem;
        });
        return cart.get(menuName);
    }
    public void removeCartItem(String menuName) {
        if (cart.isEmpty()) {
            System.out.println("장바구니가 비어있습니다");
            return;
        }

        if (!cart.containsKey(menuName)) {
            System.out.println("일치하는 메뉴가 없습니다.");
            return;
        }
        // Stream 사용 entrySet -> filter -> findFirst -> ifPresent(remove)
        cart.entrySet().stream()
                .filter(entry -> entry.getKey().equals(menuName))
                .findFirst()
                .ifPresent(entry -> {
                    cart.remove(entry.getKey());
                    System.out.println("취소되었습니다.");
                });
    }

    public void clearCartItem() {
        cart.clear();
    }
    public Collection<CartItem> getCart() {
        return Collections.unmodifiableCollection(cart.values());
    }
    //Cart는 합산만 담당 CartItem이 각 아이템 가격 계산 책임
    public int getTotalPrice() {
        return cart.values().stream()
                .mapToInt(CartItem::getTotalPrice)
                .sum();
    }


}
