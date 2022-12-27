package com.coursework.app.view.super_visor;

import com.coursework.app.TradeOrganizationApp;
import com.coursework.app.entity.Product;
import com.coursework.app.entity.Request;
import com.coursework.app.entity.RequestProduct;
import com.coursework.app.exception.AddRequestException;
import com.coursework.app.exception.NoSalePointByIdException;
import com.coursework.app.service.ProductService;
import com.coursework.app.service.RequestService;
import com.coursework.app.service.SalePointService;
import com.coursework.app.utils.StringConverterUtils;
import com.coursework.app.utils.ViewUtils;
import com.coursework.app.view.ViewControllers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;

public class AddRequestController {
    @FXML
    private TableView<RequestProduct> requestProductsTable;
    @FXML
    private TableColumn<RequestProduct, Product> productNameColumn;
    @FXML
    private TableColumn<RequestProduct, Integer> productQuantityColumn;
    @FXML
    private ComboBox<Product> productsBox;
    @FXML
    private TextField quantityField;
    private final ObservableList<RequestProduct> products = FXCollections.observableArrayList();
    private final ObservableList<Product> productsBoxItems = FXCollections.observableArrayList();
    private final ProductService productService = new ProductService();
    private final RequestService requestService = new RequestService();
    private final SalePointService salePointService = new SalePointService();

    @FXML
    protected void initialize() {
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("product"));
        productNameColumn.setCellFactory(TextFieldTableCell.forTableColumn(StringConverterUtils.productNameStringConverter));
        productQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        requestProductsTable.setItems(products);

        productsBox.setConverter(StringConverterUtils.productNameStringConverter);
        productsBox.setItems(productsBoxItems);

        try {
            productsBoxItems.addAll(productService.getProducts().stream().filter(Product::getIsActive).toList());
        } catch (SQLException exception) {
            new Alert(Alert.AlertType.ERROR, exception.getMessage(), ButtonType.OK).showAndWait();
        }
    }

    @FXML
    protected void cancelButtonClick() {
        ViewUtils.getStage(requestProductsTable).close();
    }

    @FXML
    protected void applyButtonClick() {
        try {
            Request request = requestService.addRequest(new Request(
                    salePointService.getSalePointBySuperVisorLogin(TradeOrganizationApp.getUser().getUserLogin()),
                    false, LocalDate.now()));
            for (RequestProduct product : products) {
                product.setRequest(request);
                requestService.addRequestProduct(product);
            }

            ViewControllers.getSuperVisorController().updateRequestsTable();
            ViewUtils.getStage(productsBox).close();
            new Alert(Alert.AlertType.INFORMATION, "Успешно", ButtonType.OK).showAndWait();
        } catch (SQLException | NoSalePointByIdException | AddRequestException exception) {
            new Alert(Alert.AlertType.ERROR, exception.getMessage(), ButtonType.OK).showAndWait();
        }
    }

    @FXML
    protected void addProductButtonClick() {
        Product product = productsBox.getSelectionModel().getSelectedItem();
        int quantity;

        try {
            quantity = Integer.parseInt(quantityField.getText().trim());
        } catch (NumberFormatException exception) {
            new Alert(Alert.AlertType.INFORMATION, "В поле количество должно быть число").showAndWait();
            return;
        }

        if (quantity < 0) {
            new Alert(Alert.AlertType.INFORMATION, "Количество не может быть меньше 0").showAndWait();
            return;
        }

        if (product == null) {
            new Alert(Alert.AlertType.INFORMATION, "Товар не выбран").showAndWait();
            return;
        }

        Optional<RequestProduct> optionalProduct = products.stream().filter(product_ ->
                product_.getProduct().getProductId() == product.getProductId()).findFirst();
        if (optionalProduct.isPresent()) {
            optionalProduct.get().setQuantity(
                    optionalProduct.get().getQuantity() + quantity);
        } else {
            products.add(new RequestProduct(product, quantity));
        }

        requestProductsTable.refresh();
    }
}
