//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.antam.app.controller;

import com.antam.app.gui.ShowDialog;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class MedicineController {
    @FXML
    private Button btnAddMedicine;

    public MedicineController() {
    }

    public void initialize() {
        this.btnAddMedicine.setOnAction((e) -> {
            (new ShowDialog("themthuoc")).showAndWait();
        });
    }
}
