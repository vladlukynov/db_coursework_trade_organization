package com.coursework.app.service;

import com.coursework.app.TradeOrganizationApp;
import com.coursework.app.entity.Seller;
import com.coursework.app.entity.Transaction;
import com.coursework.app.entity.TransactionProduct;
import com.coursework.app.repository.TransactionRepository;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class TransactionService {
    private final TransactionRepository transactionRepository = new TransactionRepository();
    private final ProductService productService = new ProductService();

    public void addTransaction(List<TransactionProduct> transactionProducts) throws SQLException {
        Transaction transaction = transactionRepository.addTransaction(new Transaction((Seller)TradeOrganizationApp.getUser(),
                LocalDate.now()));
        for (TransactionProduct product : transactionProducts) {
            transactionRepository.addTransactionProduct(new TransactionProduct(transaction,
                    product.getProduct(), product.getQuantity()));
            productService.changeProduct(product.getProduct().getProductId(), product.getQuantity());
        }
    }
}
