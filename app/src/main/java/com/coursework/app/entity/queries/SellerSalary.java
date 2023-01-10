package com.coursework.app.entity.queries;

public class SellerSalary {
    private final String salePointName;
    private final String typeName;
    private final String firstName;
    private final String lastName;
    private final String middleName;
    private final double salary;

    public SellerSalary(String salePointName, String typeName, String firstName, String lastName, String middleName, double salary) {
        this.salePointName = salePointName;
        this.typeName = typeName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.salary = salary;
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

    public double getSalary() {
        return salary;
    }
}
