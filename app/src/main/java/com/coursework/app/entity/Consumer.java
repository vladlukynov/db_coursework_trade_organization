package com.coursework.app.entity;

public class Consumer {
    private int consumerId;
    private final String firstName;
    private final String lastName;
    private final String middleName;
    private final Transaction transaction;

    public Consumer(int consumerId, String firstName, String lastName, String middleName, Transaction transaction) {
        this.consumerId = consumerId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.transaction = transaction;
    }

    public Consumer(String firstName, String lastName, String middleName, Transaction transaction) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.transaction = transaction;
    }

    public int getConsumerId() {
        return consumerId;
    }

    public void setConsumerId(int consumerId) {
        this.consumerId = consumerId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public Transaction getTransaction() {
        return transaction;
    }
}
