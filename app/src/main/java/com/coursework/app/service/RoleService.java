package com.coursework.app.service;

import com.coursework.app.entity.Role;
import com.coursework.app.repository.RoleRepository;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class RoleService {
    private final RoleRepository roleRepository = new RoleRepository();

    public List<Role> getRoles() throws SQLException {
        return roleRepository.getRoles();
    }

    public Role getRole(int roleId) throws SQLException {
        List<Role> roles = roleRepository.getRoles();
        Optional<Role> roleOptional = roles.stream().filter(role -> role.getRoleId() == roleId).findFirst();

        return roleOptional.orElse(null);
    }
}
