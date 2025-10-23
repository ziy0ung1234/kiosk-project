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
     * 메뉴 아이템 출력을 위한 메소드
     *  - 기존 promptMenuItemList는 입출력 클래스 Kiosk로 이동
     *  - 메뉴 아이템들을 출력하기 위한 데이터만 담는 메소드
     * @param menuDisplay : 메뉴 출력할 StringBuilder 타입 문자열
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
