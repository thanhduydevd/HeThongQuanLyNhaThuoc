//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.antam.app.controller;

import com.antam.app.gui.ShowDialog;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class InvoiceController {
    @FXML
    private Button btnAddInvoice;
    @FXML
    private Button btnReturnMedicine;
    @FXML
    private Button btnExchangeMedicine;

    public InvoiceController() {
    }

    public void initialize() {
        this.btnAddInvoice.setOnAction((e) -> {
            (new ShowDialog("themhoadon")).showAndWait();
        });
        this.btnReturnMedicine.setOnAction((e) -> {
            (new ShowDialog("trathuoc")).showAndWait();
        });
        this.btnExchangeMedicine.setOnAction((e) -> {
            (new ShowDialog("doithuoc")).showAndWait();
        });
    }
}
