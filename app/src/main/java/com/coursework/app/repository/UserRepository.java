package com.coursework.app.repository;

import com.coursework.app.entity.Seller;
import com.coursework.app.entity.SuperVisor;
import com.coursework.app.entity.User;
import com.coursework.app.utils.DBProperties;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepository {
    private final RoleRepository roleRepository = new RoleRepository();
    private final SectionRepository sectionRepository = new SectionRepository();
    private final HallRepository hallRepository = new HallRepository();

    public List<User> getUsers() throws SQLException {
        try (Connection connection = DriverManager.getConnection(DBProperties.URL)) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Users");
            ResultSet resultSet = statement.executeQuery();
            List<User> list = new ArrayList<>();
            while (resultSet.next()) {
                list.add(new User(resultSet.getString("UserLogin"),
                        resultSet.getString("Password"),
                        resultSet.getString("FirstName"),
                        resultSet.getString("LastName"),
                        resultSet.getString("MiddleName"),
                        roleRepository.getRoleById(resultSet.getInt("RoleId")),
                        resultSet.getBoolean("IsActive")));
            }
            return list;
        }
    }

    public List<Seller> getSellers() throws SQLException {
        try (Connection connection = DriverManager.getConnection(DBProperties.URL)) {
            PreparedStatement statement = connection.prepareStatement("""
                    SELECT Users.UserLogin, Users.Password, Users.FirstName, Users.LastName, Users.MiddleName, Users.RoleId, Users.IsActive, Sellers.HallId FROM Sellers 
                        JOIN Users ON Sellers.UserLogin = Users.UserLogin""");
            ResultSet resultSet = statement.executeQuery();
            List<Seller> list = new ArrayList<>();
            while (resultSet.next()) {
                list.add(new Seller(resultSet.getString("UserLogin"),
                        resultSet.getString("Password"),
                        resultSet.getString("FirstName"),
                        resultSet.getString("LastName"),
                        resultSet.getString("MiddleName"),
                        roleRepository.getRoleById(resultSet.getInt("RoleId")),
                        resultSet.getBoolean("IsActive"),
                        hallRepository.getHallById(resultSet.getInt("HallId"))));
            }
            return list;
        }
    }

    public List<SuperVisor> getSuperVisors() throws SQLException {
        try (Connection connection = DriverManager.getConnection(DBProperties.URL)) {
            PreparedStatement statement = connection.prepareStatement("""
                    SELECT Users.UserLogin, Users.Password, Users.FirstName, Users.LastName, Users.MiddleName, Users.RoleId, Users.IsActive, SuperVisors.SectionId FROM SuperVisors 
                        JOIN Users ON SuperVisors.UserLogin = Users.UserLogin""");
            ResultSet resultSet = statement.executeQuery();
            List<SuperVisor> list = new ArrayList<>();
            while (resultSet.next()) {
                list.add(new SuperVisor(resultSet.getString("UserLogin"),
                        resultSet.getString("Password"),
                        resultSet.getString("FirstName"),
                        resultSet.getString("LastName"),
                        resultSet.getString("MiddleName"),
                        roleRepository.getRoleById(resultSet.getInt("RoleId")),
                        resultSet.getBoolean("IsActive"),
                        sectionRepository.getSectionById(resultSet.getInt("SectionId"))));
            }
            return list;
        }
    }

    public User getUserByLogin(String loginUser) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DBProperties.URL)) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Users WHERE UserLogin = ?");
            statement.setString(1, loginUser);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new User(resultSet.getString("UserLogin"),
                        resultSet.getString("Password"),
                        resultSet.getString("FirstName"),
                        resultSet.getString("LastName"),
                        resultSet.getString("MiddleName"),
                        roleRepository.getRoleById(resultSet.getInt("RoleId")),
                        resultSet.getBoolean("IsActive"));
            }
            return null;
        }
    }

    public Seller getSellerByLogin(String login) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DBProperties.URL)) {
            PreparedStatement statement = connection.prepareStatement("""
                    SELECT Users.UserLogin, Users.Password, Users.FirstName, Users.LastName, Users.MiddleName, Users.RoleId, Users.IsActive, Sellers.HallId FROM Sellers
                        JOIN Users ON Sellers.UserLogin = Users.UserLogin
                    WHERE Sellers.UserLogin = ?""");
            statement.setString(1, login);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Seller(resultSet.getString("UserLogin"),
                        resultSet.getString("Password"),
                        resultSet.getString("FirstName"),
                        resultSet.getString("LastName"),
                        resultSet.getString("MiddleName"),
                        roleRepository.getRoleById(resultSet.getInt("RoleId")),
                        resultSet.getBoolean("IsActive"),
                        hallRepository.getHallById(resultSet.getInt("HallId")));
            }
            return null;
        }
    }

    public SuperVisor getSuperVisorByLogin(String login) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DBProperties.URL)) {
            PreparedStatement statement = connection.prepareStatement("""
                    SELECT Users.UserLogin, Users.Password, Users.FirstName, Users.LastName, Users.MiddleName, Users.RoleId, Users.IsActive, SuperVisors.SectionId FROM SuperVisors
                        JOIN Users ON SuperVisors.UserLogin = Users.UserLogin
                    WHERE SuperVisors.UserLogin = ?""");
            statement.setString(1, login);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new SuperVisor(resultSet.getString("UserLogin"),
                        resultSet.getString("Password"),
                        resultSet.getString("FirstName"),
                        resultSet.getString("LastName"),
                        resultSet.getString("MiddleName"),
                        roleRepository.getRoleById(resultSet.getInt("RoleId")),
                        resultSet.getBoolean("IsActive"),
                        sectionRepository.getSectionById(resultSet.getInt("SectionId")));
            }
            return null;
        }
    }

    public User addUser(User user) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DBProperties.URL)) {
            PreparedStatement statement = connection.prepareStatement("""
                    INSERT INTO Users(UserLogin, Password, FirstName, LastName, MiddleName, RoleId, IsActive) VALUES (?,?,?,?,?,?,?)""");
            statement.setString(1, user.getUserLogin());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getFirstName());
            statement.setString(4, user.getLastName());
            statement.setString(5, user.getMiddleName());
            statement.setInt(6, user.getRole().getRoleId());
            statement.setBoolean(7, user.getIsActive());
            statement.execute();

            return user;
        }
    }

    public Seller addSeller(Seller seller) throws SQLException {
        addUser(seller);
        try (Connection connection = DriverManager.getConnection(DBProperties.URL)) {
            PreparedStatement statement = connection.prepareStatement("""
                    INSERT INTO Sellers(UserLogin, HallId) VALUES (?,?)""");
            statement.setString(1, seller.getUserLogin());
            statement.setInt(2, seller.getHall().getHallId());
            statement.execute();

            return seller;
        }
    }

    public SuperVisor addSuperVisor(SuperVisor superVisor) throws SQLException {
        addUser(superVisor);
        try (Connection connection = DriverManager.getConnection(DBProperties.URL)) {
            PreparedStatement statement = connection.prepareStatement("""
                    INSERT INTO SuperVisors(UserLogin, SectionId) VALUES (?,?)""");
            statement.setString(1, superVisor.getUserLogin());
            statement.setInt(2, superVisor.getSection().getSectionId());
            statement.execute();

            return superVisor;
        }
    }

    public void changeActiveStatus(String login, boolean status) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DBProperties.URL)) {
            PreparedStatement statement = connection.prepareStatement("""
                    UPDATE Users SET IsActive = ? WHERE UserLogin = ?""");
            statement.setString(1, login);
            statement.setBoolean(2, status);
            statement.execute();
        }
    }
}
