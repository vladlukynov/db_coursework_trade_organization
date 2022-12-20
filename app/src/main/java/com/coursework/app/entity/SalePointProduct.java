package com.coursework.app.entity;

public class SalePointProduct {
    private final Product product;
    private final SalePoint salePoint;
    private final int quantity;
    private final double price;

    public SalePointProduct(Product product, SalePoint salePoint, int quantity, double price) {
        this.product = product;
        this.salePoint = salePoint;
        this.quantity = quantity;
        this.price = price;
    }

    public Product getProduct() {
        return product;
    }

    public SalePoint getSalePoint() {
        return salePoint;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }
}
