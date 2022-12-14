package com.coursework.app.repository;

import com.coursework.app.entity.Product;
import com.coursework.app.entity.Supplier;
import com.coursework.app.entity.SupplierProduct;
import com.coursework.app.entity.queries.SuppliersByOrder;
import com.coursework.app.entity.queries.SuppliersByProduct;
import com.coursework.app.utils.DBProperties;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SupplierRepository {
    private final ProductRepository productRepository = new ProductRepository();

    public List<Supplier> getSuppliers() throws SQLException {
        try (Connection connection = DriverManager.getConnection(DBProperties.URL, DBProperties.userName, DBProperties.password)) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Suppliers");
            ResultSet resultSet = statement.executeQuery();
            List<Supplier> list = new ArrayList<>();
            while (resultSet.next()) {
                list.add(new Supplier(resultSet.getInt("SupplierId"),
                        resultSet.getString("SupplierName"),
                        resultSet.getBoolean("IsActive")));
            }
            return list;
        }
    }

    public Supplier getSupplierById(int id) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DBProperties.URL, DBProperties.userName, DBProperties.password)) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Suppliers WHERE SupplierId=?");
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Supplier(resultSet.getInt("SupplierId"),
                        resultSet.getString("SupplierName"),
                        resultSet.getBoolean("IsActive"));
            }
            return null;
        }
    }

    public Supplier addSupplier(Supplier supplier) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DBProperties.URL, DBProperties.userName, DBProperties.password)) {
            PreparedStatement statement = connection.prepareStatement("""
                    INSERT INTO Suppliers(SupplierName, IsActive) VALUES (?,?)""", Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, supplier.getSupplierName());
            statement.setBoolean(2, supplier.getIsActive());
            statement.execute();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                supplier.setSupplierId(resultSet.getInt(1));
                return supplier;
            }
            return null;
        }
    }

    public void changeActiveStatus(int id, boolean status) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DBProperties.URL, DBProperties.userName, DBProperties.password)) {
            PreparedStatement statement = connection.prepareStatement("""
                    UPDATE Suppliers SET IsActive=? WHERE SupplierId=?""");
            statement.setBoolean(1, status);
            statement.setInt(2, id);
            statement.execute();
        }
    }

    public List<SupplierProduct> getSupplierProducts(int supplierId) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DBProperties.URL, DBProperties.userName, DBProperties.password)) {
            PreparedStatement statement = connection.prepareStatement("""
                    SELECT * FROM SuppliersProducts WHERE SupplierId=?""");
            statement.setInt(1, supplierId);
            ResultSet resultSet = statement.executeQuery();
            List<SupplierProduct> list = new ArrayList<>();
            while (resultSet.next()) {
                list.add(new SupplierProduct(getSupplierById(supplierId),
                        productRepository.getProductById(resultSet.getInt("ProductId")),
                        resultSet.getDouble("Price"),
                        resultSet.getBoolean("IsActive")));
            }
            return list;
        }
    }

    public void changeProductActiveStatus(int supplierId, int productId, boolean status) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DBProperties.URL, DBProperties.userName, DBProperties.password)) {
            PreparedStatement statement = connection.prepareStatement("""
                    UPDATE SuppliersProducts SET IsActive=? WHERE SupplierId=? AND ProductId=?""");
            statement.setBoolean(1, status);
            statement.setInt(2, supplierId);
            statement.setInt(3, productId);
            statement.execute();
        }
    }

    public SupplierProduct addSupplierProduct(int supplierId, int productId, double price) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DBProperties.URL, DBProperties.userName, DBProperties.password)) {
            PreparedStatement statement = connection.prepareStatement("""
                    INSERT INTO SuppliersProducts (SupplierId, ProductId, Price, IsActive) VALUES (?,?,?,?)""");
            statement.setInt(1, supplierId);
            statement.setInt(2, productId);
            statement.setDouble(3, price);
            statement.setBoolean(4, true);
            statement.execute();

            return new SupplierProduct(getSupplierById(supplierId),
                    productRepository.getProductById(productId), price, true);
        }
    }

    /* ************** ?????????????? ************** */

    // ???????????????? ???????????????? ?? ?????????? ?????????? ??????????????????????, ???????????????????????? ?????????????????? ?????? ???????????? ???? ???????? ???????????? ????????????????????????????
    public List<SuppliersByProduct> getSuppliersByProduct(Product product) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DBProperties.URL, DBProperties.userName, DBProperties.password)) {
            PreparedStatement statement = connection.prepareStatement("""
                    SELECT SupplierName, COUNT(*) as suppliersCount
                    FROM SuppliersProducts
                             JOIN Suppliers ON SuppliersProducts.SupplierId = Suppliers.SupplierId
                    WHERE ProductId = ?
                    GROUP BY SupplierName""");
            statement.setInt(1, product.getProductId());
            ResultSet resultSet = statement.executeQuery();
            List<SuppliersByProduct> list = new ArrayList<>();
            while (resultSet.next()) {
                list.add(new SuppliersByProduct(resultSet.getString("SupplierName"),
                        resultSet.getInt("SuppliersCount")));
            }
            return list;
        }
    }

    // ???????????????? ???????????????? ?? ?????????????????? ?????????????? ???? ???????????????????? ???????????? ????????????.
    public List<SuppliersByOrder> getSuppliersByOrder(int orderId) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DBProperties.URL, DBProperties.userName, DBProperties.password)) {
            PreparedStatement statement = connection.prepareStatement("""
                    SELECT SalePointName, TypeName, ProductName, Quantity, Price
                    FROM SuppliersRequests
                             JOIN Suppliers ON SuppliersRequests.SupplierId = Suppliers.SupplierId
                             JOIN Products ON SuppliersRequests.ProductId = Products.ProductId
                             JOIN Requests ON SuppliersRequests.RequestId = Requests.RequestId
                             JOIN SuppliersProducts ON Products.ProductId = SuppliersProducts.ProductId
                             JOIN SalePoints ON Requests.SalePointId = SalePoints.SalePointId
                             JOIN SalePointTypes ON SalePoints.TypeId = SalePointTypes.TypeId
                    WHERE SuppliersRequests.RequestId = ?""");
            statement.setInt(1, orderId);
            ResultSet resultSet = statement.executeQuery();
            List<SuppliersByOrder> list = new ArrayList<>();
            while (resultSet.next()) {
                list.add(new SuppliersByOrder(resultSet.getString("SalePointName"),
                        resultSet.getString("TypeName"),
                        resultSet.getString("ProductName"),
                        resultSet.getInt("Quantity"),
                        resultSet.getDouble("Price")));
            }
            return list;
        }
    }
}
