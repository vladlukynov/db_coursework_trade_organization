package com.coursework.app.entity;

public class SalePointType {
    private final int typeId;
    private final String typeName;

    public SalePointType(int typeId, String typeName) {
        this.typeId = typeId;
        this.typeName = typeName;
    }

    public int getTypeId() {
        return typeId;
    }

    public String getTypeName() {
        return typeName;
    }
}
