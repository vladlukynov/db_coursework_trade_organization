module com.coursework.app {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.apache.commons.codec;

    opens com.coursework.app to javafx.fxml;
    opens com.coursework.app.view.auth to javafx.fxml;
    opens com.coursework.app.view.admin to javafx.fxml;
    opens com.coursework.app.view.seller to javafx.fxml;
    opens com.coursework.app.view.super_visor to javafx.fxml;

    exports com.coursework.app;
    exports com.coursework.app.entity;
    exports com.coursework.app.exception;
    exports com.coursework.app.repository;
    exports com.coursework.app.service;
    exports com.coursework.app.utils;
    exports com.coursework.app.view;
    exports com.coursework.app.view.auth;
    exports com.coursework.app.view.admin;
    exports com.coursework.app.view.seller;
    exports com.coursework.app.view.super_visor;
}
