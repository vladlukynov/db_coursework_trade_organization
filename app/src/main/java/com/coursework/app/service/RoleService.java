package com.coursework.app.service;

import com.coursework.app.entity.Role;
import com.coursework.app.exception.NoRoleByIdException;
import com.coursework.app.repository.RoleRepository;

import java.sql.SQLException;
import java.util.List;

public class RoleService {
    private final RoleRepository roleRepository = new RoleRepository();
    public List<Role> getRoles() throws SQLException {
        return roleRepository.getRoles();
    }

    public Role getRoleById(int id) throws SQLException, NoRoleByIdException {
        Role role = roleRepository.getRoleById(id);
        if (role == null) {
            throw  new NoRoleByIdException("Роли с ID " + id + " не найдено");
        }
        return role;
    }
}
