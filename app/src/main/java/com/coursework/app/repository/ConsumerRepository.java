package com.coursework.app.repository;

import com.coursework.app.entity.Consumer;
import com.coursework.app.entity.queries.ActiveConsumer;
import com.coursework.app.entity.queries.ConsumerByProductName;
import com.coursework.app.entity.queries.Deliveries;
import com.coursework.app.entity.queries.ConsumerProductInfo;
import com.coursework.app.utils.DBProperties;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ConsumerRepository {
    private final TransactionRepository transactionRepository = new TransactionRepository();

    public List<Consumer> getConsumers() throws SQLException {
        try (Connection connection = DriverManager.getConnection(DBProperties.URL, DBProperties.userName, DBProperties.password)) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Consumers");
            ResultSet resultSet = statement.executeQuery();
            List<Consumer> list = new ArrayList<>();
            while (resultSet.next()) {
                list.add(new Consumer(resultSet.getInt("ConsumerId"),
                        resultSet.getString("FirstName"),
                        resultSet.getString("LastName"),
                        resultSet.getString("MiddleName"),
                        transactionRepository.getTransactionById(resultSet.getInt("TransactionId"))));
            }
            return list;
        }
    }

    public Consumer getConsumerById(int id) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DBProperties.URL, DBProperties.userName, DBProperties.password)) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Consumers WHERE ConsumerId = ?");
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Consumer(resultSet.getInt("ConsumerId"),
                        resultSet.getString("FirstName"),
                        resultSet.getString("LastName"),
                        resultSet.getString("MiddleName"),
                        transactionRepository.getTransactionById(resultSet.getInt("TransactionId")));
            }
            return null;
        }
    }

    public List<Consumer> getConsumersByTransactionId(int id) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DBProperties.URL, DBProperties.userName, DBProperties.password)) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Consumers WHERE TransactionId = ?");
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            List<Consumer> list = new ArrayList<>();
            while (resultSet.next()) {
                list.add(new Consumer(resultSet.getInt("ConsumerId"),
                        resultSet.getString("FirstName"),
                        resultSet.getString("LastName"),
                        resultSet.getString("MiddleName"),
                        transactionRepository.getTransactionById(resultSet.getInt("TransactionId"))));
            }
            return list;
        }
    }

    public Consumer addConsumer(Consumer consumer) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DBProperties.URL, DBProperties.userName, DBProperties.password)) {
            PreparedStatement statement = connection.prepareStatement("""
                            INSERT INTO Consumers(FirstName, LastName, MiddleName, TransactionId) VALUES (?,?,?,?)""",
                    Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, consumer.getFirstName());
            statement.setString(2, consumer.getLastName());
            statement.setString(3, consumer.getMiddleName());
            statement.setInt(4, consumer.getTransaction().getTransactionId());
            statement.execute();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                consumer.setConsumerId(resultSet.getInt(1));
                return consumer;
            }
            return null;
        }
    }

    /* *************** ЗАПРОСЫ *************** */

    // Получить перечень и общее число покупателей, купивших указанный вид товара за некоторый период
    public List<ConsumerByProductName> getConsumersByProductName(String productName) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DBProperties.URL, DBProperties.userName, DBProperties.password)) {
            PreparedStatement statement = connection.prepareStatement("""
                    SELECT FirstName, LastName, MiddleName, ConsumersCount
                    FROM Consumers
                             JOIN Transactions ON Consumers.TransactionId = Transactions.TransactionId
                             JOIN TransactionsProducts ON Transactions.TransactionId = TransactionsProducts.TransactionId
                             JOIN Products ON TransactionsProducts.ProductId = Products.ProductId,
                         (SELECT COUNT(*) as ConsumersCount
                          FROM Consumers
                                   JOIN Transactions ON Consumers.TransactionId = Transactions.TransactionId
                                   JOIN TransactionsProducts ON Transactions.TransactionId = TransactionsProducts.TransactionId
                                   JOIN Products ON TransactionsProducts.ProductId = Products.ProductId
                          WHERE ProductName = ?) as ConsumersTable
                    WHERE ProductName = ?""");
            statement.setString(1, productName);
            statement.setString(2, productName);
            ResultSet resultSet = statement.executeQuery();
            List<ConsumerByProductName> list = new ArrayList<>();
            while (resultSet.next()) {
                list.add(new ConsumerByProductName(resultSet.getString("FirstName"),
                        resultSet.getString("LastName"),
                        resultSet.getString("MiddleName"),
                        resultSet.getInt("ConsumersCount")));
            }
            return list;
        }
    }

    // Получить сведения о наиболее активных покупателях по всем торговым точкам, по торговым точкам указанного типа,
    // по данной торговой точке.
    public List<ActiveConsumer> getActiveConsumers() throws SQLException {
        try (Connection connection = DriverManager.getConnection(DBProperties.URL, DBProperties.userName, DBProperties.password)) {
            PreparedStatement statement = connection.prepareStatement("""
                    SELECT Halls.SalePointId, FirstName, LastName, MiddleName, COUNT(*) AS ConsumersCount
                    FROM Consumers
                             JOIN Transactions ON Consumers.TransactionId = Transactions.TransactionId
                             JOIN Sellers ON Transactions.SellerLogin = Sellers.UserLogin
                             JOIN Halls ON Sellers.HallId = Halls.HallId
                             JOIN SalePoints ON Halls.SalePointId = SalePoints.SalePointId
                    GROUP BY Halls.SalePointId, FirstName, LastName, MiddleName""");
            ResultSet resultSet = statement.executeQuery();
            List<ActiveConsumer> list = new ArrayList<>();
            while (resultSet.next()) {
                list.add(new ActiveConsumer(resultSet.getInt("SalePointId"),
                        resultSet.getString("FirstName"),
                        resultSet.getString("LastName"),
                        resultSet.getString("MiddleName"),
                        resultSet.getInt("ConsumersCount")));
            }
            return list;
        }
    }

    // Получить сведения о поставках определенного товара указанным поставщиком за все время поставок
    public List<Deliveries> getDeliveriesByProductName(String productName) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DBProperties.URL, DBProperties.userName, DBProperties.password)) {
            PreparedStatement statement = connection.prepareStatement("""
                    SELECT Requests.RequestId, SalePointName, TypeName, ProductName, Quantity, Price
                    FROM SuppliersRequests
                             JOIN Suppliers ON SuppliersRequests.SupplierId = Suppliers.SupplierId
                             JOIN Products ON SuppliersRequests.ProductId = Products.ProductId
                             JOIN Requests ON SuppliersRequests.RequestId = Requests.RequestId
                             JOIN SuppliersProducts ON Products.ProductId = SuppliersProducts.ProductId
                             JOIN SalePoints ON Requests.SalePointId = SalePoints.SalePointId
                             JOIN SalePointTypes ON SalePoints.TypeId = SalePointTypes.TypeId
                    WHERE ProductName = ?""");
            statement.setString(1, productName);
            ResultSet resultSet = statement.executeQuery();
            List<Deliveries> list = new ArrayList<>();
            while (resultSet.next()) {
                list.add(new Deliveries(resultSet.getInt("RequestId"),
                        resultSet.getString("SalePointName"),
                        resultSet.getString("TypeName"),
                        resultSet.getString("ProductName"),
                        resultSet.getInt("Quantity"),
                        resultSet.getDouble("Price")));
            }
            return list;
        }
    }

    // Получить сведения о покупателях указанного товара за весь период по всем торговым точкам
    public List<ConsumerProductInfo> getConsumerProductInfo(String productName) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DBProperties.URL, DBProperties.userName, DBProperties.password)) {
            PreparedStatement statement = connection.prepareStatement("""
                    SELECT SalePointName, TypeName, FirstName, LastName, MiddleName, TransactionDate
                    FROM Consumers
                             JOIN Transactions ON Consumers.TransactionId = Transactions.TransactionId
                             JOIN Sellers ON Transactions.SellerLogin = Sellers.UserLogin
                             JOIN TransactionsProducts ON Transactions.TransactionId = TransactionsProducts.TransactionId
                             JOIN Products ON TransactionsProducts.ProductId = Products.ProductId
                             JOIN Halls ON Sellers.HallId = Halls.HallId
                             JOIN SalePoints ON Halls.SalePointId = SalePoints.SalePointId
                             JOIN SalePointTypes ON SalePoints.TypeId = SalePointTypes.TypeId
                    WHERE ProductName = ?""");
            statement.setString(1, productName);
            ResultSet resultSet = statement.executeQuery();
            List<ConsumerProductInfo> list = new ArrayList<>();
            while (resultSet.next()) {
                list.add(new ConsumerProductInfo(resultSet.getString("SalePointName"),
                        resultSet.getString("TypeName"),
                        resultSet.getString("FirstName"),
                        resultSet.getString("LastName"),
                        resultSet.getString("MiddleName"),
                        resultSet.getDate("TransactionDate")));
            }
            return list;
        }
    }

    // Получить сведения о покупателях указанного товара за весь период по торговым точкам указанного типа
    public List<ConsumerProductInfo> getConsumerProductInfoBySalePointTypeName(String productName, String typeName) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DBProperties.URL, DBProperties.userName, DBProperties.password)) {
            PreparedStatement statement = connection.prepareStatement("""
                    SELECT SalePointName, TypeName, FirstName, LastName, MiddleName, TransactionDate
                    FROM Consumers
                             JOIN Transactions ON Consumers.TransactionId = Transactions.TransactionId
                             JOIN Sellers ON Transactions.SellerLogin = Sellers.UserLogin
                             JOIN TransactionsProducts ON Transactions.TransactionId = TransactionsProducts.TransactionId
                             JOIN Products ON TransactionsProducts.ProductId = Products.ProductId
                             JOIN Halls ON Sellers.HallId = Halls.HallId
                             JOIN SalePoints ON Halls.SalePointId = SalePoints.SalePointId
                             JOIN SalePointTypes ON SalePoints.TypeId = SalePointTypes.TypeId
                    WHERE ProductName = ?
                      AND TypeName = ?""");
            statement.setString(1, productName);
            statement.setString(2, typeName);
            ResultSet resultSet = statement.executeQuery();
            List<ConsumerProductInfo> list = new ArrayList<>();
            while (resultSet.next()) {
                list.add(new ConsumerProductInfo(resultSet.getString("SalePointName"),
                        resultSet.getString("TypeName"),
                        resultSet.getString("FirstName"),
                        resultSet.getString("LastName"),
                        resultSet.getString("MiddleName"),
                        resultSet.getDate("TransactionDate")));
            }
            return list;
        }
    }

    // Получить сведения о покупателях указанного товара за весь период по данной торговой точке.
    public List<ConsumerProductInfo> getConsumerProductInfoBySalePointId(String productName, int salePointId) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DBProperties.URL, DBProperties.userName, DBProperties.password)) {
            PreparedStatement statement = connection.prepareStatement("""
                    SELECT SalePointName, TypeName, FirstName, LastName, MiddleName, TransactionDate
                    FROM Consumers
                             JOIN Transactions ON Consumers.TransactionId = Transactions.TransactionId
                             JOIN Sellers ON Transactions.SellerLogin = Sellers.UserLogin
                             JOIN TransactionsProducts ON Transactions.TransactionId = TransactionsProducts.TransactionId
                             JOIN Products ON TransactionsProducts.ProductId = Products.ProductId
                             JOIN Halls ON Sellers.HallId = Halls.HallId
                             JOIN SalePoints ON Halls.SalePointId = SalePoints.SalePointId
                             JOIN SalePointTypes ON SalePoints.TypeId = SalePointTypes.TypeId
                    WHERE ProductName = ?
                      AND SalePoints.SalePointId = ?""");
            statement.setString(1, productName);
            statement.setInt(2, salePointId);
            ResultSet resultSet = statement.executeQuery();
            List<ConsumerProductInfo> list = new ArrayList<>();
            while (resultSet.next()) {
                list.add(new ConsumerProductInfo(resultSet.getString("SalePointName"),
                        resultSet.getString("TypeName"),
                        resultSet.getString("FirstName"),
                        resultSet.getString("LastName"),
                        resultSet.getString("MiddleName"),
                        resultSet.getDate("TransactionDate")));
            }
            return list;
        }
    }
}
