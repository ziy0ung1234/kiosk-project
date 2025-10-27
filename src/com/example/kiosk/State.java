package com.example.kiosk;

public enum State {
    START{
        @Override
        public State handle(Kiosk kiosk) {
            System.out.printf("💙안녕하세요 블루 보틀입니다\n%s\n 주문하시겠습니까  1) 네  2) 아니오\n",kiosk.LINE);
            int userSelectNumber = kiosk.changeInputType(1,2);
            return (userSelectNumber == 1) ? MAIN_MENU : EXIT;
        }
    },
    MAIN_MENU{
        @Override
        public State handle(Kiosk kiosk) {
            kiosk.promptMenuList();
            int userSelectNumber = kiosk.changeInputType(0,kiosk.getMenuList().size());
            if (userSelectNumber == 0) {
                return START;
            }
            kiosk.setSelectedMainMenu(userSelectNumber);
            // 값이 있으면 SUB_MENU, 없으면 START로
            return kiosk.getSelectedMainMenu().isPresent() ? SUB_MENU : START;
        }
    },
    SUB_MENU{
        @Override
        public State handle(Kiosk kiosk) {
            return kiosk.handleSubMenuState();
        }
    },
    CART{
        @Override
        public State handle(Kiosk kiosk) {
            return kiosk.handleCartState();
        }
    },
    ORDER{
        @Override
        public State handle(Kiosk kiosk) {
            return kiosk.handleOrderState();
        }
    },
    EXIT{
        @Override
        public State handle(Kiosk kiosk) {
            System.out.println("다음에 다시 찾아주세요 💙");
            kiosk.closeScanner();
            return EXIT;
        }
    };

    public abstract State handle(Kiosk kiosk);
}
