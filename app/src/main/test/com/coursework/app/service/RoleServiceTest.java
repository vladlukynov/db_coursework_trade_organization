package com.coursework.app.service;

import com.coursework.app.entity.Role;
import com.coursework.app.exception.NoRoleByIdException;
import com.coursework.app.utils.DBProperties;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class RoleServiceTest {
    private static final File srcFile = new File(RoleServiceTest.class.getClassLoader()
            .getResource("testDatabase.sqlite").getFile());
    private static final File destFile = new File(RoleServiceTest.class.getClassLoader()
            .getResource("").getPath() + "/_testDB_.sqlite");
    private final RoleService roleService = new RoleService();

    @BeforeEach
    public void initializeTest() {
        try {
            DBProperties.URL = "jdbc:sqlite:" + destFile;
            FileUtils.copyFile(srcFile, destFile);
        } catch (IOException exception) {
            System.err.println(exception.getMessage());
        }
    }

    @AfterEach
    public void deleteTestDB() {
        try {
            FileUtils.delete(destFile);
        } catch (IOException exception) {
            System.err.println(exception.getMessage());
        }
    }

    @Test
    public void getRoleByIdTest() {
        final int CORRECT_ID = 2;
        final int INCORRECT_ID = 999;

        Role role_ = new Role(CORRECT_ID, "Менеджер");
        try {
            Role role = roleService.getRoleById(CORRECT_ID);

            assertEquals(role.getRoleId(), role_.getRoleId());
            assertEquals(role.getRoleName(), role_.getRoleName());

            assertThrows(NoRoleByIdException.class, () -> roleService.getRoleById(INCORRECT_ID));

        } catch (SQLException | NoRoleByIdException exception) {
            fail(exception.getMessage());
        }
    }
}
