//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.antam.app.controller;

import com.antam.app.gui.GiaoDienCuaSo;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class KhuyenMaiController {
    @FXML
    private Button btnAddPromotion;

    public KhuyenMaiController() {
    }

    public void initialize() {
        this.btnAddPromotion.setOnAction((e) -> {
            (new GiaoDienCuaSo("themkhuyenmai")).showAndWait();
        });
    }
}
