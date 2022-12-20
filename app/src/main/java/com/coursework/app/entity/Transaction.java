package com.coursework.app.entity;

import java.util.Date;

public class Transaction {
    private int transactionId;
    private final Seller seller;
    private final Date transactionDate;

    public Transaction(Seller seller, Date transactionDate) {
        this.seller = seller;
        this.transactionDate = transactionDate;
    }

    public Transaction(int transactionId, Seller seller, Date transactionDate) {
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

    public Date getTransactionDate() {
        return transactionDate;
    }
}
