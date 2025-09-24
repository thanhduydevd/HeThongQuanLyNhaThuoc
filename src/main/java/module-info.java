module com.iuh.hethongquanlynhathuoc {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.antam.app to javafx.fxml;
    exports com.antam.app;
}