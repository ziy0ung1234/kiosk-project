package com.example.kiosk;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("---------------------------");
        System.out.println("|  안녕하세요. 블루 보틀 입니다. |");
        System.out.println("|      주문 하시겠습니까?      |");
        System.out.println("|     1) 네   2) 아니오      |");
        System.out.println("---------------------------");
        String inputAccess = sc.next();
        if (inputAccess.equals("2")) {
            System.out.println("주문을 종료합니다. 다음에 다시 찾아주세요");
            sc.close();
            return;
        }

        StringBuilder menuDisplay = new StringBuilder();
        boolean running = true; // 다음 과제 진행전 level1 에서는 1개선택 후 스캐너 종료시키기 위한 변수

        while (running) {
            menuDisplay.setLength(0); // 이전 내용 초기화
            menuDisplay.append("[💙Blue Bottle]\n");
            menuDisplay.append("1. 드립 커피    | 5800원 | 스페셜티 원두로 진하게 내린 오늘의 커피\n");
            menuDisplay.append("2. 모카 드리프트 | 6000원 | 최고급 초콜렛이 들어간 달달한 모카 라떼\n");
            menuDisplay.append("3. 놀라플로트    | 7100원 | 목장에 와있는듯한 생 우유 아이스크림을 올린 라떼\n");
            menuDisplay.append("4. 뉴올리언스    | 6300원 | 스페셜티 원두를 치커리 뿌리와 함께 갈아 우유와 설탕을 넣어만든 블루보틀의 시그니처\n");
            menuDisplay.append("0. 종료        | 종료\n");

            System.out.print(menuDisplay);

            System.out.print("선택: ");
            String selectMenu = sc.next();

            switch (selectMenu) {
                case "0":
                    System.out.println("종료되었습니다. 다음에 다시 찾아주세요.");
                    running = false;
                    break;
                case "1":
                    System.out.println("드립 커피 선택하셨습니다.");
                    running = false;
                    break;
                case "2":
                    System.out.println("모카 드리프트 선택하셨습니다.");
                    running = false;
                    break;
                case "3":
                    System.out.println("놀라플로트 선택하셨습니다.");
                    running = false;
                    break;
                case "4":
                    System.out.println("뉴올리언스 선택하셨습니다.");
                    running = false;
                    break;
                default:
                    System.out.println("잘못된 입력입니다. 0~4 중 하나를 입력해주세요.");
                    break;
            }
        }
        sc.close();
    }
    }
