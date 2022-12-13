package com.coursework.app.utils;

import com.coursework.app.entity.Role;
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
}
