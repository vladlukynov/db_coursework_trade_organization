package com.coursework.app.utils;

import com.coursework.app.entity.*;
import javafx.util.StringConverter;

public class StringConverterUtils {
    public static StringConverter<Role> roleStringConverter = new StringConverter<>() {
        @Override
        public String toString(Role role) {
            return role.getRoleName();
        }

        @Override
        public Role fromString(String s) {
            return null;
        }
    };

    public static StringConverter<Boolean> accountActiveStringConverter = new StringConverter<>() {
        @Override
        public String toString(Boolean isActive) {
            return isActive ? "Нет" : "Да";
        }

        @Override
        public Boolean fromString(String s) {
            return null;
        }
    };

    public static StringConverter<Product> productNameStringConverter = new StringConverter<>() {
        @Override
        public String toString(Product product) {
            return product.getProductName();
        }

        @Override
        public Product fromString(String s) {
            return null;
        }
    };

    public static StringConverter<Supplier> supplierNameStringConverter = new StringConverter<>() {
        @Override
        public String toString(Supplier supplier) {
            return supplier.getSupplierName();
        }

        @Override
        public Supplier fromString(String s) {
            return null;
        }
    };

    public static StringConverter<SalePointType> salePointTypeStringConverter = new StringConverter<>() {
        @Override
        public String toString(SalePointType salePointType) {
            return salePointType.getTypeName();
        }

        @Override
        public SalePointType fromString(String s) {
            return null;
        }
    };

    public static StringConverter<SalePoint> salePointNameStringConverter = new StringConverter<>() {
        @Override
        public String toString(SalePoint salePoint) {
            return salePoint.getSalePointName();
        }

        @Override
        public SalePoint fromString(String s) {
            return null;
        }
    };

    public static StringConverter<Hall> hallNameStringConverter = new StringConverter<>() {
        @Override
        public String toString(Hall hall) {
            return hall.getHallName();
        }

        @Override
        public Hall fromString(String s) {
            return null;
        }
    };

    public static StringConverter<Section> sectionNameStringConverter = new StringConverter<>() {
        @Override
        public String toString(Section section) {
            return section.getSectionName();
        }

        @Override
        public Section fromString(String s) {
            return null;
        }
    };
}
