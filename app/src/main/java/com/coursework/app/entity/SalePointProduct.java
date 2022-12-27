package com.coursework.app.entity;

public class SalePointProduct {
    private final Product product;
    private final SalePoint salePoint;
    private final int quantity;
    private final double price;
    private final double discount;

    public SalePointProduct(Product product, SalePoint salePoint, int quantity, double price, double discount) {
        this.product = product;
        this.salePoint = salePoint;
        this.quantity = quantity;
        this.price = price;
        this.discount = discount;
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

    public double getDiscount() {
        return discount;
    }
}
