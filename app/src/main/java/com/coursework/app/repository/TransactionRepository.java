package com.coursework.app.repository;

import com.coursework.app.entity.Transaction;
import com.coursework.app.entity.TransactionProduct;
import com.coursework.app.utils.DBConstants;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TransactionRepository {
    private final UserRepository userRepository = new UserRepository();

    public void addTransactionProduct(TransactionProduct transactionProduct) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DBConstants.URL)) {
            PreparedStatement statement = connection.prepareStatement("""
                        INSERT INTO TransactionsProducts(TransactionId, ProductId, Quantity) VALUES (?,?,?)
                    """);
            statement.setInt(1, transactionProduct.getTransaction().getTransactionId());
            statement.setInt(2, transactionProduct.getProduct().getProductId());
            statement.setInt(3, transactionProduct.getQuantity());
            statement.execute();
        }
    }

    public Transaction addTransaction(Transaction transaction) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DBConstants.URL)) {
            PreparedStatement statement = connection.prepareStatement("""
                        INSERT INTO Transactions(SellerLogin, TransactionDate) VALUES (?,?)
                    """, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, transaction.getSeller().getUserLogin());
            statement.setString(2, transaction.getTransactionDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
            statement.execute();
            transaction.setTransactionId(statement.getGeneratedKeys().getInt(1));
            return transaction;
        }
    }
}
