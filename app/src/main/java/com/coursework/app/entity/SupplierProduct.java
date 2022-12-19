package com.coursework.app.entity;

public class SupplierProduct {
    private final Supplier supplier;
    private final Product product;
    private final double price;
    private final boolean isActive;

    public SupplierProduct(Supplier supplier, Product product, double price, boolean isActive) {
        this.supplier = supplier;
        this.product = product;
        this.price = price;
        this.isActive = isActive;
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

    public boolean isActive() {
        return isActive;
    }
}
