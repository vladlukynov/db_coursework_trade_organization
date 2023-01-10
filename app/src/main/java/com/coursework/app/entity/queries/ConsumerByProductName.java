package com.coursework.app.entity.queries;

public class ConsumerByProductName {
    private final String firstName;
    private final String lastName;
    private final String middleName;
    private final int consumersCount;

    public ConsumerByProductName(String firstName, String lastName, String middleName, int consumersCount) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.consumersCount = consumersCount;
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

    public int getConsumersCount() {
        return consumersCount;
    }
}
