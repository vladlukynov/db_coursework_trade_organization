package com.coursework.app.entity;

public class SalePoint {
    private int salePointId;
    private final String name;
    private final SalePointType type;
    private final double pointSize;
    private final double rentalPrice;
    private final double comServ;
    private final int counters;
    private final boolean isActive;

    public SalePoint(String name, SalePointType type, double pointSize, double rentalPrice, double comServ, int counters, boolean isActive) {
        this.type = type;
        this.name = name;
        this.pointSize = pointSize;
        this.rentalPrice = rentalPrice;
        this.comServ = comServ;
        this.counters = counters;
        this.isActive = isActive;
    }

    public SalePoint(int salePointId, String name, SalePointType type, double pointSize, double rentalPrice, double comServ, int counters, boolean isActive) {
        this.salePointId = salePointId;
        this.name = name;
        this.type = type;
        this.pointSize = pointSize;
        this.rentalPrice = rentalPrice;
        this.comServ = comServ;
        this.counters = counters;
        this.isActive = isActive;
    }

    public int getSalePointId() {
        return salePointId;
    }

    public String getName() {
        return name;
    }

    public SalePointType getType() {
        return type;
    }

    public double getPointSize() {
        return pointSize;
    }

    public double getRentalPrice() {
        return rentalPrice;
    }

    public double getComServ() {
        return comServ;
    }

    public int getCounters() {
        return counters;
    }

    public boolean getIsActive() {
        return isActive;
    }
}
