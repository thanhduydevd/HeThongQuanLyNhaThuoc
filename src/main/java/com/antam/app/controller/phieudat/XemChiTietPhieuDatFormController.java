//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.antam.app.controller.phieudat;

import javafx.fxml.FXML;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;

public class XemChiTietPhieuDatFormController {
    @FXML
    DialogPane dialogPane;

    public XemChiTietPhieuDatFormController() {
    }

    public void initialize() {
        ButtonType cancelButton = new ButtonType("Huỷ", ButtonData.CANCEL_CLOSE);
        this.dialogPane.getButtonTypes().add(cancelButton);
    }
}
