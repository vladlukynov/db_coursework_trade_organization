package com.coursework.app.entity.queries;

import java.time.LocalDate;
import java.util.Date;

public class ConsumerProductInfo {
    private final String salePointName;
    private final String typeName;
    private final String firstName;
    private final String lastName;
    private final String middleName;
    private final LocalDate transactionDate;

    public ConsumerProductInfo(String salePointName, String typeName, String firstName, String lastName, String middleName, LocalDate transactionDate) {
        this.salePointName = salePointName;
        this.typeName = typeName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.transactionDate = transactionDate;
    }

    public String getSalePointName() {
        return salePointName;
    }

    public String getTypeName() {
        return typeName;
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

    public LocalDate getTransactionDate() {
        return transactionDate;
    }
}
