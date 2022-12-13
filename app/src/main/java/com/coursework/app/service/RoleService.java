package com.coursework.app.service;

import com.coursework.app.entity.Role;
import com.coursework.app.repository.RoleRepository;

import java.sql.SQLException;
import java.util.List;

public class RoleService {
    private final RoleRepository roleRepository = new RoleRepository();

    public List<Role> getRoles() throws SQLException {
        return roleRepository.getRoles();
    }

    public Role getRole(int roleId) throws SQLException {
        return roleRepository.getRole(roleId);
    }
}
