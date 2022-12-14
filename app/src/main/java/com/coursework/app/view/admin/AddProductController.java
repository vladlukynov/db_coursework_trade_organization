package com.coursework.app.view.admin;

import com.coursework.app.entity.Product;
import com.coursework.app.service.ProductService;
import com.coursework.app.utils.ViewUtils;
import com.coursework.app.view.ViewControllers;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;

import java.sql.SQLException;

public class AddProductController {
    @FXML
    private TextField nameField;
    private final ProductService productService = new ProductService();

    @FXML
    void onAddButtonClick() {
        String name = nameField.getText().trim();
        if (name.isBlank()) {
            new Alert(Alert.AlertType.INFORMATION, "Заполните наименование товара", ButtonType.OK).showAndWait();
            return;
        }
        try {
            productService.addProduct(new Product(name, true));
            ViewControllers.getAdminController().updateProductsPage();
            ViewUtils.getStage(nameField).close();
        } catch (SQLException exception) {
            new Alert(Alert.AlertType.ERROR, exception.getMessage(), ButtonType.OK);
        }
    }

    @FXML
    void onCancelButtonClick() {
        ViewUtils.getStage(nameField).close();
    }
}
