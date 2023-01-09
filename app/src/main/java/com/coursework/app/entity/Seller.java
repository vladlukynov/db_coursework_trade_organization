package com.coursework.app.entity;

public class Seller extends User {
    private final Hall hall;
    private final double salary;

    public Seller(String userLogin, String password, String firstName, String lastName, String middleName, Role role,
                boolean isActive, Hall hall, double salary) {
        super(userLogin, password, firstName, lastName, middleName, role, isActive);
        this.hall = hall;
        this.salary = salary;
    }

    public Hall getHall() {
        return hall;
    }

    public double getSalary() {
        return salary;
    }
}
