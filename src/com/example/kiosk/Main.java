package com.example.kiosk;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // MenuItem 리스트 생성
        List<MenuItem> coffeeList = new ArrayList<>();
        coffeeList.add(new MenuItem("드립 커피", 5800, "스페셜티 원두로 진하게 내린 오늘의 커피"));
        coffeeList.add(new MenuItem("모카 드리프트", 6000, "최고급 초콜렛이 들어간 달달한 모카 라떼"));
        coffeeList.add(new MenuItem("놀라플로트", 7100, "목장에 와있는듯한 생 우유 아이스크림을 올린 라떼"));
        coffeeList.add(new MenuItem("뉴올리언스", 6300, "스페셜티 원두를 치커리 뿌리와 함께 갈아 우유와 설탕을 넣어만든 블루보틀 시그니처"));
        Menu coffeeMenu = new Menu("Coffee",  coffeeList);
        List<MenuItem> noncoffeeList = new ArrayList<>();
        noncoffeeList.add(new MenuItem("아이스티", 6100, "새콤달콤한 아이스티"));
        noncoffeeList.add(new MenuItem("오렌지 주스", 6500, "매장에서 직접 착즙한 주스"));
        noncoffeeList.add(new MenuItem("유자 피즈", 6600, "새콤한 유자와 탄산감을 느낄 수 있는 음료"));
        noncoffeeList.add(new MenuItem("레몬에이드", 6800, "입맛돋구는 톡쏘는 음료"));
        Menu nonCoffeeMenu = new Menu("Non-Coffee",  noncoffeeList);
        // Menu 리스트 생성
        List<Menu>  coffeeMenuList = new ArrayList<>();
        coffeeMenuList.add(coffeeMenu);
        coffeeMenuList.add(nonCoffeeMenu);
        Kiosk kiosk = new Kiosk(coffeeMenuList);
        kiosk.start();
    }
}
