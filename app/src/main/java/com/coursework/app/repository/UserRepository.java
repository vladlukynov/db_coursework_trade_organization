package com.coursework.app.repository;

import com.coursework.app.entity.SalePoint;
import com.coursework.app.entity.Seller;
import com.coursework.app.entity.SuperVisor;
import com.coursework.app.entity.User;
import com.coursework.app.utils.DBConstants;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepository {
    private final RoleRepository roleRepository = new RoleRepository();
    private final SalePointTypesRepository salePointTypesRepository = new SalePointTypesRepository();
    private final HallRepository hallRepository = new HallRepository();

    public List<User> getUsers() throws SQLException {
        try (Connection connection = DriverManager.getConnection(DBConstants.URL)) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Users");
            ResultSet resultSet = statement.executeQuery();
            List<User> users = new ArrayList<>();

            while (resultSet.next()) {
                users.add(new User(resultSet.getString("UserLogin"),
                        resultSet.getString("Password"),
                        resultSet.getString("FirstName"),
                        resultSet.getString("LastName"),
                        resultSet.getString("MiddleName"),
                        roleRepository.getRole(resultSet.getInt("RoleId")),
                        resultSet.getBoolean("IsActive")));
            }

            return users;
        }
    }

    public User getUser(String userLogin) throws SQLException {
        List<User> users = getUsers();
        Optional<User> optionalUser = users.stream().filter(user -> user.getUserLogin().equals(userLogin)).findFirst();
        return optionalUser.orElse(null);
    }

    public void setActiveStatus(String userLogin, int status) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DBConstants.URL)) {
            PreparedStatement statement = connection.prepareStatement("UPDATE Users SET IsActive=? WHERE UserLogin=?");
            statement.setInt(1, status);
            statement.setString(2, userLogin);
            statement.execute();
        }
    }

    public void addUser(User user) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DBConstants.URL)) {
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO Users (UserLogin, Password, FirstName, LastName, MiddleName, RoleId, IsActive) VALUES (?,?,?,?,?,?,?)");
            statement.setString(1, user.getUserLogin());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getFirstName());
            statement.setString(4, user.getLastName());
            statement.setString(5, user.getMiddleName());
            statement.setInt(6, user.getRole().getRoleId());
            statement.setBoolean(7, user.getIsActive());

            statement.execute();
        }
    }

    public void addSuperVisor(SuperVisor superVisor) throws SQLException {
        addUser(superVisor);

        try (Connection connection = DriverManager.getConnection(DBConstants.URL)) {
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO SuperVisors (UserLogin, SectionId) VALUES (?,?)");
            statement.setString(1, superVisor.getUserLogin());
            statement.setInt(2, superVisor.getSection().getSectionId());

            statement.execute();
        }
    }

    public void addSeller(Seller seller) throws SQLException {
        addUser(seller);

        try (Connection connection = DriverManager.getConnection(DBConstants.URL)) {
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO Sellers (UserLogin, HallId) VALUES (?,?)");
            statement.setString(1, seller.getUserLogin());
            statement.setInt(2, seller.getHall().getHallId());

            statement.execute();
        }
    }

    public SalePoint getSellerSalePoint(String sellerLogin) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DBConstants.URL)) {
            PreparedStatement statement = connection.prepareStatement("""
                    SELECT SalePoints.SalePointId, SalePoints.TypeId, SalePoints.PointSize, SalePoints.RentalPrice,SalePoints.СommunalService,
                           SalePoints.CountersNumber, SalePoints.IsActive, SalePoints.SalePointName FROM Sellers
                        JOIN Halls ON Sellers.HallId = Halls.HallId
                            JOIN SalePoints ON Halls.SalePointId = SalePoints.SalePointId
                    WHERE Sellers.UserLogin = ?
                    """);
            statement.setString(1, sellerLogin);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new SalePoint(resultSet.getInt("SalePointId"),
                        resultSet.getString("SalePointName"),
                        salePointTypesRepository.getSalePointType(resultSet.getInt("TypeId")),
                        resultSet.getDouble("PointSize"),
                        resultSet.getDouble("RentalPrice"),
                        resultSet.getDouble("СommunalService"),
                        resultSet.getInt("CountersNumber"),
                        resultSet.getBoolean("IsActive"));
            }
            return null;
        }
    }

    public Seller getSeller(String sellerLogin) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DBConstants.URL)) {
            PreparedStatement statement = connection.prepareStatement("""
                    SELECT Sellers.UserLogin, HallId, Password, FirstName, LastName, MiddleName, RoleId, IsActive FROM Sellers
                        JOIN Users ON Sellers.UserLogin = Users.UserLogin
                    WHERE Users.UserLogin = ?
                    """);
            statement.setString(1, sellerLogin);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Seller(resultSet.getString("UserLogin"),
                        resultSet.getString("Password"),
                        resultSet.getString("FirstName"),
                        resultSet.getString("LastName"),
                        resultSet.getString("MiddleName"),
                        roleRepository.getRole(resultSet.getInt("RoleId")),
                        resultSet.getBoolean("IsActive"),
                        hallRepository.getHall(resultSet.getInt("HallId")));
            }
            return null;
        }
    }
}
