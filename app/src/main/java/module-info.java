module com.coursework.app {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.coursework.app to javafx.fxml;
    exports com.coursework.app;
}