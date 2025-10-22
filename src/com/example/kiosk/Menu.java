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

    public List<MenuItem> ReadOnlyMenuItems() {
        return Collections.unmodifiableList(menuItemList);
    }

    public void promptMenuItems() {
        StringBuilder menuDisplay = new StringBuilder();
        menuDisplay.append("[ 💙Blue Bottle ]\n");
        int index = 1;
        for (MenuItem item : menuItemList) {
            menuDisplay.append(String.format("%2d. %-8s | %5d원 | %s\n",
                    index, item.getMenuName(), item.getMenuPrice(), item.getMenuDescription()));
            index++;
        }
        menuDisplay.append(" 0. 뒤로가기 \n");
        System.out.println(menuDisplay);
    }

}
