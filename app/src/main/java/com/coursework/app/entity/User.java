package com.coursework.app.entity;

public class User {
    private final String userLogin;
    private final String password;
    private final String firstName;
    private final String lastName;
    private final String middleName;
    private final Role role;
    private final boolean isActive;

    public User(String userLogin, String password, String firstName, String lastName, String middleName, Role role, boolean isActive) {
        this.userLogin = userLogin;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.role = role;
        this.isActive = isActive;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public String getPassword() {
        return password;
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

    public Role getRole() {
        return role;
    }

    public boolean isActive() {
        return isActive;
    }

    @Override
    public String toString() {
        return "User{" +
                "userLogin='" + userLogin + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", role=" + role +
                ", isActive=" + isActive +
                '}';
    }
}
