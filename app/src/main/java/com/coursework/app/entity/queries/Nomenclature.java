package com.coursework.app.entity.queries;

public class Nomenclature {
    private final String productName;
    private final int quantity;
    private final double price;
    private final double discount;

    public Nomenclature(String productName, int quantity, double price, double discount) {
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
        this.discount = discount;
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

    public double getDiscount() {
        return discount;
    }
}
