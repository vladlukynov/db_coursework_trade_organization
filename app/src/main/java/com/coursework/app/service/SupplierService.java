package com.coursework.app.service;

import com.coursework.app.entity.Supplier;
import com.coursework.app.entity.SupplierProduct;
import com.coursework.app.repository.SupplierRepository;

import java.sql.SQLException;
import java.util.List;

public class SupplierService {
    private final SupplierRepository supplierRepository = new SupplierRepository();

    public List<Supplier> getSuppliers() throws SQLException {
        return supplierRepository.getSuppliers();
    }

    public List<SupplierProduct> getSupplierProducts(int supplierId) throws SQLException {
        return supplierRepository.getSupplierProducts(supplierId);
    }
}
