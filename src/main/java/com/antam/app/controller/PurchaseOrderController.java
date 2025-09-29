//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.antam.app.controller;

import com.antam.app.gui.ShowDialog;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class PurchaseOrderController {
    @FXML
    private Button btnAddPurchaseOrder;

    public PurchaseOrderController() {
    }

    public void initialize() {
        this.btnAddPurchaseOrder.setOnAction((e) -> {
            (new ShowDialog("themphieudat")).showAndWait();
        });
    }
}
