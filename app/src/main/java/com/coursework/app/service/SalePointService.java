package com.coursework.app.service;

import com.coursework.app.entity.SalePoint;
import com.coursework.app.entity.SalePointProduct;
import com.coursework.app.exception.AddSalePointException;
import com.coursework.app.exception.NoSalePointByIdException;
import com.coursework.app.repository.SalePointRepository;

import java.sql.SQLException;
import java.util.List;

public class SalePointService {
    private final SalePointRepository salePointRepository = new SalePointRepository();

    public List<SalePoint> getSalePoints() throws SQLException {
        return salePointRepository.getSalePoints();
    }

    public SalePoint getSalePointById(int id) throws SQLException, NoSalePointByIdException {
        SalePoint salePoint = salePointRepository.getSalePointById(id);
        if (salePoint == null) {
            throw new NoSalePointByIdException("Торговой точки с ID " + id + " не найдено");
        }
        return salePoint;
    }

    public SalePoint addSalePoint(SalePoint salePoint) throws SQLException, AddSalePointException {
        SalePoint salePoint_ = salePointRepository.addSalePoint(salePoint);
        if (salePoint_ == null) {
            throw new AddSalePointException("Не удалось добавить торговую точку в БД");
        }
        return salePoint_;
    }

    public void deactivateById(int id) throws SQLException {
        salePointRepository.changeActiveStatusById(id, false);
    }

    public SalePointProduct addSalePointProduct(SalePointProduct product) throws SQLException {
        return salePointRepository.addSalePointProduct(product);
    }

    public List<SalePointProduct> getSalePointProducts(int id) throws SQLException {
        return salePointRepository.getSalePointProducts(id);
    }

    public int getSalePointProductQuantity(int salePointId, int productId) throws SQLException {
        return salePointRepository.getSalePointProductQuantity(salePointId, productId);
    }

    public SalePoint getSalePointBySellerLogin(String sellerLogin) throws SQLException, NoSalePointByIdException {
        SalePoint point = salePointRepository.getSalePointBySellerLogin(sellerLogin);
        if (point == null) {
            throw new NoSalePointByIdException("Не найдено торговых точек, где закреплен сотрудник " + sellerLogin);
        }
        return point;
    }

    public void changeSalePointProductQuantity(int productId, int salePointId, int quantity) throws SQLException {
        salePointRepository.changeSalePointProductQuantity(productId, salePointId, quantity);
    }

    public SalePoint getSalePointBySuperVisorLogin(String login) throws SQLException, NoSalePointByIdException {
        SalePoint salePoint = salePointRepository.getSalePointBySuperVisorLogin(login);
        if (salePoint == null) {
            throw new NoSalePointByIdException("Не найдено торговых точек, где закреплен сотрудник " + login);
        }
        return salePoint;
    }
}
