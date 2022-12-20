package com.coursework.app.service;

import com.coursework.app.entity.SalePoint;
import com.coursework.app.entity.SalePointProduct;
import com.coursework.app.exception.AddSalePointException;
import com.coursework.app.exception.NoProductByIdException;
import com.coursework.app.exception.NoSalePointByIdException;
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

public class SalePointServiceTest {
    private static final File srcFile = new File(RoleServiceTest.class.getClassLoader()
            .getResource("testDatabase.sqlite").getFile());
    private static final File destFile = new File(RoleServiceTest.class.getClassLoader()
            .getResource("").getPath() + "/_testDB_.sqlite");

    private record SalePointProduct_(int productId, int salePointId, int quantity, double price) {

    }

    private record SalePoint_(int salePointId, int typeId, double pointSize, double rentalPrice, double communalService,
                              int countersNumber, boolean isActive, String name) {
    }

    private final ProductService productService = new ProductService();
    private final SalePointService salePointService = new SalePointService();

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
    public void getSalePoints() {
        try {
            List<SalePoint> points = salePointService.getSalePoints();
            List<SalePoint_> points_ = new ArrayList<>();

            points_.add(new SalePoint_(1, 1, 150, 100000, 10000, 5,
                    false, "Торговая точка 1"));
            points_.add(new SalePoint_(2, 2, 1, 1, 1, 1,
                    true, "Торговая точка 2"));
            points_.add(new SalePoint_(3, 1, 156, 100000, 15000, 5,
                    true, "Торговая точка 3"));

            for (int i = 0; i < points.size(); i++) {
                assertEquals(points.get(i).getSalePointId(), points_.get(i).salePointId);
                assertEquals(points.get(i).getType().getTypeId(), points_.get(i).typeId);
                assertEquals(points.get(i).getPointSize(), points_.get(i).pointSize);
                assertEquals(points.get(i).getRentalPrice(), points_.get(i).rentalPrice);
                assertEquals(points.get(i).getCommunalService(), points_.get(i).communalService);
                assertEquals(points.get(i).getCountersNumber(), points_.get(i).countersNumber);
                assertEquals(points.get(i).getIsActive(), points_.get(i).isActive);
                assertEquals(points.get(i).getSalePointName(), points_.get(i).name);
            }
        } catch (SQLException exception) {
            fail(exception.getMessage());
        }
    }

    @Test
    public void getSalePointByIdTest() {
        try {
            final int CORRECT_POINT_ID = 3;
            final int INCORRECT_POINT_ID = 999;
            SalePoint_ point_ = new SalePoint_(CORRECT_POINT_ID, 1, 156, 100000, 15000, 5,
                    true, "Торговая точка 3");
            SalePoint point = salePointService.getSalePointById(CORRECT_POINT_ID);

            assertEquals(point.getSalePointId(), point_.salePointId);
            assertEquals(point.getType().getTypeId(), point_.typeId);
            assertEquals(point.getPointSize(), point_.pointSize);
            assertEquals(point.getRentalPrice(), point_.rentalPrice);
            assertEquals(point.getCommunalService(), point_.communalService);
            assertEquals(point.getCountersNumber(), point_.countersNumber);
            assertEquals(point.getIsActive(), point_.isActive);
            assertEquals(point.getSalePointName(), point_.name);

            assertThrows(NoSalePointByIdException.class, () -> salePointService.getSalePointById(INCORRECT_POINT_ID));

        } catch (SQLException | NoSalePointByIdException exception) {
            fail(exception.getMessage());
        }
    }

