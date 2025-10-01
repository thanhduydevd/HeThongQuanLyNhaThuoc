//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.antam.app.controller;

import com.antam.app.gui.GiaoDienCuaSo;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class KeThuocController {
    @FXML
    private Button btnAddShelf;

    public KeThuocController() {
    }

    public void initialize() {
        this.btnAddShelf.setOnAction((e) -> {
            (new GiaoDienCuaSo("themke")).showAndWait();
        });
    }
}
