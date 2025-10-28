package com.example.kiosk;

/**
 * 할인에 대한 상수 집합 클래스
 * */
public enum Discount {
    DISABILITY("복지", 20),
    SOLIDER("군인", 10),
    STUDENT("학생", 5),
     GENERAL("일반", 0);

    private final String discountKorean;
    private final int discountRate;
    Discount(String discountKorean, int discountRate) {
        this.discountKorean = discountKorean;
        this.discountRate = discountRate;

    }
    public String getDiscountKorean() {
        return discountKorean;
    }
    public int getDiscountRate() {
        return discountRate;
    }
    /**
     * @param userSelect 유저가 선택한 할인코드 인덱스 1-based
     * */
    public int checkDiscountRate(int userSelect) {
        int index = 0;
        for (Discount discount : Discount.values()) {
            if (index == userSelect-1) {
                return discount.discountRate;
            }
            index++;
        }
        return 0;
    }

}
