package com.coursework.app.view.seller;

import com.coursework.app.TradeOrganizationApp;
import com.coursework.app.entity.Seller;
import com.coursework.app.service.UserService;
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
    private final UserService userService = new UserService();

    @FXML
    protected void initialize() {
        updateAccountPage();
    }

    @FXML
    protected void createTransactionButton() {

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
            salePointLabel.setText("Торговая точка: " + userService.getSellerSalePoint(seller.getUserLogin()).getName());
        } catch (SQLException exception) {
            new Alert(Alert.AlertType.ERROR, exception.getMessage(), ButtonType.OK).showAndWait();
        }
    }
}
