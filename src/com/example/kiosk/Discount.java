package com.example.kiosk;

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
