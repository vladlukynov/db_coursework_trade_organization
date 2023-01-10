package com.coursework.app.entity.queries;

public class SalesVolume {
    private final String salePointName;
    private final int quantity;

    public SalesVolume(String salePointName, int quantity) {
        this.salePointName = salePointName;
        this.quantity = quantity;
    }

    public String getSalePointName() {
        return salePointName;
    }

    public int getQuantity() {
        return quantity;
    }
}
