package com.coursework.app.repository;

import com.coursework.app.entity.Transaction;
import com.coursework.app.entity.TransactionProduct;
import com.coursework.app.utils.DBProperties;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class TransactionRepository {
    private final UserRepository userRepository = new UserRepository();
    private final ProductRepository productRepository = new ProductRepository();

    public List<Transaction> getSellerTransactions(String login) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DBProperties.URL)) {
            PreparedStatement statement = connection.prepareStatement("""
                    SELECT * FROM Transactions WHERE SellerLogin = ?""");
            statement.setString(1, login);
            ResultSet resultSet = statement.executeQuery();
            List<Transaction> list = new ArrayList<>();
            while (resultSet.next()) {
                list.add(new Transaction(resultSet.getInt("TransactionId"),
                        userRepository.getSellerByLogin(resultSet.getString("SellerLogin")),
                        LocalDate.parse(resultSet.getString("TransactionDate"))));
            }
            return list;
        }
    }

    public Transaction getTransactionById(int id) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DBProperties.URL)) {
            PreparedStatement statement = connection.prepareStatement("""
                    SELECT * FROM Transactions WHERE TransactionId = ?""");
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Transaction(resultSet.getInt("TransactionId"),
                        userRepository.getSellerByLogin(resultSet.getString("SellerLogin")),
                        LocalDate.parse(resultSet.getString("TransactionDate")));
            }
            return null;
        }
    }

    public Transaction addTransaction(Transaction transaction) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DBProperties.URL)) {
            PreparedStatement statement = connection.prepareStatement("""
                    INSERT INTO Transactions(SellerLogin, TransactionDate) VALUES (?,?)""", Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, transaction.getSeller().getUserLogin());
            statement.setString(2, transaction.getTransactionDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            statement.execute();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                transaction.setTransactionId(resultSet.getInt(1));
                return transaction;
            }
            return null;
        }
    }

    public TransactionProduct addTransactionProduct(TransactionProduct product) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DBProperties.URL)) {
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO TransactionsProducts(TransactionId, ProductId, Quantity) VALUES (?,?,?)");
            statement.setInt(1, product.getTransaction().getTransactionId());
            statement.setInt(2, product.getProduct().getProductId());
            statement.setInt(3, product.getQuantity());
            statement.execute();

            return product;
        }
    }

    public List<TransactionProduct> getTransactionProducts(int transactionId) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DBProperties.URL)) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM TransactionsProducts WHERE TransactionId = ?");
            statement.setInt(1, transactionId);
            ResultSet resultSet = statement.executeQuery();
            List<TransactionProduct> list = new ArrayList<>();
            while (resultSet.next()) {
                list.add(new TransactionProduct(getTransactionById(resultSet.getInt("TransactionId")),
                        productRepository.getProductById(resultSet.getInt("ProductId")),
                        resultSet.getInt("Quantity")));
            }
            return list;
        }
    }
}
