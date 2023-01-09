package com.coursework.app.entity.queries;

public class SuppliersByOrder {
    private final String salePointName;
    private final String typeName;
    private final String productName;
    private final int quantity;
    private final double price;

    public SuppliersByOrder(String salePointName, String typeName, String productName, int quantity, double price) {
        this.salePointName = salePointName;
        this.typeName = typeName;
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
    }

    public String getSalePointName() {
        return salePointName;
    }

    public String getTypeName() {
        return typeName;
    }

    public String getProductName() {
        return productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }
}
