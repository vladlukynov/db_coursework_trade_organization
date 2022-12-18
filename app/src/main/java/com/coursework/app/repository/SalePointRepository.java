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
                        resultSet.getString("SalePointName"),
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

    public SalePoint getSalePointById(int id) throws SQLException {
        List<SalePoint> points = getSalePoints();
        return points.stream().filter(point -> point.getSalePointId() == id).findFirst().orElse(null);
    }

    public void addSalePoint(SalePoint point) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DBConstants.URL)) {
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO SalePoints (SalePointName, TypeId, PointSize, RentalPrice, СommunalService, CountersNumber, IsActive) " +
                            "VALUES (?,?,?,?,?,?,?)");
            statement.setString(1, point.getName());
            statement.setInt(2, point.getType().getTypeId());
            statement.setDouble(3, point.getPointSize());
            statement.setDouble(4, point.getRentalPrice());
            statement.setDouble(5, point.getComServ());
            statement.setInt(6, point.getCounters());
            statement.setBoolean(7, point.getIsActive());
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
