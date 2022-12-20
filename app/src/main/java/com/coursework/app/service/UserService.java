package com.coursework.app.service;

import com.coursework.app.entity.Seller;
import com.coursework.app.entity.SuperVisor;
import com.coursework.app.entity.User;
import com.coursework.app.exception.NoUserByLoginException;
import com.coursework.app.repository.UserRepository;

import java.sql.*;
import java.util.List;

public class UserService {
    private final UserRepository userRepository = new UserRepository();

    public List<User> getUsers() throws SQLException {
        return userRepository.getUsers();
    }

    public User getUserByLogin(String login) throws SQLException, NoUserByLoginException {
        User user = userRepository.getUserByLogin(login);
        if (user == null) {
            throw new NoUserByLoginException("Пользователя " + login + " не найдено");
        }
        return user;
    }

    public Seller getSellerByLogin(String login) throws SQLException, NoUserByLoginException {
        Seller seller = userRepository.getSellerByLogin(login);
        if (seller == null) {
            throw new NoUserByLoginException("Продавца " + login + " не найдено");
        }
        return seller;
    }

    public SuperVisor getSuperVisorByLogin(String login) throws SQLException, NoUserByLoginException {
        SuperVisor superVisor = userRepository.getSuperVisorByLogin(login);
        if (superVisor == null) {
            throw new NoUserByLoginException("Руководителя " + login + " не найдено");
        }
        return superVisor;
    }

    public User addUser(User user) throws SQLException {
        return userRepository.addUser(user);
    }

    public Seller addSeller(Seller seller) throws SQLException {
        return userRepository.addSeller(seller);
    }

    public SuperVisor addSuperVisor(SuperVisor superVisor) throws SQLException {
        return userRepository.addSuperVisor(superVisor);
    }

    public void deactivateUser(String login) throws SQLException {
        userRepository.changeActiveStatus(login, false);
    }
}
