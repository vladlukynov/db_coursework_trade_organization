package com.coursework.app.view.admin;

import com.coursework.app.entity.*;
import com.coursework.app.exception.NoSalePointByIdException;
import com.coursework.app.exception.NoUserByLoginException;
import com.coursework.app.service.*;
import com.coursework.app.utils.StringConverterUtils;
import com.coursework.app.utils.ViewUtils;
import com.coursework.app.view.ViewControllers;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.apache.commons.codec.digest.DigestUtils;

import java.sql.SQLException;

public class ChangeEmployeeDataController {
    @FXML
    private ComboBox<Hall> hallBox;

    @FXML
    private TextField loginField;

    @FXML
    private TextField nameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private ComboBox<Role> roleBox;

    @FXML
    private ComboBox<SalePoint> salePointBox;

    @FXML
    private ComboBox<Section> sectionBox;
    private final User user = ViewControllers.getAdminController().getSelectedEmployee();
    private final SectionService sectionService = new SectionService();
    private final HallService hallService = new HallService();
    private final RoleService roleService = new RoleService();
    private final SalePointService salePointService = new SalePointService();
    private final UserService userService = new UserService();

    @FXML
    protected void initialize() {
        try {
            roleBox.getItems().addAll(roleService.getRoles());
            roleBox.setConverter(StringConverterUtils.roleStringConverter);

            Role role_ = user.getRole();
            roleBox.getSelectionModel().select(
                    roleBox.getItems().stream().filter(item -> item.getRoleId() ==
                            role_.getRoleId()).findFirst().orElse(null));

            if (userService.isSuperVisor(user.getUserLogin())) {
                salePointBox.getItems().addAll(salePointService.getSalePoints());
                salePointBox.setConverter(StringConverterUtils.salePointNameStringConverter);
                sectionBox.setConverter(StringConverterUtils.sectionNameStringConverter);

                SalePoint salePoint_ = salePointService.getSalePointBySuperVisorLogin(user.getUserLogin());
                salePointBox.getSelectionModel().select(
                        salePointBox.getItems().stream().filter(item -> item.getSalePointId() ==
                                salePoint_.getSalePointId()).findFirst().orElse(null));
                salePointBox.setDisable(false);

                salePointChange();

                Section section_ = userService.getSuperVisorByLogin(user.getUserLogin()).getSection();
                sectionBox.getSelectionModel().select(
                        sectionBox.getItems().stream().filter(item -> item.getSectionId() ==
                                section_.getSectionId()).findFirst().orElse(null));
                sectionBox.setDisable(false);
            } else if (userService.isSeller(user.getUserLogin())) {
                salePointBox.getItems().addAll(salePointService.getSalePoints());
                salePointBox.setConverter(StringConverterUtils.salePointNameStringConverter);
                hallBox.setConverter(StringConverterUtils.hallNameStringConverter);

                SalePoint salePoint_ = salePointService.getSalePointBySellerLogin(user.getUserLogin());
                salePointBox.getSelectionModel().select(
                        salePointBox.getItems().stream().filter(item -> item.getSalePointId() ==
                                salePoint_.getSalePointId()).findFirst().orElse(null));
                salePointBox.setDisable(false);

                salePointChange();

                Hall hall_ = userService.getSellerByLogin(user.getUserLogin()).getHall();
                hallBox.getSelectionModel().select(
                        hallBox.getItems().stream().filter(item -> item.getHallId() ==
                                hall_.getHallId()).findFirst().orElse(null));
                hallBox.setDisable(false);
            }

            loginField.setText(user.getUserLogin());
            nameField.setText(user.getLastName() + " " + user.getFirstName() + " " + user.getMiddleName());
            passwordField.setText(user.getPassword());
        } catch (SQLException | NoSalePointByIdException | NoUserByLoginException exception) {
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

            if (!user.getUserLogin().equals(login) &&
                    userService.getUsers().stream().anyMatch(user -> user.getUserLogin().equals(login))) {
                new Alert(Alert.AlertType.INFORMATION, "Логин занят", ButtonType.OK).showAndWait();
                return;
            }

            if (!user.getPassword().equals(password)) {
                password = DigestUtils.md5Hex(password);
            }

            if (role.getRoleName().equals("Руководитель")) {
                Section section = sectionBox.getSelectionModel().getSelectedItem();
                if (section == null) {
                    new Alert(Alert.AlertType.INFORMATION, "Секция не выбрана", ButtonType.OK).showAndWait();
                    return;
                }
                userService.updateSupervisor(new SuperVisor(login, password, name[1], name[0], name[2], role, true, section),
                        user.getUserLogin());
            } else if (role.getRoleName().equals("Продавец")) {
                Hall hall = hallBox.getSelectionModel().getSelectedItem();
                if (hall == null) {
                    new Alert(Alert.AlertType.INFORMATION, "Зал не выбран", ButtonType.OK).showAndWait();
                    return;
                }
                userService.updateSeller(new Seller(login, password, name[1], name[0], name[2], role, true, hall),
                        user.getUserLogin());
            } else {
                userService.updateUser(new User(login, password, name[1], name[0], name[2], role, true),
                        user.getUserLogin());
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
        } else if (role.getRoleName().equals("Продавец")) {
            salePointBox.setDisable(false);
            sectionBox.setDisable(true);
            hallBox.setDisable(false);
        } else {
            salePointBox.setDisable(true);
            sectionBox.setDisable(true);
            hallBox.setDisable(true);
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
