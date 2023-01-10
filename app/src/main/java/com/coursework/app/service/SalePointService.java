package com.coursework.app.service;

import com.coursework.app.entity.SalePoint;
import com.coursework.app.entity.SalePointProduct;
import com.coursework.app.entity.queries.Nomenclature;
import com.coursework.app.entity.queries.SalePointByProduct;
import com.coursework.app.entity.queries.SalesVolume;
import com.coursework.app.entity.queries.TradeTurnover;
import com.coursework.app.exception.AddSalePointException;
import com.coursework.app.exception.NoSalePointByIdException;
import com.coursework.app.repository.SalePointRepository;

import java.sql.*;
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

    /* **************** ЗАПРОСЫ **************** */
    public List<Nomenclature> getNomenclature(int salePointId) throws SQLException {
        return salePointRepository.getNomenclature(salePointId);
    }

    public double getProfitability(int salePointId) throws SQLException {
        return salePointRepository.getProfitability(salePointId);
    }

    public List<SalePointByProduct> getSalePointsByProductName(String productName) throws SQLException {
        return salePointRepository.getSalePointsByProductName(productName);
    }

    public List<SalePointByProduct> getSalePointsByProductNameAndTypeName(String productName, String typeName) throws SQLException {
        return salePointRepository.getSalePointsByProductNameAndTypeName(productName, typeName);
    }

    public List<SalePointByProduct> getSalePointsByProductNameAndSalePointId(String productName, int salePointId) throws SQLException {
        return salePointRepository.getSalePointsByProductNameAndSalePointId(productName, salePointId);
    }

    public List<TradeTurnover> getTradeTurnover(int salePointId) throws SQLException {
        return salePointRepository.getTradeTurnover(salePointId);
    }

    public List<SalesVolume> getSalesVolumeByProductId(int productId) throws SQLException {
        return salePointRepository.getSalesVolumeByProductId(productId);
    }

    public List<SalesVolume> getSalesVolumeByProductIdAndSalePointTypeName(int productId, String typeName) throws SQLException {
        return salePointRepository.getSalesVolumeByProductIdAndSalePointTypeName(productId, typeName);
    }

    public List<SalesVolume> getSalesVolumeByProductIdAndSalePointId(int productId, int salePointId) throws SQLException {
        return salePointRepository.getSalesVolumeByProductIdAndSalePointId(productId, salePointId);
    }
}
