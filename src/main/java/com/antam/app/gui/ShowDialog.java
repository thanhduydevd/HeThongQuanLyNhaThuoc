//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.antam.app.gui;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Alert.AlertType;

public class ShowDialog extends Dialog {
    private String mainFunction;

    public ShowDialog(String mainFunction) {
        this.mainFunction = mainFunction;
        this.setHeaderText((String)null);
        this.setGraphic((Node)null);

        try {
            String fxmlPath = this.getFxmlPath(mainFunction);
            FXMLLoader loader = new FXMLLoader(this.getClass().getResource(fxmlPath));
            DialogPane dialogPane = (DialogPane)loader.load();
            this.setDialogPane(dialogPane);
        } catch (IOException var5) {
            IOException e = var5;
            throw new RuntimeException(e);
        }
    }

    public static void showMessageDialog(Node owner, String message) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("Thông báo");
        alert.setHeaderText((String)null);
        alert.setContentText(message);
        alert.initOwner(owner.getScene().getWindow());
        alert.showAndWait();
    }

    private String getFxmlPath(String mainFunction) {
        String var10000;
        switch (mainFunction.toLowerCase()) {
            case "themthuoc" -> var10000 = "/com/antam/app/views/product/dialog/add_medicine.fxml";
            case "themke" -> var10000 = "/com/antam/app/views/product/dialog/add_shelf.fxml";
            case "themphieunhap" -> var10000 = "/com/antam/app/views/product/dialog/add_goods_receipt.fxml";
            case "themhoadon" -> var10000 = "/com/antam/app/views/sales/dialog/add_invoice.fxml";
            case "themkhuyenmai" -> var10000 = "/com/antam/app/views/sales/dialog/add_promotion.fxml";
            case "themphieudat" -> var10000 = "/com/antam/app/views/sales/dialog/add_purchase_order.fxml";
            case "doithuoc" -> var10000 = "/com/antam/app/views/sales/dialog/exchange_medicine.fxml";
            case "trathuoc" -> var10000 = "/com/antam/app/views/sales/dialog/return_medicine.fxml";
            case "xemhoadon" -> var10000 = "/com/antam/app/views/sales/dialog/view_invoice.fxml";
            case "xemphieudat" -> var10000 = "/com/antam/app/views/sales/dialog/view_purchase_order.fxml";
            case "themnhanvien" -> var10000 = "/com/antam/app/views/user/dialog/add_employee.fxml";
            case "xemkhachhang" -> var10000 = "/com/antam/app/views/user/dialog/view_customer.fxml";
            default -> throw new IllegalArgumentException("Chức năng không tồn tại: " + mainFunction);
        }

        return var10000;
    }
}
