package com.example.kiosk;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

/**
 * 메뉴 관리, 사용자 입력 처리
 * */
public class Kiosk {
    private State currentState;
    private final List<Menu> menuList;
    private final Scanner scanner;           // 공통적으로 입력되던 매개변수 필드로 지정
    private Optional<Menu> selectedMainMenu; // 공통적으로 입력되던 매개변수 필드로 지정(사용자가 선택한 메뉴를 메서드 간 공유해야 함)


    public Kiosk(List<Menu> menuList) {
        this.menuList = menuList;
        this.currentState = State.START;
        this.scanner = new Scanner(System.in);
        this.selectedMainMenu = Optional.empty();
    }
    // -- 실행 메소드 --
    public void start() {
        while(currentState != State.EXIT) {
            currentState.run(this);
        }
    }
    // -- 상태별 handle 메소드 --
    public void handleStartState() {
        System.out.println("💙안녕하세요 블루 보틀입니다\n---------------------------------------------------------------------------\n 주문하시겠습니까  1) 네  2) 아니오");
        int userSelectNumber = changeInputType(1,2);
        currentState = (userSelectNumber == 1) ? State.MAIN_MENU : State.EXIT;
    }

    public void handleMainMenuState() {
        promptMenuList();
        int userSelectNumber = changeInputType(0,menuList.size());
        if (userSelectNumber == 0) {
            currentState = State.START;
            return;
        }
        // 참조된 객체 (리스트 0-based)
        selectedMainMenu = Optional.ofNullable(menuList.get(userSelectNumber - 1));
        if(selectedMainMenu.isPresent()) {
            currentState = State.SUB_MENU;
        } else currentState = State.START;
    }


    public void handleSubMenuState() {
        if(selectedMainMenu.isPresent()) {
            Menu selectedMenu = selectedMainMenu.get();
            List<MenuItem> menuItemList = selectedMenu.getMenuItemList();
            promptMenuItemList(selectedMenu);
            int userSelectNumber =  changeInputType(0,menuItemList.size());
            //뒤로 가기 기능
            if (userSelectNumber == 0) {
                currentState = State.MAIN_MENU;
                selectedMainMenu = Optional.empty();    // 메뉴 선택 Optional 객체도 지워줌
            } else if (userSelectNumber >= 1 && userSelectNumber <= menuItemList.size()) {
                MenuItem selected = menuItemList.get(userSelectNumber - 1);
                System.out.printf("선택하신 메뉴 : %-8s | %5d원 | %s 입니다.\n", selected.getName(), selected.getPrice(), selected.getDescription());
                currentState = State.EXIT;
            } else System.out.println("잘못된 입력입니다. 0~" + menuItemList.size() + " 중 하나를 입력해주세요.\n");
        } else currentState = State.MAIN_MENU;

    }

    public void handleExitState() {
        System.out.println("다음에 다시 찾아주세요");
        scanner.close();
    }
    // -- 공통 메서드 --
    private String readUserInput() {
        System.out.print("선택: ");
        return scanner.next();
    }

    private int changeInputType(int min, int max) {
        while (true) {
            String input = readUserInput();
            try {
                int selectedNumber = Integer.parseInt(input);
                if(selectedNumber >= min && selectedNumber <= max) {
                    return  selectedNumber;
                } else {
                    System.out.printf("잘못된 입력입니다. %d과 %d사이에서 선택해 주세요\n", min, max);
                }
            } catch (NumberFormatException | IndexOutOfBoundsException e) {
                System.out.printf("잘못된 입력입니다. %d과 %d사이에서 선택해 주세요\n", min, max);
            }
        }
    }
    private void promptMenuList() {
        StringBuilder displayPrompt = new StringBuilder();
        displayPrompt.append("[ 💙Main Menu ]\n---------------------------------------------------------------------------\n");
        int index = 1;
        for(Menu menu : menuList) {
            displayPrompt.append(String.format("%2d. %-8s\n", index, menu.getCategoryName()));
            index++;
        }
        displayPrompt.append(" 0. 뒤로 가기 \n");
        System.out.println(displayPrompt);
    }
    /**
     * 각 상위 카테고리에 대한 메뉴 아이템 프롬포트
     * @param menu : not null Menu object
     * */
    private void promptMenuItemList(Menu menu) {
        StringBuilder displayPrompt = new StringBuilder();
        displayPrompt.append("[ 💙Blue Bottle ]\n---------------------------------------------------------------------------\n");
        int index = 1;
        for (MenuItem item : menu.getMenuItemList()) {
            displayPrompt.append(String.format("%2d. %-8s | %5d원 | %s\n",
                    index, item.getName(), item.getPrice(), item.getDescription()));
            index++;
        }
        displayPrompt.append(" 0. 뒤로가기 \n");
        System.out.println(displayPrompt);
    }
}

