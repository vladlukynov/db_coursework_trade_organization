package com.coursework.app.entity;

public class SalePoint {
    private int salePointId;
    private final SalePointType type;
    private final double pointSize;
    private final double rentalPrice;
    private final double communalService;
    private final int countersNumber;
    private final boolean isActive;
    private final String salePointName;

    public SalePoint(SalePointType type, double pointSize, double rentalPrice, double communalService, int countersNumber, boolean isActive, String salePointName) {
        this.type = type;
        this.pointSize = pointSize;
        this.rentalPrice = rentalPrice;
        this.communalService = communalService;
        this.countersNumber = countersNumber;
        this.isActive = isActive;
        this.salePointName = salePointName;
    }

    public SalePoint(int salePointId, SalePointType type, double pointSize, double rentalPrice, double communalService, int countersNumber, boolean isActive, String salePointName) {
        this.salePointId = salePointId;
        this.type = type;
        this.pointSize = pointSize;
        this.rentalPrice = rentalPrice;
        this.communalService = communalService;
        this.countersNumber = countersNumber;
        this.isActive = isActive;
        this.salePointName = salePointName;
    }

    public int getSalePointId() {
        return salePointId;
    }

    public void setSalePointId(int salePointId) {
        this.salePointId = salePointId;
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

    public double getCommunalService() {
        return communalService;
    }

    public int getCountersNumber() {
        return countersNumber;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public String getSalePointName() {
        return salePointName;
    }
}
