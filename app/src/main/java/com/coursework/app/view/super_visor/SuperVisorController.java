package com.coursework.app.view.super_visor;

import com.coursework.app.TradeOrganizationApp;
import com.coursework.app.entity.SuperVisor;
import com.coursework.app.exception.NoSalePointByIdException;
import com.coursework.app.service.SalePointService;
import com.coursework.app.utils.ViewUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;

import java.io.IOException;
import java.sql.SQLException;

public class SuperVisorController {
    @FXML
    private Tab requestsTab;
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
    private final SalePointService salePointService = new SalePointService();

    @FXML
    protected void requestsTabSelected() {
        if (requestsTab.isSelected()) {
            System.out.println("Заявки");
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
