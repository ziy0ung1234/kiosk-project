package com.example.kiosk;

/**
 *  장바구니 내 아이템에 대한 책임(수량, 가격) 관리 클래스
 * */
public class CartItem {
    // 메뉴명, 수량, 가격 정보
    private final MenuItem menuItem;
    private  int quantity;

    public CartItem( MenuItem menuItem) {
        this.menuItem = menuItem;
        this.quantity = 0;
    }
    public int getQuantity() {
        return quantity;
    }
    /**
     * @param quantity  유저가 선택한 수량
     * */
    public void increaseQuantity(int quantity) {
        this.quantity += Math.min(quantity, 10 - this.quantity);
    }
    public void decreaseQuantity(int quantity) {
        this.quantity -= Math.max(0, this.quantity - quantity);
    }
    public String getMenuName() {
        return menuItem.getName();
    }
    public int getMenuPrice() {
        return  menuItem.getPrice();
    }
}
