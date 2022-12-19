package com.coursework.app.repository;

import com.coursework.app.entity.Product;
import com.coursework.app.utils.DBConstants;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductRepository {
    public List<Product> getProducts() throws SQLException {
        try (Connection connection = DriverManager.getConnection(DBConstants.URL)) {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM Products");
            ResultSet resultSet = statement.executeQuery();
            List<Product> products = new ArrayList<>();
            while (resultSet.next()) {
                products.add(new Product(resultSet.getInt("ProductId"),
                        resultSet.getString("ProductName"),
                        resultSet.getBoolean("IsActive")));
            }
            return products;
        }
    }

    public Product getProduct(int productId) throws SQLException {
        List<Product> products = getProducts();
        Optional<Product> optionalProduct = products.stream().filter(product -> product.getProductId() == productId).findFirst();
        return optionalProduct.orElse(null);
    }

    public void addProduct(Product product) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DBConstants.URL)) {
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO Products(ProductName, IsActive) VALUES (?,?)");
            statement.setString(1, product.getProductName());
            statement.setBoolean(2, product.getIsActive());
            statement.execute();
        }
    }

    public void changeActiveStatus(int productId, boolean status) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DBConstants.URL)) {
            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE Products SET IsActive=? WHERE ProductId=?");
            statement.setBoolean(1, status);
            statement.setInt(2, productId);
            statement.execute();
        }
    }

    public List<Product> getSalePointProducts(int salePointId) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DBConstants.URL)) {
            PreparedStatement statement = connection.prepareStatement("""
                    SELECT Products.ProductId, Products.ProductName, Products.IsActive FROM ProductsSalePoints
                        JOIN Products ON ProductsSalePoints.ProductId = Products.ProductId
                    WHERE SalePointId = ?
                    """);
            statement.setInt(1, salePointId);
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

    public int getSalePointProductQuantity(int salePointId, int productId) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DBConstants.URL)) {
            PreparedStatement statement = connection.prepareStatement("""
                    SELECT Quantity FROM ProductsSalePoints
                    WHERE SalePointId = ? AND ProductId = ?
                    """);
            statement.setInt(1, salePointId);
            statement.setInt(2, productId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("Quantity");
            }
            return 0;
        }
    }

    public void changeProduct(int productId, int salePointId, int minusQuantity) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DBConstants.URL)) {
            PreparedStatement statement = connection.prepareStatement("""
                    UPDATE ProductsSalePoints SET Quantity=? WHERE ProductId=?
                    """);

        }
    }
}
