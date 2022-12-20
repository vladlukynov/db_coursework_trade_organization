package com.coursework.app.service;

import com.coursework.app.entity.Transaction;
import com.coursework.app.entity.TransactionProduct;
import com.coursework.app.exception.AddTransactionException;
import com.coursework.app.exception.NoTransactionByIdException;
import com.coursework.app.repository.TransactionRepository;

import java.sql.SQLException;
import java.util.List;

public class TransactionService {
    private final TransactionRepository transactionRepository = new TransactionRepository();

    public List<Transaction> getSellerTransaction(String login) throws SQLException {
        return transactionRepository.getSellerTransactions(login);
    }

    public Transaction getTransactionById(int id) throws SQLException, NoTransactionByIdException {
        Transaction transaction = transactionRepository.getTransactionById(id);
        if (transaction == null) {
            throw new NoTransactionByIdException("Транзакции с ID " + id + " не найдено");
        }
        return transaction;
    }

    public Transaction addTransaction(List<TransactionProduct> products, String sellerLogin) throws SQLException, AddTransactionException {
        Transaction transaction_ = transactionRepository.addTransaction(products, sellerLogin);
        if (transaction_ == null) {
            throw new AddTransactionException("Ошибка добавления транзакции в БД");
        }
        return transaction_;
    }
}
