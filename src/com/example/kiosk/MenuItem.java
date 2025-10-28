package com.example.kiosk;

/**
 *  각 상위 메뉴 안에 속한 하위 메뉴 아이템에 대한 책임(이름,가격,설명) 관리 클래스
 * */
public class MenuItem {
    //속성
    private final String name;
    private final int price;
    private final String description;

    //생성자
    public MenuItem(String name, int price, String description) {
        this.name = name;
        this.price = price;
        this.description = description;
    }
    //기능
    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }
    public int getPrice() {
        return price;
    }

}
