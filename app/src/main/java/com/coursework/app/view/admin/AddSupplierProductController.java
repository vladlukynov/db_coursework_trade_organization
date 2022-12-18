package com.coursework.app.view.admin;

import com.coursework.app.entity.Product;
import com.coursework.app.entity.Supplier;
import com.coursework.app.service.ProductService;
import com.coursework.app.service.SupplierService;
import com.coursework.app.utils.StringConverterUtils;
import com.coursework.app.utils.ViewUtils;
import com.coursework.app.view.ViewControllers;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.sql.SQLException;

public class AddSupplierProductController {
    @FXML
    private TextField priceField;
    @FXML
    private ComboBox<Product> productBox;
    private final ProductService productService = new ProductService();
    private final SupplierService supplierService= new SupplierService();

    @FXML
    protected void initialize() {
        try {
            productService.getProducts().forEach(product -> {
                if (product.getIsActive()) {
                    productBox.getItems().add(product);
                }
            });
            productBox.setConverter(StringConverterUtils.productNameStringConverter);
        } catch (SQLException exception) {
            new Alert(Alert.AlertType.ERROR, exception.getMessage(), ButtonType.OK).showAndWait();
        }
    }

    @FXML
    protected void onAddButtonClick() {
        Product product = productBox.getSelectionModel().getSelectedItem();
        double price;
        try {
            price = Double.parseDouble(priceField.getText().trim());
        } catch (NumberFormatException exception) {
            new Alert(Alert.AlertType.INFORMATION, "Поле стоимость должно быть числом", ButtonType.OK).showAndWait();
            return;
        }

        if (product == null) {
            new Alert(Alert.AlertType.INFORMATION, "Не выбран товар", ButtonType.OK).showAndWait();
            return;
        }

        if (price < 0) {
            new Alert(Alert.AlertType.INFORMATION, "Поле стоимость должно быть больше 0", ButtonType.OK).showAndWait();
            return;
        }
        try {
            Supplier supplier = ViewControllers.getAdminController().getSelectedSupplier();
            if (supplier == null) {
                new Alert(Alert.AlertType.INFORMATION, "На главном окне не выбран поставщик", ButtonType.OK).showAndWait();
                ViewUtils.getStage(priceField).close();
                return;
            }
            supplierService.addSupplierProduct(supplier.getSupplierId(), product.getProductId(), price);
            ViewControllers.getAdminController().updateSuppliersPage();
            ViewUtils.getStage(priceField).close();
        } catch (SQLException exception) {
            new Alert(Alert.AlertType.ERROR, exception.getMessage(), ButtonType.OK).showAndWait();
        }
    }

    @FXML
    protected void onCancelButtonClick() {
        ViewUtils.getStage(priceField).close();
    }
}
