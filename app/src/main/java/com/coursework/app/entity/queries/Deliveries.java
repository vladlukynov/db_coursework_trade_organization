package com.coursework.app.entity.queries;

public class Deliveries {
    private final int requestId;
    private final String salePointName;
    private final String typeName;
    private final String productName;
    private final int quantity;
    private final double price;

    public Deliveries(int requestId, String salePointName, String typeName, String productName, int quantity, double price) {
        this.requestId = requestId;
        this.salePointName = salePointName;
        this.typeName = typeName;
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
    }

    public int getRequestId() {
        return requestId;
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
