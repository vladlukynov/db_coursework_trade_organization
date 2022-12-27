package com.coursework.app;

import com.coursework.app.entity.User;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class TradeOrganizationApp extends Application {
    private static User user;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(TradeOrganizationApp.class.getResource("auth/auth-view.fxml"));

        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Авторизация");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        TradeOrganizationApp.user = user;
    }
}
