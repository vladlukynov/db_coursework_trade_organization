package com.coursework.app.service;

import com.coursework.app.entity.Product;
import com.coursework.app.exception.AddProductException;
import com.coursework.app.exception.NoProductByIdException;
import com.coursework.app.repository.ProductRepository;

import java.sql.SQLException;
import java.util.List;

public class ProductService {
    private final ProductRepository productRepository = new ProductRepository();

    public List<Product> getProducts() throws SQLException {
        return productRepository.getProducts();
    }

    public Product getProductById(int id) throws SQLException, NoProductByIdException {
        Product product = productRepository.getProductById(id);
        if (product == null) {
            throw new NoProductByIdException("Товара с ID " + id + " не найдено");
        }
        return product;
    }

    public Product addProduct(Product product) throws SQLException, AddProductException {
        Product product_ = productRepository.addProduct(product);
        if (product_ == null) {
            throw new AddProductException("Ошибка при добавлении товара в БД");
        }
        return product_;
    }

    public void deactivateProduct(int id) throws SQLException {
        productRepository.changeActiveStatus(id, false);
    }
}
