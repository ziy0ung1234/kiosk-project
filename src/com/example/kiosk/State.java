package com.example.kiosk;

public enum State {
    START {
        @Override
        public State handle(Kiosk kiosk) {
            kiosk.showStart();
            int userSelect = kiosk.readUserInput(1,2);
            return userSelect == 1 ? MAIN_MENU : EXIT;
        }
    },
    MAIN_MENU {
        @Override
        public State handle(Kiosk kiosk) {
            int userSelect = kiosk.showMainMenuAndGetInput();
            if (userSelect == 0) return START; // 뒤로가기
            kiosk.setSelectedMainMenu(userSelect - 1);
            return kiosk.getSelectedMainMenu().isPresent() ? SUB_MENU : START;
        }
    },
    SUB_MENU {
        @Override
        public State handle(Kiosk kiosk) {
            int userSelect = kiosk.showSubMenuAndGetInput();
            if (userSelect == 0) {
                kiosk.setSelectedMainMenu(-1); // 뒤로가기
                return MAIN_MENU;
            } else return CART;
        }
    },
    CART {
        @Override
        public State handle(Kiosk kiosk) {
            int userSelect = kiosk.showCartAndGetInput();
            if (userSelect == 0) return MAIN_MENU; // 장바구니 추가 취소
            return userSelect == 1 ? MAIN_MENU : ORDER; // 1) 메뉴판 더보기 2) 결제
        }
    },
    ORDER {
        @Override
        public State handle(Kiosk kiosk) {
            int choice = kiosk.showOrderAndGetInput();
            return choice == 1 ? PAYMENT : ORDER;
        }
    },
    PAYMENT {
        @Override
        public State handle(Kiosk kiosk) {
            int choice = kiosk.showPaymentAndGetInput();
            return choice > 0 ? EXIT : CART;
        }
    },
    EXIT {
        @Override
        public State handle(Kiosk kiosk) {
            kiosk.showExit();
            kiosk.closeScanner();
            return this;
        }
    };

    public abstract State handle(Kiosk kiosk);
}
