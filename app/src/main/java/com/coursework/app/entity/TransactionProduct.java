package com.coursework.app.entity;

public class TransactionProduct {
    private Transaction transaction;
    private final Product product;
    private int quantity;

    public TransactionProduct(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }
    public TransactionProduct(Transaction transaction, Product product, int quantity) {
        this.transaction = transaction;
        this.product = product;
        this.quantity = quantity;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }
}
