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

    public void addProduct(Product product) throws SQLException {
        productRepository.addProduct(product);
    }

    public void changeProductStatus(int productId, boolean status) throws SQLException {
        productRepository.changeActiveStatus(productId, status);
    }

    public List<Product> getSalePointProducts(int salePointId) throws SQLException {
        return productRepository.getSalePointProducts(salePointId);
    }

    public int getSalePointProductQuantity(int salePointId, int productId) throws SQLException {
        return productRepository.getSalePointProductQuantity(salePointId, productId);
    }

    public void changeProduct(int productId, int minusQuantity) throws SQLException {
        productRepository.changeProduct(productId, minusQuantity);
    }
}
