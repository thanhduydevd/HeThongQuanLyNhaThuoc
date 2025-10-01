//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.antam.app.controller;

import com.antam.app.gui.ShowDialog;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class PromotionController {
    @FXML
    private Button btnAddPromotion;

    public PromotionController() {
    }

    public void initialize() {
        this.btnAddPromotion.setOnAction((e) -> {
            (new ShowDialog("themkhuyenmai")).showAndWait();
        });
    }
}
