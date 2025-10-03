module com.antam.app {
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.desktop;
    requires java.sql;
    requires javafx.controls;

    // cho FXML
    opens com.antam.app.controller to javafx.fxml;
    opens com.antam.app.controller.dialog to javafx.fxml;
    opens com.antam.app.gui to javafx.fxml;

    // cho JavaFX TableView/PropertyValueFactory đọc getter của entity
    opens com.antam.app.entity to javafx.base;

    // exports nếu muốn dùng entity ở package khác
    exports com.antam.app.entity;
    exports com.antam.app.controller;
    exports com.antam.app.controller.dialog;
    exports com.antam.app.gui;
}
