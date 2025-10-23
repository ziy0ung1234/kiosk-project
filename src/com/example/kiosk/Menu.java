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
    public List<MenuItem> ReadOnlyMenuItemList() {
        return Collections.unmodifiableList(menuItemList);
    }

    /**
     *
     * */
    public void printAppendMenuItemList(StringBuilder menuDisplay) {
        int index = 1;
        for (MenuItem item : menuItemList) {
            menuDisplay.append(String.format("%2d. %-8s | %5d원 | %s\n",
                    index, item.getMenuName(), item.getMenuPrice(), item.getMenuDescription()));
            index++;
        }
    }

}
