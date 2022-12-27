package com.coursework.app.view.seller;

import com.coursework.app.TradeOrganizationApp;
import com.coursework.app.entity.*;
import com.coursework.app.exception.AddConsumerException;
import com.coursework.app.exception.AddTransactionException;
import com.coursework.app.service.ConsumerService;
import com.coursework.app.service.SalePointService;
import com.coursework.app.service.TransactionService;
import com.coursework.app.utils.StringConverterUtils;
import com.coursework.app.utils.ViewUtils;
import com.coursework.app.view.ViewControllers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.VBox;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;

public class AddTransactionController {
    @FXML
    private VBox primaryLayout;
    @FXML
    private ComboBox<Product> productBox;
    @FXML
    private TableColumn<TransactionProduct, Product> productNameColumn;
    @FXML
    private TableColumn<TransactionProduct, Integer> productQuantityColumn;
    @FXML
    private TextField quantityField;
    @FXML
    private TableView<TransactionProduct> productTable;
    private final ObservableList<TransactionProduct> transactionProducts = FXCollections.observableArrayList();
    private final SalePointService salePointService = new SalePointService();
    private final SalePoint salePoint = ((Seller) TradeOrganizationApp.getUser()).getHall().getSalePoint();
    private final TransactionService transactionService = new TransactionService();
    private final ConsumerService consumerService = new ConsumerService();
    private TextField consumerName;

    @FXML
    protected void initialize() {
        try {
            productNameColumn.setCellValueFactory(new PropertyValueFactory<>("product"));
            productNameColumn.setCellFactory(TextFieldTableCell.forTableColumn(StringConverterUtils.productNameStringConverter));
            productQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

            productBox.getItems().addAll(salePointService.getSalePointProducts(salePoint.getSalePointId()).stream().filter(Product::getIsActive).toList());
            productBox.setConverter(StringConverterUtils.productNameStringConverter);

            productTable.setItems(transactionProducts);

            if ((salePoint.getType().getTypeName().equals("Универмаг")) ||
                    (salePoint.getType().getTypeName().equals("Магазин"))) {
                consumerName = new TextField();
                consumerName.setPromptText("ФИО покупателя");
                primaryLayout.getChildren().add(
                        primaryLayout.getChildren().size() - 1,
                        consumerName
                );
            }
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

        if (quantity < 0) {
            new Alert(Alert.AlertType.INFORMATION, "В поле количество должно быть не отрицательное число", ButtonType.OK).showAndWait();
            return;
        }

        try {
            if (product != null) {
                Optional<TransactionProduct> optionalProduct = transactionProducts.stream().filter(product_ ->
                        product_.getProduct().getProductId() == product.getProductId()).findFirst();
                if (optionalProduct.isPresent()) {
                    TransactionProduct product_ = optionalProduct.get();
                    if (isNotEqualProduct(product_, product_.getQuantity() + quantity)) {
                        new Alert(Alert.AlertType.INFORMATION, "Такого количества товара нет на складе", ButtonType.OK).showAndWait();
                        return;
                    }
                    product_.setQuantity(product_.getQuantity() + quantity);
                } else {
                    TransactionProduct product_ = new TransactionProduct(product, quantity);
                    if (isNotEqualProduct(product_, product_.getQuantity())) {
                        new Alert(Alert.AlertType.INFORMATION, "Такого количества товара нет на складе", ButtonType.OK).showAndWait();
                        return;
                    }
                    transactionProducts.add(product_);
                }
                productTable.refresh();
            }
        } catch (SQLException exception) {
            new Alert(Alert.AlertType.ERROR, exception.getMessage(), ButtonType.OK).showAndWait();
        }
    }

    private boolean isNotEqualProduct(TransactionProduct product, int newQuantity) throws SQLException {
        return salePointService.getSalePointProductQuantity(salePoint.getSalePointId(),
                product.getProduct().getProductId()) < newQuantity;
    }

    @FXML
    protected void applyClick() {
        try {
            String[] consumerName_ = new String[0];
            if (consumerName != null) {
                if (consumerName.getText().trim().isBlank()) {
                    new Alert(Alert.AlertType.INFORMATION, "ФИО покупателя не заполнено", ButtonType.OK).showAndWait();
                    return;
                }
                consumerName_ = consumerName.getText().trim().split(" ");
            }

            Transaction transaction = transactionService.addTransaction(new Transaction(
                    (Seller) TradeOrganizationApp.getUser(),
                    LocalDate.now()
            ));

            for (TransactionProduct product : transactionProducts) {
                product.setTransaction(transaction);
                transactionService.addTransactionProduct(product);
                salePointService.changeSalePointProductQuantity(
                        product.getProduct().getProductId(),
                        salePoint.getSalePointId(),
                        salePointService.getSalePointProductQuantity(
                                salePoint.getSalePointId(),
                                product.getProduct().getProductId()) - product.getQuantity());
            }

            if (consumerName != null) {
                consumerService.addConsumer(new Consumer(consumerName_[1], consumerName_[0], consumerName_[2], transaction));
            }

            ViewControllers.getSellerController().updateTransactionsTable();
            ViewUtils.getStage(quantityField).close();
            new Alert(Alert.AlertType.INFORMATION, "Успешно", ButtonType.OK).showAndWait();
        } catch (SQLException | AddConsumerException | AddTransactionException exception) {
            new Alert(Alert.AlertType.ERROR, exception.getMessage(), ButtonType.OK).showAndWait();
        }
    }

    @FXML
    protected void cancelClick() {
        ViewUtils.getStage(productBox).close();
    }
}
