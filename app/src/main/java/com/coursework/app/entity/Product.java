package com.coursework.app.entity;

public class Product {
    private int productId;
    private final String productName;
    private final boolean isActive;

    public Product(String productName, boolean isActive) {
        this.productName = productName;
        this.isActive = isActive;
    }

    public Product(int productId, String productName, boolean isActive) {
        this.productId = productId;
        this.productName = productName;
        this.isActive = isActive;
    }

    public int getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public boolean getIsActive() {
        return isActive;
    }
}
