package com.coursework.app.view.auth;

import com.coursework.app.TradeOrganizationApp;
import com.coursework.app.entity.User;
import com.coursework.app.exception.NoUserByLoginException;
import com.coursework.app.service.UserService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.sql.SQLException;

public class AuthController {
    private final UserService userService = new UserService();
    @FXML
    private TextField loginField;
    @FXML
    private PasswordField passwordField;

    @FXML
    protected void onAuthButtonClick() {
        String login = loginField.getText().trim();
        String password = passwordField.getText();

        if (login.isBlank() || password.isEmpty()) {
            new Alert(Alert.AlertType.INFORMATION, "Заполните все поля", ButtonType.OK).show();
            return;
        }

        User user;
        try {
            user = userService.getUser(login);
        } catch (SQLException exception) {
            new Alert(Alert.AlertType.ERROR, exception.getMessage(), ButtonType.OK).show();
            return;
        } catch (NoUserByLoginException exception) {
            new Alert(Alert.AlertType.INFORMATION, "Такого пользователя не существует в системе", ButtonType.OK).show();
            return;
        }

        if (!user.getPassword().equals(password)) {
            new Alert(Alert.AlertType.INFORMATION, "Пароль указан неверно", ButtonType.OK).show();
            return;
        }

        TradeOrganizationApp.setUser(user);
    }
}
