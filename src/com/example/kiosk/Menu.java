package com.example.kiosk;

import java.util.Collections;
import java.util.List;

public class Menu {
    private String categoryName;
    private List<MenuItem> menuItemList;

    public Menu(String categoryName, List<MenuItem> menuItemList) {
        this.categoryName = categoryName;
        this.menuItemList = menuItemList;
    }
    public String getCategoryName() {
        return categoryName;
    }

    public void addMenuItem(MenuItem item) {
        if (item == null) {
            throw new IllegalArgumentException("MenuItem cannot be null");
        }
        menuItemList.add(item);
    }

    // 참조 공유 방지를 위한 읽기전용 getter 메소드
    public List<MenuItem> listMenuItems() {
        return Collections.unmodifiableList(menuItemList);
    }

}
