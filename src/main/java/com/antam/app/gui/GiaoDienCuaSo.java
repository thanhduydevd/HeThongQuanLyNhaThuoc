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

public class GiaoDienCuaSo extends Dialog {
    private String mainFunction;
    private FXMLLoader loader;

    public GiaoDienCuaSo(String mainFunction) {
        this.mainFunction = mainFunction;
        this.setHeaderText((String)null);
        this.setGraphic((Node)null);

        try {
            String fxmlPath = this.getFxmlPath(mainFunction);
            loader = new FXMLLoader(this.getClass().getResource(fxmlPath));
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
            //Hoadon
            case "xemchitiethoadon" -> var10000 = "/com/antam/app/views/hoadon/dialog/xemChiTietHoaDon.fxml";
            case "themhoadon" -> var10000 = "/com/antam/app/views/hoadon/dialog/themHoaDonForm.fxml";
            case "trathuoc" -> var10000 = "/com/antam/app/views/hoadon/dialog/traThuocForm.fxml";
            case "doithuoc" -> var10000 = "/com/antam/app/views/hoadon/dialog/doiThuocForm.fxml";
            //Phieudat
            case "themphieudat" -> var10000 = "/com/antam/app/views/phieudat/dialog/themPhieuDatForm.fxml";
            case "xemchitietphieudat" -> var10000 = "/com/antam/app/views/phieudat/dialog/xemChiTietPhieuDatForm.fxml";
            case "capnhatphieudat" -> var10000 = "/com/antam/app/views/phieudat/dialog/capNhatPhieuDatForm.fxml";
            //Thuoc
            case "themthuoc" -> var10000 = "/com/antam/app/views/thuoc/dialog/themThuocForm.fxml";
            case "capnhatthuoc" -> var10000 = "/com/antam/app/views/thuoc/dialog/capNhatThuocForm.fxml";
            case "xemchitietthuoc" -> var10000 = "/com/antam/app/views/thuoc/dialog/xemChiTietThuocForm.fxml";
            //ke
            case "themke" -> var10000 = "/com/antam/app/views/product/dialog/add_shelf.fxml";
            //khuyen mai
            case "themkhuyenmai" -> var10000 = "/com/antam/app/views/khuyenmai/dialog/themKhuyenMaiForm.fxml";
            case "capnhatkhuyenmai" -> var10000 = "/com/antam/app/views/khuyenmai/dialog/capNhatKhuyenMaiForm.fxml";
            //phieunhap
            case "themphieunhap" -> var10000 = "/com/antam/app/views/phieunhap/dialog/themPhieuNhapForm.fxml";
            case "capnhatphieunhap" -> var10000 = "/com/antam/app/views/phieunhap/dialog/capNhatPhieuNhapForm.fxml";
            case "xemchitietphieunhap" -> var10000 = "/com/antam/app/views/phieunhap/dialog/xemChiTietPhieuNhapForm.fxml";
            case "themnhanvien" -> var10000 = "/com/antam/app/views/user/dialog/add_employee.fxml";
            case "xemkhachhang" -> var10000 = "/com/antam/app/views/user/dialog/view_customer.fxml";
            default -> throw new IllegalArgumentException("Chức năng không tồn tại: " + mainFunction);
        }

        return var10000;
    }

    public <T> T getController() {
        return loader.getController();
    }
}
