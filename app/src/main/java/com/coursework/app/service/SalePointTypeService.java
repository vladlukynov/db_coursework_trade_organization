package com.coursework.app.service;

import com.coursework.app.entity.SalePointType;
import com.coursework.app.exception.NoSalePointTypeByIdException;
import com.coursework.app.repository.SalePointTypeRepository;

import java.sql.SQLException;
import java.util.List;

public class SalePointTypeService {
    private final SalePointTypeRepository salePointTypeRepository = new SalePointTypeRepository();

    public List<SalePointType> getSalePointTypes() throws SQLException {
        return salePointTypeRepository.getSalePointTypes();
    }

    public SalePointType getSalePointTypeById(int id) throws SQLException, NoSalePointTypeByIdException {
        SalePointType salePointType = salePointTypeRepository.getSalePointTypeById(id);
        if (salePointType == null) {
            throw new NoSalePointTypeByIdException("Типа торговой точки с ID " + id + " не найдено");
        }
        return salePointType;
    }
}
