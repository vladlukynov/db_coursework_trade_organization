package com.coursework.app.view.admin;

import com.coursework.app.entity.SalePoint;
import com.coursework.app.entity.SalePointType;
import com.coursework.app.exception.AddSalePointException;
import com.coursework.app.service.SalePointService;
import com.coursework.app.service.SalePointTypeService;
import com.coursework.app.utils.StringConverterUtils;
import com.coursework.app.utils.ViewUtils;
import com.coursework.app.view.ViewControllers;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.sql.SQLException;

public class AddSalePointController {
    @FXML
    private TextField salePointNameField;
    @FXML
    private TextField comServ;

    @FXML
    private TextField counters;

    @FXML
    private TextField rentalPrice;

    @FXML
    private TextField size;

    @FXML
    private ComboBox<SalePointType> type;
    private final SalePointTypeService salePointTypesService = new SalePointTypeService();
    private final SalePointService salePointService = new SalePointService();

    @FXML
    protected void initialize() {
        try {
            type.getItems().addAll(salePointTypesService.getSalePointTypes());
        } catch (SQLException exception) {
            new Alert(Alert.AlertType.ERROR, exception.getMessage(), ButtonType.OK).showAndWait();
        }
        type.setConverter(StringConverterUtils.salePointTypeStringConverter);
    }

    @FXML
    protected void addButtonClick() {
        String name = salePointNameField.getText().trim();
        SalePointType salePointType = type.getSelectionModel().getSelectedItem();
        double pointSize;
        double rental;
        double communalService;
        int countersNumber;
        if (salePointType == null || name.isBlank()) {
            new Alert(Alert.AlertType.INFORMATION, "Тип точки или название не введены", ButtonType.OK).showAndWait();
            return;
        }
        try {
            pointSize = Double.parseDouble(size.getText().trim());
            rental = Double.parseDouble(rentalPrice.getText().trim());
            communalService = Double.parseDouble(comServ.getText().trim());
            countersNumber = Integer.parseInt(counters.getText().trim());
        } catch (NumberFormatException exception) {
            new Alert(Alert.AlertType.INFORMATION, "Где-то введено не число", ButtonType.OK).showAndWait();
            return;
        }
        try {
            salePointService.addSalePoint(new SalePoint(salePointType, pointSize, rental, communalService, countersNumber, true, name));
            ViewControllers.getAdminController().updateSalePointsTable();
            ViewUtils.getStage(rentalPrice).close();
        } catch (SQLException | AddSalePointException exception) {
            new Alert(Alert.AlertType.ERROR, exception.getMessage(), ButtonType.OK).showAndWait();
        }
    }

    @FXML
    protected void cancelButtonClick() {
        ViewUtils.getStage(rentalPrice).close();
    }
}
