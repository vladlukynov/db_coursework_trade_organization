package com.coursework.app.entity;

public class Hall {
    private int hallId;
    private final String hallName;
    private final SalePoint salePoint;
    private final boolean isActive;

    public Hall(String hallName, SalePoint salePoint, boolean isActive) {
        this.hallName = hallName;
        this.salePoint = salePoint;
        this.isActive = isActive;
    }

    public Hall(int hallId, String hallName, SalePoint salePoint, boolean isActive) {
        this.hallId = hallId;
        this.hallName = hallName;
        this.salePoint = salePoint;
        this.isActive = isActive;
    }

    public int getHallId() {
        return hallId;
    }

    public String getHallName() {
        return hallName;
    }

    public SalePoint getSalePoint() {
        return salePoint;
    }

    public boolean isActive() {
        return isActive;
    }
}
