package com.example.kiosk;

import java.util.*;

/**
 *  전체적인 장바구니 관리 클래스
 * */
public class Cart<K extends MenuItem> {
    private final Map<K,CartItem> cart = new HashMap<>();

    /**
     * 장바구니에 아이템 추가
     * @param key : 아이템 구분 키 (현재 MenuItem)
     * @param quantity : 추가 수량
     * @return 최종 장바구니에 담긴 아이템
     */
    public CartItem addCartItem(K key, int quantity) {
        cart.compute(key, (k, cartItem) -> {
            //Kiosk는 MenuItem과 수량 전달만, Cart가 CartItem 객체화
            if (cartItem == null) cartItem = new CartItem(k);
            cartItem.increaseQuantity(quantity);
            return cartItem;
        });
        return cart.get(key);
    }
    public void removeCartItem(String menuName) {
        if (cart.isEmpty()) {
            System.out.println("장바구니가 비어있습니다");
            return;
        }
        boolean isExist = cart.keySet().stream()
                        .anyMatch(key -> key.getName().equals(menuName.trim()));
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
