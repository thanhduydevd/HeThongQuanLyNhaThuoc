//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.antam.app.controller;

import com.antam.app.gui.GiaoDienCuaSo;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class ThuocController {
    @FXML
    private Button btnAddMedicine;

    public ThuocController() {
    }

    public void initialize() {
        this.btnAddMedicine.setOnAction((e) -> {
            (new GiaoDienCuaSo("themthuoc")).showAndWait();
        });
    }
}
