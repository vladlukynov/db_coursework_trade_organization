package com.coursework.app.view.seller;

import com.coursework.app.TradeOrganizationApp;
import com.coursework.app.entity.Product;
import com.coursework.app.entity.Seller;
import com.coursework.app.entity.TransactionProduct;
import com.coursework.app.service.ProductService;
import com.coursework.app.service.TransactionService;
import com.coursework.app.utils.StringConverterUtils;
import com.coursework.app.utils.ViewUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

import java.sql.SQLException;

public class AddTransactionController {
    @FXML
    private ComboBox<Product> productBox;

    @FXML
    private TableColumn<TransactionProduct, Product> productName;

    @FXML
    private TableColumn<TransactionProduct, Integer> productQuantity;
    @FXML
    private TextField quantityField;

    @FXML
    private TableView<TransactionProduct> productTable;
    private final ObservableList<TransactionProduct> transactionProducts = FXCollections.observableArrayList();
    private final ProductService productService = new ProductService();
    private final int salePointId = ((Seller) TradeOrganizationApp.getUser()).getHall().getSalePoint().getSalePointId();
    private final TransactionService transactionService = new TransactionService();

    @FXML
    protected void initialize() {
        try {
            productName.setCellValueFactory(new PropertyValueFactory<>("product"));
            productName.setCellFactory(TextFieldTableCell.forTableColumn(StringConverterUtils.productNameStringConverter));
            productQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));

            productBox.getItems().addAll(productService.getSalePointProducts(salePointId).stream().filter(Product::getIsActive).toList());
            productBox.setConverter(StringConverterUtils.productNameStringConverter);

            productTable.setItems(transactionProducts);
        } catch (SQLException exception) {
            new Alert(Alert.AlertType.ERROR, exception.getMessage(), ButtonType.OK).showAndWait();
        }
    }

    @FXML
    protected void addProduct() {
        Product product = productBox.getSelectionModel().getSelectedItem();
        int quantity;
        try {
            quantity = Integer.parseInt(quantityField.getText().trim());
        } catch (NumberFormatException exception) {
            new Alert(Alert.AlertType.INFORMATION, "В поле количество должно быть число", ButtonType.OK).showAndWait();
            return;
        }
        if (product != null) {
            transactionProducts.add(new TransactionProduct(product, quantity));
        }
    }

    @FXML
    protected void applyClick() {
        try {
            for (TransactionProduct products : transactionProducts) {
                if (productService.getSalePointProductQuantity(salePointId, products.getProduct().getProductId()) < products.getQuantity()) {
                    new Alert(Alert.AlertType.INFORMATION, "Такого количества товара нет на складе", ButtonType.OK).showAndWait();
                    return;
                }
            }
            transactionService.addTransaction(transactionProducts);
        } catch (SQLException exception) {
            new Alert(Alert.AlertType.ERROR, exception.getMessage(), ButtonType.OK).showAndWait();
        }
    }

    @FXML
    protected void cancelClick() {
        ViewUtils.getStage(productBox).close();
    }
}