    @Test
    public void addSalePointTest() {
        try {
            final int POINT_ID = 3;
            final int NEW_POINT_ID = 4;

            SalePoint_ point_ = new SalePoint_(NEW_POINT_ID, 1, 156, 100000, 15000, 5,
                    true, "Торговая точка 3");
            SalePoint point = salePointService.getSalePointById(POINT_ID);

            SalePoint returnPoint = salePointService.addSalePoint(point);
            SalePoint getPoint = salePointService.getSalePointById(NEW_POINT_ID);

            assertEquals(returnPoint.getSalePointId(), point_.salePointId);
            assertEquals(returnPoint.getType().getTypeId(), point_.typeId);
            assertEquals(returnPoint.getPointSize(), point_.pointSize);
            assertEquals(returnPoint.getRentalPrice(), point_.rentalPrice);
            assertEquals(returnPoint.getCommunalService(), point_.communalService);
            assertEquals(returnPoint.getCountersNumber(), point_.countersNumber);
            assertEquals(returnPoint.getIsActive(), point_.isActive);
            assertEquals(returnPoint.getSalePointName(), point_.name);

            assertEquals(getPoint.getSalePointId(), point_.salePointId);
            assertEquals(getPoint.getType().getTypeId(), point_.typeId);
            assertEquals(getPoint.getPointSize(), point_.pointSize);
            assertEquals(getPoint.getRentalPrice(), point_.rentalPrice);
            assertEquals(getPoint.getCommunalService(), point_.communalService);
            assertEquals(getPoint.getCountersNumber(), point_.countersNumber);
            assertEquals(getPoint.getIsActive(), point_.isActive);
            assertEquals(getPoint.getSalePointName(), point_.name);
        } catch (SQLException | AddSalePointException | NoSalePointByIdException exception) {
            fail(exception.getMessage());
        }
    }

    @Test
    public void addSalePointProductTest() {
        try {
            // Когда товар в списке повторяется
            final int PRODUCT_ID = 2;
            final int SALE_POINT_ID = 3;

            SalePointProduct_ updateProduct_ = new SalePointProduct_(PRODUCT_ID, SALE_POINT_ID, 15, 79.99);
            SalePointProduct updateProduct = new SalePointProduct(productService.getProductById(PRODUCT_ID),
                    salePointService.getSalePointById(SALE_POINT_ID),
                    5, 79.99);

            SalePointProduct returnedProduct = salePointService.addSalePointProduct(updateProduct);
            SalePointProduct getProduct = salePointService.getSalePointProducts(SALE_POINT_ID).stream().filter(obj -> (obj.getProduct().getProductId() == PRODUCT_ID &&
                    obj.getSalePoint().getSalePointId() == SALE_POINT_ID)).findFirst().orElse(null);
            int size = salePointService.getSalePointProducts(SALE_POINT_ID).size();

            assertEquals(size, 2);
            assertEquals(returnedProduct.getProduct().getProductId(), updateProduct_.productId);
            assertEquals(returnedProduct.getSalePoint().getSalePointId(), updateProduct_.salePointId);
            assertEquals(returnedProduct.getQuantity(), updateProduct_.quantity);
            assertEquals(returnedProduct.getPrice(), updateProduct_.price);
            assertEquals(getProduct.getProduct().getProductId(), updateProduct_.productId);
            assertEquals(getProduct.getSalePoint().getSalePointId(), updateProduct_.salePointId);
            assertEquals(getProduct.getQuantity(), updateProduct_.quantity);
            assertEquals(getProduct.getPrice(), updateProduct_.price);

            // Когда товар новый и не появторяется
            final int NEW_PRODUCT_ID = 5;
            final int NEW_SALE_POINT_ID = 3;

            SalePointProduct_ newProduct_ = new SalePointProduct_(NEW_PRODUCT_ID, NEW_SALE_POINT_ID, 3, 50.0);
            SalePointProduct newProduct = new SalePointProduct(productService.getProductById(NEW_PRODUCT_ID),
                    salePointService.getSalePointById(NEW_SALE_POINT_ID),
                    3, 50.0);

            returnedProduct = salePointService.addSalePointProduct(newProduct);
            getProduct = salePointService.getSalePointProducts(SALE_POINT_ID).stream().filter(obj -> (obj.getProduct().getProductId() == NEW_PRODUCT_ID &&
                    obj.getSalePoint().getSalePointId() == NEW_SALE_POINT_ID)).findFirst().orElse(null);
            size = salePointService.getSalePointProducts(SALE_POINT_ID).size();

            assertEquals(size, 3);
            assertEquals(returnedProduct.getProduct().getProductId(), newProduct_.productId);
            assertEquals(returnedProduct.getSalePoint().getSalePointId(), newProduct_.salePointId);
            assertEquals(returnedProduct.getQuantity(), newProduct_.quantity);
            assertEquals(returnedProduct.getPrice(), newProduct_.price);
            assertEquals(getProduct.getProduct().getProductId(), newProduct_.productId);
            assertEquals(getProduct.getSalePoint().getSalePointId(), newProduct_.salePointId);
            assertEquals(getProduct.getQuantity(), newProduct_.quantity);
            assertEquals(getProduct.getPrice(), newProduct_.price);
        } catch (SQLException | NoSalePointByIdException | NoProductByIdException | NullPointerException exception) {
            fail(exception.getMessage());
        }
    }

