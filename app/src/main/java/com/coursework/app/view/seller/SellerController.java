package com.coursework.app.view.seller;

import com.coursework.app.TradeOrganizationApp;
import com.coursework.app.entity.Seller;
import com.coursework.app.exception.NoSalePointByIdException;
import com.coursework.app.service.SalePointService;
import com.coursework.app.utils.ViewUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;

import java.io.IOException;
import java.sql.SQLException;

public class SellerController {
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

    @FXML
    protected void initialize() {
        updateAccountPage();
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
