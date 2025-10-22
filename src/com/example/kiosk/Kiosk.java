package com.example.kiosk;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

/**
 * 메뉴 관리, 사용자 입력 처리
 * */
public class Kiosk {
    private enum CurrentState {
        ACCESS, MAIN_MENU, SUB_MENU, EXIT
    }
    private CurrentState currentState;
    private List<Menu> menuList;

    public Kiosk(List<Menu> menus) {
        this.menuList = menus;
        this.currentState = CurrentState.ACCESS;
    }
    /**
     * 전반적인 프롬포트 처리
     * */
    public void start() {
        Scanner sc = new Scanner(System.in);
        Optional<Menu> selectedMainMenu = Optional.empty();

        while (currentState != CurrentState.EXIT) {
            switch (currentState) {
                // 처음 세팅 화면
                case ACCESS:
                    int intIntputAccess = promptOrderAcess(sc);
                    currentState = (intIntputAccess == 1) ? CurrentState.MAIN_MENU: CurrentState.EXIT; break;
                case MAIN_MENU:
                    promptMenuList(menuList);
                    String selectedCategory = promptUserInput(sc);
                    try {
                        int intSelectedCategory = Integer.parseInt(selectedCategory);
                        if (intSelectedCategory == 0) {
                            currentState = CurrentState.ACCESS;
                            break;
                        }
                        // 참조된 객체
                        selectedMainMenu = Optional.ofNullable(menuList.get(intSelectedCategory - 1));
                        if(selectedMainMenu.isPresent()) {
                            currentState = CurrentState.SUB_MENU;
                        } else {
                            currentState = CurrentState.ACCESS;
                        }
                        break;

                    } catch (NumberFormatException | IndexOutOfBoundsException e) {
                        System.out.println("잘못된 입력입니다. 숫자로 입력해주세요.\n");
                        continue;
                    }
                case SUB_MENU:
                    if(selectedMainMenu.isPresent()) {
                        Menu selectedMenuObject = selectedMainMenu.get();
                        List<MenuItem> menuItems = selectedMenuObject.ReadOnlyMenuItems();
                        selectedMenuObject.promptMenuItems();
                        // 커피 목록
                        String selectedSubMenu = promptUserInput(sc);
                        int intSelectMenu;
                        try {
                            intSelectMenu = Integer.parseInt(selectedSubMenu);
                        } catch (NumberFormatException | IndexOutOfBoundsException e) {
                            System.out.println("잘못된 입력입니다. 숫자로 입력해주세요.\n");
                            continue;
                        }
                        if (intSelectMenu == 0) {
                            currentState = CurrentState.MAIN_MENU;
                            selectedMainMenu = Optional.empty(); // 메뉴 선택 Optional 객체도 지워줌
                        } else if (intSelectMenu >= 1 && intSelectMenu <= menuItems.size()) {
                            MenuItem selected = menuItems.get(intSelectMenu - 1);
                            System.out.printf("선택하신 메뉴 : %-8s | %5d원 | %s 입니다.\n", selected.getMenuName(), selected.getMenuPrice(), selected.getMenuDescription());
                            currentState = CurrentState.EXIT;
                            break;
                        } else {
                            System.out.println("잘못된 입력입니다. 0~" + menuItems.size() + " 중 하나를 입력해주세요.\n");
                        }
                    } else {
                        currentState = CurrentState.MAIN_MENU;
                        break;
                    }
            }
        }
    }
    private static String promptUserInput(Scanner sc) {
        System.out.print("선택: ");
        return sc.next();
    }
    private static void promptMenuList(List<Menu> meunList) {
        StringBuilder menuPrompt = new StringBuilder();
        menuPrompt.append("[ Main Menu ]\n");
        int index = 1;
        for(Menu menu : meunList) {
            menuPrompt.append(String.format("%2d. %-8s\n", index, menu.getCategoryName()));
            index++;
        }
        menuPrompt.append(" 0. 종료 \n");
        System.out.println(menuPrompt);

    }
    /**
     * 키오스크 최 상위 프롬포트
     * @param scanner 스캐너
     * */
    private static int promptOrderAcess(Scanner scanner) {
        while (true) {
            System.out.println("---------------------------");
            System.out.println("|  안녕하세요. 블루 보틀 입니다. |");
            System.out.println("|      주문 하시겠습니까?      |");
            System.out.println("|     1) 네   2) 아니오      |");
            System.out.println("---------------------------");

            String inputAccess = scanner.next();
            int intInput;
            try{
                intInput = Integer.parseInt(inputAccess);
                if(intInput == 1 || intInput == 2) {
                    return intInput;
                } else {
                    System.out.println("잘못된 입력입니다. 1과 2 중 하나를 선택해주세요.\n");
                }
            } catch (NumberFormatException e) {

                System.out.println("잘못된 입력입니다. 1과 2 중 하나를 선택해주세요.\n");
            }
        }
    }
}
