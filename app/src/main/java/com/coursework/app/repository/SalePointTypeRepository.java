package com.coursework.app.repository;

import com.coursework.app.entity.SalePointType;
import com.coursework.app.utils.DBProperties;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SalePointTypeRepository {
    public List<SalePointType> getSalePointTypes() throws SQLException {
        try (Connection connection = DriverManager.getConnection(DBProperties.URL)) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM SalePointTypes");
            ResultSet resultSet = statement.executeQuery();
            List<SalePointType> list = new ArrayList<>();
            while (resultSet.next()) {
                list.add( new SalePointType(resultSet.getInt("TypeId"),
                        resultSet.getString("TypeName")));
            }
            return list;
        }
    }
    public SalePointType getSalePointTypeById(int id) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DBProperties.URL)) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM SalePointTypes WHERE TypeId = ?");
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new SalePointType(resultSet.getInt("TypeId"),
                        resultSet.getString("TypeName"));
            }
            return null;
        }
    }
}
