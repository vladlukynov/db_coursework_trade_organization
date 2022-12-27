package com.coursework.app.view.seller;

import com.coursework.app.TradeOrganizationApp;
import com.coursework.app.entity.Seller;
import com.coursework.app.entity.Transaction;
import com.coursework.app.exception.NoSalePointByIdException;
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

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

public class SellerController {
    // Окно продажи
    @FXML
    private Tab salesTab;
    @FXML
    private TableView<Transaction> transactionsTable;
    @FXML
    private TableColumn<Transaction, Integer> transactionIdColumn;
    @FXML
    private TableColumn<Transaction, LocalDate> transactionDateColumn;
    private final ObservableList<Transaction> transactions = FXCollections.observableArrayList();

    // Окно аккаунт
    @FXML
    private Tab accountTab;
    @FXML
    private Label loginLabel;

    @FXML
    private Label hallLabel;

    @FXML
    private Label nameLabel;

    @FXML
    private Label roleLabel;

    @FXML
    private Label salePointLabel;
    private final SalePointService salePointService = new SalePointService();
    private final TransactionService transactionService = new TransactionService();

    @FXML
    protected void initialize() {
        transactionIdColumn.setCellValueFactory(new PropertyValueFactory<>("transactionId"));
        transactionDateColumn.setCellValueFactory(new PropertyValueFactory<>("transactionDate"));
        transactionDateColumn.setCellFactory(TextFieldTableCell.forTableColumn(StringConverterUtils.transactionDateConverter));
        transactionsTable.setItems(transactions);

        ViewControllers.setSellerController(this);
    }

    @FXML
    protected void createTransactionButton() {
        try {
            ViewUtils.openWindow("seller/add-transaction-view.fxml", "Добавление транзакции",
                    ViewUtils.getStage(loginLabel), false);
        } catch (IOException exception) {
            new Alert(Alert.AlertType.ERROR, exception.getMessage(), ButtonType.OK).showAndWait();
        }
    }

    @FXML
    protected void onExitButtonClick() {
        try {
            TradeOrganizationApp.setUser(null);
            ViewUtils.openWindow("auth/auth-view.fxml", "Авторизация",
                    ViewUtils.getStage(loginLabel), true);
        } catch (IOException exception) {
            new Alert(Alert.AlertType.ERROR, exception.getMessage(), ButtonType.OK).showAndWait();
        }
    }

    // Окно продажи
    @FXML
    private void salesTabSelected() {
        if (salesTab.isSelected()) {
            updateTransactionsTable();
        }
    }

    protected void updateTransactionsTable() {
        try {
            transactions.clear();
            transactions.addAll(transactionService.getSellerTransaction(TradeOrganizationApp.getUser().getUserLogin()));
        } catch (SQLException exception) {
            new Alert(Alert.AlertType.ERROR, exception.getMessage(), ButtonType.OK).showAndWait();
        }
    }

    // Окно аккаунт
    @FXML
    private void accountTabSelected() {
        if (accountTab.isSelected()) {
            updateAccountPage();
        }
    }

    private void updateAccountPage() {
        try {
            Seller seller = (Seller) TradeOrganizationApp.getUser();
            loginLabel.setText("Логин: " + seller.getUserLogin());
            hallLabel.setText("Зал: " + seller.getHall().getHallName());
            nameLabel.setText("ФИО: " + seller.getLastName() + " " + seller.getFirstName() + " " + seller.getMiddleName());
            roleLabel.setText("Роль в системе: " + seller.getRole().getRoleName());
            salePointLabel.setText("Торговая точка: " + salePointService.getSalePointBySellerLogin(seller.getUserLogin()).getSalePointName());
        } catch (SQLException | NoSalePointByIdException exception) {
            new Alert(Alert.AlertType.ERROR, exception.getMessage(), ButtonType.OK).showAndWait();
        }
    }
}
