package com.example.kiosk;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.IntStream;
import java.util.ArrayList;

/**
 * 입출력 처리, 필드 관리 클래스
 *  - 메뉴 리스트, 장바구니, 사용자 입력등 공유 필드 관리
 *  - 각 State가 사용할 유틸 메서드, UI 출력 메서드 제공
 * */
public class Kiosk {
    private static final String LINE = "-".repeat(60);
    private State currentState;
    private final Discount discount;
    private final List<Menu> menuList;
    private final Cart<MenuItem, CartItem> cart;
    private final Scanner scanner;               // 매개변수로 두번 이상 사용되는 객체 공유 필드로 사용
    private Optional<Menu> selectedMainMenu;     // 매개변수로 두번 이상 사용되는 객체 공유 필드로 사용
    private Optional<MenuItem> selectedMenuItem; // 매개변수로 두번 이상 사용되는 객체 공유 필드로 사용

    public Kiosk(List<Menu> menuList) {
        this.menuList = menuList;
        this.currentState = State.START;
        this.discount = Discount.GENERAL;
        this.scanner = new Scanner(System.in);
        this.selectedMainMenu = Optional.empty();
        this.selectedMenuItem = Optional.empty();
        this.cart = new Cart<>();
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
    /**
     * @param min  사용자 입력화면에서 입력할 수 있는 최소 숫자 1-based
     * @param max  사용자 입력화면에서 입력할 수 있는 최대 숫자
     * */
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

    /**
     * @param index  사용자 입력화면 1-base에서 리스트 0-base로 전환한 인덱스
     * */
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
        displayMenu.append("\n[ 💙 Main Menu ]\n").append(LINE).append("\n");
        //lambda,stream 사용 리스트 조회 구현
        IntStream.range(0, menuList.size()).forEach(i -> {
            Menu menu = menuList.get(i);
            displayMenu.append(String.format("%2d. %-8s\n",i+1,menu.getCategoryName()));
        });
        displayMenu.append(" 0. 뒤로가기\n");
        System.out.println(displayMenu);
        return readUserInput(0, menuList.size());
    }

    public int showSubMenuAndGetInput() {
        Menu menu = selectedMainMenu.orElseThrow();
        StringBuilder displayMenu = new StringBuilder();
        displayMenu.append(String.format("\n[ 💙 %s ]\n",menu.getCategoryName())).append(LINE).append("\n");
        List<MenuItem> menuItemList = menu.readOnlyMenuItemList();
        //lambda,stream 사용 리스트 조회 구현
        IntStream.range(0, menuItemList.size()).forEach (i -> {
            MenuItem menuItem = menuItemList.get(i);
            displayMenu.append(String.format("%2d. %-8s | %5d원 | %s\n",i+1,menuItem.getName(),menuItem.getPrice(),menuItem.getDescription()));
        });
        displayMenu.append(" 0. 뒤로가기\n");
        System.out.println(displayMenu);
        int userSelect = readUserInput(0, menuItemList.size());
        if (userSelect != 0) selectedMenuItem = Optional.of(menuItemList.get(userSelect - 1));
        return userSelect;
    }

    public int showCartAndGetInput() {
        MenuItem MenuItem = selectedMenuItem.orElseThrow();
        String displayMenu = String.format("\n선택하신 메뉴: %s %d원입니다.\n", MenuItem.getName(), MenuItem.getPrice()) +
                "👆🏻 이 메뉴를 장바구니에 추가할까요?\n 1) 확인  2) 취소";
        System.out.println(displayMenu);
        int selectCartAdd = readUserInput(1,2);
        if (selectCartAdd == 1) {
            System.out.println("수량 선택 (1~10):");
            int selectedQuantity = readUserInput(1,10);
            CartItem cartItem = cart.addCartItem(MenuItem, selectedQuantity);
            if(cartItem.getQuantity() == 10 && selectedQuantity > 0){
                System.out.printf("%s는 최대 10개까지만 담을 수 있습니다. 수량 10개로 변경합니다.\n선택한 메뉴 확인하시겠어요?\n1) 메뉴 선택  2) 확인  \n", MenuItem.getName());
                return readUserInput(1,2);
            }
            System.out.printf("%s %d개 추가되었습니다.선택한 메뉴 확인하시겠어요?\n1) 메뉴 선택  2) 확인  \n", MenuItem.getName(), selectedQuantity);
            return readUserInput(1,2);
        }
        return 0;
    }

    public int showOrderAndGetInput() {
        StringBuilder displayMenu = new StringBuilder();
        displayMenu.append("\n[ 💙 Order List ]\n").append(LINE).append("\n");
        //lambda stream 사용 컬렉션 조회
        List<CartItem> cartItemList = new ArrayList<>(cart.getCart());
        IntStream.range(0, cartItemList.size()).mapToObj(
                i -> String.format("%2d. %-8s x%d | %20d원\n",i+1,
                        cartItemList.get(i).getMenuName(),
                        cartItemList.get(i).getQuantity(),
                        cartItemList.get(i).getMenuPrice()*cartItemList.get(i).getQuantity()
                )).forEach(displayMenu::append);
        displayMenu.append(LINE).append("\n");
        displayMenu.append(String.format("%35s총합 %d원\n","", cart.getTotalPrice()));
        displayMenu.append("취소 하고 싶으신 메뉴 있으신가요?\n1)아니오(결제) 2. 네");
        System.out.println(displayMenu);
        int userSelect = readUserInput(1,2);
        if (userSelect == 1) {
            return 1;
        } else {
            cancelOrder();
            if(cart.getCart().isEmpty()){
                System.out.println("장바구니가 비어 메뉴로 돌아갑니다");
                return 0; // 뒤로 가기
            }
            return 2;
        }
    }
    public void cancelOrder() {
        System.out.print("취소 하고싶은 메뉴 이름을 입력해주세요 : ");
        scanner.nextLine(); // 이전 입력 후 남은 버퍼 비우기
        String input = scanner.nextLine();
        cart.removeCartItem(input.trim());
    }
    public int showPaymentAndGetInput() {
        StringBuilder displayMenu = new StringBuilder();
        displayMenu.append("💡해당 되는 할인 코드 있으신가요?\n").append(LINE).append("\n");
        IntStream.range(0, Discount.values().length).forEach(i -> {
            Discount discount = Discount.values()[i];
            displayMenu.append(String.format("%s. %s  %d %%\n",i+1,discount.getDiscountKorean(),discount.getDiscountRate()));
        });
        System.out.println(displayMenu);
        int userSelect = readUserInput(1,Discount.values().length);
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
