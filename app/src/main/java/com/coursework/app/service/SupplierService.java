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

    public void addSupplier(Supplier supplier) throws SQLException {
        supplierRepository.addSupplier(supplier);
    }

    public void changeSupplierStatus(int supplierId, boolean status) throws SQLException {
        supplierRepository.changeSupplierStatus(supplierId, status);
    }

    public void addSupplierProduct(int supplierId, int productId, double price, boolean isActive) throws SQLException {
        supplierRepository.addSupplierProduct(supplierId, productId, price, isActive);
    }

    public void changeSupplierProductStatus(int supplierId, int productId, boolean status) throws SQLException {
        supplierRepository.changeSupplierProductStatus(supplierId, productId, status);
    }
}
