module com.antam.app {
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.desktop;
    requires java.sql;
    requires javafx.controls;
    requires fontawesomefx;
    requires jbcrypt;

    // cho FXML
    opens com.antam.app.controller to javafx.fxml;
    opens com.antam.app.controller.hoadon to javafx.fxml;
    opens com.antam.app.controller.phieudat to javafx.fxml;
    opens com.antam.app.controller.thuoc to javafx.fxml;
    opens com.antam.app.controller.kethuoc to javafx.fxml;
    opens com.antam.app.controller.dangdieuche to javafx.fxml;
    opens com.antam.app.controller.donvitinh to javafx.fxml;
    opens com.antam.app.controller.khuyenmai to javafx.fxml;
    opens com.antam.app.controller.phieunhap to javafx.fxml;
    opens com.antam.app.gui to javafx.fxml;

    // cho JavaFX TableView/PropertyValueFactory đọc getter của entity
    opens com.antam.app.entity to javafx.base;

    // exports nếu muốn dùng entity ở package khác
    exports com.antam.app.entity;
    exports com.antam.app.controller;
    exports com.antam.app.controller.hoadon;
    exports com.antam.app.controller.phieudat;
    exports com.antam.app.controller.thuoc;
    exports com.antam.app.controller.kethuoc;
    exports com.antam.app.controller.dangdieuche;
    exports com.antam.app.controller.donvitinh;
    exports com.antam.app.controller.khuyenmai;
    exports com.antam.app.controller.phieunhap;
    exports com.antam.app.gui;
}
