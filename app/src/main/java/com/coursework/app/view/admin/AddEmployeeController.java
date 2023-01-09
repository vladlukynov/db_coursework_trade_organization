package com.coursework.app.view.admin;

import com.coursework.app.entity.*;
import com.coursework.app.service.*;
import com.coursework.app.utils.StringConverterUtils;
import com.coursework.app.utils.ViewUtils;
import com.coursework.app.view.ViewControllers;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.apache.commons.codec.digest.DigestUtils;

import java.sql.SQLException;

public class AddEmployeeController {
    @FXML
    private TextField loginField;

    @FXML
    private TextField nameField;

    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField salaryTextField;
    @FXML
    private ComboBox<Role> roleBox;
    @FXML
    private ComboBox<SalePoint> salePointBox;
    @FXML
    private ComboBox<Hall> hallBox;
    @FXML
    private ComboBox<Section> sectionBox;

    private final UserService userService = new UserService();
    private final RoleService roleService = new RoleService();
    private final SalePointService salePointService = new SalePointService();
    private final SectionService sectionService = new SectionService();
    private final HallService hallService = new HallService();

    @FXML
    protected void initialize() {
        try {
            roleBox.getItems().addAll(roleService.getRoles());
            roleBox.setConverter(StringConverterUtils.roleStringConverter);

            salePointBox.getItems().addAll(salePointService.getSalePoints());
            salePointBox.setConverter(StringConverterUtils.salePointNameStringConverter);

            hallBox.setConverter(StringConverterUtils.hallNameStringConverter);
            sectionBox.setConverter(StringConverterUtils.sectionNameStringConverter);
        } catch (SQLException exception) {
            new Alert(Alert.AlertType.ERROR, exception.getMessage(), ButtonType.OK).showAndWait();
        }
    }

    @FXML
    protected void onCancelButtonClick() {
        ViewUtils.getStage(loginField).close();
    }

    @FXML
    protected void onRegistrationButtonClick() {
        try {
            String login = loginField.getText().trim();
            String password = passwordField.getText();
            String[] name = nameField.getText().split(" ");
            Role role = roleBox.getSelectionModel().getSelectedItem();

            if (login.isBlank() || password.isEmpty() || name.length != 3 || role == null) {
                new Alert(Alert.AlertType.INFORMATION, "Заполните все поля", ButtonType.OK).showAndWait();
                return;
            }

            if (userService.getUsers().stream().anyMatch(user -> user.getUserLogin().equals(login))) {
                new Alert(Alert.AlertType.INFORMATION, "Логин занят", ButtonType.OK).showAndWait();
                return;
            }

            password = DigestUtils.md5Hex(password);
            if (role.getRoleName().equals("Руководитель")) {
                Section section = sectionBox.getSelectionModel().getSelectedItem();
                if (section == null) {
                    new Alert(Alert.AlertType.INFORMATION, "Секция не выбрана", ButtonType.OK).showAndWait();
                    return;
                }
                userService.addSuperVisor(new SuperVisor(login, password, name[1], name[0], name[2], role, true, section));
            } else if (role.getRoleName().equals("Продавец")) {
                Hall hall = hallBox.getSelectionModel().getSelectedItem();
                if (hall == null) {
                    new Alert(Alert.AlertType.INFORMATION, "Зал не выбран", ButtonType.OK).showAndWait();
                    return;
                }
                double salary;
                try {
                    salary = Double.parseDouble(salaryTextField.getText().trim());
                } catch (NumberFormatException exception) {
                    new Alert(Alert.AlertType.INFORMATION, "Заработная плата должна быть числом", ButtonType.OK).showAndWait();
                    return;
                }
                if (salary < 0) {
                    new Alert(Alert.AlertType.INFORMATION, "Заработная плата должна быть больше 0", ButtonType.OK).showAndWait();
                    return;
                }

                userService.addSeller(new Seller(login, password, name[1], name[0], name[2], role, true, hall, salary));
            } else {
                userService.addUser(new User(login, password, name[1], name[0], name[2], role, true));
            }

            ViewControllers.getAdminController().updateEmployeesTable();
            ViewUtils.getStage(loginField).close();
        } catch (SQLException exception) {
            new Alert(Alert.AlertType.ERROR, exception.getMessage(), ButtonType.OK).showAndWait();
        }
    }

    @FXML
    protected void roleBoxChange() {
        Role role = roleBox.getSelectionModel().getSelectedItem();
        if (role == null) {
            return;
        }
        salePointBox.getSelectionModel().clearSelection();
        hallBox.getItems().clear();
        sectionBox.getItems().clear();
        if (role.getRoleName().equals("Руководитель")) {
            salePointBox.setDisable(false);
            sectionBox.setDisable(false);
            hallBox.setDisable(true);
            salaryTextField.setDisable(true);
        } else if (role.getRoleName().equals("Продавец")) {
            salePointBox.setDisable(false);
            sectionBox.setDisable(true);
            hallBox.setDisable(false);
            salaryTextField.setDisable(false);
        } else {
            salePointBox.setDisable(true);
            sectionBox.setDisable(true);
            hallBox.setDisable(true);
            salaryTextField.setDisable(true);
        }
    }

    @FXML
    protected void salePointChange() {
        SalePoint salePoint = salePointBox.getSelectionModel().getSelectedItem();
        Role role = roleBox.getSelectionModel().getSelectedItem();
        if (salePoint == null || role == null) {
            return;
        }
        try {
            if (role.getRoleName().equals("Руководитель")) {
                sectionBox.getItems().clear();
                sectionBox.getItems().addAll(sectionService.getSectionsBySalePointId(salePoint.getSalePointId())
                        .stream().filter(Section::getIsActive).toList());
            } else if (role.getRoleName().equals("Продавец")) {
                hallBox.getItems().clear();
                hallBox.getItems().addAll(hallService.getHallsBySalePointId(salePoint.getSalePointId())
                        .stream().filter(Hall::getIsActive).toList());
            }
        } catch (SQLException exception) {
            new Alert(Alert.AlertType.ERROR, exception.getMessage(), ButtonType.OK).showAndWait();
        }
    }
}
