package com.coursework.app.repository;

import com.coursework.app.entity.Transaction;
import com.coursework.app.entity.TransactionProduct;
import com.coursework.app.utils.DBProperties;

import java.sql.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
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
                        resultSet.getDate("TransactionDate")));
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
                        resultSet.getDate("TransactionDate"));
            }
            return null;
        }
    }

    public Transaction addTransaction(List<TransactionProduct> products, String sellerLogin) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DBProperties.URL)) {
            PreparedStatement statement = connection.prepareStatement("""
                    INSERT INTO Transactions(SellerLogin, TransactionDate) VALUES (?,?)""", Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, sellerLogin);
            statement.setString(2, LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
            statement.execute();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                return new Transaction(resultSet.getInt(1),
                        userRepository.getSellerByLogin(sellerLogin),
                        Date.from(LocalDate.now().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
            }
            return null;
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
