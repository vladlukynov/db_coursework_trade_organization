package com.coursework.app.service;

import com.coursework.app.entity.Product;
import com.coursework.app.exception.AddProductException;
import com.coursework.app.exception.NoProductByIdException;
import com.coursework.app.utils.DBProperties;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ProductServiceTest {
    private static final File srcFile = new File(HallServiceTest.class.getClassLoader()
            .getResource("testDatabase.sqlite").getFile());
    private static final File destFile = new File(HallServiceTest.class.getClassLoader()
            .getResource("").getPath() + "/_testDB_.sqlite");
    private final ProductService productService = new ProductService();

    private record Product_(int productId, String productName, boolean isActive) {

    }

    @BeforeEach
    public void initializeTest() {
        try {
            DBProperties.URL = "jdbc:sqlite:" + destFile;
            FileUtils.copyFile(srcFile, destFile);
        } catch (IOException exception) {
            System.err.println(exception.getMessage());
        }
    }

    @AfterEach
    public void deleteTestDB() {
        try {
            FileUtils.delete(destFile);
        } catch (IOException exception) {
            System.err.println(exception.getMessage());
        }
    }

    @Test
    public void getProductsTest() {
        try {
            List<Product> products = productService.getProducts();
            List<Product_> products_ = new ArrayList<>();
            products_.add(new Product_(1, "Молоко", true));
            products_.add(new Product_(2, "Кефир", true));
            products_.add(new Product_(5, "Шоколад", true));

            for (int i = 0; i < products.size(); i++) {
                assertEquals(products.get(i).getProductId(), products_.get(i).productId);
                assertEquals(products.get(i).getProductName(), products_.get(i).productName);
                assertEquals(products.get(i).getIsActive(), products_.get(i).isActive);
            }
        } catch (SQLException exception) {
            fail(exception.getMessage());
        }
    }

    @Test
    public void getProductByIdTest() {
        try {
            final int CORRECT_ID = 5;
            final int INCORRECT_ID = 999;
            Product_ product_ = new Product_(CORRECT_ID, "Шоколад", true);
            Product product = productService.getProductById(CORRECT_ID);

            assertEquals(product.getProductId(), product_.productId);
            assertEquals(product.getProductName(), product_.productName);
            assertEquals(product.getIsActive(), product_.isActive);

            assertThrows(NoProductByIdException.class, () -> productService.getProductById(INCORRECT_ID));
        } catch (SQLException | NoProductByIdException exception) {
            fail(exception.getMessage());
        }
    }

    @Test
    public void addProductTest() {
        try {
            final int GENERATED_ID = 7;
            Product_ product_ = new Product_(GENERATED_ID, "Кефир", true);
            Product product = productService.getProductById(2);

            Product returnedProduct = productService.addProduct(product);
            Product getProduct = productService.getProductById(GENERATED_ID);

            assertEquals(returnedProduct.getProductId(), product_.productId);
            assertEquals(returnedProduct.getProductName(), product_.productName);
            assertEquals(returnedProduct.getIsActive(), product_.isActive);

            assertEquals(getProduct.getProductId(), product_.productId);
            assertEquals(getProduct.getProductName(), product_.productName);
            assertEquals(getProduct.getIsActive(), product_.isActive);

        } catch (SQLException | NoProductByIdException | AddProductException exception) {
            fail(exception.getMessage());
        }
    }
}
