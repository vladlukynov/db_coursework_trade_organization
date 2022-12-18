package com.coursework.app.repository;

import com.coursework.app.entity.Section;
import com.coursework.app.utils.DBConstants;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SectionRepository {
    private final HallRepository hallRepository = new HallRepository();

    public List<Section> getSections() throws SQLException {
        try (Connection connection = DriverManager.getConnection(DBConstants.URL)) {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM Sections");
            ResultSet resultSet = statement.executeQuery();
            List<Section> sections = new ArrayList<>();
            while (resultSet.next()) {
                sections.add(new Section(resultSet.getInt("SectionId"),
                        resultSet.getString("SectionName"),
                        hallRepository.getHall(resultSet.getInt("HallId")),
                        resultSet.getBoolean("IsActive")));
            }
            return sections;
        }
    }

    public List<Section> getSectionsBySalePointId(int salePointId) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DBConstants.URL)) {
            PreparedStatement statement = connection.prepareStatement("""
                    SELECT Sections.SectionId, Sections.SectionName, Sections.HallId, Sections.IsActive FROM Sections
                        JOIN Halls on Halls.HallId = Sections.HallId
                    WHERE Halls.SalePointId = ?;
                    """);
            statement.setInt(1, salePointId);
            ResultSet resultSet = statement.executeQuery();
            List<Section> list = new ArrayList<>();
            while (resultSet.next()) {
                list.add(new Section(resultSet.getInt("SectionId"),
                        resultSet.getString("SectionName"),
                        hallRepository.getHall(resultSet.getInt("HallId")),
                        resultSet.getBoolean("IsActive")));
            }
            return list;
        }
    }
}
