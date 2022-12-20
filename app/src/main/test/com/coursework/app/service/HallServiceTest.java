package com.coursework.app.service;

import com.coursework.app.entity.Hall;
import com.coursework.app.exception.AddHallException;
import com.coursework.app.exception.NoHallByIdException;
import com.coursework.app.exception.NoRoleByIdException;
import com.coursework.app.utils.DBProperties;
import org.junit.jupiter.api.*;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HallServiceTest {
    private static final File srcFile = new File(HallServiceTest.class.getClassLoader()
            .getResource("testDatabase.sqlite").getFile());
    private static final File destFile = new File(HallServiceTest.class.getClassLoader()
            .getResource("").getPath() + "/_testDB_.sqlite");
    private final HallService hallService = new HallService();

    private record Hall_(int hallId, String hallName, int pointId, boolean isActive) {
    }

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
    public void getHallsTest() {
        try {
            List<Hall> halls = hallService.getHalls();

            List<Hall_> halls_ = new ArrayList<>();
            halls_.add(new Hall_(1, "Зал 1", 3, true));
            halls_.add(new Hall_(2, "Зал 2", 2, true));
            halls_.add(new Hall_(3, "Зал 3", 2, true));

            for (int i = 0; i < halls.size(); i++) {
                assertEquals(halls.get(i).getHallId(), halls_.get(i).hallId);
                assertEquals(halls.get(i).getHallName(), halls_.get(i).hallName);
                assertEquals(halls.get(i).getSalePoint().getSalePointId(), halls_.get(i).pointId);
                assertEquals(halls.get(i).getIsActive(), halls_.get(i).isActive);
            }
        } catch (SQLException exception) {
            fail(exception.getMessage());
        }
    }

    @Test
    public void getHallByIdTest() {
        final int CORRECT_ID = 2;
        final int INCORRECT_ID = 999;

        Hall_ hall_ = new Hall_(2, "Зал 2", 2, true);

        try {
            Hall hall = hallService.getHallById(CORRECT_ID);
            assertEquals(hall.getHallId(), hall_.hallId);
            assertEquals(hall.getHallName(), hall_.hallName);
            assertEquals(hall.getSalePoint().getSalePointId(), hall_.pointId);
            assertEquals(hall.getIsActive(), hall_.isActive);
        } catch (SQLException | NoHallByIdException exception) {
            fail(exception.getMessage());
        }

        assertThrows(NoHallByIdException.class, () -> hallService.getHallById(INCORRECT_ID));
    }

    @Test
    public void addHallTest() {
        try {
            Hall hall = hallService.getHallById(2);
            Hall_ hall_ = new Hall_(4, "Зал 2", 2, true);

            Hall returnedHall = hallService.addHall(hall);
            Hall getHall = hallService.getHallById(4);

            assertEquals(returnedHall.getHallId(), hall_.hallId);
            assertEquals(returnedHall.getHallName(), hall_.hallName);
            assertEquals(returnedHall.getSalePoint().getSalePointId(), hall_.pointId);
            assertEquals(returnedHall.getIsActive(), hall_.isActive);

            assertEquals(getHall.getHallId(), hall_.hallId);
            assertEquals(getHall.getHallName(), hall_.hallName);
            assertEquals(getHall.getSalePoint().getSalePointId(), hall_.pointId);
            assertEquals(getHall.getIsActive(), hall_.isActive);

        } catch (SQLException | NoHallByIdException | AddHallException exception) {
            fail(exception.getMessage());
        }
    }

    @Test
    public void changeActiveStatusTest() {
        try {
            Hall_ hallBefore_ = new Hall_(3, "Зал 3", 2, true);
            Hall_ hallAfter_ = new Hall_(3, "Зал 3", 2, false);

            Hall hallBefore = hallService.getHallById(3);
            hallService.deactivateById(3);
            Hall hallAfter = hallService.getHallById(3);

            assertEquals(hallBefore.getHallId(), hallBefore_.hallId);
            assertEquals(hallBefore.getHallName(), hallBefore_.hallName);
            assertEquals(hallBefore.getSalePoint().getSalePointId(), hallBefore_.pointId);
            assertEquals(hallBefore.getIsActive(), hallBefore_.isActive);

            assertEquals(hallAfter.getHallId(), hallAfter_.hallId);
            assertEquals(hallAfter.getHallName(), hallAfter_.hallName);
            assertEquals(hallAfter.getSalePoint().getSalePointId(), hallAfter_.pointId);
            assertEquals(hallAfter.getIsActive(), hallAfter_.isActive);
        } catch (SQLException | NoHallByIdException exception) {
            fail(exception.getMessage());
        }
    }
}
