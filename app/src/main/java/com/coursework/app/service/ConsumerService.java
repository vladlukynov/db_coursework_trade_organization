package com.coursework.app.service;

import com.coursework.app.entity.Consumer;
import com.coursework.app.exception.AddConsumerException;
import com.coursework.app.exception.NoConsumerByIdException;
import com.coursework.app.repository.ConsumerRepository;

import java.sql.*;
import java.util.List;

public class ConsumerService {
    private final ConsumerRepository consumerRepository = new ConsumerRepository();

    public List<Consumer> getConsumers() throws SQLException {
        return consumerRepository.getConsumers();
    }

    public Consumer getConsumerById(int id) throws SQLException, NoConsumerByIdException {
        Consumer consumer = consumerRepository.getConsumerById(id);
        if (consumer == null) {
            throw new NoConsumerByIdException("Покупатель с ID " + id + " не найден");
        }
        return consumer;
    }

    public List<Consumer> getConsumersByTransactionId(int id) throws SQLException {
        return consumerRepository.getConsumersByTransactionId(id);
    }

    public Consumer addConsumer(Consumer consumer) throws SQLException, AddConsumerException {
        Consumer consumer_ = consumerRepository.addConsumer(consumer);
        if (consumer_ == null) {
            throw new AddConsumerException("Ошибка добавления покупателя в БД");
        }
        return consumer;
    }
}
