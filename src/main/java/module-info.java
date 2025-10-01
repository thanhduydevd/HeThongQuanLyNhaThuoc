//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

module com.antam.app {
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.desktop;
    requires java.sql;
    requires javafx.controls;

    exports com.antam.app.controller;
    exports com.antam.app.controller.dialog;
    exports com.antam.app.gui;

    opens com.antam.app.controller to
            javafx.fxml;
    opens com.antam.app.controller.dialog to
            javafx.fxml;
    opens com.antam.app.gui to
            javafx.fxml;
}
