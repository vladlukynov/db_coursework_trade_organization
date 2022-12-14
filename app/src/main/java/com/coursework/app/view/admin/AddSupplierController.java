package com.coursework.app.view.admin;

import com.coursework.app.entity.Supplier;
import com.coursework.app.service.SupplierService;
import com.coursework.app.utils.ViewUtils;
import com.coursework.app.view.ViewControllers;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;

import java.sql.SQLException;

public class AddSupplierController {
    @FXML
    private TextField supplierNameField;
    private final SupplierService supplierService = new SupplierService();

    @FXML
    protected void onAddButtonClick() {
        String name = supplierNameField.getText().trim();

        if (name.isBlank()) {
            new Alert(Alert.AlertType.ERROR, "Заполните наименование", ButtonType.OK).showAndWait();
            return;
        }

        try {
            supplierService.addSupplier(new Supplier(name, true));
            ViewControllers.getAdminController().initializeSupplierBox();
            ViewUtils.getStage(supplierNameField).close();
        } catch (SQLException exception) {
            new Alert(Alert.AlertType.ERROR, exception.getMessage(), ButtonType.OK).showAndWait();
        }
    }

    @FXML
    protected void onCancelButtonClick() {
        ViewUtils.getStage(supplierNameField).close();
    }
}
