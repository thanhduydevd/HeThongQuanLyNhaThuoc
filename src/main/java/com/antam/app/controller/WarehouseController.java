//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.antam.app.controller;

import com.antam.app.gui.ShowDialog;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class WarehouseController {
    @FXML
    private Button btnAddGoodsReceipt;

    public WarehouseController() {
    }

    public void initialize() {
        this.btnAddGoodsReceipt.setOnAction((e) -> {
            (new ShowDialog("themphieunhap")).showAndWait();
        });
    }
}
