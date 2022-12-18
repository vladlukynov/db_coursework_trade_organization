package com.coursework.app.repository;

import com.coursework.app.entity.Hall;
import com.coursework.app.utils.DBConstants;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HallRepository {
    private final SalePointRepository salePointRepository = new SalePointRepository();

    public List<Hall> getHalls() throws SQLException {
        try (Connection connection = DriverManager.getConnection(DBConstants.URL)) {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM Halls");
            ResultSet resultSet = statement.executeQuery();

            List<Hall> halls = new ArrayList<>();
            while (resultSet.next()) {
                halls.add(new Hall(resultSet.getInt("HallId"),
                        resultSet.getString("HallName"),
                        salePointRepository.getSalePointById(resultSet.getInt("SalePointId")),
                        resultSet.getBoolean("IsActive")
                ));
            }
            return halls;
        }
    }

    public void addHall(Hall hall) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DBConstants.URL)) {
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO Halls(HallName, SalePointId, IsActive) VALUES (?,?,?)");
            statement.setString(1, hall.getHallName());
            statement.setInt(2, hall.getSalePoint().getSalePointId());
            statement.setBoolean(3, hall.isActive());
            statement.execute();
        }
    }

    public void changeHallStatus(int hallId, boolean status) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DBConstants.URL)) {
            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE Halls SET IsActive=? WHERE HallId=?");
            statement.setBoolean(1, status);
            statement.setInt(2, hallId);
            statement.execute();
        }
    }
}
