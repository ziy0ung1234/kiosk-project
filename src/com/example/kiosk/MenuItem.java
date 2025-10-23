package com.example.kiosk;

public class MenuItem {
    //속성
    private String menuName;
    private int menuPrice;
    private String menuDescription;

    //생성자
    public MenuItem(String name, int price, String description) {
        this.menuName = name;
        this.menuPrice = price;
        this.menuDescription = description;
    }
    //기능
    public String getMenuName() {
        return menuName;
    }
    public String getMenuDescription() {
        return menuDescription;
    }
    public int getMenuPrice() {
        return menuPrice;
    }

}
