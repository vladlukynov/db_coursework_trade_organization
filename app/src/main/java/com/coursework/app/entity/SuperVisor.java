package com.coursework.app.entity;

public class SuperVisor extends User {
    private final Section section;

    public SuperVisor(String userLogin, String password, String firstName, String lastName, String middleName, Role role,
                      boolean isActive, Section section) {
        super(userLogin, password, firstName, lastName, middleName, role, isActive);
        this.section = section;
    }

    public Section getSection() {
        return section;
    }
}
