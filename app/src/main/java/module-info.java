module com.coursework.app {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.coursework.app to javafx.fxml;
    opens com.coursework.app.view.auth to javafx.fxml;

    exports com.coursework.app;
    exports com.coursework.app.entity;
    exports com.coursework.app.exception;
    exports com.coursework.app.repository;
    exports com.coursework.app.service;
    exports com.coursework.app.utils;
    exports com.coursework.app.view.auth;
}
