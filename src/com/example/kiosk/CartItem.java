package com.example.kiosk;

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
