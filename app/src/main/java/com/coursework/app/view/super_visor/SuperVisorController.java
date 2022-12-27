package com.coursework.app.view.super_visor;

import com.coursework.app.TradeOrganizationApp;
import com.coursework.app.entity.*;
import com.coursework.app.exception.NoSalePointByIdException;
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

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

public class SuperVisorController {
    // Окно заявки
    @FXML
    private Tab requestsTab;
    @FXML
    private TableView<Request> requestsTable;
    @FXML
    private TableColumn<Request, Integer> requestIdColumn;
    @FXML
    private TableColumn<Request, LocalDate> createDateColumn;
    @FXML
    private TableColumn<Request, LocalDate> completeDateColumn;
    private final ObservableList<Request> requests = FXCollections.observableArrayList();

    // Окно товары
    @FXML
    private Tab productsTab;
    @FXML
    private TableView<SalePointProduct> productsTable;
    @FXML
    private TableColumn<SalePointProduct, Product> productNameColumn;
    @FXML
    private TableColumn<SalePointProduct, Integer> productQuantityColumn;
    @FXML
    private TableColumn<SalePointProduct, Double> productPriceColumn;
    @FXML
    private TableColumn<SalePointProduct, Double> productDiscountColumn;
    private final ObservableList<SalePointProduct> products = FXCollections.observableArrayList();

    // Окно аккаунт
    @FXML
    private Tab accountTab;
    @FXML
    private Label loginLabel;
    @FXML
    private Label nameLabel;
    @FXML
    private Label roleLabel;
    @FXML
    private Label salePointLabel;
    @FXML
    private Label sectionLabel;

    // Сервисы для доступа к БД
    private final SalePointService salePointService = new SalePointService();
    private final RequestService requestService = new RequestService();

    @FXML
    protected void initialize() {
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("product"));
        productNameColumn.setCellFactory(TextFieldTableCell.forTableColumn(StringConverterUtils.productNameStringConverter));
        productQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        productPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        productDiscountColumn.setCellValueFactory(new PropertyValueFactory<>("discount"));
        productDiscountColumn.setCellFactory(TextFieldTableCell.forTableColumn(StringConverterUtils.productDiscountConverter));
        productsTable.setItems(products);

        requestIdColumn.setCellValueFactory(new PropertyValueFactory<>("RequestId"));
        createDateColumn.setCellValueFactory(new PropertyValueFactory<>("CreationDate"));
        completeDateColumn.setCellValueFactory(new PropertyValueFactory<>("CompleteDate"));
        requestsTable.setItems(requests);

        ViewControllers.setSuperVisorController(this);
    }


    // Окно заявки
    @FXML
    protected void requestsTabSelected() {
        if (requestsTab.isSelected()) {
            updateRequestsTable();
        }
    }

    protected void updateRequestsTable() {
        try {
            requests.clear();
            requests.addAll(requestService.getRequestsBySalePointId(
                    salePointService.getSalePointBySuperVisorLogin(
                            TradeOrganizationApp.getUser().getUserLogin()).getSalePointId()));
        } catch (SQLException | NoSalePointByIdException exception) {
            new Alert(Alert.AlertType.ERROR, exception.getMessage(), ButtonType.OK).showAndWait();
        }
    }

    @FXML
    protected void createRequestClick() {
        try {
            ViewUtils.openWindow("super_visor/add-request-view.fxml", "Новая заявка",
                    ViewUtils.getStage(requestsTable), false);
        } catch (IOException exception) {
            new Alert(Alert.AlertType.ERROR, exception.getMessage(), ButtonType.OK).showAndWait();
        }
    }

    // Окно товары

    @FXML
    protected void productsTabSelected() {
        if (productsTab.isSelected()) {
            updateProductsTable();
        }
    }

    protected void updateProductsTable() {
        try {
            products.clear();
            products.addAll(salePointService.getSalePointProducts(
                    salePointService.getSalePointBySuperVisorLogin(TradeOrganizationApp.getUser().getUserLogin()).getSalePointId()));
        } catch (SQLException | NoSalePointByIdException exception) {
            new Alert(Alert.AlertType.ERROR, exception.getMessage(), ButtonType.OK).showAndWait();
        }
    }

    // Окно аккаунт
    @FXML
    protected void accountTabSelected() {
        if (accountTab.isSelected()) {
            updateAccountPage();
        }
    }

    protected void updateAccountPage() {
        try {
            SuperVisor superVisor = (SuperVisor) TradeOrganizationApp.getUser();
            loginLabel.setText("Логин: " + superVisor.getUserLogin());
            nameLabel.setText("ФИО: " + superVisor.getLastName() + " " + superVisor.getFirstName() + " " + superVisor.getMiddleName());
            roleLabel.setText("Роль в системе: " + superVisor.getRole().getRoleName());
            salePointLabel.setText("Торговая точка: " + salePointService.getSalePointBySuperVisorLogin(
                    superVisor.getUserLogin()).getSalePointName());
            sectionLabel.setText("Секция: " + superVisor.getSection().getSectionName());
        } catch (SQLException | NoSalePointByIdException exception) {
            new Alert(Alert.AlertType.INFORMATION, exception.getMessage(), ButtonType.OK).showAndWait();
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
}
