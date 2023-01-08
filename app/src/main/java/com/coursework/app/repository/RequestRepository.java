package com.coursework.app.repository;

import com.coursework.app.entity.Request;
import com.coursework.app.entity.RequestProduct;
import com.coursework.app.utils.DBProperties;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class RequestRepository {
    private final SalePointRepository salePointRepository = new SalePointRepository();

    public List<Request> getRequestsBySalePointId(int id) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DBProperties.URL, DBProperties.userName, DBProperties.password)) {
            PreparedStatement statement = connection.prepareStatement("""
                    SELECT * FROM Requests WHERE SalePointId=?""");
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            List<Request> list = new ArrayList<>();
            while (resultSet.next()) {
                list.add(new Request(
                        resultSet.getInt("RequestId"),
                        salePointRepository.getSalePointById(resultSet.getInt("SalePointId")),
                        resultSet.getBoolean("IsProcessed"),
                        LocalDate.parse(resultSet.getString("CreationDate")),
                        resultSet.getBoolean("IsProcessed") ?
                                LocalDate.parse(resultSet.getString("CompleteDate")) : null));
            }
            return list;
        }
    }

    public Request addRequest(Request request) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DBProperties.URL, DBProperties.userName, DBProperties.password)) {
            PreparedStatement statement = connection.prepareStatement("""
                    INSERT INTO Requests (SalePointId, IsProcessed, CreationDate) VALUES (?,?,?)""", Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, request.getSalePoint().getSalePointId());
            statement.setBoolean(2, request.isProcessed());
            statement.setString(3, request.getCreationDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            statement.execute();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                request.setRequestId(resultSet.getInt(1));
                return request;
            }
            return null;
        }
    }

    public RequestProduct addRequestProduct(RequestProduct product) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DBProperties.URL, DBProperties.userName, DBProperties.password)) {
            PreparedStatement statement = connection.prepareStatement("""
                    INSERT INTO RequestsProducts (RequestId, ProductId, Quantity) VALUES (?,?,?)""");
            statement.setInt(1, product.getRequest().getRequestId());
            statement.setInt(2, product.getProduct().getProductId());
            statement.setInt(3, product.getQuantity());
            statement.execute();
            return product;
        }
    }
}
