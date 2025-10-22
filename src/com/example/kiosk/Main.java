package com.example.kiosk;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // MenuItem 리스트 생성
        List<MenuItem> menuList = new ArrayList<>();
        menuList.add(new MenuItem("드립 커피", 5800, "스페셜티 원두로 진하게 내린 오늘의 커피"));
        menuList.add(new MenuItem("모카 드리프트", 6000, "최고급 초콜렛이 들어간 달달한 모카 라떼"));
        menuList.add(new MenuItem("놀라플로트", 7100, "목장에 와있는듯한 생 우유 아이스크림을 올린 라떼"));
        menuList.add(new MenuItem("뉴올리언스", 6300, "스페셜티 원두를 치커리 뿌리와 함께 갈아 우유와 설탕을 넣어만든 블루보틀 시그니처"));
        Kiosk kiosk = new Kiosk(menuList);
        kiosk.start();
    }
}
