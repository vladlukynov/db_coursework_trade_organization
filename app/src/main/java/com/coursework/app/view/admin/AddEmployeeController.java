package com.coursework.app.view.admin;

import com.coursework.app.entity.Role;
import com.coursework.app.entity.User;
import com.coursework.app.service.RoleService;
import com.coursework.app.service.UserService;
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
    private ComboBox<Role> roleBox;
    private final UserService userService = new UserService();
    private final RoleService roleService = new RoleService();

    @FXML
    protected void initialize() {
        try {
            roleBox.getItems().addAll(roleService.getRoles());
            roleBox.setConverter(StringConverterUtils.roleStringConverter);
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
            userService.addUser(new User(login, password, name[1], name[0], name[2], role, true));

            ViewControllers.getAdminController().fullUpdate();

            ViewUtils.getStage(loginField).close();
        } catch (SQLException exception) {
            new Alert(Alert.AlertType.ERROR, exception.getMessage(), ButtonType.OK).showAndWait();
        }
    }
}
