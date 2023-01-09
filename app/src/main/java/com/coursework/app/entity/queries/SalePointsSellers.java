package com.coursework.app.entity.queries;

public class SalePointsSellers {
    private final String sellerLogin;
    private final String firstName;
    private final String lastName;
    private final String middleName;
    private final String salePointName;
    private final String typeName;
    private final double total;

    public SalePointsSellers(String sellerLogin, String firstName, String lastName, String middleName,
                             String salePointName, String typeName, double total) {
        this.sellerLogin = sellerLogin;
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.salePointName = salePointName;
        this.typeName = typeName;
        this.total = total;
    }

    public String getSellerLogin() {
        return sellerLogin;
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

    public String getSalePointName() {
        return salePointName;
    }

    public String getTypeName() {
        return typeName;
    }

    public double getTotal() {
        return total;
    }
}
