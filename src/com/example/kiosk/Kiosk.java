package com.example.kiosk;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

/**
 * 메뉴 관리, 사용자 입력 처리
 * */
public class Kiosk {
    public static final String LINE = "-".repeat(60); //불변 상수
    private State currentState;
    private final List<Menu> menuList;
    private final Scanner scanner;               // 공통적으로 입력되던 매개변수 필드로 지정
    private Optional<Menu> selectedMainMenu;     // 공통적으로 입력되던 매개변수 필드로 지정(사용자가 선택한 메뉴를 메서드 간 공유해야 함)
    private Optional<MenuItem> selectedMenuItem; // 공통적으로 입력되던 매개변수 필드로 지정(사용자가 선택한 메뉴를 메서드 간 공유해야 함)
    private final Cart cart;

    public Kiosk(List<Menu> menuList) {
        this.menuList = menuList;
        this.currentState = State.START;
        this.scanner = new Scanner(System.in);
        this.selectedMainMenu = Optional.empty();
        this.selectedMenuItem = Optional.empty();
        this.cart = new Cart();
    }
    // -- 실행 메소드 --
    public void start() {
        while(currentState != State.EXIT) {
            currentState = currentState.handle(this);
        }
    }
    // -- 상태별 handle 메소드 --
    public State handleSubMenuState() {
        if (selectedMainMenu.isEmpty()) return State.MAIN_MENU;
        Menu selectedMenu = selectedMainMenu.get();
        List<MenuItem> menuItemList = selectedMenu.readOnlyMenuItemList();
        promptMenuItemList(selectedMenu);
        int userSelectNumber = changeInputType(0, menuItemList.size());
        if (userSelectNumber == 0) {
            selectedMainMenu = Optional.empty();
            return State.MAIN_MENU;
        }
        if (userSelectNumber >= 1 && userSelectNumber <= menuItemList.size()) {
            selectedMenuItem = Optional.of(menuItemList.get(userSelectNumber - 1));
            selectedMenuItem.ifPresent(menuItem ->
                    System.out.printf("선택하신 메뉴 : %-8s | %5d원 | %s 입니다.\n",
                            menuItem.getName(), menuItem.getPrice(), menuItem.getDescription())
            );
            return State.CART;
        }
        System.out.println("잘못된 입력입니다. 0~" + menuItemList.size() + " 중 하나를 입력해주세요.\n");
        return State.SUB_MENU;
    }
    public State handleCartState(){
        System.out.println("👆🏻 이 메뉴를 장바구니에 추가할까요?\n 1) 확인  2) 취소 ");
        int userSelectNumber =  changeInputType(1,2);
        if (userSelectNumber == 1) {
            System.out.println("수량을 선택해주세요( 동일 메뉴 최대 10개 )");
            int selectedQuantity =  changeInputType(1,10);
            if(selectedMenuItem.isPresent()){
                MenuItem menuItem = selectedMenuItem.get();
                cart.addCartItem(menuItem, selectedQuantity);
                System.out.printf("%s %d개 추가되었습니다.\n 메뉴를 더 보시겠어요?\n1) 네  2) 아니오  \n",
                        menuItem.getName(), selectedQuantity);
                int userSelectState = changeInputType(1,2);
                return (userSelectState == 1) ? State.MAIN_MENU : State.ORDER;
            } else {
                System.out.print("선택되지 않았습니다. 메뉴로 돌아갑니다.");
                return State.MAIN_MENU; // 메뉴 선택의 가장 상위로 돌아감
            }
        } else return State.MAIN_MENU;
    }
    public State handleOrderState() {
        promptCartItemList(cart);
        int userSelectNumber = changeInputType(1,2);
        if (userSelectNumber == 1) {
            System.out.println("주문이 완료되었습니다.");
            return State.EXIT;
        }
        return State.MAIN_MENU;
    }

    // -- 공통 메서드 --
    public String readUserInput() {
        System.out.print("선택: ");
        return scanner.next();
    }
    public int changeInputType(int min, int max) {
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
    public void closeScanner() {
        scanner.close();
    }
    public List<Menu> getMenuList() {
        return Collections.unmodifiableList(menuList);
    }
    public Optional<Menu> getSelectedMainMenu() {
        return selectedMainMenu;
    }
    public void setSelectedMainMenu(int index) {
        if (index >= 0 && index < menuList.size()) {
            selectedMainMenu = Optional.of(menuList.get(index));
        } else {
            selectedMainMenu = Optional.empty();
        }
    }
    // ----- 리스트 출력 메소드 -------
    public void promptMenuList() {
        StringBuilder displayPrompt = new StringBuilder();
        displayPrompt.append("[ 💙Main Menu ]\n").append(LINE).append("\n");
        int index = 1;
        for(Menu menu : menuList) {
            displayPrompt.append(String.format("%2d. %-8s\n", index, menu.getCategoryName()));
            index++;
        }
        displayPrompt.append(" 0. 뒤로 가기 \n");
        System.out.println(displayPrompt);
    }

    public void promptMenuItemList(Menu menu) {
        StringBuilder displayPrompt = new StringBuilder();
        displayPrompt.append("[ 💙Blue Bottle ]\n").append(LINE).append("\n");
        int index = 1;
        for (MenuItem item : menu.readOnlyMenuItemList()) {
            displayPrompt.append(String.format("%2d. %-8s | %5d원 | %s\n",
                    index, item.getName(), item.getPrice(), item.getDescription()));
            index++;
        }
        displayPrompt.append(" 0. 뒤로가기 \n");
        System.out.println(displayPrompt);
    }

    public void promptCartItemList(Cart cart) {
        StringBuilder displayPrompt = new StringBuilder();
        displayPrompt.append("[ 💙Order List ]\n").append(LINE).append("\n");
        int index = 1;
        for ( CartItem cartItem: cart.getCart()) {
            displayPrompt.append(String.format("%2d. %-8s x%d  %30d원\n",
                    index,cartItem.getMenuName(), cartItem.getQuantity(), cartItem.getMenuPrice()*cartItem.getQuantity()));
            index++;
        }
        displayPrompt.append(LINE).append(String.format("\n%40s총합 %d원\n%s\n1. 결제하기 2. 뒤로가기","",cart.getTotalPrice(),LINE));
        System.out.println(displayPrompt);
    }

}

