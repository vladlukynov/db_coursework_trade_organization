package com.coursework.app.service;

import com.coursework.app.entity.SalePointType;
import com.coursework.app.repository.SalePointTypesRepository;

import java.sql.SQLException;
import java.util.List;

public class SalePointTypesService {
    private final SalePointTypesRepository repository = new SalePointTypesRepository();
    public List<SalePointType> getSalePointTypes() throws SQLException {
        return repository.getSalePointTypes();
    }
}
