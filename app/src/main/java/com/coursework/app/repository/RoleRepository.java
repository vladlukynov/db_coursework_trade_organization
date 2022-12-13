package com.coursework.app.repository;

import com.coursework.app.entity.Role;
import com.coursework.app.utils.DBConstants;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RoleRepository {
    public List<Role> getRoles() throws SQLException {
        try (Connection connection = DriverManager.getConnection(DBConstants.URL)) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Roles");
            ResultSet resultSet = statement.executeQuery();
            List<Role> roles = new ArrayList<>();

            while (resultSet.next()) {
                roles.add(new Role(resultSet.getInt("RoleId"),
                        resultSet.getString("RoleName")));
            }
            return roles;
        }
    }

    public Role getRole(int roleId) throws SQLException {
        List<Role> roles = getRoles();
        Optional<Role> optionalRole = roles.stream().filter(role -> role.getRoleId() == roleId).findFirst();
        return optionalRole.orElse(null);
    }
}
