package com.coursework.app.repository;

import com.coursework.app.entity.Consumer;
import com.coursework.app.utils.DBProperties;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ConsumerRepository {
    private final TransactionRepository transactionRepository = new TransactionRepository();

    public List<Consumer> getConsumers() throws SQLException {
        try (Connection connection = DriverManager.getConnection(DBProperties.URL)) {
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
        try (Connection connection = DriverManager.getConnection(DBProperties.URL)) {
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
        try (Connection connection = DriverManager.getConnection(DBProperties.URL)) {
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
        try (Connection connection = DriverManager.getConnection(DBProperties.URL)) {
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
}
