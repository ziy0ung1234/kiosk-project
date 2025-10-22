package com.example.kiosk;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // MenuItem 리스트 생성
        List<MenuItem> menuList = new ArrayList<>();
        menuList.add(new MenuItem("드립 커피", 5800, "스페셜티 원두로 진하게 내린 오늘의 커피"));
        menuList.add(new MenuItem("모카 드리프트", 6000, "최고급 초콜렛이 들어간 달달한 모카 라떼"));
        menuList.add(new MenuItem("놀라플로트", 7100, "목장에 와있는듯한 생 우유 아이스크림을 올린 라떼"));
        menuList.add(new MenuItem("뉴올리언스", 6300, "스페셜티 원두를 치커리 뿌리와 함께 갈아 우유와 설탕을 넣어만든 블루보틀 시그니처"));

        Scanner sc = new Scanner(System.in);
        boolean running = true;

        while (true) {
            System.out.println("---------------------------");
            System.out.println("|  안녕하세요. 블루 보틀 입니다. |");
            System.out.println("|      주문 하시겠습니까?      |");
            System.out.println("|     1) 네   2) 아니오      |");
            System.out.println("---------------------------");

            String inputAccess = sc.nextLine();

            if (inputAccess.equals("1")) {
                break; // 메뉴로 진입
            } else if (inputAccess.equals("2")) {
                System.out.println("주문을 종료합니다. 다음에 다시 찾아주세요.");
                sc.close();
                return;
            } else {
                System.out.println("잘못된 입력입니다. 1과 2 중 하나를 선택해주세요.\n");
            }
        }

        // 메뉴 선택
        StringBuilder menuDisplay = new StringBuilder();

        while (running) {
            menuDisplay.setLength(0);
            menuDisplay.append("[ 💙Blue Bottle ]\n");
            menuDisplay.append("------------------------------------------------------------------\n");

            for (int i = 0; i < menuList.size(); i++) {
                MenuItem item = menuList.get(i);
                menuDisplay.append(String.format("%2d. %-8s | %5d원 | %s\n",
                        (i + 1), item.getName(), item.getPrice(), item.getDescription()));
            }
            menuDisplay.append(" 0. 종료 | 종료\n");

            System.out.print(menuDisplay);
            System.out.print("선택: ");
            String selectMenu = sc.nextLine();

            int userChoice;
            try {
                userChoice = Integer.parseInt(selectMenu);
            } catch (NumberFormatException e) {
                System.out.println("잘못된 입력입니다. 숫자로 입력해주세요.\n");
                continue;
            }

            if (userChoice == 0) {
                System.out.println("종료되었습니다. 다음에 다시 찾아주세요.");
                running = false;
            } else if (userChoice >= 1 && userChoice <= menuList.size()) {
                MenuItem selected = menuList.get(userChoice - 1);
                System.out.printf("선택하신 메뉴 : %-8s | %5d원 | %s 입니다.\n", selected.getName(),selected.getPrice(), selected.getDescription());
                running = false;
            } else {
                System.out.println("잘못된 입력입니다. 0~" + menuList.size() + " 중 하나를 입력해주세요.\n");
            }
        }

        sc.close();
    }
}
