package com.coursework.app.entity;

import java.time.LocalDate;

public class Transaction {
    private int transactionId;
    private final Seller seller;
    private final LocalDate transactionDate;

    public Transaction(Seller seller, LocalDate transactionDate) {
        this.seller = seller;
        this.transactionDate = transactionDate;
    }

    public Transaction(int transactionId, Seller seller, LocalDate transactionDate) {
        this.transactionId = transactionId;
        this.seller = seller;
        this.transactionDate = transactionDate;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public Seller getSeller() {
        return seller;
    }

    public LocalDate getTransactionDate() {
        return transactionDate;
    }
}
