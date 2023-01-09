package com.coursework.app.service;

import com.coursework.app.entity.Seller;
import com.coursework.app.entity.SuperVisor;
import com.coursework.app.entity.User;
import com.coursework.app.entity.queries.SalePointsSellers;
import com.coursework.app.exception.GetDBInformationException;
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

    public void updateUser(User user, String oldLogin) throws SQLException {
        userRepository.updateUser(user, oldLogin);
    }

    public Seller addSeller(Seller seller) throws SQLException {
        return userRepository.addSeller(seller);
    }

    public void updateSeller(Seller seller, String oldLogin) throws SQLException {
        userRepository.updateSeller(seller, oldLogin);
    }

    public SuperVisor addSuperVisor(SuperVisor superVisor) throws SQLException {
        return userRepository.addSuperVisor(superVisor);
    }

    public void updateSupervisor(SuperVisor superVisor, String oldLogin) throws SQLException {
        userRepository.updateSuperVisor(superVisor, oldLogin);
    }

    public void deactivateUser(String login) throws SQLException {
        userRepository.changeActiveStatus(login, false);
    }

    public boolean isSeller(String login) throws SQLException {
        return userRepository.isSeller(login);
    }

    public boolean isSuperVisor(String login) throws SQLException {
        return userRepository.isSuperVisor(login);
    }

    /* ******************* Продавцы ******************* */
    public List<SalePointsSellers> getAllSalePointsSellers() throws SQLException {
        return userRepository.getAllSalePointsSellers();
    }

    public List<SalePointsSellers> getSalePointSellers(String salePointTypeName) throws SQLException {
        return userRepository.getSalePointSellers(salePointTypeName);
    }

    public SalePointsSellers getSalePointSeller(String login) throws SQLException, GetDBInformationException {
        SalePointsSellers result = userRepository.getSalePointSeller(login);
        if (result == null) {
            throw new GetDBInformationException("Выработка по данному продавцу отсутствует в базе данных");
        }
        return result;
    }

    public double getRelation(String typeName) throws SQLException {
        return userRepository.getRelation(typeName);
    }
}
