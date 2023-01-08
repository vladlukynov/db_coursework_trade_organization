package com.coursework.app.repository;

import com.coursework.app.entity.Product;
import com.coursework.app.utils.DBProperties;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductRepository {
    public List<Product> getProducts() throws SQLException {
        try (Connection connection = DriverManager.getConnection(DBProperties.URL, DBProperties.userName, DBProperties.password)) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Products");
            ResultSet resultSet = statement.executeQuery();
            List<Product> list = new ArrayList<>();
            while (resultSet.next()) {
                list.add(new Product(resultSet.getInt("ProductId"),
                        resultSet.getString("ProductName"),
                        resultSet.getBoolean("IsActive")));
            }
            return list;
        }
    }

    public Product getProductById(int id) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DBProperties.URL, DBProperties.userName, DBProperties.password)) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Products WHERE ProductId = ?");
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Product(resultSet.getInt("ProductId"),
                        resultSet.getString("ProductName"),
                        resultSet.getBoolean("IsActive"));
            }
            return null;
        }
    }

    public Product addProduct(Product product) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DBProperties.URL, DBProperties.userName, DBProperties.password)) {
            PreparedStatement statement = connection.prepareStatement("""
                    INSERT INTO Products(ProductName, IsActive) VALUES (?,?)""", Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, product.getProductName());
            statement.setBoolean(2, product.getIsActive());
            statement.execute();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                product.setProductId(resultSet.getInt(1));
                return product;
            }
            return null;
        }
    }

    public void changeActiveStatus(int id, boolean status) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DBProperties.URL, DBProperties.userName, DBProperties.password)) {
            PreparedStatement statement = connection.prepareStatement("""
                    UPDATE Products SET IsActive=? WHERE ProductId=?""");
            statement.setBoolean(1, status);
            statement.setInt(2, id);
            statement.execute();
        }
    }
}
