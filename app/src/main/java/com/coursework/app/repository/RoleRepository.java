package com.coursework.app.repository;

import com.coursework.app.entity.Role;
import com.coursework.app.utils.DBProperties;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoleRepository {
    public List<Role> getRoles() throws SQLException {
        try (Connection connection = DriverManager.getConnection(DBProperties.URL)) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Roles");
            ResultSet resultSet = statement.executeQuery();
            List<Role> list = new ArrayList<>();
            while (resultSet.next()) {
                list.add(new Role(resultSet.getInt("RoleId"),
                        resultSet.getString("RoleName")));
            }
            return list;
        }
    }

    public Role getRoleById(int id) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DBProperties.URL)) {
            PreparedStatement statement = connection.prepareStatement("""
                    SELECT * FROM Roles WHERE RoleId = ?""");
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Role(resultSet.getInt("RoleId"),
                        resultSet.getString("RoleName"));
            }
            return null;
        }
    }
}
