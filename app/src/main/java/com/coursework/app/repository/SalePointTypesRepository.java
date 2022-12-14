package com.coursework.app.repository;

import com.coursework.app.entity.SalePointType;
import com.coursework.app.utils.DBConstants;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SalePointTypesRepository {
    public List<SalePointType> getSalePointTypes() throws SQLException {
        try (Connection connection = DriverManager.getConnection(DBConstants.URL)) {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM SalePointTypes");
            ResultSet resultSet = statement.executeQuery();
            List<SalePointType> salePointTypes = new ArrayList<>();
            while (resultSet.next()) {
                salePointTypes.add(new SalePointType(resultSet.getInt("TypeId"),
                        resultSet.getString("TypeName")));
            }
            return salePointTypes;
        }
    }

    public SalePointType getSalePointType(int id) throws SQLException {
        Optional<SalePointType> salePointType = getSalePointTypes().stream()
                .filter(type -> type.getTypeId() == id).findFirst();
        return salePointType.orElse(null);
    }
}
