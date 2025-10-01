//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.antam.app.controller;

import com.antam.app.gui.GiaoDienCuaSo;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class HoaDonController {
    @FXML
    private Button btnAddInvoice;
    @FXML
    private Button btnReturnMedicine;
    @FXML
    private Button btnExchangeMedicine;

    public HoaDonController() {
    }

    public void initialize() {
        this.btnAddInvoice.setOnAction((e) -> {
            (new GiaoDienCuaSo("themhoadon")).showAndWait();
        });
        this.btnReturnMedicine.setOnAction((e) -> {
            (new GiaoDienCuaSo("trathuoc")).showAndWait();
        });
        this.btnExchangeMedicine.setOnAction((e) -> {
            (new GiaoDienCuaSo("doithuoc")).showAndWait();
        });
    }
}
