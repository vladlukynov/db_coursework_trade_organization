package com.coursework.app.utils;

import com.coursework.app.TradeOrganizationApp;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ViewUtils {
    public static Stage getStage(Node node) {
        return (Stage) node.getScene().getWindow();
    }

    public static void openWindow(String view, String title, Stage oldStage, boolean closeCurrentWindow) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(TradeOrganizationApp.class.getResource(view));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setTitle(title);
        stage.setResizable(false);
        stage.setScene(scene);
        if (closeCurrentWindow) {
            oldStage.close();
        }
        stage.show();
    }
}
