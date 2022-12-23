package com.coursework.app.view.admin;

import com.coursework.app.entity.Hall;
import com.coursework.app.entity.SalePoint;
import com.coursework.app.exception.AddHallException;
import com.coursework.app.service.HallService;
import com.coursework.app.utils.ViewUtils;
import com.coursework.app.view.ViewControllers;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;

import java.sql.SQLException;

public class AddHallController {
    @FXML
    private TextField nameField;
    private final HallService hallService = new HallService();

    @FXML
    protected void onAddButtonClick() {
        try {
            String name = nameField.getText().trim();
            SalePoint salePoint = ViewControllers.getAdminController().getSelectedHallSalePoint();

            if (name.isBlank()) {
                new Alert(Alert.AlertType.INFORMATION, "Введите наименование зала", ButtonType.OK).showAndWait();
                return;
            }

            if (salePoint == null) {
                new Alert(Alert.AlertType.INFORMATION, "На главном окне не выбрана торговая точка",
                        ButtonType.OK).showAndWait();
                ViewUtils.getStage(nameField).close();
                return;
            }

            hallService.addHall(new Hall(name, salePoint, true));
            ViewControllers.getAdminController().updateHallsPage();
            ViewUtils.getStage(nameField).close();
        } catch (SQLException | AddHallException exception) {
            new Alert(Alert.AlertType.ERROR, exception.getMessage(), ButtonType.OK).showAndWait();
        }
    }

    @FXML
    protected void onCancelButtonClick() {
        ViewUtils.getStage(nameField).close();
    }
}
