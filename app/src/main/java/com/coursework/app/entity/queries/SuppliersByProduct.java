package com.coursework.app.entity.queries;

public class SuppliersByProduct {
    private final String supplierName;
    private final int supplierCount;

    public SuppliersByProduct(String supplierName, int suppliersCount) {
        this.supplierName = supplierName;
        this.supplierCount = suppliersCount;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public int getSupplierCount() {
        return supplierCount;
    }
}
