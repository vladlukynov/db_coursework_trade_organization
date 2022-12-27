package com.coursework.app.view.admin;

import com.coursework.app.entity.Hall;
import com.coursework.app.entity.Section;
import com.coursework.app.exception.AddSectionException;
import com.coursework.app.service.SectionService;
import com.coursework.app.utils.ViewUtils;
import com.coursework.app.view.ViewControllers;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;

import java.sql.SQLException;

public class AddSectionController {
    @FXML
    private TextField nameField;
    private final SectionService sectionService = new SectionService();

    @FXML
    protected void onCancelButtonClick() {
        ViewUtils.getStage(nameField).close();
    }

    @FXML
    protected void onAddButtonClick() {
        String name = nameField.getText().trim();
        Hall hall = ViewControllers.getAdminController().getSelectedSectionHall();

        if (hall == null) {
            new Alert(Alert.AlertType.INFORMATION, "На главном окне не выбран зал", ButtonType.OK).showAndWait();
            ViewUtils.getStage(nameField).close();
        }

        if (name.isBlank()) {
            new Alert(Alert.AlertType.INFORMATION, "Не введено наименование", ButtonType.OK).showAndWait();
        }

        try {
            sectionService.addSection(new Section(name, hall, true));
            ViewControllers.getAdminController().updateSectionTable();
            ViewUtils.getStage(nameField).close();
        } catch (SQLException | AddSectionException exception) {
            new Alert(Alert.AlertType.INFORMATION, "Не введено наименование", ButtonType.OK).showAndWait();
            ViewUtils.getStage(nameField).close();
        }
    }
}
