package com.coursework.app.service;

import com.coursework.app.entity.Seller;
import com.coursework.app.entity.SuperVisor;
import com.coursework.app.entity.User;
import com.coursework.app.exception.NoUserByLoginException;
import com.coursework.app.repository.UserRepository;

import java.sql.SQLException;
import java.util.List;

public class UserService {
    private final UserRepository userRepository = new UserRepository();

    public List<User> getUsers() throws SQLException {
        return userRepository.getUsers();
    }

    public User getUser(String userLogin) throws SQLException, NoUserByLoginException {
        User user = userRepository.getUser(userLogin);

        if (user != null) {
            return user;
        }

        throw new NoUserByLoginException("Not find user by login " + userLogin);
    }

    public void setActiveStatus(String userLogin, int status) throws SQLException {
        userRepository.setActiveStatus(userLogin, status);
    }

    public void addUser(User user) throws SQLException {
        userRepository.addUser(user);
    }

    public void addSuperVisor(SuperVisor superVisor) throws SQLException {
        userRepository.addSuperVisor(superVisor);
    }

    public void addSeller(Seller seller) throws SQLException {
        userRepository.addSeller(seller);
    }
}
