//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.antam.app.controller;

import com.antam.app.gui.GiaoDienCuaSo;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class DatThuocController {
    @FXML
    private Button btnAddPurchaseOrder;

    public DatThuocController() {
    }

    public void initialize() {
        this.btnAddPurchaseOrder.setOnAction((e) -> {
            (new GiaoDienCuaSo("themphieudat")).showAndWait();
        });
    }
}
