package com.coursework.app.view.auth;

import com.coursework.app.TradeOrganizationApp;
import com.coursework.app.entity.User;
import com.coursework.app.exception.NoUserByLoginException;
import com.coursework.app.service.UserService;
import com.coursework.app.utils.ViewUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.SQLException;

import org.apache.commons.codec.digest.DigestUtils;

import static com.coursework.app.utils.ViewUtils.openWindow;

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
            new Alert(Alert.AlertType.INFORMATION, "Заполните все поля", ButtonType.OK).showAndWait();
            return;
        }

        User user;
        try {
            user = userService.getUserByLogin(login);
        } catch (SQLException exception) {
            new Alert(Alert.AlertType.ERROR, exception.getMessage(), ButtonType.OK).showAndWait();
            return;
        } catch (NoUserByLoginException exception) {
            new Alert(Alert.AlertType.INFORMATION, "Такого пользователя не существует в системе", ButtonType.OK).showAndWait();
            return;
        }

        if (!DigestUtils.md5Hex(password).equals(user.getPassword())) {
            new Alert(Alert.AlertType.INFORMATION, "Пароль указан неверно", ButtonType.OK).showAndWait();
            return;
        }

        if (!user.getIsActive()) {
            new Alert(Alert.AlertType.INFORMATION, "Аккаунт пользователя заблокирован", ButtonType.OK).showAndWait();
            return;
        }

        try {
            switch (user.getRole().getRoleName()) {
                case "Администратор" -> {
                    TradeOrganizationApp.setUser(user);
                    openWindow("admin/admin-view.fxml", "Администратор", ViewUtils.getStage(loginField),
                            true);
                }
                case "Продавец" -> {
                    TradeOrganizationApp.setUser(userService.getSellerByLogin(login));
                    openWindow("seller/seller-view.fxml", "Продавец", ViewUtils.getStage(loginField),
                            true);
                }
                default -> System.out.println("Другие роли в разработке");
            }
        } catch (IOException | SQLException | NoUserByLoginException exception) {
            exception.printStackTrace(System.err);
            new Alert(Alert.AlertType.ERROR, exception.getMessage(), ButtonType.OK).showAndWait();
        }
    }
}
