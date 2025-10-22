package com.example.kiosk;

import java.util.List;
import java.util.Scanner;

/**
 * 메뉴 관리, 사용자 입력 처리
 * */
public class Kiosk {
    private List<MenuItem> meunItems;

    public Kiosk(List<MenuItem> menuList) {
        this.meunItems = menuList;
    }
    /**
     * 전반적인 프롬포트 처리
     * */
    public void start() {
        Scanner sc = new Scanner(System.in);
        boolean appRunning = true;
        while (appRunning) {
            //첫 세팅 화면
            int intputAccess = promptOrderAcess(sc);
            if(intputAccess == 2) {
                break;
            }
            // 이전 로직 넘어온거면 1이라고 판단
            boolean menuRunning = true;
            while (menuRunning) {
                promptMenuList(meunItems);

                System.out.print("선택: ");
                String selectMenu = sc.next();
                int intSelectMenu;
                try {
                    intSelectMenu = Integer.parseInt(selectMenu);
                } catch (NumberFormatException e) {
                    System.out.println("잘못된 입력입니다. 숫자로 입력해주세요.\n");
                    continue;
                }
                if (intSelectMenu == 0) {
                    menuRunning = false;
                } else if (intSelectMenu >= 1 && intSelectMenu <= meunItems.size()) {
                    MenuItem selected = meunItems.get(intSelectMenu - 1);
                    System.out.printf("선택하신 메뉴 : %-8s | %5d원 | %s 입니다.\n", selected.getName(),selected.getPrice(), selected.getDescription());
                    appRunning = false;
                    break;
                } else {
                    System.out.println("잘못된 입력입니다. 0~" + meunItems.size() + " 중 하나를 입력해주세요.\n");
                }
            }
        }
        sc.close();
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

    /**
     * 커피 메뉴 프롬포트
     * @param menuItemList MenuItem객체 리스트
     * */
    private static void promptMenuList(List<MenuItem> menuItemList) {
        StringBuilder menuDisplay = new StringBuilder();
        menuDisplay.append("[ 💙Blue Bottle ]\n");
        int index = 1;
        for (MenuItem item : menuItemList) {
            menuDisplay.append(String.format("%2d. %-8s | %5d원 | %s\n",
                    index, item.getName(), item.getPrice(), item.getDescription()));
            index++;
        }
        menuDisplay.append(" 0. 뒤로가기 \n");
        System.out.println(menuDisplay);
    }
}
