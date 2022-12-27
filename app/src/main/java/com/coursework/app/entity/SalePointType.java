package com.coursework.app.entity;

public class SalePointType {
    private int typeId;
    private final String typeName;

    public SalePointType(String typeName) {
        this.typeName = typeName;
    }

    public SalePointType(int typeId, String typeName) {
        this.typeId = typeId;
        this.typeName = typeName;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }
}
