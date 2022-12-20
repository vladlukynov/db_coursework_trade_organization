package com.coursework.app.entity;

public class Seller extends User {
    private final Hall hall;

    public Seller(String userLogin, String password, String firstName, String lastName, String middleName, Role role,
                boolean isActive, Hall hall) {
        super(userLogin, password, firstName, lastName, middleName, role, isActive);
        this.hall = hall;
    }

    public Hall getHall() {
        return hall;
    }
}
