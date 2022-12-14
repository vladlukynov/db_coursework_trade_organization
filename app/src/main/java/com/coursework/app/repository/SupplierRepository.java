package com.coursework.app.repository;

import com.coursework.app.entity.Supplier;
import com.coursework.app.entity.SupplierProduct;
import com.coursework.app.utils.DBConstants;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SupplierRepository {
    private final ProductRepository productRepository = new ProductRepository();

    public List<Supplier> getSuppliers() throws SQLException {
        try (Connection connection = DriverManager.getConnection(DBConstants.URL)) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Suppliers");
            ResultSet resultSet = statement.executeQuery();
            List<Supplier> suppliers = new ArrayList<>();
            while (resultSet.next()) {
                suppliers.add(new Supplier(resultSet.getInt("SupplierId"),
                        resultSet.getString("SupplierName"),
                        resultSet.getBoolean("IsActive")));
            }
            return suppliers;
        }
    }

    public Supplier getSupplier(int supplierId) throws SQLException {
        List<Supplier> suppliers = getSuppliers();
        Optional<Supplier> optionalSupplier = suppliers.stream().filter(supplier -> supplier.getSupplierId() == supplierId).findFirst();
        return optionalSupplier.orElse(null);
    }

    public List<SupplierProduct> getSupplierProducts(int supplierId) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DBConstants.URL)) {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT ProductId, Price FROM SuppliersProducts WHERE SupplierId=?");
            statement.setInt(1, supplierId);
            ResultSet resultSet = statement.executeQuery();
            List<SupplierProduct> list = new ArrayList<>();
            while (resultSet.next()) {
                list.add(new SupplierProduct(getSupplier(supplierId),
                        productRepository.getProduct(resultSet.getInt("ProductId")),
                        resultSet.getDouble("Price")));
            }
            return list;
        }
    }

    public void addSupplier(Supplier supplier) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DBConstants.URL)) {
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO Suppliers (SupplierName, IsActive) VALUES (?,?)");
            statement.setString(1, supplier.getSupplierName());
            statement.setBoolean(2, supplier.getIsActive());
            statement.execute();
        }
    }

    public void changeSupplierStatus(int supplierId, boolean status) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DBConstants.URL)) {
            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE Suppliers SET IsActive=? WHERE SupplierId=?");
            statement.setBoolean(1, status);
            statement.setInt(2, supplierId);
            statement.execute();
        }
    }

    public void addSupplierProduct(int supplierId, int productId, double price) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DBConstants.URL)) {
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO SuppliersProducts (SupplierId, ProductId, Price) VALUES (?,?,?)");
            statement.setInt(1, supplierId);
            statement.setInt(2, productId);
            statement.setDouble(3, price);
            statement.execute();
        }
    }
}
