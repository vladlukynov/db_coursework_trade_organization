package com.coursework.app.repository;

import com.coursework.app.entity.Section;
import com.coursework.app.utils.DBProperties;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SectionRepository {
    private final HallRepository hallRepository = new HallRepository();

    public List<Section> getSections() throws SQLException {
        try (Connection connection = DriverManager.getConnection(DBProperties.URL, DBProperties.userName, DBProperties.password)) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Sections");
            ResultSet resultSet = statement.executeQuery();
            List<Section> list = new ArrayList<>();
            while (resultSet.next()) {
                list.add(new Section(resultSet.getInt("SectionId"),
                        resultSet.getString("SectionName"),
                        hallRepository.getHallById(resultSet.getInt("HallId")),
                        resultSet.getBoolean("IsActive")));
            }
            return list;
        }
    }

    public Section getSectionById(int id) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DBProperties.URL, DBProperties.userName, DBProperties.password)) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Sections WHERE SectionId = ?");
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Section(resultSet.getInt("SectionId"),
                        resultSet.getString("SectionName"),
                        hallRepository.getHallById(resultSet.getInt("HallId")),
                        resultSet.getBoolean("IsActive"));
            }
            return null;
        }
    }

    public Section addSection(Section section) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DBProperties.URL, DBProperties.userName, DBProperties.password)) {
            PreparedStatement statement = connection.prepareStatement("""
                    INSERT INTO Sections(SectionName, HallId, IsActive) VALUES (?,?,?)""", Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, section.getSectionName());
            statement.setInt(2, section.getHall().getHallId());
            statement.setBoolean(3, section.getIsActive());
            statement.execute();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                section.setSectionId(resultSet.getInt(1));
                return section;
            }
            return null;
        }
    }

    public void changeActiveStatus(int id, boolean status) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DBProperties.URL, DBProperties.userName, DBProperties.password)) {
            PreparedStatement statement = connection.prepareStatement("""
                    UPDATE Sections SET IsActive = ? WHERE SectionId = ?""");
            statement.setBoolean(1, status);
            statement.setInt(2, id);
            statement.execute();
        }
    }

    public List<Section> getSectionsBySalePointId(int salePointId) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DBProperties.URL, DBProperties.userName, DBProperties.password)) {
            PreparedStatement statement = connection.prepareStatement("""
                    SELECT Sections.SectionId, Sections.SectionName, Sections.HallId, Sections.IsActive FROM Sections
                        JOIN Halls ON Sections.HallId = Halls.HallId
                    WHERE Halls.SalePointId=?""");
            statement.setInt(1, salePointId);
            ResultSet resultSet = statement.executeQuery();
            List<Section> list = new ArrayList<>();
            while (resultSet.next()) {
                list.add(new Section(resultSet.getInt("SectionId"),
                        resultSet.getString("SectionName"),
                        hallRepository.getHallById(resultSet.getInt("HallId")),
                        resultSet.getBoolean("IsActive")));
            }
            return list;
        }
    }

    public List<Section> getSectionsByHallId(int id) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DBProperties.URL, DBProperties.userName, DBProperties.password)) {
            PreparedStatement statement = connection.prepareStatement("""
                    SELECT * FROM Sections WHERE HallId=?""");
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            List<Section> list = new ArrayList<>();
            while (resultSet.next()) {
                list.add(new Section(resultSet.getInt("SectionId"),
                        resultSet.getString("SectionName"),
                        hallRepository.getHallById(resultSet.getInt("HallId")),
                        resultSet.getBoolean("IsActive")));
            }
            return list;
        }
    }
}
