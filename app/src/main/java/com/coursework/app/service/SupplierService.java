package com.coursework.app.service;

import com.coursework.app.entity.Product;
import com.coursework.app.entity.Supplier;
import com.coursework.app.entity.SupplierProduct;
import com.coursework.app.entity.queries.SuppliersByOrder;
import com.coursework.app.entity.queries.SuppliersByProduct;
import com.coursework.app.exception.AddSupplierException;
import com.coursework.app.exception.NoSupplierByIdException;
import com.coursework.app.repository.SupplierRepository;

import java.sql.*;
import java.util.List;

public class SupplierService {
    private final SupplierRepository supplierRepository = new SupplierRepository();

    public List<Supplier> getSuppliers() throws SQLException {
        return supplierRepository.getSuppliers();
    }

    public Supplier getSupplierById(int id) throws SQLException, NoSupplierByIdException {
        Supplier supplier = supplierRepository.getSupplierById(id);
        if (supplier == null) {
            throw new NoSupplierByIdException("Поставщиков с ID " + id + " не найдено");
        }
        return supplier;
    }

    public Supplier addSupplier(Supplier supplier) throws SQLException, AddSupplierException {
        Supplier supplier_ = supplierRepository.addSupplier(supplier);
        if (supplier_ == null) {
            throw new AddSupplierException("Ошибка при добавлении поставщика в БД");
        }
        return supplier_;
    }

    public void deactivateSupplier(int id) throws SQLException {
        supplierRepository.changeActiveStatus(id, false);
    }

    public List<SupplierProduct> getSupplierProducts(int id) throws SQLException {
        return supplierRepository.getSupplierProducts(id);
    }

    public void deactivateSupplierProduct(int supplierId, int productId) throws SQLException {
        supplierRepository.changeProductActiveStatus(supplierId, productId, false);
    }

    public SupplierProduct addSupplierProduct(int supplierId, int productId, double price) throws SQLException {
        return supplierRepository.addSupplierProduct(supplierId, productId, price);
    }

    /* ************** ЗАПРОСЫ ************** */
    public List<SuppliersByProduct> getSuppliersByProduct(Product product) throws SQLException {
        return supplierRepository.getSuppliersByProduct(product);
    }

    public List<SuppliersByOrder> getSuppliersByOrder(int orderId) throws SQLException {
        return supplierRepository.getSuppliersByOrder(orderId);
    }
}
