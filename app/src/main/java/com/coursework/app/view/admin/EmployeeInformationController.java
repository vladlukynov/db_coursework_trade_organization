package com.coursework.app.view.admin;

import com.coursework.app.entity.Seller;
import com.coursework.app.entity.SuperVisor;
import com.coursework.app.exception.NoSalePointByIdException;
import com.coursework.app.service.SalePointService;
import com.coursework.app.utils.ViewUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;

import java.sql.SQLException;

public class EmployeeInformationController {
    @FXML
    private Label salePointLabel;

    @FXML
    private Label sectionOrHallLabel;

    @FXML
    private Label titleLabel;
    private final SalePointService salePointService = new SalePointService();

    protected void seller(Seller seller) {
        try {
            titleLabel.setText("Информация о продавце");
            salePointLabel.setText("Торговая точка: " + salePointService.getSalePointBySellerLogin(seller.getUserLogin()).getSalePointName());
            sectionOrHallLabel.setText("Зал: " + seller.getHall().getHallName());
        } catch (SQLException | NoSalePointByIdException exception) {
            new Alert(Alert.AlertType.ERROR, exception.getMessage(), ButtonType.OK).showAndWait();
            ViewUtils.getStage(titleLabel).close();
        }
    }

    protected void superVisor(SuperVisor superVisor) {
        try {
            titleLabel.setText("Информация о руководителе");
            salePointLabel.setText("Торговая точка: " + salePointService.getSalePointBySuperVisorLogin(superVisor.getUserLogin()).getSalePointName());
            sectionOrHallLabel.setText("Секция: " + superVisor.getSection().getSectionName());
        } catch (SQLException | NoSalePointByIdException exception) {
            new Alert(Alert.AlertType.ERROR, exception.getMessage(), ButtonType.OK).showAndWait();
            ViewUtils.getStage(titleLabel).close();
        }
    }

    @FXML
    protected void closeButtonClick() {
        ViewUtils.getStage(salePointLabel).close();
    }
}
