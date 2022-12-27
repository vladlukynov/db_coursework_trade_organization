package com.coursework.app.entity;

public class Supplier {
    private int supplierId;
    private final String supplierName;
    private final boolean isActive;

    public Supplier(String supplierName, boolean isActive) {
        this.supplierName = supplierName;
        this.isActive = isActive;
    }

    public Supplier(int supplierId, String supplierName, boolean isActive) {
        this.supplierId = supplierId;
        this.supplierName = supplierName;
        this.isActive = isActive;
    }

    public int getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public boolean getIsActive() {
        return isActive;
    }
}
