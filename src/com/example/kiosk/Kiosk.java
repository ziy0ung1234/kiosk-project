package com.example.kiosk;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.function.Consumer;

/**
 * 메뉴 관리, 사용자 입력 처리
 * */
public class Kiosk {
    private static final String LINE = "-".repeat(60); //불변 상수
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
            currentState.run(this);
        }
    }
    // -- 상태별 handle 메소드 --
    public void handleStartState() {
        System.out.printf("💙안녕하세요 블루 보틀입니다\n%s\n 주문하시겠습니까  1) 네  2) 아니오\n",LINE);
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
        handleOptional(selectedMainMenu, menu -> currentState = State.SUB_MENU,()-> currentState = State.MAIN_MENU);
        selectedMainMenu = Optional.ofNullable(menuList.get(userSelectNumber - 1));
        if(selectedMainMenu.isPresent()) {
            currentState = State.SUB_MENU;
        } else currentState = State.START;
    }


    public void handleSubMenuState() {
        handleOptional(selectedMainMenu, selectedMenu -> {
            List<MenuItem> menuItemList = selectedMenu.readOnlyMenuItemList();
            promptMenuItemList(selectedMenu);
            int userSelectNumber = changeInputType(0, menuItemList.size());
            if (userSelectNumber == 0) {
                currentState = State.MAIN_MENU;
                selectedMainMenu = Optional.empty();
                return;
            }
            if (userSelectNumber >= 1 && userSelectNumber <= menuItemList.size()) {
                selectedMenuItem = Optional.of(menuItemList.get(userSelectNumber - 1));
                handleOptional(selectedMenuItem, menuItem -> {
                    System.out.printf("선택하신 메뉴 : %-8s | %5d원 | %s 입니다.\n",
                            menuItem.getName(), menuItem.getPrice(), menuItem.getDescription());
                    currentState = State.CART;
                }, () -> currentState = State.MAIN_MENU);
            } else {
                System.out.println("잘못된 입력입니다. 0~" + menuItemList.size() + " 중 하나를 입력해주세요.\n");
            }
        }, () -> currentState = State.MAIN_MENU);
    }
    public void handleCartState(){
        System.out.println("👆🏻 이 메뉴를 장바구니에 추가할까요?\n 1) 확인  2) 취소 ");
        int userSelectNumber =  changeInputType(1,2);
        if (userSelectNumber == 1) {
            System.out.println("수량을 선택해주세요( 동일 메뉴 최대 10개 )");
            int selectedQuantity =  changeInputType(1,10);
            handleOptional(selectedMenuItem, menuItem-> {
                cart.addCartItem(menuItem, selectedQuantity);
                System.out.printf("%s %d개 추가되었습니다.\n 메뉴를 더 보시겠어요?\n1) 네  2) 아니오  \n", menuItem.getName(),selectedQuantity);
                int userSelectState = changeInputType(1,2);
                if (userSelectState == 1) {
                    currentState = State.MAIN_MENU;
                } else currentState = State.ORDER;
            },()-> {
                System.out.print("선택되지 않았습니다.");
                currentState = State.SUB_MENU;
            });
        } else currentState = State.SUB_MENU;
    }
    public void handleOrderState() {
        promptCartItemList(cart);
        int userSelectNumber = changeInputType(1,2);
        if (userSelectNumber == 1) {
            System.out.println("주문이 완료되었습니다.");
            currentState = State.EXIT;
        }else currentState = State.MAIN_MENU;
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
    private <T> void handleOptional(Optional<T> optional, Consumer<T> action, Runnable emptyAction) {
        if(optional.isPresent()) action.accept(optional.get());
        else emptyAction.run();
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
    // ----- 리스트 출력 메소드 -------
    private void promptMenuList() {
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

    private void promptMenuItemList(Menu menu) {
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

    private void promptCartItemList(Cart cart) {
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

