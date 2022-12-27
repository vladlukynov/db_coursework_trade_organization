package com.coursework.app.repository;

import com.coursework.app.entity.Hall;
import com.coursework.app.utils.DBProperties;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HallRepository {
    private final SalePointRepository salePointRepository = new SalePointRepository();

    public List<Hall> getHalls() throws SQLException {
        try (Connection connection = DriverManager.getConnection(DBProperties.URL)) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Halls");
            ResultSet resultSet = statement.executeQuery();
            List<Hall> list = new ArrayList<>();
            while (resultSet.next()) {
                list.add(new Hall(resultSet.getInt("HallId"),
                        resultSet.getString("HallName"),
                        salePointRepository.getSalePointById(resultSet.getInt("SalePointId")),
                        resultSet.getBoolean("IsActive")));
            }
            return list;
        }
    }

    public Hall getHallById(int id) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DBProperties.URL)) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Halls WHERE HallId = ?");
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Hall(resultSet.getInt("HallId"),
                        resultSet.getString("HallName"),
                        salePointRepository.getSalePointById(resultSet.getInt("SalePointId")),
                        resultSet.getBoolean("IsActive"));
            }
            return null;
        }
    }

    public Hall addHall(Hall hall) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DBProperties.URL)) {
            PreparedStatement statement = connection.prepareStatement("""
                    INSERT INTO Halls(HallName, SalePointId, IsActive) VALUES (?,?,?)""", Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, hall.getHallName());
            statement.setInt(2, hall.getSalePoint().getSalePointId());
            statement.setBoolean(3, hall.getIsActive());
            statement.execute();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                hall.setHallId(resultSet.getInt(1));
                return hall;
            }
            return null;
        }
    }

    public void changeActiveStatus(int id, boolean status) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DBProperties.URL)) {
            PreparedStatement statement = connection.prepareStatement("""
                    UPDATE Halls SET IsActive=? WHERE HallId=?""");
            statement.setBoolean(1, status);
            statement.setInt(2, id);
            statement.execute();
        }
    }

    public List<Hall> getHallsBySalePointId(int salePointId) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DBProperties.URL)) {
            PreparedStatement statement = connection.prepareStatement("""
                    SELECT Halls.HallId, Halls.HallName, Halls.SalePointId, Halls.IsActive FROM Halls
                        JOIN SalePoints ON Halls.SalePointId = SalePoints.SalePointId
                    WHERE Halls.SalePointId=?""");
            statement.setInt(1, salePointId);
            ResultSet resultSet = statement.executeQuery();
            List<Hall> list = new ArrayList<>();
            while (resultSet.next()) {
                list.add(new Hall(resultSet.getInt("HallId"),
                        resultSet.getString("HallName"),
                        salePointRepository.getSalePointById(resultSet.getInt("SalePointId")),
                        resultSet.getBoolean("IsActive")));
            }
            return list;
        }
    }
}
