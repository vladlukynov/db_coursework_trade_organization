package com.coursework.app.view.manager;

import com.coursework.app.entity.Request;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.time.LocalDate;

public class ManagerController {
    @FXML
    private Tab requestsTab;

    @FXML
    private TableColumn<Request, LocalDate> completeDateColumn;

    @FXML
    private TableColumn<Request, LocalDate> createDateColumn;

    @FXML
    private TableColumn<Request, Integer> requestIdColumn;

    @FXML
    private TableView<Request> requestsColumn;

    @FXML
    private TableColumn<?, ?> salePointColumn;

    @FXML
    protected void initialize() {

    }

    @FXML
    protected void requestsTabSelected() {
        if (requestsTab.isSelected()) {

        }
    }

    protected void updateRequestsTable() {

    }
}
