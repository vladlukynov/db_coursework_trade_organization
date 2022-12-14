package com.coursework.app.service;

import com.coursework.app.entity.SalePoint;
import com.coursework.app.repository.SalePointRepository;

import java.sql.SQLException;
import java.util.List;

public class SalePointService {
    private final SalePointRepository salePointRepository = new SalePointRepository();

    public List<SalePoint> getSalePoints() throws SQLException {
        return salePointRepository.getSalePoints();
    }

    public void addSalePoint(SalePoint point) throws SQLException {
        salePointRepository.addSalePoint(point);
    }

    public void changeStatus(int id, boolean status) throws SQLException {
        salePointRepository.changeStatus(id, status);
    }
}
