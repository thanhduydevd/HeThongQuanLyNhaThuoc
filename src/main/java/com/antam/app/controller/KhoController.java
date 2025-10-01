//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.antam.app.controller;

import com.antam.app.gui.GiaoDienCuaSo;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class KhoController {
    @FXML
    private Button btnAddGoodsReceipt;

    public KhoController() {
    }

    public void initialize() {
        this.btnAddGoodsReceipt.setOnAction((e) -> {
            (new GiaoDienCuaSo("themphieunhap")).showAndWait();
        });
    }
}
