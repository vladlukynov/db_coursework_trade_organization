package com.coursework.app.service;

import com.coursework.app.entity.SalePointType;
import com.coursework.app.exception.NoSalePointTypeByIdException;
import com.coursework.app.utils.DBProperties;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class SalePointTypesServiceTest {
    private static final File srcFile = new File(RoleServiceTest.class.getClassLoader()
            .getResource("testDatabase.sqlite").getFile());
    private static final File destFile = new File(RoleServiceTest.class.getClassLoader()
            .getResource("").getPath() + "/_testDB_.sqlite");
    private final SalePointTypeService salePointTypeService = new SalePointTypeService();

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
    public void getSalePointTypeByIdTest() {
        try {
            final int CORRECT_ID = 3;
            final int INCORRECT_ID = 999;
            SalePointType type_ = new SalePointType(CORRECT_ID, "Киоск");

            SalePointType type = salePointTypeService.getSalePointTypeById(CORRECT_ID);

            assertEquals(type.getTypeId(), type_.getTypeId());
            assertEquals(type.getTypeName(), type_.getTypeName());

            assertThrows(NoSalePointTypeByIdException.class, () -> salePointTypeService.getSalePointTypeById(INCORRECT_ID));
        } catch (SQLException | NoSalePointTypeByIdException exception) {
            fail(exception.getMessage());
        }
    }
}
