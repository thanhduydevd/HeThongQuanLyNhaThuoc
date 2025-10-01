//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.antam.app.controller;

import com.antam.app.gui.ShowDialog;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class EmployeeController {
    @FXML
    private Button btnAddEmployee;

    public EmployeeController() {
    }

    public void initialize() {
        this.btnAddEmployee.setOnAction((e) -> {
            (new ShowDialog("themnhanvien")).showAndWait();
        });
    }
}
