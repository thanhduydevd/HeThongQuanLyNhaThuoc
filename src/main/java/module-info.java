module com.iuh.hethongquanlynhathuoc {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.antam.app to javafx.fxml;
    exports com.antam.app;
}