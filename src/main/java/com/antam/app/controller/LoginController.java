//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.antam.app.controller;

import com.antam.app.gui.MainApp;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class LoginController {
    @FXML
    private Button btnLogin;

    public LoginController() {
    }

    @FXML
    protected void onLoginButtonClick() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("/com/antam/app/views/dashboard.fxml"));
            Parent root = (Parent)fxmlLoader.load();
            Stage newStage = new Stage();
            newStage.setTitle("");
            newStage.setScene(new Scene(root));
            newStage.setMaximized(true);
            newStage.initStyle(StageStyle.DECORATED);
            newStage.show();
            Stage oldStage = (Stage)this.btnLogin.getScene().getWindow();
            oldStage.close();
        } catch (Exception var5) {
            Exception e = var5;
            e.printStackTrace();
        }

    }
}
