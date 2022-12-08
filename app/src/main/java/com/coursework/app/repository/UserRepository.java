package com.coursework.app.repository;

import com.coursework.app.entity.User;
import com.coursework.app.service.RoleService;
import com.coursework.app.utils.DBConstants;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepository {
    private final RoleService roleService = new RoleService();

    public List<User> getUsers() throws SQLException {
        try (Connection connection = DriverManager.getConnection(DBConstants.URL);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM Users");
             ResultSet resultSet = statement.executeQuery()) {
            List<User> users = new ArrayList<>();

            while (resultSet.next()) {
                users.add(new User(resultSet.getString("UserLogin"),
                        resultSet.getString("Password"),
                        resultSet.getString("FirstName"),
                        resultSet.getString("LastName"),
                        resultSet.getString("MiddleName"),
                        roleService.getRole(resultSet.getInt("RoleId")),
                        resultSet.getBoolean("IsActive")));
            }

            return users;
        }
    }
}
