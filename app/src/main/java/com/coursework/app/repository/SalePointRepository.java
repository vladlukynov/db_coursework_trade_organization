package com.coursework.app.repository;

import com.coursework.app.entity.Product;
import com.coursework.app.entity.SalePoint;
import com.coursework.app.entity.SalePointProduct;
import com.coursework.app.utils.DBProperties;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SalePointRepository {
    private final SalePointTypeRepository salePointTypeRepository = new SalePointTypeRepository();
    private final ProductRepository productRepository = new ProductRepository();

    public List<SalePoint> getSalePoints() throws SQLException {
        try (Connection connection = DriverManager.getConnection(DBProperties.URL)) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM SalePoints");
            ResultSet resultSet = statement.executeQuery();
            List<SalePoint> list = new ArrayList<>();
            while (resultSet.next()) {
                list.add(new SalePoint(resultSet.getInt("SalePointId"),
                        salePointTypeRepository.getSalePointTypeById(resultSet.getInt("TypeId")),
                        resultSet.getDouble("PointSize"),
                        resultSet.getDouble("RentalPrice"),
                        resultSet.getDouble("CommunalService"),
                        resultSet.getInt("CountersNumber"),
                        resultSet.getBoolean("IsActive"),
                        resultSet.getString("SalePointName")));
            }
            return list;
        }
    }

    public SalePoint getSalePointById(int id) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DBProperties.URL)) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM SalePoints WHERE SalePointId=?");
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new SalePoint(resultSet.getInt("SalePointId"),
                        salePointTypeRepository.getSalePointTypeById(resultSet.getInt("TypeId")),
                        resultSet.getDouble("PointSize"),
                        resultSet.getDouble("RentalPrice"),
                        resultSet.getDouble("CommunalService"),
                        resultSet.getInt("CountersNumber"),
                        resultSet.getBoolean("IsActive"),
                        resultSet.getString("SalePointName"));
            }
            return null;
        }
    }

    public SalePoint addSalePoint(SalePoint salePoint) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DBProperties.URL)) {
            PreparedStatement statement = connection.prepareStatement("""
                    INSERT INTO SalePoints(TypeId, PointSize, RentalPrice, CommunalService, CountersNumber, IsActive, SalePointName)
                        VALUES (?,?,?,?,?,?,?)""", Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, salePoint.getType().getTypeId());
            statement.setDouble(2, salePoint.getPointSize());
            statement.setDouble(3, salePoint.getRentalPrice());
            statement.setDouble(4, salePoint.getCommunalService());
            statement.setInt(5, salePoint.getCountersNumber());
            statement.setBoolean(6, salePoint.getIsActive());
            statement.setString(7, salePoint.getSalePointName());
            statement.execute();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                salePoint.setSalePointId(resultSet.getInt(1));
                return salePoint;
            }
            return null;
        }
    }

    public void changeActiveStatusById(int id, boolean status) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DBProperties.URL)) {
            PreparedStatement statement = connection.prepareStatement("""
                    UPDATE SalePoints SET IsActive=?
                        WHERE SalePointId=?""");
            statement.setBoolean(1, status);
            statement.setInt(2, id);
            statement.execute();
        }
    }

    public List<Product> getSalePointProducts(int salePointId) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DBProperties.URL)) {
            PreparedStatement statement = connection.prepareStatement("""
                    SELECT ProductId FROM ProductsSalePoints WHERE SalePointId=?""");
            statement.setInt(1, salePointId);
            ResultSet resultSet = statement.executeQuery();
            List<Product> list = new ArrayList<>();
            while (resultSet.next()) {
                list.add(productRepository.getProductById(resultSet.getInt("ProductId")));
            }
            return list;
        }
    }

    public SalePointProduct addSalePointProduct(SalePointProduct product) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DBProperties.URL)) {
            PreparedStatement statement;
            SalePointProduct product_;
            if (isExistsProduct(product.getSalePoint().getSalePointId(), product.getProduct().getProductId())) {
                statement = connection.prepareStatement("""
                        UPDATE ProductsSalePoints SET Quantity=? WHERE SalePointId=? AND ProductId=?""");
                int newQuantity = getSalePointProductQuantity(product.getSalePoint().getSalePointId(),
                        product.getProduct().getProductId()) + product.getQuantity();
                statement.setInt(1, newQuantity);
                statement.setInt(2, product.getSalePoint().getSalePointId());
                statement.setInt(3, product.getProduct().getProductId());
                product_ = new SalePointProduct(product.getProduct(), product.getSalePoint(),
                        newQuantity, product.getPrice());
            } else {
                statement = connection.prepareStatement("""
                        INSERT INTO ProductsSalePoints(ProductId, SalePointId, Quantity, Price)  VALUES (?,?,?,?)""");
                statement.setInt(1, product.getProduct().getProductId());
                statement.setInt(2, product.getSalePoint().getSalePointId());
                statement.setInt(3, product.getQuantity());
                statement.setDouble(4, product.getPrice());
                product_ = product;
            }
            statement.execute();

            return product_;
        }
    }

    public void changeSalePointProductQuantity(int productId, int salePointId, int quantity) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DBProperties.URL)) {
            PreparedStatement statement = connection.prepareStatement("""
                    UPDATE ProductsSalePoints SET Quantity=? WHERE ProductId=? AND SalePointId=?""");
            statement.setInt(1, quantity);
            statement.setInt(2, productId);
            statement.setInt(3, salePointId);
            statement.execute();
        }
    }

    public SalePoint getSalePointBySellerLogin(String sellerLogin) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DBProperties.URL)) {
            PreparedStatement statement = connection.prepareStatement("""
                    SELECT Halls.SalePointId FROM Sellers
                        JOIN Halls ON Sellers.HallId = Halls.HallId
                    WHERE Sellers.UserLogin=?""");
            statement.setString(1, sellerLogin);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return getSalePointById(resultSet.getInt("SalePointId"));
            }
            return null;
        }
    }

    public int getSalePointProductQuantity(int salePointId, int productId) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DBProperties.URL)) {
            PreparedStatement statement = connection.prepareStatement("""
                    SELECT Quantity FROM ProductsSalePoints WHERE SalePointId = ? AND ProductId = ?""");
            statement.setInt(1, salePointId);
            statement.setInt(2, productId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
            return 0;
        }
    }

    private boolean isExistsProduct(int salePointId, int productId) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DBProperties.URL)) {
            PreparedStatement statement = connection.prepareStatement("""
                    SELECT EXISTS(SELECT * FROM ProductsSalePoints WHERE SalePointId = ? AND ProductId = ?)""");
            statement.setInt(1, salePointId);
            statement.setInt(2, productId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getBoolean(1);
            }

            return false;
        }
    }
}
