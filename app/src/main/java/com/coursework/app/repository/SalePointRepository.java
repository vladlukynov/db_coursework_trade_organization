package com.coursework.app.repository;

import com.coursework.app.entity.SalePoint;
import com.coursework.app.utils.DBConstants;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SalePointRepository {
    private final SalePointTypesRepository salePointTypesRepository = new SalePointTypesRepository();

    public List<SalePoint> getSalePoints() throws SQLException {
        try (Connection connection = DriverManager.getConnection(DBConstants.URL)) {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM SalePoints");
            ResultSet resultSet = statement.executeQuery();
            List<SalePoint> salePoints = new ArrayList<>();
            while (resultSet.next()) {
                salePoints.add(new SalePoint(resultSet.getInt("SalePointId"),
                        salePointTypesRepository.getSalePointType(resultSet.getInt("TypeId")),
                        resultSet.getDouble("PointSize"),
                        resultSet.getDouble("RentalPrice"),
                        resultSet.getDouble("СommunalService"),
                        resultSet.getInt("CountersNumber"),
                        resultSet.getBoolean("IsActive")));
            }
            return salePoints;
        }
    }

    public void addSalePoint(SalePoint point) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DBConstants.URL)) {
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO SalePoints (TypeId, PointSize, RentalPrice, СommunalService, CountersNumber, IsActive) " +
                            "VALUES (?,?,?,?,?,?)");
            statement.setInt(1, point.getType().getTypeId());
            statement.setDouble(2, point.getPointSize());
            statement.setDouble(3, point.getRentalPrice());
            statement.setDouble(4, point.getComServ());
            statement.setInt(5, point.getCounters());
            statement.setBoolean(6, point.getIsActive());
            statement.execute();
        }
    }

    public void changeStatus(int id, boolean status) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DBConstants.URL)) {
            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE SalePoints SET IsActive=? WHERE SalePointId=?");
            statement.setBoolean(1, status);
            statement.setInt(2, id);
            statement.execute();
        }
    }
}
