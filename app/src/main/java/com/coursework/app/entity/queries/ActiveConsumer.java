package com.coursework.app.entity.queries;

public class ActiveConsumer {
    private final int salePointId;
    private final String firstName;
    private final String lastName;
    private final String middleName;
    private final int consumersCount;

    public ActiveConsumer(int salePointId, String firstName, String lastName, String middleName, int consumersCount) {
        this.salePointId = salePointId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.consumersCount = consumersCount;
    }

    public int getSalePointId() {
        return salePointId;
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
