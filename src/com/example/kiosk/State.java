package com.example.kiosk;

import java.util.function.Consumer;

public enum State {
    //람다 메서드 참조
    START(Kiosk::handleStartState),
    MAIN_MENU(Kiosk::handleMainMenuState),
    SUB_MENU(Kiosk::handleSubMenuState),
    EXIT(Kiosk::handleExitState);

    //Consumer 인터페이스 (하나의 입력을 받아서 처리, 아무것도 반환하지 않는(void))
    private final Consumer<Kiosk> handle;
    State(Consumer<Kiosk> handle) {
        this.handle = handle;
    }
    public void run(Kiosk kiosk) {
        //Consumer의 단일 추상 메서드
        handle.accept(kiosk);
    }
}
