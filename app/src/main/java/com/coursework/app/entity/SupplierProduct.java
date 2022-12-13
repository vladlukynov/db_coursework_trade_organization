package com.coursework.app.entity;

public class SupplierProduct {
    private final Supplier supplier;
    private final Product product;
    private final double price;

    public SupplierProduct(Supplier supplier, Product product, double price) {
        this.supplier = supplier;
        this.product = product;
        this.price = price;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public Product getProduct() {
        return product;
    }

    public double getPrice() {
        return price;
    }
}
