package com.coursework.app.service;

import com.coursework.app.entity.Product;
import com.coursework.app.repository.ProductRepository;

import java.sql.SQLException;
import java.util.List;

public class ProductService {
    private final ProductRepository productRepository = new ProductRepository();

    public List<Product> getProducts() throws SQLException {
        return productRepository.getProducts();
    }
}
