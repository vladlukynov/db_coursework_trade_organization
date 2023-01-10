package com.coursework.app.service;

import com.coursework.app.entity.Consumer;
import com.coursework.app.entity.queries.ActiveConsumer;
import com.coursework.app.entity.queries.ConsumerByProductName;
import com.coursework.app.entity.queries.ConsumerProductInfo;
import com.coursework.app.entity.queries.Deliveries;
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

    /* ******************* ЗАПРОСЫ ******************* */
    public List<ConsumerByProductName> getConsumersByProductName(String productName) throws SQLException {
        return consumerRepository.getConsumersByProductName(productName);
    }

    public List<ActiveConsumer> getActiveConsumers() throws SQLException {
        return consumerRepository.getActiveConsumers();
    }

    public List<Deliveries> getDeliveriesByProductName(String productName) throws SQLException {
        return consumerRepository.getDeliveriesByProductName(productName);
    }

    public List<ConsumerProductInfo> getConsumerProductInfo(String productName) throws SQLException {
        return consumerRepository.getConsumerProductInfo(productName);
    }

    public List<ConsumerProductInfo> getConsumerProductInfoBySalePointTypeName(String productName, String typeName) throws SQLException {
        return consumerRepository.getConsumerProductInfoBySalePointTypeName(productName, typeName);
    }

    public List<ConsumerProductInfo> getConsumerProductInfoBySalePointId(String productName, int salePointId) throws SQLException {
        return consumerRepository.getConsumerProductInfoBySalePointId(productName, salePointId);
    }
}
