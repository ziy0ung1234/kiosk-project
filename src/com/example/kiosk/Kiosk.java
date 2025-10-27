package com.example.kiosk;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Kiosk {
    private static final String LINE = "-".repeat(60);

    private State currentState;
    private final Discount discount;
    private final List<Menu> menuList;
    private final Scanner scanner;
    private Optional<Menu> selectedMainMenu;
    private Optional<MenuItem> selectedMenuItem;
    private final Cart cart;

    public Kiosk(List<Menu> menuList) {
        this.menuList = menuList;
        this.currentState = State.START;
        this.discount = Discount.GENERAL;
        this.scanner = new Scanner(System.in);
        this.selectedMainMenu = Optional.empty();
        this.selectedMenuItem = Optional.empty();
        this.cart = new Cart();
    }

    // --- 실행 메서드 ---
    public void start() {
        while (true) {
            currentState = currentState.handle(this);
            if (currentState == State.EXIT) {
                currentState.handle(this); // EXIT 메시지 출력 하고 종료
                break;
            }
        }
    }

    // --- 유틸 메서드 ---
    public int readUserInput(int min, int max) {
        while (true) {
            System.out.print("선택: ");
            String input = scanner.next();
            try {
                int value = Integer.parseInt(input);
                if (value >= min && value <= max) return value;
                System.out.printf("잘못된 입력입니다. %d~%d 사이에서 선택해주세요.\n", min, max);
            } catch (NumberFormatException e) {
                System.out.printf("숫자를 입력해주세요. %d~%d 사이에서 선택해주세요.\n", min, max);
            }
        }
    }

    public void closeScanner() {
        scanner.close();
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

    // --- 각 상태별 UI 출력 + 입력 반환 ---
    public void showStart() {
        String displayMenu = "💙 안녕하세요 블루 보틀입니다\n" + LINE +
                "\n주문하시겠습니까? 1) 네 2) 아니오";
        System.out.println(displayMenu);
    }

    public int showMainMenuAndGetInput() {
        StringBuilder displayMenu = new StringBuilder();
        displayMenu.append("[ 💙Main Menu ]\n").append(LINE).append("\n");
        int index = 1;
        for (Menu menu : menuList) {
            displayMenu.append(String.format("%2d. %-8s\n", index, menu.getCategoryName()));
            index++;
        }
        displayMenu.append(" 0. 뒤로가기\n");
        System.out.println(displayMenu);
        return readUserInput(0, menuList.size());
    }

    public int showSubMenuAndGetInput() {
        Menu menu = selectedMainMenu.orElseThrow();
        StringBuilder displayMenu = new StringBuilder();
        displayMenu.append(String.format("[ 💙%s ]\n",menu.getCategoryName())).append(LINE).append("\n");
        List<MenuItem> items = menu.readOnlyMenuItemList();
        int index = 1;
        for (MenuItem item : items) {
            displayMenu.append(String.format("%2d. %-8s | %5d원 | %s\n",
                    index, item.getName(), item.getPrice(), item.getDescription()));
            index++;
        }
        displayMenu.append(" 0. 뒤로가기\n");
        System.out.println(displayMenu);
        int userSelect = readUserInput(0, items.size());
        if (userSelect != 0) selectedMenuItem = Optional.of(items.get(userSelect - 1));
        return userSelect;
    }

    public int showCartAndGetInput() {
        MenuItem item = selectedMenuItem.orElseThrow();
        String displayMenu = String.format("선택하신 메뉴: %s | %d원\n", item.getName(), item.getPrice()) +
                "👆🏻 이 메뉴를 장바구니에 추가할까요?\n 1) 확인  2) 취소\n";
        System.out.println(displayMenu);
        int selectCartAdd = readUserInput(1,2);
        if (selectCartAdd == 1) {
            System.out.println("수량 선택 (1~10):");
            int selectedQuantity = readUserInput(1,10);
            CartItem cartItem = cart.addCartItem(item, selectedQuantity);
            if(cartItem.getQuantity() == 10 && selectedQuantity > 0){
                System.out.printf("%s는 최대 10개까지만 담을 수 있습니다. 수량 10개로 변경합니다.\n선택한 메뉴 확인하시겠어요?\n1) 메뉴 선택  2) 확인  \n", item.getName());
                return readUserInput(1,2);
            }
            System.out.printf("%s %d개 추가되었습니다.선택한 메뉴 확인하시겠어요?\n1) 메뉴 선택  2) 확인  \n", item.getName(), selectedQuantity);
            return readUserInput(1,2);
        }
        return 2;
    }

    public int showOrderAndGetInput() {
        StringBuilder displayMenu = new StringBuilder();
        displayMenu.append("[ 💙Order List ]\n").append(LINE).append("\n");
        int idx = 1;
        for (CartItem cartItem : cart.getCart()) {
            displayMenu.append(String.format("%2d. %-8s x%d | %5d원\n",
                    idx++, cartItem.getMenuName(), cartItem.getQuantity(),
                    cartItem.getMenuPrice() * cartItem.getQuantity()));
        }
        displayMenu.append(LINE).append("\n");
        displayMenu.append(String.format("총합: %d원\n1) 결제하기 2) 뒤로가기\n", cart.getTotalPrice()));
        System.out.println(displayMenu);
        int userSelect = readUserInput(1,2);
        if(userSelect == 1){
            return userSelect;
        }
        return userSelect;
    }
    public int showPaymentAndGetInput() {
        StringBuilder displayMenu = new StringBuilder();
        displayMenu.append("💡해당 되는 할인 코드 있으신가요?\n").append(LINE).append("\n");
        int index=1;
        for(Discount discount :Discount.values()) {
            displayMenu.append(String.format("%s. %s  %d %%\n",index,discount.getDiscountKorean(),discount.getDiscountRate()));
            index++;
        }
        System.out.println(displayMenu);
        int userSelect = readUserInput(1,index);
        int selectedRate = discount.checkDiscountRate(userSelect); // 20, 10, 5, 0
        double discountMultiplier = 1 - (selectedRate / 100.0);
        double discountedPrice = cart.getTotalPrice() * discountMultiplier;
        System.out.printf("🔔 주문 완료되었습니다. 결제 금액 %d입니다.\n", (int)discountedPrice);
        cart.clearCartItem();
        return userSelect;
    }

    public void showExit() {
        System.out.println("다음에 다시 찾아주세요 💙");
    }
}