    @Test
    public void getSalePointProductsTest() {
        try {
            final int CORRECT_POINT_ID = 2;

            List<SalePointProduct> products = salePointService.getSalePointProducts(CORRECT_POINT_ID);

            List<SalePointProduct_> products_ = new ArrayList<>();
            products_.add(new SalePointProduct_(5, 2, 3, 50.0));

            for (int i = 0; i < products.size(); i++) {
                assertEquals(products.get(i).getProduct().getProductId(), products_.get(i).productId);
                assertEquals(products.get(i).getSalePoint().getSalePointId(), products_.get(i).salePointId);
                assertEquals(products.get(i).getQuantity(), products_.get(i).quantity);
                assertEquals(products.get(i).getPrice(), products_.get(i).price);
            }
        } catch (SQLException exception) {
            fail(exception.getMessage());
        }
    }

    @Test
    public void deactivateByIdTest() {
        try {
            final int SALE_POINT_ID = 2;

            SalePoint_ before_ = new SalePoint_(2, 2, 1, 1, 1,
                    1, true, "Торговая точка 2");
            SalePoint_ after_ = new SalePoint_(2, 2, 1, 1, 1,
                    1, false, "Торговая точка 2");

            SalePoint beforeDeactivate = salePointService.getSalePointById(SALE_POINT_ID);
            salePointService.deactivateById(2);
            SalePoint afterDeactivate = salePointService.getSalePointById(SALE_POINT_ID);

            assertEquals(beforeDeactivate.getSalePointId(), before_.salePointId);
            assertEquals(beforeDeactivate.getType().getTypeId(), before_.typeId);
            assertEquals(beforeDeactivate.getPointSize(), before_.pointSize);
            assertEquals(beforeDeactivate.getRentalPrice(), before_.rentalPrice);
            assertEquals(beforeDeactivate.getCommunalService(), before_.communalService);
            assertEquals(beforeDeactivate.getCountersNumber(), before_.countersNumber);
            assertEquals(beforeDeactivate.getIsActive(), before_.isActive);
            assertEquals(beforeDeactivate.getSalePointName(), before_.name);

            assertEquals(afterDeactivate.getSalePointId(), after_.salePointId);
            assertEquals(afterDeactivate.getType().getTypeId(), after_.typeId);
            assertEquals(afterDeactivate.getPointSize(), after_.pointSize);
            assertEquals(afterDeactivate.getRentalPrice(), after_.rentalPrice);
            assertEquals(afterDeactivate.getCommunalService(), after_.communalService);
            assertEquals(afterDeactivate.getCountersNumber(), after_.countersNumber);
            assertEquals(afterDeactivate.getIsActive(), after_.isActive);
            assertEquals(afterDeactivate.getSalePointName(), after_.name);

        } catch (SQLException | NoSalePointByIdException exception) {
            fail(exception.getMessage());
        }
    }
}
