package com.coursework.app.entity.queries;

public class TradeTurnover {
    private final String salePointName;
    private final String typeName;
    private final String productName;
    private final int quantity;
    private final double price;
    private final double discount;
    private final double total;
    private final double summa;

    public TradeTurnover(String salePointName, String typeName, String productName, int quantity, double price, double discount, double total, double summa) {
        this.salePointName = salePointName;
        this.typeName = typeName;
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
        this.discount = discount;
        this.total = total;
        this.summa = summa;
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

    public double getDiscount() {
        return discount;
    }

    public double getTotal() {
        return total;
    }

    public double getSumma() {
        return summa;
    }
}
