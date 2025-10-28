package com.example.kiosk;

import java.util.*;

/**
 *  전체적인 장바구니 관리 클래스
 * */
public class Cart<K extends MenuItem,V extends CartItem> {
    private final Map<K,V> cart = new HashMap<>();

    /**
     * 장바구니에 아이템 추가
     * @param key : 아이템 구분 키 (현재 MenuItem)
     * @param quantity : 추가 수량
     * @return 최종 장바구니에 담긴 아이템
     */
    public V addCartItem(K key, int quantity) {
        cart.compute(key, (k, existedCartItem) -> {
            if (existedCartItem == null) existedCartItem = (V) new CartItem(k);
            existedCartItem.increaseQuantity(quantity);
            return existedCartItem;
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
    public Collection<V> getCart() {
        return Collections.unmodifiableCollection(cart.values());
    }
    public int getTotalPrice() {
        return cart.values().stream()
                .mapToInt(cartItem -> cartItem.getMenuPrice() * cartItem.getQuantity())
                .sum();
    }


}
